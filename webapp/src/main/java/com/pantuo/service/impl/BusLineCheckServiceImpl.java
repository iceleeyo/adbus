package com.pantuo.service.impl;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.mysema.query.types.expr.BooleanExpression;
import com.pantuo.ActivitiConfiguration;
import com.pantuo.dao.BodyContractRepository;
import com.pantuo.dao.BusLockRepository;
import com.pantuo.dao.BuslineRepository;
import com.pantuo.dao.pojo.JpaBodyContract;
import com.pantuo.dao.pojo.JpaBus;
import com.pantuo.dao.pojo.JpaBus.Category;
import com.pantuo.dao.pojo.JpaBusLock;
import com.pantuo.dao.pojo.JpaBusline;
import com.pantuo.dao.pojo.QJpaBusLock;
import com.pantuo.dao.pojo.QJpaBusline;
import com.pantuo.mybatis.domain.Bodycontract;
import com.pantuo.mybatis.domain.BodycontractExample;
import com.pantuo.mybatis.domain.Bus;
import com.pantuo.mybatis.domain.BusContract;
import com.pantuo.mybatis.domain.BusContractExample;
import com.pantuo.mybatis.domain.BusExample;
import com.pantuo.mybatis.domain.BusLock;
import com.pantuo.mybatis.domain.BusLockExample;
import com.pantuo.mybatis.domain.BusModel;
import com.pantuo.mybatis.persistence.BodycontractMapper;
import com.pantuo.mybatis.persistence.BusContractMapper;
import com.pantuo.mybatis.persistence.BusLineMapper;
import com.pantuo.mybatis.persistence.BusLockMapper;
import com.pantuo.mybatis.persistence.BusMapper;
import com.pantuo.mybatis.persistence.BusSelectMapper;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.ActivitiService;
import com.pantuo.service.ActivitiService.TaskQueryType;
import com.pantuo.service.BusLineCheckService;
import com.pantuo.service.MailService;
import com.pantuo.service.MailTask;
import com.pantuo.service.MailTask.Type;
import com.pantuo.simulate.MailJob;
import com.pantuo.util.Constants;
import com.pantuo.util.DateUtil;
import com.pantuo.util.NumberPageUtil;
import com.pantuo.util.OrderException;
import com.pantuo.util.OrderIdSeq;
import com.pantuo.util.Pair;
import com.pantuo.util.Request;
import com.pantuo.vo.GroupVo;
import com.pantuo.web.view.AutoCompleteView;
import com.pantuo.web.view.LineBusCpd;
import com.pantuo.web.view.OrderView;

@Service
public class BusLineCheckServiceImpl implements BusLineCheckService {
	private static Logger log = LoggerFactory.getLogger(BusLineCheckServiceImpl.class);
	public static final String BODY_ACTIVITY = "busFlowV2";
	@Autowired
	BusSelectMapper busSelectMapper;

	@Autowired
	BusLineMapper buslineMapper;
	@Autowired
	BusLockMapper busLockMapper;
	@Autowired
	BodycontractMapper bodycontractMapper;
	@Autowired
	BodyContractRepository bodyContractRepository;
	@Autowired
	BuslineRepository BuslineRepository;
	@Autowired
	BusContractMapper busContractMapper;

	@Autowired
	BusMapper busMapper;

	///

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private HistoryService historyService;

	private ProcessInstance instance;

	@Autowired
	private MailService mailService;

	@Autowired
	private MailJob mailJob;

	@Override
	public int countByFreeCars(int lineId, Integer modelId, JpaBus.Category category, String start, String end) {

		int busLineCarCount = busSelectMapper.countBusCar(lineId, modelId, category.ordinal(),
				BooleanUtils.toInteger(true));
		int carIds = busSelectMapper.countOnlineCarList(lineId, modelId, category.ordinal(), start, end);
		//查被锁定的数量
		Integer lockCarNumber = busSelectMapper.countWorkingCarList(lineId, modelId,
				JpaBusLock.Status.enable.ordinal(), start, end);
		if (lockCarNumber == null) {
			lockCarNumber = 0;
		}
		log.info("line_modelid:{}_{},total:{},sales:{},lock:{}", lineId, modelId, busLineCarCount, carIds,
				lockCarNumber);
		//总数-被占用数据
		return busLineCarCount - carIds - lockCarNumber;
	}

	@Autowired
	BuslineRepository buslineRepository;
	@Autowired
	BusLockRepository busLockRepository;

