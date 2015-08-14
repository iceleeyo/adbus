package com.pantuo.service.impl;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
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
import com.pantuo.dao.pojo.JpaBusLock;
import com.pantuo.dao.pojo.JpaBusline;
import com.pantuo.dao.pojo.JpaOrders;
import com.pantuo.dao.pojo.QJpaBusLock;
import com.pantuo.dao.pojo.QJpaBusline;
import com.pantuo.mybatis.domain.Bodycontract;
import com.pantuo.mybatis.domain.BusLock;
import com.pantuo.mybatis.domain.BusLockExample;
import com.pantuo.mybatis.persistence.BodycontractMapper;
import com.pantuo.mybatis.persistence.BusLineMapper;
import com.pantuo.mybatis.persistence.BusLockMapper;
import com.pantuo.mybatis.persistence.BusSelectMapper;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.ActivitiService;
import com.pantuo.service.ActivitiService.TaskQueryType;
import com.pantuo.service.BusLineCheckService;
import com.pantuo.service.MailService;
import com.pantuo.service.MailTask;
import com.pantuo.service.MailTask.Type;
import com.pantuo.simulate.MailJob;
import com.pantuo.util.NumberPageUtil;
import com.pantuo.util.OrderIdSeq;
import com.pantuo.util.Pair;
import com.pantuo.util.Request;
import com.pantuo.vo.GroupVo;
import com.pantuo.web.view.AutoCompleteView;
import com.pantuo.web.view.OrderView;

@Service
public class BusLineCheckServiceImpl implements BusLineCheckService {

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
		//总数-被占用数据
		return busLineCarCount - carIds;
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
		//buslock.setEnable(true);
		buslock.setContractId(0);
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
	public Page<OrderView> getBodyContractList(int city, TableRequest req, Principal principal){
		String userid = Request.getUserId(principal);
		int page = req.getPage(), pageSize = req.getLength();
		Sort sort = req.getSort("created");

		String  userIdQuery = req.getFilter("userId");
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

		/*
		ProcessInstanceQuery debugQuery = runtimeService.createProcessInstanceQuery()
				.processDefinitionKey(MAIN_PROCESS).includeProcessVariables()
				.variableValueEquals(ActivitiService.CITY, city);
		List<ProcessInstance> psff = debugQuery.list();
		for (ProcessInstance processInstance : psff) {
			System.out.println(processInstance.getProcessVariables());
		}*/

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
		for (ProcessInstance processInstance : processInstances) {

			List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId())
					.processVariableValueEquals(ActivitiService.CITY, city).includeProcessVariables()
					.orderByTaskCreateTime().desc().list();

			Integer orderid = (Integer) tasks.get(0).getProcessVariables().get(ActivitiService.ORDER_ID);
			OrderView v = new OrderView();
			JpaBodyContract order = bodyContractRepository.findOne(orderid);
			v.setJpaBodyContract(order);
			v.setProcessInstanceId(processInstance.getId());
			//v.setProcessDefinition(getProcessDefinition(processInstance.getProcessDefinitionId()));
			Task top1Task = tasks.get(0);
			v.setTask(top1Task);
			v.setHaveTasks(tasks.size());
			//if (StringUtils.isNoneBlank(taskKey) && !StringUtils.startsWith(taskKey, ActivitiService.R_DEFAULTALL)) {
			v.setTask_name(getOrderState(top1Task.getProcessVariables(), top1Task));
			orders.add(v);
		}
		Pageable p = new PageRequest(page, pageSize, sort);
		org.springframework.data.domain.PageImpl<OrderView> r = new org.springframework.data.domain.PageImpl<OrderView>(
				orders, p, pageUtil.getTotal());
		return r;
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
		BusLock busLock=busLockMapper.selectByPrimaryKey(id);
		if(busLock==null){
			return new Pair<Boolean, String>(false,"信息丢失");
		}
		busLock.setLockExpiredTime((Date) new SimpleDateFormat("yyyy-MM-dd").parseObject(lockDate));
		if(busLockMapper.updateByPrimaryKey(busLock)>0){
			return new Pair<Boolean, String>(true,"设置锁定时间成功");
		}else{
			return new Pair<Boolean, String>(false,"操作失败");
		}
	}
}
