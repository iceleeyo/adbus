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

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.mysema.query.types.expr.BooleanExpression;
import com.pantuo.ActivitiConfiguration;
import com.pantuo.dao.BodyContractRepository;
import com.pantuo.dao.BusLockRepository;
import com.pantuo.dao.BuslineRepository;
import com.pantuo.dao.DividPayRepository;
import com.pantuo.dao.OffContactRepository;
import com.pantuo.dao.PublishLineRepository;
import com.pantuo.dao.pojo.JapDividPay;
import com.pantuo.dao.pojo.JpaAttachment;
import com.pantuo.dao.pojo.JpaBodyContract;
import com.pantuo.dao.pojo.JpaBus;
import com.pantuo.dao.pojo.JpaBus.Category;
import com.pantuo.dao.pojo.JpaBusLock;
import com.pantuo.dao.pojo.JpaBusline;
import com.pantuo.dao.pojo.JpaOfflineContract;
import com.pantuo.dao.pojo.JpaOfflineContract.OType;
import com.pantuo.dao.pojo.JpaPublishLine;
import com.pantuo.dao.pojo.QJapDividPay;
import com.pantuo.dao.pojo.QJpaBodyContract;
import com.pantuo.dao.pojo.QJpaBusLock;
import com.pantuo.dao.pojo.QJpaBusline;
import com.pantuo.dao.pojo.QJpaOfflineContract;
import com.pantuo.dao.pojo.QJpaPublishLine;
import com.pantuo.mybatis.domain.Bodycontract;
import com.pantuo.mybatis.domain.BodycontractExample;
import com.pantuo.mybatis.domain.Bus;
import com.pantuo.mybatis.domain.BusContract;
import com.pantuo.mybatis.domain.BusContractExample;
import com.pantuo.mybatis.domain.BusExample;
import com.pantuo.mybatis.domain.BusLine;
import com.pantuo.mybatis.domain.BusLock;
import com.pantuo.mybatis.domain.BusLockExample;
import com.pantuo.mybatis.domain.BusModel;
import com.pantuo.mybatis.domain.BusinessCompany;
import com.pantuo.mybatis.domain.BusinessCompanyExample;
import com.pantuo.mybatis.domain.Dividpay;
import com.pantuo.mybatis.domain.DividpayExample;
import com.pantuo.mybatis.domain.Offlinecontract;
import com.pantuo.mybatis.domain.OfflinecontractExample;
import com.pantuo.mybatis.domain.PublishLine;
import com.pantuo.mybatis.domain.PublishLineExample;
import com.pantuo.mybatis.persistence.BodycontractMapper;
import com.pantuo.mybatis.persistence.BusContractMapper;
import com.pantuo.mybatis.persistence.BusLineMapper;
import com.pantuo.mybatis.persistence.BusLockMapper;
import com.pantuo.mybatis.persistence.BusMapper;
import com.pantuo.mybatis.persistence.BusSelectMapper;
import com.pantuo.mybatis.persistence.BusinessCompanyMapper;
import com.pantuo.mybatis.persistence.DividpayMapper;
import com.pantuo.mybatis.persistence.OfflinecontractMapper;
import com.pantuo.mybatis.persistence.PublishLineMapper;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.ActivitiService;
import com.pantuo.service.ActivitiService.TaskQueryType;
import com.pantuo.service.AttachmentService;
import com.pantuo.service.BusLineCheckService;
import com.pantuo.service.MailService;
import com.pantuo.simulate.MailJob;
import com.pantuo.util.BusinessException;
import com.pantuo.util.Constants;
import com.pantuo.util.DateUtil;
import com.pantuo.util.NumberPageUtil;
import com.pantuo.util.OrderException;
import com.pantuo.util.OrderIdSeq;
import com.pantuo.util.Pair;
import com.pantuo.util.Request;
import com.pantuo.util.cglib.ProxyVoForPageOrJson;
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
	BusinessCompanyMapper businessCompanyMapper;
	@Autowired
	BusLineMapper buslineMapper;
	@Autowired
	BusLockMapper busLockMapper;
	@Autowired
	PublishLineMapper publishLineMapper;
	@Autowired
	DividpayMapper dividpayMapper;
	@Autowired
	BusLineMapper busLineMapper;
	@Autowired
	BodycontractMapper bodycontractMapper;
	@Autowired
	OfflinecontractMapper offlinecontractMapper;
	@Autowired
	BodyContractRepository bodyContractRepository;
	@Autowired
	OffContactRepository offContactRepository;
	@Autowired
	PublishLineRepository publishLineRepository;
	@Autowired
	BuslineRepository BuslineRepository;
	@Autowired
	BusContractMapper busContractMapper;
	@Autowired
	AttachmentService attachmentService;

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
	DividPayRepository dividPayRepository;
	@Autowired
	BusLockRepository busLockRepository;

	@Override
	public List<AutoCompleteView> autoCompleteByName(int city, String name, JpaBus.Category category, String tag) {
		List<AutoCompleteView> r = new ArrayList<AutoCompleteView>();
		BooleanExpression query = QJpaBusline.jpaBusline.city.eq(city);
		if (StringUtils.isNotBlank(name)) {
			query = query.and(QJpaBusline.jpaBusline.name.like("%" + name + "%"));
		}
		Pageable p = new PageRequest(0, 900, new Sort("name"));
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
				if (StringUtils.isNotBlank(tag)) {
					String viewString = obj.getName();
					r.add(new AutoCompleteView(viewString, viewString,String.valueOf(obj.getId())));//String.valueOf(obj.getId())
				} else {
					String viewString = obj.getName() + "  " + obj.getLevelStr() + " [" + carNumber + "]";
					r.add(new AutoCompleteView(viewString, viewString, String.valueOf(obj.getId())));//String.valueOf(obj.getId())
				}

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
		Map<String, Integer> map = null;
		BooleanExpression mainQuery = QJpaBodyContract.jpaBodyContract.seriaNum.eq(seriaNum);
		JpaBodyContract body = bodyContractRepository.findOne(mainQuery);
		if (body != null) {
			map = countContractidCarGroupbyLineModel(body.getId(), JpaBus.Category.yunyingche);
		}
		if (map != null) {
			List<JpaBusLock> r = new ArrayList<JpaBusLock>();
			for (JpaBusLock jpaBusLock : list) {
				final Map<String, Object> cblibField = new HashMap<String, Object>(1);
				//往JpaBusLock里多增加一个doneCar字段
				Integer v = map.get(jpaBusLock.getLine().getId() + "#" + jpaBusLock.getModel().getId());
				cblibField.put(String.format(ProxyVoForPageOrJson.FORMATKEY, "doneCar"), v == null ? 0 : v);
				JpaBusLock after = (JpaBusLock) ProxyVoForPageOrJson.andFieldAndGetJavaBean(jpaBusLock, cblibField);
				r.add(after);
			}
			return r;
		}
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

	public List<String> queryLineDesByModelid(int lineId, int modelid) {
		return busSelectMapper.queryLineDesByModelid(lineId, modelid);
	}

	public List<BusinessCompany> queryLineCompanyByModelid(int lineId, int modelid) {
		List<Integer> ids = busSelectMapper.queryLineCompanyByModelid(lineId, modelid);
		if (ids == null || ids.isEmpty())
			return new ArrayList<BusinessCompany>(0);
		BusinessCompanyExample example = new BusinessCompanyExample();
		example.createCriteria().andIdIn(ids);
		return businessCompanyMapper.selectByExample(example);
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
		if (a > 0) {
			for (BusLock busLock : list) {
				if (busLock != null) {
					busLock.setContractId(bodycontract.getId());
					busLock.setStats(JpaBusLock.Status.ready.ordinal());
					busLockMapper.updateByPrimaryKey(busLock);
				}
			}
		} else {
			return new Pair<Boolean, String>(false, "申请合同失败");
		}
		Map<String, Object> initParams = new HashMap<String, Object>();
		initParams.put("username", userId);
		initParams.put(ActivitiService.ORDER_ID, bodycontract.getId());
		initParams.put(ActivitiService.CITY, bodycontract.getCity());
		initParams.put(ActivitiService.THE_COMPANY, bodycontract.getCompany());
		initParams.put(ActivitiService.CLOSED, false);
		initParams.put(ActivitiService.CONTRACT_ENABLE, false);
		initParams.put(ActivitiService.ISUPLOADXY, false);
		initParams.put(ActivitiService.NOW, new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date()));
		ProcessInstance process = runtimeService.startProcessInstanceByKey("busFlowV2", initParams);

		List<Task> tasks = taskService.createTaskQuery().processInstanceId(process.getId())
				.processVariableValueEquals(ActivitiService.CITY, bodycontract.getCity()).orderByTaskCreateTime()
				.desc().listPage(0, 1);
		if (!tasks.isEmpty()) {
			Task task = tasks.get(0);
			taskService.claim(task.getId(), userId);
			//			MailTask mailTask = new MailTask(userId, bodycontract.getId(), null, task.getTaskDefinitionKey(),
			//					Type.sendCompleteMail);
			taskService.complete(task.getId());
			//	mailJob.putMailTask(mailTask);
		}

		return new Pair<Boolean, String>(true, "申请合同成功");
	}

	public Page<OrderView> getBodyContractList(int city, TableRequest req, Principal principal) {
		String userid = Request.getUserId(principal);
		int page = req.getPage(), pageSize = req.getLength();
		Sort sort = req.getSort("created");

		String companyname = req.getFilter("companyname");
		page = page + 1;
		List<Task> tasks = new ArrayList<Task>();
		List<OrderView> leaves = new ArrayList<OrderView>();
		TaskQuery countQuery = taskService.createTaskQuery().processDefinitionKey("busFlowV2");
		TaskQuery queryList = taskService.createTaskQuery().processDefinitionKey("busFlowV2");

		countQuery.taskCandidateOrAssigned(userid);
		queryList.taskCandidateOrAssigned(userid);
		countQuery.processVariableValueEquals(ActivitiService.CITY, city);
		queryList.processVariableValueEquals(ActivitiService.CITY, city).includeProcessVariables();
		if (StringUtils.isNoneBlank(companyname)) {
			countQuery.processVariableValueLike(ActivitiService.THE_COMPANY, "%" + companyname + "%");
			queryList.processVariableValueLike(ActivitiService.THE_COMPANY, "%" + companyname + "%");
		}
		/*排序 */
		Sort.Order sor = sort.getOrderFor("created");
		if (sor != null) {
			queryList.orderByProcessInstanceId();
		} else if ((sor = sort.getOrderFor("taskKey")) != null) {
			queryList.orderByTaskDefinitionKey();//listQuery();
		}
		if (sor != null && sor.getDirection() == Sort.Direction.DESC) {
			queryList.desc();
		} else {
			queryList.asc();
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

	public Page<OrderView> queryOrders(int city, Principal principal, TableRequest req, TaskQueryType tqType,
			String actionType) {
		String userid = Request.getUserId(principal);

		int page = req.getPage(), pageSize = req.getLength();
		Sort sort = req.getSort("created");
		page = page + 1;
		String companyname = req.getFilter("companyname"), taskKey = req.getFilter("taskKey");
		List<OrderView> orders = new ArrayList<OrderView>();
		ProcessInstanceQuery countQuery = runtimeService.createProcessInstanceQuery()
				.processDefinitionKey(BODY_ACTIVITY).variableValueEquals(ActivitiService.CITY, city);

		ProcessInstanceQuery listQuery = runtimeService.createProcessInstanceQuery()
				.processDefinitionKey(BODY_ACTIVITY).variableValueEquals(ActivitiService.CITY, city);

		/* 运行中的订单和 我的订单区分*/
		if (StringUtils.isNotBlank(userid) && tqType == TaskQueryType.my) {
			countQuery.involvedUser(userid);
			listQuery.involvedUser(userid);
		}
		if (StringUtils.equals(actionType, "work")) {
			countQuery.variableValueEquals(ActivitiService.ISUPLOADXY, true);
			listQuery.variableValueEquals(ActivitiService.ISUPLOADXY, true);
		}
		//runtimeService.createNativeProcessInstanceQuery().sql("SELECT * FROM " + managementService.getTableName(ProcessInstance.class)).list().size());
		/*按签约公司查询 */
		if (StringUtils.isNoneBlank(companyname)) {
			countQuery.variableValueLike(ActivitiService.THE_COMPANY, "%" + companyname + "%");
			listQuery.variableValueLike(ActivitiService.THE_COMPANY, "%" + companyname + "%");
		}
		setVarFilter(taskKey, countQuery, listQuery);
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
			Task top1Task = tasks.get(0);
			v.setTask(top1Task);
			v.setHaveTasks(tasks.size());
			v.setTask_name(top1Task.getName());
			orders.add(v);
		}

		setCars(orders, contractIds);
		Pageable p = new PageRequest(page, pageSize, sort);
		org.springframework.data.domain.PageImpl<OrderView> r = new org.springframework.data.domain.PageImpl<OrderView>(
				orders, p, pageUtil.getTotal());
		return r;
	}

	private void setVarFilter(String taskKey, ProcessInstanceQuery countQuery, ProcessInstanceQuery listQuery) {
		if (StringUtils.isNoneBlank(taskKey) && !StringUtils.startsWith(taskKey, ActivitiService.R_DEFAULTALL)) {
			if (StringUtils.equals(ActivitiService.OrderStatus.be_contractEnable.name(), taskKey)) {
				//待合同生效
				countQuery.variableValueEquals(ActivitiService.CONTRACT_ENABLE, false);
				listQuery.variableValueEquals(ActivitiService.CONTRACT_ENABLE, false);
			} else if (StringUtils.equals(ActivitiService.OrderStatus.payment.name(), taskKey)) {
				//已支付待审核
				countQuery.variableValueEquals(ActivitiService.CONTRACT_ENABLE, true).variableValueEquals(
						"paymentResult", false);
				listQuery.variableValueEquals(ActivitiService.CONTRACT_ENABLE, true).variableValueEquals(
						"paymentResult", false);
			} else if (StringUtils.equals(ActivitiService.OrderStatus.be_workcomple.name(), taskKey)) {
				//待施工完成
				countQuery.variableValueEquals("paymentResult", true);
				listQuery.variableValueEquals("paymentResult", true);
			}
		}
	}

	private void setCars(List<OrderView> orders, List<Integer> contractIds) {
		Map<Integer, Integer> countMap = getContractCars(contractIds);
		Map<Integer, Integer> doneMap = countContractDoneCar(contractIds);
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

		String companyname = req.getFilter("companyname");
		page = page + 1;
		List<OrderView> orders = new ArrayList<OrderView>();

		HistoricProcessInstanceQuery countQuery = historyService.createHistoricProcessInstanceQuery()
				.processDefinitionKey(BODY_ACTIVITY).variableValueEquals(ActivitiService.CITY, city).finished();

		HistoricProcessInstanceQuery listQuery = historyService.createHistoricProcessInstanceQuery()
				.processDefinitionKey(BODY_ACTIVITY).variableValueEquals(ActivitiService.CITY, city)
				.includeProcessVariables().finished();

		/*按签约公司查询 */
		if (StringUtils.isNoneBlank(companyname)) {
			countQuery.variableValueLike(ActivitiService.THE_COMPANY, "%" + companyname + "%");
			listQuery.variableValueLike(ActivitiService.THE_COMPANY, "%" + companyname + "%");
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

	public void updateBusDone(int bodycontract_id, int busid, Principal principal, HttpServletRequest request)
			throws BusinessException {
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
			record.setEnable(false);
			record.setStartDate(busLock.getStartDate());
			record.setEndDate(busLock.getEndDate());
			record.setCreated(new Date());
			record.setUpdated(record.getCreated());
			int a = busContractMapper.insert(record);
			if (a > 0) {
				attachmentService.saveAttachment(request, Request.getUserId(principal), record.getId(),
						JpaAttachment.Type.workP, null);
			}
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

	/**
	 * 
	 * 查单个合同 按车辆和类型分组算车辆总数 
	 *
	 * @param contractId
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public Map<String, Integer/*lineId+modelid,合同需求车辆总数*/> countContractidCarGroupbyLineModel(Integer contractId,
			Category category) {
		Map<String, Integer> subMap = new HashMap<String, Integer>();
		List<GroupVo> vos = busSelectMapper.countContractidCarGroupbyLineModel(contractId, category.ordinal());
		for (GroupVo groupVo : vos) {
			subMap.put(groupVo.getGn1() + "#" + groupVo.getGn2(), groupVo.getCount());
			String line_total = String.valueOf(groupVo.getGn1() + "#0");
			if (!subMap.containsKey(line_total)) {
				subMap.put(line_total, 0);
			}
			subMap.put(line_total, subMap.get(line_total) + groupVo.getCount());
		}
		return subMap;
	}

	@Override
	public LineBusCpd selectLineBusCpd(int busContractId, int lineid) {
		LineBusCpd lineBusCpd = new LineBusCpd();
		BusContract busContract = busContractMapper.selectByPrimaryKey(busContractId);
		JpaBusline jpaBusline = buslineRepository.findOne(lineid);
		Bus bus = null;
		if (busContract != null) {
			bus = busMapper.selectByPrimaryKey(busContract.getBusid());
			lineBusCpd.setBus(bus);
			lineBusCpd.setBuslock(getBusLock(busContract.getContractid(), bus.getLineId(), bus.getModelId()));
		}
		lineBusCpd.setBusContract(busContract);
		lineBusCpd.setLine(jpaBusline);
		return lineBusCpd;
	}

	public BusLock getBusLock(int contractId, int lineid, int modleId) {
		BusLockExample example = new BusLockExample();
		BusLockExample.Criteria criteria = example.createCriteria();
		criteria.andContractIdEqualTo(contractId);
		criteria.andLineIdEqualTo(lineid);
		criteria.andModelIdEqualTo(modleId);
		List<BusLock> list = busLockMapper.selectByExample(example);
		if (list.isEmpty()) {
			example = new BusLockExample();
			criteria = example.createCriteria();
			criteria.andContractIdEqualTo(contractId);
			criteria.andLineIdEqualTo(lineid);
			criteria.andModelIdEqualTo(0);
			list = busLockMapper.selectByExample(example);
		}

		return list.isEmpty() ? null : list.get(0);
	}

	@Override
	public Pair<Boolean, String> confirm_bus(int busContractId, int lineid, String startdate, String endDate,
			Principal principal) throws ParseException {
		BusContract busContract = busContractMapper.selectByPrimaryKey(busContractId);
		if (busContract != null) {
			if (StringUtils.isBlank(busContract.getUserid())) {
				BusLockExample example = new BusLockExample();
				BusLockExample.Criteria criteria = example.createCriteria();
				criteria.andContractIdEqualTo(busContract.getContractid());
				criteria.andLineIdEqualTo(lineid);
				if (busLockMapper.selectByExample(example).size() > 0) {
					BusLock busLock = busLockMapper.selectByExample(example).get(0);
					if (busLock.getRemainNuber() - 1 < 0) {
						return new Pair<Boolean, String>(false, "已经超出合同规定上刊车辆数，操作失败！");
					} else {
						busLock.setRemainNuber(busLock.getRemainNuber() - 1);
						busLockMapper.updateByPrimaryKey(busLock);
					}
				}
			}
			busContract.setUserid(Request.getUserId(principal));
			busContract.setUpdated(new Date());
			busContract.setEnable(true);
			busContract.setStartDate((Date) new SimpleDateFormat("yyyy-MM-dd").parseObject(startdate));
			busContract.setEndDate((Date) new SimpleDateFormat("yyyy-MM-dd").parseObject(endDate));
			if (busContractMapper.updateByPrimaryKey(busContract) > 0) {
				return new Pair<Boolean, String>(true, "操作成功");
			} else {
				return new Pair<Boolean, String>(false, "操作失败");
			}
		} else {
			return new Pair<Boolean, String>(false, "信息丢失");
		}
	}

	public List<LineBusCpd> getlines(Integer[] ids, Map<Integer, String> map) {
		List<Integer> idsList = new ArrayList<Integer>();
		for (int i = 0; i < ids.length; i++) {
			idsList.add(ids[i]);
		}
		BooleanExpression query = QJpaBusline.jpaBusline.id.in(idsList);
		List<JpaBusline> list = (List<JpaBusline>) buslineRepository.findAll(query);
		List<LineBusCpd> cpds = new ArrayList<LineBusCpd>();
		for (JpaBusline jpaBusline : list) {
			LineBusCpd cpd = new LineBusCpd();
			cpd.setImpSite(map.get(jpaBusline.getId()));
			if (jpaBusline != null) {
				cpd.setLine(jpaBusline);
			}
			cpds.add(cpd);
		}
		return cpds;
	}

	@Override
	public Pair<Boolean, String> saveOffContract(Offlinecontract offcontract, long seriaNum, String userId,
			String signDate1, String otype) throws ParseException {
	
		offcontract.setDays(0);
		offcontract.setTotalNum(0);
		if (null != offcontract.getId() && offcontract.getId() > 0) {
			Offlinecontract contract = offlinecontractMapper.selectByPrimaryKey(offcontract.getId());
			contract.setUpdated(new Date());
			com.pantuo.util.BeanUtils.copyProperties(offcontract, contract);
			int a = offlinecontractMapper.updateByPrimaryKey(contract);
			if (a > 0) {
				return new Pair<Boolean, String>(true, "合同修改成功");
			} else {
				return new Pair<Boolean, String>(false, "合同修改失败");
			}
		}

		PublishLineExample example = new PublishLineExample();
		PublishLineExample.Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userId);
		criteria.andSeriaNumEqualTo(seriaNum);
		List<PublishLine> list = publishLineMapper.selectByExample(example);
		DividpayExample example2 = new DividpayExample();
		DividpayExample.Criteria criteria2 = example2.createCriteria();
		criteria2.andSeriaNumEqualTo(seriaNum);
		List<Dividpay> list2 = dividpayMapper.selectByExample(example2);
		if (list2.size() == 0) {
			//return new Pair<Boolean, String>(false, "请设置合同分期信息");
		}
		if (list.size() == 0) {
			return new Pair<Boolean, String>(false, "请发布线路");
		}
		offcontract.setCreator(userId);
		offcontract.setCreated(new Date());
		offcontract.setUpdated(new Date());
		offcontract.setIsSchedule(false);
		try {
			offcontract.setOtype(OType.valueOf(otype).ordinal());
		} catch (Exception e) {
			offcontract.setOtype(OType.PRIVATE_STATUS.ordinal());
		}
		if(StringUtils.isNoneBlank(signDate1)){
			Date signDate = (Date) new SimpleDateFormat("yyyy-MM-dd").parseObject(signDate1);
			offcontract.setSignDate(signDate);
		}
		int b = offlinecontractMapper.insert(offcontract);
		if (b > 0) {
			for (PublishLine busLock : list) {
				if (busLock != null) {
					busLock.setContractId(offcontract.getId());
					busLock.setStats(JpaBusLock.Status.ready.ordinal());
					publishLineMapper.updateByPrimaryKey(busLock);
				}
			}
		} else {
			return new Pair<Boolean, String>(false, "申请合同失败");
		}

		return new Pair<Boolean, String>(true, "创建合同成功");
	}

	@Override
	public List<JpaPublishLine> getpublishLineBySeriNum(long seriaNum) {
		BooleanExpression query = QJpaPublishLine.jpaPublishLine.seriaNum.eq(seriaNum);
		List<JpaPublishLine> list = (List<JpaPublishLine>) publishLineRepository.findAll(query);
		return list;
	}

	@Override
	public Pair<Boolean, String> savePublishLine(PublishLine publishLine, String startD)
			throws ParseException {
		publishLine.setUpdated(new Date());
		Date date1 = (Date) new SimpleDateFormat("yyyy-MM-dd").parseObject(startD);
		Date date2 = DateUtil.dateAdd(date1,publishLine.getDays());
		publishLine.setStartDate(date1);
		publishLine.setEndDate(date2);
		if (null != publishLine.getId() && publishLine.getId() > 0) {
			PublishLine publishLine2 = publishLineMapper.selectByPrimaryKey(publishLine.getId());
			com.pantuo.util.BeanUtils.copyProperties(publishLine, publishLine2);
			if (publishLineMapper.updateByPrimaryKey(publishLine2) > 0) {
				return new Pair<Boolean, String>(true, "修改成功");
			}
			return new Pair<Boolean, String>(false, "修改失败");
		}
		OfflinecontractExample example = new OfflinecontractExample();
		OfflinecontractExample.Criteria criteria = example.createCriteria();
		criteria.andSeriaNumEqualTo(publishLine.getSeriaNum());
		List<Offlinecontract> list = offlinecontractMapper.selectByExample(example);
		if (list.size() > 0) {
			publishLine.setContractId(list.get(0).getId());
		}
		publishLine.setStats(JpaBusLock.Status.ready.ordinal());
		publishLine.setCreated(new Date());
		publishLine.setRemainNuber(0);
		if (publishLineMapper.insert(publishLine) > 0) {
			return new Pair<Boolean, String>(true, "保存成功");
		}
		return new Pair<Boolean, String>(false, "保存失败");
	}

	@Override
public Pair<Boolean, String> saveDivid(Dividpay dividpay, long seriaNum, String userId, String payDate1) throws ParseException {
         if(StringUtils.isBlank(payDate1)){
        	 return new Pair<Boolean, String>(false, "请选择付款日期");
        }
	dividpay.setPayDate((Date) new SimpleDateFormat("yyyy-MM-dd").parseObject(payDate1));
	dividpay.setUpdator(userId);
	dividpay.setStats(0);
	if(null!=dividpay.getId() && dividpay.getId()>0){
		Dividpay dividpay2=dividpayMapper.selectByPrimaryKey(dividpay.getId());
		com.pantuo.util.BeanUtils.copyProperties(dividpay, dividpay2);
		if(dividpayMapper.updateByPrimaryKey(dividpay2)>0){
			return new Pair<Boolean, String>(true, "修改成功");
		}
		return new Pair<Boolean, String>(false, "操作失败");
	}
	dividpay.setSeriaNum(seriaNum);
	if (dividpayMapper.insert(dividpay) > 0) {
		return new Pair<Boolean, String>(true, "保存成功");
	}
	return new Pair<Boolean, String>(false, "保存失败");
}

	@Override
	public List<JapDividPay> getDividPay(long seriaNum) {
		BooleanExpression query = QJapDividPay.japDividPay.seriaNum.eq(seriaNum);
		List<JapDividPay> list = (List<JapDividPay>) dividPayRepository.findAll(query);
		return list;
	}

	@Override
	public Pair<Boolean, String> removePublishLine(Principal principal, int city, long seriaNum, int id) {
		PublishLine publishLine = publishLineMapper.selectByPrimaryKey(id);
		if (publishLine == null) {
			return new Pair<Boolean, String>(false, "信息丢失");
		}
		if (publishLine.getSalesNumber() != publishLine.getRemainNuber()) {
			return new Pair<Boolean, String>(false, "已有车辆在刊，不能删除");
		}
		if (publishLineMapper.deleteByPrimaryKey(id) > 0) {
			return new Pair<Boolean, String>(true, "删除成功");
		}
		return new Pair<Boolean, String>(false, "操作失败");
	}

	@Override
	public boolean removedividPay(Principal principal, int city, long seriaNum, int id) {
		DividpayExample example = new DividpayExample();
		DividpayExample.Criteria criteria = example.createCriteria();
		criteria.andCityEqualTo(city);
		criteria.andSeriaNumEqualTo(seriaNum);
		criteria.andIdEqualTo(id);
		List<Dividpay> list = dividpayMapper.selectByExample(example);
		if (list.size() > 0) {
			if (dividpayMapper.deleteByPrimaryKey(id) > 0) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Page<JpaOfflineContract> queryOfflineContract(int city, TableRequest req, int page, int pageSize, Sort sort) {
		if (page < 0)
			page = 0;
		if (pageSize < 1)
			pageSize = 1;
		if (sort == null)
			sort = new Sort("id");
		Pageable p = new PageRequest(page, pageSize, sort);
		BooleanExpression query = QJpaOfflineContract.jpaOfflineContract.city.eq(city);
		String contractCode = req.getFilter("contractCode"),otype=req.getFilter("otype");
		if (StringUtils.isNotBlank(contractCode)) {
			query = query.and(QJpaOfflineContract.jpaOfflineContract.contractCode.like("%" + contractCode + "%"));
		}
		if (StringUtils.isNotBlank(otype)) {
			query = query.and(QJpaOfflineContract.jpaOfflineContract.otype.eq(OType.valueOf(otype)));
		}
		return query == null ? offContactRepository.findAll(p) : offContactRepository.findAll(query, p);
	}

	@Override
	public Offlinecontract findOffContractById(int contract_id) {
		return offlinecontractMapper.selectByPrimaryKey(contract_id);
	}

	@Override
	public Dividpay queryDividPayByid(int id) {
		return dividpayMapper.selectByPrimaryKey(id);
	}

	@Override
	public JpaPublishLine queryPublishLineByid(int id) {
		//BooleanExpression query=QJpaPublishLine.jpaPublishLine.id.eq(id);
		return publishLineRepository.findOne(id);
	}

	@Override
	public Page<JpaPublishLine> queryAllPublish(int cityId, TableRequest req, int page, int length, Sort sort) {
		if (page < 0)
			page = 0;
		if (length < 1)
			length = 1;
		if (sort == null)
			sort = new Sort("id");
		Pageable p = new PageRequest(page, length, sort);
		BooleanExpression query = QJpaPublishLine.jpaPublishLine.city.eq(cityId);
		query = query.and(QJpaPublishLine.jpaPublishLine.OfflineContract.id.isNotNull());
		String contractCode = req.getFilter("contractCode"), contractid = req.getFilter("contractid"), model = req
				.getFilter("model"), linename = req.getFilter("linename"), company = req.getFilter("company");
		if (StringUtils.isNotBlank(contractCode)) {
		//	query = query.and(QJpaPublishLine.jpaPublishLine.OfflineContract.contractCode
		//			.like("%" + contractCode + "%"));
		}
		if (StringUtils.isNotBlank(contractCode)) {
			int cid = NumberUtils.toInt(contractCode);
			query = query.and(QJpaPublishLine.jpaPublishLine.OfflineContract.id.eq(cid));
		}
		if (StringUtils.isNotBlank(model)) {
			query = query.and(QJpaPublishLine.jpaPublishLine.model.name.like("%" + model + "%"));
		}
		if (StringUtils.isNotBlank(linename)) {
			query = query.and(QJpaPublishLine.jpaPublishLine.line.name.like("%" + linename + "%"));
		}
		if (StringUtils.isNotBlank(company) && !StringUtils.equals(company, "defaultAll")) {
			query = query.and(QJpaPublishLine.jpaPublishLine.jpaBusinessCompany.name.like("%" + company + "%"));
		}
		return query == null ? publishLineRepository.findAll(p) : publishLineRepository.findAll(query, p);
	}

	@Override
	public List<AutoCompleteView> ContractAutoCompleteByName(int city, String name) {
		List<AutoCompleteView> r = new ArrayList<AutoCompleteView>();
		BooleanExpression query = QJpaOfflineContract.jpaOfflineContract.city.eq(city);
		if (StringUtils.isNotBlank(name)) {
			query = query.and(QJpaOfflineContract.jpaOfflineContract.contractCode.like("%" + name + "%"));
		}
		Pageable p = new PageRequest(0, 30, new Sort(Direction.fromString("desc"), "id"));
		Page<JpaOfflineContract> list = (Page<JpaOfflineContract>) offContactRepository.findAll(query, p);
		if (list.getContent().size() > 0) {
			for (JpaOfflineContract obj : list) {
				String lable = obj.getContractCode();
				String value = String.valueOf(obj.getId());
				r.add(new AutoCompleteView(lable, lable, value));
			}
		}
		return r;
	}

	@Override
	public Pair<Boolean, String> saveLine(BusLine busLine) {
		busLine.setCreated(new Date());
		busLine.setMonth1day(0);
		busLine.setMonth2day(0);
		busLine.setMonth3day(0);
		busLine.setToday(0);
		busLine.setPersons(0);
		if (busLineMapper.insert(busLine) > 0) {
			return new Pair<Boolean, String>(true, "添加线路成功");
		}
		return new Pair<Boolean, String>(false, "保存失败");
	}
}