	@Override
	public List<AutoCompleteView> autoCompleteByName(int city, String name, JpaBus.Category category) {
		List<AutoCompleteView> r = new ArrayList<AutoCompleteView>();
		BooleanExpression query = QJpaBusline.jpaBusline.city.eq(city);
		if (StringUtils.isNotBlank(name)) {
			query = query.and(QJpaBusline.jpaBusline.name.like("%" + name + "%"));
		}
		Pageable p = new PageRequest(0, 30, new Sort("name"));
		List<Integer> idsList = new ArrayList<Integer>();
		Page<JpaBusline> list = buslineRepository.findAll(query, p);
		if (!list.getContent().isEmpty()) {
			for (JpaBusline obj : list.getContent()) {
				idsList.add(obj.getId());
			}
		}
		if (idsList.size() > 0) {
			//query carnumber group by line
			List<GroupVo> vos = busSelectMapper.countCarByLines(idsList, category.ordinal());
			Map<Integer, Integer> cache = getBusLineMap(vos);
			for (JpaBusline obj : list.getContent()) {
				int carNumber = cache.containsKey(obj.getId()) ? cache.get(obj.getId()) : 0;
				String viewString = obj.getName() + "  " + obj.getLevelStr() + " [" + carNumber + "]";
				r.add(new AutoCompleteView(viewString, viewString, String.valueOf(obj.getId())));//String.valueOf(obj.getId())

			}
		}
		return r;
	}

	public Map<Integer, Integer> getBusLineMap(List<GroupVo> vos) {
		Map<Integer, Integer> r = new HashMap<Integer, Integer>();
		for (GroupVo groupVo : vos) {
			r.put(groupVo.getGn1(), groupVo.getCount());
		}
		return r;
	}

	public List<JpaBusLock> getBusLockListByBid(int contractId) {
		BooleanExpression query = QJpaBusLock.jpaBusLock.contractId.eq(contractId);
		List<JpaBusLock> list = (List<JpaBusLock>) busLockRepository.findAll(query);
		return list;
	}

	@Override
	public List<JpaBusLock> getBusLockListBySeriNum(long seriaNum) {
		BooleanExpression query = QJpaBusLock.jpaBusLock.seriaNum.eq(seriaNum);
		List<JpaBusLock> list = (List<JpaBusLock>) busLockRepository.findAll(query);
		return list;
	}

	@Override
	public JpaBusLock findBusLockById(int id) {
		return busLockRepository.findOne(id);
	}

	@Override
	public List<GroupVo> countCarTypeByLine(int lineId, JpaBus.Category category) {
		return busSelectMapper.countCarTypeByLine(lineId, category.ordinal());
	}

	@Override
	public Pair<Boolean, String> saveBusLock(BusLock buslock, String startD, String endD) throws ParseException {
		BodycontractExample example = new BodycontractExample();
		BodycontractExample.Criteria criteria = example.createCriteria();
		criteria.andSeriaNumEqualTo(buslock.getSeriaNum());
		List<Bodycontract> list = bodycontractMapper.selectByExample(example);
		if (list.size() > 0) {
			buslock.setContractId(list.get(0).getId());
		} else {
			buslock.setContractId(0);
		}
		buslock.setStats(JpaBusLock.Status.ready.ordinal());
		buslock.setCreated(new Date());
		buslock.setUpdated(new Date());
		buslock.setSalesNumber(buslock.getRemainNuber());
		buslock.setStartDate((Date) new SimpleDateFormat("yyyy-MM-dd").parseObject(startD));
		buslock.setEndDate((Date) new SimpleDateFormat("yyyy-MM-dd").parseObject(endD));
		if (busLockMapper.insert(buslock) > 0) {
			return new Pair<Boolean, String>(true, "保存成功");
		}
		return new Pair<Boolean, String>(false, "保存失败");
	}

	@Override
	public boolean removeBusLock(Principal principal, int city, long seriaNum, int id) {
		BusLockExample example = new BusLockExample();
		BusLockExample.Criteria criteria = example.createCriteria();
		criteria.andCityEqualTo(city);
		if (!Request.hasAuth(principal, ActivitiConfiguration.bodyContractManager)) {
			criteria.andUserIdEqualTo(Request.getUserId(principal));
		}
		criteria.andSeriaNumEqualTo(seriaNum);
		criteria.andIdEqualTo(id);
		List<BusLock> list = busLockMapper.selectByExample(example);
		if (list.size() > 0) {
			if (busLockMapper.deleteByPrimaryKey(id) > 0) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Pair<Boolean, String> saveBodyContract(Bodycontract bodycontract, long seriaNum, String userId) {
		bodycontract.setCreator(userId);
		bodycontract.setCreated(new Date());
		bodycontract.setUpdated(new Date());
		bodycontract.setIsSchedule(false);
		bodycontract.setStats(JpaBodyContract.Status.ready.ordinal());
		bodycontract.setContractid(0);
		int a = bodycontractMapper.insert(bodycontract);
		BusLockExample example = new BusLockExample();
		BusLockExample.Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userId);
		criteria.andSeriaNumEqualTo(seriaNum);
		List<BusLock> list = busLockMapper.selectByExample(example);
		if (list.size() == 0) {
			return new Pair<Boolean, String>(false, "请选择车辆");
		}
		for (BusLock busLock : list) {
			if (busLock != null) {
				if (a > 0) {
					busLock.setContractId(bodycontract.getId());
					busLock.setStats(JpaBusLock.Status.ready.ordinal());
					busLockMapper.updateByPrimaryKey(busLock);
				} else {
					return new Pair<Boolean, String>(false, "申请合同失败");
				}
			}
		}

		Map<String, Object> initParams = new HashMap<String, Object>();
		initParams.put("username", userId);
		initParams.put(ActivitiService.ORDER_ID, bodycontract.getId());
		initParams.put(ActivitiService.CITY, bodycontract.getCity());
		initParams.put(ActivitiService.CLOSED, false);
		initParams.put(ActivitiService.NOW, new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date()));
		ProcessInstance process = runtimeService.startProcessInstanceByKey("busFlowV2", initParams);

		List<Task> tasks = taskService.createTaskQuery().processInstanceId(process.getId())
				.processVariableValueEquals(ActivitiService.CITY, bodycontract.getCity()).orderByTaskCreateTime()
				.desc().listPage(0, 1);
		if (!tasks.isEmpty()) {
			Task task = tasks.get(0);
			taskService.claim(task.getId(), userId);
			MailTask mailTask = new MailTask(userId, bodycontract.getId(), null, task.getTaskDefinitionKey(),
					Type.sendCompleteMail);
			taskService.complete(task.getId());
			//	mailJob.putMailTask(mailTask);
		}

		return new Pair<Boolean, String>(true, "申请合同成功");
	}

	public Page<OrderView> getBodyContractList(int city, TableRequest req, Principal principal) {
		String userid = Request.getUserId(principal);
		int page = req.getPage(), pageSize = req.getLength();
		Sort sort = req.getSort("created");

		String userIdQuery = req.getFilter("userId");
		page = page + 1;
		List<Task> tasks = new ArrayList<Task>();
		List<OrderView> leaves = new ArrayList<OrderView>();
		TaskQuery countQuery = taskService.createTaskQuery().processDefinitionKey("busFlowV2");
		TaskQuery queryList = taskService.createTaskQuery().processDefinitionKey("busFlowV2");

		countQuery.taskCandidateOrAssigned(userid);
		queryList.taskCandidateOrAssigned(userid);
		countQuery.processVariableValueEquals(ActivitiService.CITY, city);
		queryList.processVariableValueEquals(ActivitiService.CITY, city).includeProcessVariables();
		if (StringUtils.isNoneBlank(userIdQuery)) {
			countQuery.processVariableValueLike(ActivitiService.CREAT_USERID, "%" + userIdQuery + "%");
			queryList.processVariableValueLike(ActivitiService.CREAT_USERID, "%" + userIdQuery + "%");
		}

		long c = countQuery.count();
		NumberPageUtil pageUtil = new NumberPageUtil((int) c, page, pageSize);
		tasks = queryList.listPage(pageUtil.getLimitStart(), pageUtil.getPagesize());

		for (Task task : tasks) {
			String processInstanceId = task.getProcessInstanceId();
			ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
					.processInstanceId(processInstanceId).singleResult();
			Map<String, Object> var = task.getProcessVariables();
			Integer orderid = (Integer) var.get(ActivitiService.ORDER_ID);

			OrderView v = new OrderView();
			JpaBodyContract order = bodyContractRepository.findOne(orderid);
			if (order != null) {
				v.setJpaBodyContract(order);
				v.setTask(task);
				v.setProcessInstanceId(processInstance.getId());
				v.setTask_createTime(task.getCreateTime());
				leaves.add(v);
			}
		}
		Pageable p = new PageRequest(page, pageSize, sort);
		org.springframework.data.domain.PageImpl<OrderView> r = new org.springframework.data.domain.PageImpl<OrderView>(
				leaves, p, pageUtil.getTotal());
		return r;
	}

	public Page<OrderView> queryOrders(int city, Principal principal, TableRequest req, TaskQueryType tqType) {
		String userid = Request.getUserId(principal);

		int page = req.getPage(), pageSize = req.getLength();
		Sort sort = req.getSort("created");
		page = page + 1;
		String longId = req.getFilter("longOrderId"), userIdQuery = req.getFilter("userId"), taskKey = req
				.getFilter("taskKey"), productId = req.getFilter("productId");
		Long longOrderId = StringUtils.isBlank(longId) ? 0 : NumberUtils.toLong(longId);
		List<OrderView> orders = new ArrayList<OrderView>();

		ProcessInstanceQuery countQuery = runtimeService.createProcessInstanceQuery()
				.processDefinitionKey(BODY_ACTIVITY).variableValueEquals(ActivitiService.CITY, city);

		ProcessInstanceQuery listQuery = runtimeService.createProcessInstanceQuery()
				.processDefinitionKey(BODY_ACTIVITY).variableValueEquals(ActivitiService.CITY, city);

		/* 运行中的订单和 我的订单区分*/
		if (tqType == TaskQueryType.my) {
			countQuery.involvedUser(userid);
			listQuery.involvedUser(userid);
		}

		//runtimeService.createNativeProcessInstanceQuery().sql("SELECT * FROM " + managementService.getTableName(ProcessInstance.class)).list().size());
		/*按订单号查询 */
		if (longOrderId > 0) {
			countQuery.variableValueEquals(ActivitiService.ORDER_ID, OrderIdSeq.checkAndGetRealyOrderId(longOrderId));
			listQuery.variableValueEquals(ActivitiService.ORDER_ID, OrderIdSeq.checkAndGetRealyOrderId(longOrderId));
		}
		/*按用户查询 */
		if (StringUtils.isNoneBlank(userIdQuery)) {
			countQuery.variableValueLike(ActivitiService.CREAT_USERID, "%" + userIdQuery + "%");
			listQuery.variableValueLike(ActivitiService.CREAT_USERID, "%" + userIdQuery + "%");
		}
		/*按商品查询 */
		if (StringUtils.isNoneBlank(productId)) {
			countQuery.variableValueEquals(ActivitiService.PRODUCT, NumberUtils.toInt(productId));
			listQuery.variableValueEquals(ActivitiService.PRODUCT, NumberUtils.toInt(productId));
		}
		//setVarFilter(taskKey, countQuery, listQuery);

		/*排序 */
		Sort.Order sor = sort.getOrderFor("created");
		if (sor != null) {

			listQuery.orderByProcessInstanceId();
		} else if ((sor = sort.getOrderFor("taskKey")) != null) {
			listQuery.orderByProcessInstanceId();//listQuery();
		}
		if (sor != null && sor.getDirection() == Sort.Direction.DESC) {
			listQuery.desc();
		} else {
			listQuery.asc();
		}
		int totalnum = (int) countQuery.count();
		NumberPageUtil pageUtil = new NumberPageUtil((int) totalnum, page, pageSize);
		List<ProcessInstance> processInstances = listQuery.listPage(pageUtil.getLimitStart(), pageUtil.getPagesize());
		List<Integer> contractIds = new ArrayList<Integer>();
		for (ProcessInstance processInstance : processInstances) {

			List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId())
					.processVariableValueEquals(ActivitiService.CITY, city).includeProcessVariables()
					.orderByTaskCreateTime().desc().list();

			Integer orderid = (Integer) tasks.get(0).getProcessVariables().get(ActivitiService.ORDER_ID);
			OrderView v = new OrderView();
			v.setId(orderid);
			contractIds.add(orderid);
			JpaBodyContract order = bodyContractRepository.findOne(orderid);
			v.setJpaBodyContract(order);
			v.setProcessInstanceId(processInstance.getId());
			//v.setProcessDefinition(getProcessDefinition(processInstance.getProcessDefinitionId()));
			Task top1Task = tasks.get(0);
			v.setTask(top1Task);
			v.setHaveTasks(tasks.size());
			//if (StringUtils.isNoneBlank(taskKey) && !StringUtils.startsWith(taskKey, ActivitiService.R_DEFAULTALL)) {
			//v.setTask_name(getOrderState(top1Task.getProcessVariables(), top1Task));
			v.setTask_name(top1Task.getName());
			orders.add(v);
		}

		setCars(orders, contractIds);

		Pageable p = new PageRequest(page, pageSize, sort);
		org.springframework.data.domain.PageImpl<OrderView> r = new org.springframework.data.domain.PageImpl<OrderView>(
				orders, p, pageUtil.getTotal());
		return r;
	}

	private void setCars(List<OrderView> orders, List<Integer> contractIds) {
		Map<Integer, Integer> countMap = getContractCars(contractIds);
		Map<Integer, Integer> doneMap =  countContractDoneCar(contractIds);
		for (OrderView view : orders) {
			if (countMap != null && countMap.containsKey(view.getId())) {
				view.setNeed_cars(countMap.get(view.getId()));
			}
			if (doneMap != null && doneMap.containsKey(view.getId())) {
				view.setDone_cars(doneMap.get(view.getId()));
			}
		}
	}

	/**
	 * 
	 * 根据工作流中的 值 来显示订单状态
	 *
	 * @param vars
	 * @return
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	public String getOrderState(Map<String, Object> vars, Task top1Task) {
		String r = ActivitiService.bodyWaitAuth;

		if (ObjectUtils.equals(vars.get(ActivitiService.R_USERPAYED), false)) {
			r = ActivitiService.paymentString;
		}
		if (ObjectUtils.equals(vars.get(ActivitiService.R_USERPAYED), true)
				&& ObjectUtils.equals(vars.get(ActivitiService.R_SCHEDULERESULT), false)) {
			r = ActivitiService.authString;
		}
		if (ObjectUtils.equals(vars.get(ActivitiService.R_SCHEDULERESULT), true)
				&& ObjectUtils.equals(vars.get(ActivitiService.R_SHANGBORESULT), false)) {
			r = ActivitiService.reportString;
		}
		if (ObjectUtils.equals(vars.get(ActivitiService.R_SCHEDULERESULT), true)
				&& ObjectUtils.equals(vars.get(ActivitiService.R_SHANGBORESULT), true)) {
			r = ActivitiService.overString;
		}
		if (StringUtils.equals("usertask1", top1Task.getTaskDefinitionKey())) {
			r = ActivitiService.bodyWaitAuth;
		}
		return r;

	}

	public Page<OrderView> findTask(int city, Principal principal, TableRequest req, TaskQueryType tqType) {
		String userid = Request.getUserId(principal);
		int page = req.getPage(), pageSize = req.getLength();
		Sort sort = req.getSort("created");

		String longId = req.getFilter("longOrderId"), userIdQuery = req.getFilter("userId"), taskKey = req
				.getFilter("taskKey");
		Long longOrderId = StringUtils.isBlank(longId) ? 0 : NumberUtils.toLong(longId);
		page = page + 1;
		List<Task> tasks = new ArrayList<Task>();
		List<OrderView> leaves = new ArrayList<OrderView>();
		TaskQuery countQuery = taskService.createTaskQuery().processDefinitionKey(BODY_ACTIVITY);
		TaskQuery queryList = taskService.createTaskQuery().processDefinitionKey(BODY_ACTIVITY);
		/**
		 * 根据查询方式 查我的待办事项或是 我提交的task
		 */
		if (tqType == TaskQueryType.task) {
			countQuery.taskCandidateOrAssigned(userid);
			queryList.taskCandidateOrAssigned(userid);
		} else {
			countQuery.processVariableValueEquals(ActivitiService.CREAT_USERID, userid);
			queryList.processVariableValueEquals(ActivitiService.CREAT_USERID, userid);
		}

		countQuery.processVariableValueEquals(ActivitiService.CITY, city);
		queryList.processVariableValueEquals(ActivitiService.CITY, city).includeProcessVariables();
		if (longOrderId > 0) {
			countQuery.processVariableValueEquals(ActivitiService.ORDER_ID,
					OrderIdSeq.checkAndGetRealyOrderId(longOrderId));
			queryList.processVariableValueEquals(ActivitiService.ORDER_ID,
					OrderIdSeq.checkAndGetRealyOrderId(longOrderId));
		}
		if (StringUtils.isNoneBlank(userIdQuery)) {
			countQuery.processVariableValueLike(ActivitiService.CREAT_USERID, "%" + userIdQuery + "%");
			queryList.processVariableValueLike(ActivitiService.CREAT_USERID, "%" + userIdQuery + "%");
		}
		if (StringUtils.isNoneBlank(taskKey) && !StringUtils.startsWith(taskKey, ActivitiService.R_DEFAULTALL)) {
			countQuery.taskDefinitionKey(taskKey);
			queryList.taskDefinitionKey(taskKey);
		}

		long c = countQuery.count();
		NumberPageUtil pageUtil = new NumberPageUtil((int) c, page, pageSize);

		taskOrderBy(sort, queryList);
		tasks = queryList.listPage(pageUtil.getLimitStart(), pageUtil.getPagesize());

		for (Task task : tasks) {
			String processInstanceId = task.getProcessInstanceId();
			ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
					.processInstanceId(processInstanceId).singleResult();
			Map<String, Object> var = task.getProcessVariables();
			Integer orderid = (Integer) var.get(ActivitiService.ORDER_ID);
			OrderView v = new OrderView();
			JpaBodyContract order = bodyContractRepository.findOne(orderid);
			if (order != null) {
				v.setJpaBodyContract(order);
				v.setTask(task);
				v.setProcessInstanceId(processInstance.getId());
				v.setTask_createTime(task.getCreateTime());
				if (var.get(ActivitiService.R_USERPAYED) == null
						|| !BooleanUtils.toBoolean((Boolean) var.get(ActivitiService.R_USERPAYED))) {
					v.setCanClosed(true);
				}
				leaves.add(v);
			}
		}
		Pageable p = new PageRequest(page, pageSize, sort);
		org.springframework.data.domain.PageImpl<OrderView> r = new org.springframework.data.domain.PageImpl<OrderView>(
				leaves, p, pageUtil.getTotal());
		return r;
	}

	private void taskOrderBy(Sort sort, TaskQuery queryList) {
		Sort.Order sor = sort.getOrderFor("created");
		if (sor != null) {
			queryList.orderByTaskCreateTime();
		} else if ((sor = sort.getOrderFor("taskKey")) != null) {
			queryList.orderByTaskDefinitionKey();
		}
		if (sor != null && sor.getDirection() == Sort.Direction.DESC) {
			queryList.desc();
		} else {
			queryList.asc();
		}
	}

	@Override
	public JpaBodyContract selectBcById(int id) {
		return bodyContractRepository.findOne(id);
	}

	@Override
	public Pair<Boolean, String> setLockDate(String lockDate, int id, Principal principal) throws ParseException {
		Bodycontract bodycontract = bodycontractMapper.selectByPrimaryKey(id);
		if (bodycontract == null) {
			return new Pair<Boolean, String>(false, "信息丢失");
		}
		bodycontract.setLockExpiredTime((Date) new SimpleDateFormat("yyyy-MM-dd").parseObject(lockDate));
		if (bodycontractMapper.updateByPrimaryKey(bodycontract) > 0) {
			BusLockExample example = new BusLockExample();
			BusLockExample.Criteria criteria = example.createCriteria();
			criteria.andSeriaNumEqualTo(bodycontract.getSeriaNum());
			List<BusLock> list = busLockMapper.selectByExample(example);
			for (BusLock busLock : list) {
				if (busLock != null) {
					busLock.setLockExpiredTime((Date) new SimpleDateFormat("yyyy-MM-dd").parseObject(lockDate));
					busLockMapper.updateByPrimaryKey(busLock);
				}
			}
		}
		return new Pair<Boolean, String>(true, "预留截止日期设置成功");
	}

	public List<LineBusCpd> getBusListChart(int lineId, Integer modelId, JpaBus.Category category) {
		List<com.pantuo.mybatis.domain.Bus> busList = busSelectMapper.getBusList(lineId, modelId, category.ordinal());
		List<LineBusCpd> r = new ArrayList<LineBusCpd>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		int days = 90;
		List<BusContract> contractList = busSelectMapper.getBusContract(lineId, modelId, category.ordinal());
		Map<Integer, List<BusContract>> mainLocation = new HashMap<Integer, List<BusContract>>();
		List<Integer> ids = new ArrayList<Integer>();
		for (BusContract bodycontract : contractList) {
			if (!mainLocation.containsKey(bodycontract.getBusid())) {
				mainLocation.put(bodycontract.getBusid(), new ArrayList<BusContract>());
			}
			mainLocation.get(bodycontract.getBusid()).add(bodycontract);
			ids.add(bodycontract.getContractid());
		}
		Map<Integer, JpaBodyContract> contractMap = new HashMap<Integer, JpaBodyContract>();
		if (!ids.isEmpty()) {
			List<JpaBodyContract> c = bodyContractRepository.findAll(ids);
			for (JpaBodyContract jpaBodyContract : c) {
				contractMap.put(jpaBodyContract.getId(), jpaBodyContract);
			}

		}
		for (Bus bus : busList) {
			Map<String, JpaBodyContract> map = new HashMap<String, JpaBodyContract>();
			LineBusCpd c = new LineBusCpd();
			List<BusContract> haveContract = mainLocation.get(bus.getId());
			if (haveContract != null && !haveContract.isEmpty()) {
				for (BusContract busContract : haveContract) {
					Date begin = DateUtil.trimDateDefault(busContract.getStartDate());
					Date end = busContract.getEndDate();
					Date tempDate = begin;
					while ((tempDate.after(begin) || begin.equals(tempDate))
							&& (tempDate.before(end) || end.equals(tempDate))) {
						map.put(format.format(tempDate), contractMap.get(busContract.getContractid()));
						tempDate = DateUtil.dateAdd(tempDate, 1);
					}
				}
			}
			c.setMap(map);
			c.setBus(bus);
			c.setSerialNumber(String.valueOf(bus.getSerialNumber()));
			r.add(c);
		}
		return r;
	}

	@Override
	public List<BusModel> getBusModel(int lineId, int category) {
		return busSelectMapper.getBusModel(lineId, category);
	}

	public Page<OrderView> finished(int city, Principal principal, TableRequest req) {
		int page = req.getPage(), pageSize = req.getLength();
		Sort sort = req.getSort("created");

		String longId = req.getFilter("longOrderId"), userIdQuery = req.getFilter("userId"), taskKey = req
				.getFilter("taskKey"), stateKey = req.getFilter("stateKey"), productId = req.getFilter("productId");
		Long longOrderId = StringUtils.isBlank(longId) ? 0 : NumberUtils.toLong(longId);

		page = page + 1;
		List<OrderView> orders = new ArrayList<OrderView>();

		HistoricProcessInstanceQuery countQuery = historyService.createHistoricProcessInstanceQuery()
				.processDefinitionKey(BODY_ACTIVITY).variableValueEquals(ActivitiService.CITY, city).finished();

		HistoricProcessInstanceQuery listQuery = historyService.createHistoricProcessInstanceQuery()
				.processDefinitionKey(BODY_ACTIVITY).variableValueEquals(ActivitiService.CITY, city)
				.includeProcessVariables().finished();

		if (longOrderId > 0) {
			countQuery.variableValueEquals(ActivitiService.ORDER_ID, OrderIdSeq.checkAndGetRealyOrderId(longOrderId));
			listQuery.variableValueEquals(ActivitiService.ORDER_ID, OrderIdSeq.checkAndGetRealyOrderId(longOrderId));
		}

		if (StringUtils.isNoneBlank(stateKey) && !StringUtils.startsWith(stateKey, ActivitiService.R_DEFAULTALL)) {
			boolean isClosed = StringUtils.startsWith(stateKey, ActivitiService.R_CLOSED) ? true : false;
			countQuery.variableValueEquals(ActivitiService.CLOSED, isClosed);
			listQuery.variableValueEquals(ActivitiService.CLOSED, isClosed);
		}

		/*按用户查询 */
		if (StringUtils.isNoneBlank(userIdQuery)) {
			countQuery.variableValueLike(ActivitiService.CREAT_USERID, "%" + userIdQuery + "%");
			listQuery.variableValueLike(ActivitiService.CREAT_USERID, "%" + userIdQuery + "%");
		}
		int c = 0;
		if (Request.hasAuth(principal, ActivitiConfiguration.ADVERTISER)
				&& !Request.hasAuth(principal, ActivitiConfiguration.ORDER)) {
			c = (int) countQuery.involvedUser(Request.getUserId(principal)).count();
		} else {
			c = (int) countQuery.count();
		}
		NumberPageUtil pageUtil = new NumberPageUtil((int) c, page, pageSize);
		//Request.hasAuth(principal, ActivitiConfiguration.ADVERTISER)
		List<HistoricProcessInstance> list = null;
		if (Request.hasAuth(principal, ActivitiConfiguration.ADVERTISER)
				&& !Request.hasAuth(principal, ActivitiConfiguration.ORDER)) {
			listQuery.involvedUser(Request.getUserId(principal));
		}
		hisotrySort(sort, listQuery);
		list = listQuery.listPage(pageUtil.getLimitStart(), pageUtil.getPagesize());
		List<Integer> contractIds = new ArrayList<Integer>();
		for (HistoricProcessInstance historicProcessInstance : list) {
			Integer orderid = (Integer) historicProcessInstance.getProcessVariables().get(ActivitiService.ORDER_ID);
			OrderView v = new OrderView();
			if (orderid != null && orderid > 0) {
				JpaBodyContract or = bodyContractRepository.findOne(orderid);
				if (or != null) {
					v.setJpaBodyContract(or);
					orders.add(v);
					v.setId(orderid);
					contractIds.add(orderid);
					Boolean bn = (Boolean) historicProcessInstance.getProcessVariables().get(ActivitiService.CLOSED);
					boolean b = bn == null ? false : bn;
					v.setFinishedState(b ? Constants.CLOSED_STATE : Constants.FINAL_STATE);
					v.setProcessInstanceId(historicProcessInstance.getId());
					v.setStartTime(historicProcessInstance.getStartTime());
					v.setEndTime(historicProcessInstance.getEndTime());
				}
			}
		}
		setCars(orders, contractIds);
		Pageable p = new PageRequest(page, pageSize, sort);
		org.springframework.data.domain.PageImpl<OrderView> r = new org.springframework.data.domain.PageImpl<OrderView>(
				orders, p, pageUtil.getTotal());
		return r;
	}

	private void hisotrySort(Sort sort, HistoricProcessInstanceQuery query) {
		Sort.Order sor = sort.getOrderFor("startTime");
		if (sor != null) {
			query.orderByProcessInstanceStartTime();
		} else if ((sor = sort.getOrderFor("endTime")) != null) {
			query.orderByProcessInstanceEndTime();
		}
		if (sor != null && sor.getDirection() == Sort.Direction.DESC) {
			query.desc();
		} else {
			query.asc();
		}
	}

	@Override
	public List<LineBusCpd> queryWorkNote(int bodycontract_id, int lineId, Integer modelId, Category category) {
		//查所有车辆
		List<com.pantuo.mybatis.domain.Bus> busList = busSelectMapper.getBusList(lineId, modelId, category.ordinal());

		JpaBusline lineVo = BuslineRepository.findOne(lineId);
		List<LineBusCpd> r = new ArrayList<LineBusCpd>();
		Bodycontract bc = bodycontractMapper.selectByPrimaryKey(bodycontract_id);
		if (bc == null) {
			throw new RuntimeException("合同丢失!");
		}
		//查已经上刑的车辆
		BusLockExample example = new BusLockExample();
		BusLockExample.Criteria c = example.createCriteria();
		c.andContractIdEqualTo(bodycontract_id);
		c.andLineIdEqualTo(lineId);
		c.andModelIdEqualTo(modelId);
		List<BusLock> lock = busLockMapper.selectByExample(example);
		Set<Integer> idsSet = new HashSet<Integer>();
		int s = lock.size();
		if (s == 1) {
			BusLock obj = lock.get(0);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			List<Integer> ids = busSelectMapper.getOnlineCarList(lineId, category.ordinal(),
					format.format(obj.getStartDate()), format.format(obj.getEndDate()));
			idsSet.addAll(ids);
		} else if (s > 1) {
			log.warn("合同:{},发现有重复线路需求 {},{}", bodycontract_id, lineId, modelId);
		}

		for (Bus bus : busList) {
			LineBusCpd cpd = new LineBusCpd();
			cpd.setBus(bus);
			cpd.setLine(lineVo);
			cpd.setSerialNumber(String.valueOf(bus.getSerialNumber()));
			if (!idsSet.contains(bus.getId())) {
				r.add(cpd);
			}

		}
		return r;
	}

	public void updateBusDone(int bodycontract_id, int busid) {
		Bus bus = busMapper.selectByPrimaryKey(busid);
		if (bus == null) {
			throw new OrderException("车辆信息丢失!");
		}
		Bodycontract bc = bodycontractMapper.selectByPrimaryKey(bodycontract_id);
		if (bc == null) {
			throw new OrderException("合同丢失!");
		}
		//查已经上刑的车辆
		BusLockExample example = new BusLockExample();
		BusLockExample.Criteria c = example.createCriteria();
		c.andContractIdEqualTo(bodycontract_id);
		c.andLineIdEqualTo(bus.getLineId());
		c.andModelIdEqualTo(bus.getModelId());
		List<BusLock> lock = busLockMapper.selectByExample(example);
		if (lock.isEmpty()) {
			example = new BusLockExample();
			c = example.createCriteria();
			c.andContractIdEqualTo(bodycontract_id);
			c.andLineIdEqualTo(bus.getLineId());
			lock = busLockMapper.selectByExample(example);
		}
		if (lock.isEmpty()) {
			log.warn("车辆可能换线路了!busid:{},lineId:{}", busid, bus.getLineId());
			throw new OrderException("车辆可能换线路了!");
		}
		BusLock busLock = lock.get(0);

		BusContractExample bcExample = new BusContractExample();
		BusContractExample.Criteria cbc = bcExample.createCriteria();
		cbc.andContractidEqualTo(bodycontract_id);
		cbc.andBusidEqualTo(busid);
		List<BusContract> list = busContractMapper.selectByExample(bcExample);
		if (list.isEmpty()) {
			BusContract record = new BusContract();
			record.setCity(busLock.getCity());
			record.setContractid(bodycontract_id);
			record.setBusid(busid);
			record.setEnable(true);
			record.setStartDate(busLock.getStartDate());
			record.setEndDate(busLock.getEndDate());
			record.setCreated(new Date());
			record.setUpdated(record.getCreated());
			busContractMapper.insert(record);
		} else {
			log.warn("车辆重复上刊!bodycontract_id:{},busid:{},lineId:{}", bodycontract_id, busid, bus.getLineId());
		}

	}

	@Override
	public List<LineBusCpd> queryWorkDone(int bodycontract_id, int lineId, Integer modelId, Category category) {
		JpaBusline lineVo = BuslineRepository.findOne(lineId);
		List<LineBusCpd> r = new ArrayList<LineBusCpd>();
		Bodycontract bc = bodycontractMapper.selectByPrimaryKey(bodycontract_id);
		if (bc == null) {
			throw new RuntimeException("合同丢失!");
		}
		//查已施工表
		BusContractExample example = new BusContractExample();
		BusContractExample.Criteria c = example.createCriteria();
		c.andContractidEqualTo(bodycontract_id);
		List<BusContract> list = busSelectMapper
				.selectWorkDoneBus(bodycontract_id, modelId, lineId, category.ordinal()); //busContractMapper.selectByExample(example);
		List<Integer> idsSet = new ArrayList<Integer>();
		for (BusContract bt : list) {
			idsSet.add(bt.getBusid());
		}
		//查车辆信息
		Map<Integer, Bus> map = new HashMap<Integer, Bus>();

		if (!idsSet.isEmpty()) {
			BusExample subEx = new BusExample();
			BusExample.Criteria c2 = subEx.createCriteria();
			c2.andIdIn(idsSet);
			List<Bus> buslist = busMapper.selectByExample(subEx);
			for (Bus bus : buslist) {
				map.put(bus.getId(), bus);
			}
		}
		for (BusContract busContract : list) {
			LineBusCpd cpd = new LineBusCpd();
			Bus b = map.get(busContract.getBusid());
			cpd.setBus(map.get(busContract.getBusid()));
			cpd.setLine(lineVo);
			cpd.setBusContract(busContract);
			cpd.setSerialNumber(b == null ? StringUtils.EMPTY : String.valueOf(b.getSerialNumber()));
			r.add(cpd);
		}
		return r;
	}
	
	
	/**
	 * 
	 * 按合同查已安装车辆总数 
	 *
	 * @param contractId
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public Map<Integer, Integer/*合同号,合同需求车辆总数*/> countContractDoneCar(List<Integer> contractId) {
		Map<Integer, Integer> subMap = new HashMap<Integer, Integer>();
		if (!contractId.isEmpty()) {
			List<GroupVo> vos = busSelectMapper.countContractDoneCar(contractId);
			for (GroupVo groupVo : vos) {
				subMap.put(groupVo.getGn1(), groupVo.getCount());
			}
		}
		return subMap;
	}
	
	/**
	 * 
	 * 按合同查车辆总数 
	 *
	 * @param contractId
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public Map<Integer, Integer/*合同号,合同需求车辆总数*/> getContractCars(List<Integer> contractId) {
		Map<Integer, Integer> subMap = new HashMap<Integer, Integer>();
		if (!contractId.isEmpty()) {
			List<GroupVo> vos = busSelectMapper.countContractCar(contractId);
			for (GroupVo groupVo : vos) {
				subMap.put(groupVo.getGn1(), groupVo.getCount());
			}
		}
		return subMap;
	}

}
