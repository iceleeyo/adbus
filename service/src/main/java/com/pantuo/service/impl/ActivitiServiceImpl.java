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
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.alibaba.druid.support.json.JSONUtils;
import com.pantuo.ActivitiConfiguration;
import com.pantuo.dao.PayPlanRepository;
import com.pantuo.dao.pojo.JpaCity;
import com.pantuo.dao.pojo.JpaOrders;
import com.pantuo.dao.pojo.JpaPayPlan;
import com.pantuo.dao.pojo.JpaProduct;
import com.pantuo.dao.pojo.UserDetail;
import com.pantuo.dao.pojo.JpaPayPlan.PayState;
import com.pantuo.mybatis.domain.Contract;
import com.pantuo.mybatis.domain.Invoice;
import com.pantuo.mybatis.domain.InvoiceDetail;
import com.pantuo.mybatis.domain.Orders;
import com.pantuo.mybatis.domain.PayPlan;
import com.pantuo.mybatis.domain.PayPlanExample;
import com.pantuo.mybatis.domain.Paycontract;
import com.pantuo.mybatis.domain.Product;
import com.pantuo.mybatis.persistence.ContractMapper;
import com.pantuo.mybatis.persistence.InvoiceDetailMapper;
import com.pantuo.mybatis.persistence.InvoiceMapper;
import com.pantuo.mybatis.persistence.OrdersMapper;
import com.pantuo.mybatis.persistence.PayPlanMapper;
import com.pantuo.mybatis.persistence.PaycontractMapper;
import com.pantuo.mybatis.persistence.ProductMapper;
import com.pantuo.pojo.HistoricTaskView;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.ActivitiService;
import com.pantuo.service.AttachmentService;
import com.pantuo.service.CityService;
import com.pantuo.service.ContractService;
import com.pantuo.service.CpdService;
import com.pantuo.service.MailService;
import com.pantuo.service.MailTask;
import com.pantuo.service.MailTask.Type;
import com.pantuo.service.OrderService;
import com.pantuo.service.ProductService;
import com.pantuo.service.SuppliesService;
import com.pantuo.service.security.Request;
import com.pantuo.simulate.MailJob;
import com.pantuo.util.BeanUtils;
import com.pantuo.util.Constants;
import com.pantuo.util.DateUtil;
import com.pantuo.util.NumberPageUtil;
import com.pantuo.util.OrderException;
import com.pantuo.util.OrderIdSeq;
import com.pantuo.util.Pair;
import com.pantuo.web.view.CardView;
import com.pantuo.web.view.OrderPlanView;
import com.pantuo.web.view.OrderView;
import com.pantuo.web.view.SuppliesView;

@Service
public class ActivitiServiceImpl implements ActivitiService {
	private static Logger log = LoggerFactory.getLogger(ActivitiServiceImpl.class);
	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private HistoryService historyService;

	private ProcessInstance instance;
	// private SimpleSmtpServer mailServer;
	@Autowired
	private OrderService orderService;
	@Autowired
	private PayPlanMapper payPlanMapper;

	@Autowired
	private PayPlanRepository payPlanRepository;
	@Autowired
	@Lazy
	private ProductService productService;
	@Autowired
	private SuppliesService suppliesService;
	@Autowired
	private OrdersMapper ordersMapper;
	@Autowired
	private PaycontractMapper paycontractMapper;
	@Autowired
	private ContractMapper contractMapper;
	@Autowired
	private InvoiceMapper invoiceMapper;
	@Autowired
	private ProductMapper productMapper;
	@Autowired
	private InvoiceDetailMapper invoiceDetailMapper;
	@Autowired
	private CityService cityService;

	@Autowired
	private CpdService cpdService;

	@Autowired
	private MailService mailService;
	@Autowired
	private ContractService contractService;
	@Autowired
	private AttachmentService attachmentService;

	@Autowired
	private MailJob mailJob;

	/**
	 * @deprecated
	 * @see com.pantuo.service.ActivitiService#running(int, java.lang.String, com.pantuo.pojo.TableRequest)
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	public Page<OrderView> running(int city, Principal principal, TableRequest req) {
		int page = req.getPage(), pageSize = req.getLength();
		Sort sort = req.getSort("created");
		page = page + 1;
		String longId = req.getFilter("longOrderId"), taskKey = req.getFilter("taskKey");
		Long longOrderId = StringUtils.isBlank(longId) ? 0 : NumberUtils.toLong(longId);

		//-----begin
		List<OrderView> orders = new ArrayList<OrderView>();
		int totalnum = runtimeService.createProcessInstanceQuery().processDefinitionKey(MAIN_PROCESS).variableValueEquals(ActivitiService.CITY, city).list().size();
		NumberPageUtil pageUtil = new NumberPageUtil((int) totalnum, page, pageSize);

		List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().processDefinitionKey(MAIN_PROCESS).variableValueEquals(ActivitiService.CITY, city)
				.orderByProcessInstanceId().desc().listPage(pageUtil.getLimitStart(), pageUtil.getPagesize());
		for (ProcessInstance processInstance : processInstances) {
			List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).processVariableValueEquals(ActivitiService.CITY, city)
					.includeProcessVariables().orderByTaskCreateTime().desc().listPage(0, 1);
			Integer orderid = (Integer) tasks.get(0).getProcessVariables().get(ORDER_ID);
			OrderView v = new OrderView();
			JpaOrders order = orderService.queryOrderDetail(orderid, principal);
			if (order != null) {
				v.setOrder(order);
				JpaProduct product = productService.findById(order.getProductId());
				v.setProduct(product);
				v.setProcessInstanceId(processInstance.getId());
				if (!tasks.isEmpty()) {
					v.setTask(tasks.get(0));
				}
				orders.add(v);
			}
		}
		Pageable p = new PageRequest(page, pageSize, sort);
		org.springframework.data.domain.PageImpl<OrderView> r = new org.springframework.data.domain.PageImpl<OrderView>(orders, p, pageUtil.getTotal());
		return r;
	}

	public Page<OrderView> queryOrders(int city, Principal principal, TableRequest req, TaskQueryType tqType) {
		String userid = Request.getUserId(principal);

		int page = req.getPage(), pageSize = req.getLength();
		Sort sort = req.getSort("created");
		page = page + 1;
		String longId = req.getFilter("longOrderId"), userIdQuery = req.getFilter("userId"), taskKey = req.getFilter("taskKey"), productId = req.getFilter("productId"), customerName = req
				.getFilter("customerName"), salesMan = req.getFilter("salesMan");
		Long longOrderId = StringUtils.isBlank(longId) ? 0 : NumberUtils.toLong(longId);
		List<OrderView> orders = new ArrayList<OrderView>();

		ProcessInstanceQuery countQuery = runtimeService.createProcessInstanceQuery().processDefinitionKey(MAIN_PROCESS).variableValueEquals(ActivitiService.CITY, city);

		ProcessInstanceQuery listQuery = runtimeService.createProcessInstanceQuery().processDefinitionKey(MAIN_PROCESS).variableValueEquals(ActivitiService.CITY, city);

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
		if (StringUtils.isNoneBlank(customerName)) {
			countQuery.variableValueLike(ActivitiService.COMPANY, "%" + customerName + "%");
			listQuery.variableValueLike(ActivitiService.COMPANY, "%" + customerName + "%");
		}
		if (StringUtils.isNoneBlank(salesMan)) {
			countQuery.variableValueEquals(ActivitiService.CREAT_USERID, salesMan);
			listQuery.variableValueEquals(ActivitiService.CREAT_USERID, salesMan);
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
		for (ProcessInstance processInstance : processInstances) {

			List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).processVariableValueEquals(ActivitiService.CITY, city)
					.includeProcessVariables().orderByTaskCreateTime().desc().list();

			Integer orderid = (Integer) tasks.get(0).getProcessVariables().get(ORDER_ID);
			OrderView v = new OrderView();
			JpaOrders order = orderService.queryOrderDetail(orderid, principal);
			ProcessInstance instance = runtimeService.createProcessInstanceQuery().includeProcessVariables().variableValueEquals(ORDER_ID, orderid).singleResult();
			Boolean approve1 = (Boolean) instance.getProcessVariables().get("approve1Result");
			if (approve1 != null && approve1) {
				v.setApprove1Result(true);
			} else {
				v.setApprove1Result(false);
			}
			if (order != null) {
				v.setOrder(order);
			}
			if (tqType == TaskQueryType.all_running && order != null) {
				JpaProduct product = productService.findById(order.getProductId());
				v.setProduct(product);
			}
			v.setProcessInstanceId(processInstance.getId());
			//v.setProcessDefinition(getProcessDefinition(processInstance.getProcessDefinitionId()));
			Task top1Task = tasks.get(0);
			v.setTask(top1Task);
			v.setHaveTasks(tasks.size());
			//if (StringUtils.isNoneBlank(taskKey) && !StringUtils.startsWith(taskKey, ActivitiService.R_DEFAULTALL)) {
			v.setTask_name(getOrderState(top1Task.getProcessVariables()));
			//}

			//如果按订单号查询,避免20000000001 也能查询到订单号1的订单
			boolean r = true;
			if (longOrderId > 0) {
				r = longOrderId == OrderIdSeq.getIdFromDate(orderid, order == null ? new Date() : order.getCreated());
			}
			if (r) {
				orders.add(v);
			}
		}
		Pageable p = new PageRequest(page, pageSize, sort);
		org.springframework.data.domain.PageImpl<OrderView> r = new org.springframework.data.domain.PageImpl<OrderView>(orders, p, pageUtil.getTotal());
		return r;
	}

	@Override
	public Pair<Boolean, String> checkApproveResult(String orderid) {
		ProcessInstance instance = runtimeService.createProcessInstanceQuery().includeProcessVariables().variableValueEquals(ORDER_ID, orderid).singleResult();
		Boolean approve1 = (Boolean) instance.getProcessVariables().get("approve1Result");
		if (approve1) {
			return new Pair<Boolean, String>(false, "该订单已经初审通过不能修改");
		}
		return new Pair<Boolean, String>(true, "可以修改");
	}

	private void setVarFilter(String taskKey, ProcessInstanceQuery countQuery, ProcessInstanceQuery listQuery) {
		if (StringUtils.isNoneBlank(taskKey) && !StringUtils.startsWith(taskKey, ActivitiService.R_DEFAULTALL)) {
			if (StringUtils.equals(ActivitiService.OrderStatus.payment.name(), taskKey) || StringUtils.equals(ActivitiService.OrderStatus.setPayPlan.name(), taskKey)
					|| StringUtils.equals(ActivitiService.OrderStatus.userFristPay.name(), taskKey)) {
				//未支付
				countQuery.variableValueEquals(ActivitiService.R_USERPAYED, false);
				listQuery.variableValueEquals(ActivitiService.R_USERPAYED, false);
			} else if (StringUtils.equals(ActivitiService.OrderStatus.auth.name(), taskKey)) {
				//已支付待审核
				countQuery.variableValueEquals(ActivitiService.R_USERPAYED, true).variableValueEquals(ActivitiService.R_SCHEDULERESULT, false);
				listQuery.variableValueEquals(ActivitiService.R_USERPAYED, true).variableValueEquals(ActivitiService.R_SCHEDULERESULT, false);
			} else if (StringUtils.equals(ActivitiService.OrderStatus.report.name(), taskKey)) {
				//已排期待上播
				countQuery.variableValueEquals(ActivitiService.R_SCHEDULERESULT, true).variableValueEquals(ActivitiService.R_SHANGBORESULT, false);
				listQuery.variableValueEquals(ActivitiService.R_SCHEDULERESULT, true).variableValueEquals(ActivitiService.R_SHANGBORESULT, false);
			} else if (StringUtils.equals(ActivitiService.OrderStatus.over.name(), taskKey)) {
				//已上播
				countQuery.variableValueEquals(ActivitiService.R_SCHEDULERESULT, true).variableValueEquals(ActivitiService.R_SHANGBORESULT, true);
				listQuery.variableValueEquals(ActivitiService.R_SCHEDULERESULT, true).variableValueEquals(ActivitiService.R_SHANGBORESULT, true);
			}
			//	return findTask(city, userid, req, TaskQueryType.process);
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
	public String getOrderState(Map<String, Object> vars) {
		String r = ActivitiService.paymentString;

		if (ObjectUtils.equals(vars.get(ActivitiService.R_USERPAYED), false)) {
			r = ActivitiService.paymentString;
		}
		if (ObjectUtils.equals(vars.get(ActivitiService.R_USERPAYED), true) && ObjectUtils.equals(vars.get(ActivitiService.R_SCHEDULERESULT), false)) {
			r = ActivitiService.authString;
		}
		if (ObjectUtils.equals(vars.get(ActivitiService.R_SCHEDULERESULT), true) && ObjectUtils.equals(vars.get(ActivitiService.R_SHANGBORESULT), false)) {
			r = ActivitiService.reportString;
		}
		if (ObjectUtils.equals(vars.get(ActivitiService.R_SCHEDULERESULT), true) && ObjectUtils.equals(vars.get(ActivitiService.R_SHANGBORESULT), true)) {
			r = ActivitiService.overString;
		}
		return r;

	}

	public Pair<Boolean, String> closeOrder(int orderid, String closeRemark, String taskid, Principal principal) {

		TaskQuery countQuery = taskService.createTaskQuery().includeProcessVariables().processDefinitionKey(MAIN_PROCESS);

		Task task = countQuery.taskId(taskid).singleResult();
		if (task == null) {
			return new Pair<Boolean, String>(false, Constants.TASK_NOT_EXIT);
		} else {
			Map<String, Object> var = task.getProcessVariables();

			Integer dbOrderId = (Integer) var.get(ActivitiService.ORDER_ID);
			//2:判断订单是否存在
			if (dbOrderId == null) {
				return new Pair<Boolean, String>(false, Constants.ORDER_NOT_EXIT);
			}
			JpaOrders order = orderService.queryOrderDetail(dbOrderId, principal);
			long longOrderId = 0;
			if (order != null) {
				longOrderId = OrderIdSeq.getIdFromDate(order.getId(), order.getCreated());
			} else {
				return new Pair<Boolean, String>(false, Constants.ORDER_NOT_EXIT);
			}
			boolean canClose = false;//世巴管理员可以关闭订单
			if (Request.hasAuth(principal, ActivitiConfiguration.ORDER)) {
				canClose = true;
			} else {
				if (!StringUtils.equals(Request.getUserId(principal), (String) var.get(ActivitiService.CREAT_USERID))) {
					return new Pair<Boolean, String>(false, Constants.CREATEID_WRONG);
				}
				canClose = true;
			}
			if (canClose) {
				if (var.get(ActivitiService.R_USERPAYED) == null || !BooleanUtils.toBoolean((Boolean) var.get(ActivitiService.R_USERPAYED))) {

					try {
						ProcessInstance processInstance = findProcessInstanceByTaskId(taskid);
						runtimeService.setVariable(processInstance.getProcessInstanceId(), ActivitiService.CLOSED, true);
						runtimeService.deleteProcessInstance(processInstance.getProcessInstanceId(), "closeOrder");
						/**
						 * 保存关闭原因
						 */
						if (StringUtils.isNotBlank(closeRemark)) {
							Orders dbOrder = ordersMapper.selectByPrimaryKey(dbOrderId);
							if (dbOrder != null) {
								dbOrder.setCloseRemark(closeRemark);
								ordersMapper.updateByPrimaryKey(dbOrder);
							}
						}

					} catch (Exception e) {
					}

				} else {

					return new Pair<Boolean, String>(false, String.format(Constants.NOT_SUPPORT, longOrderId));
				}
			}

			return new Pair<Boolean, String>(false, String.format(Constants.CLOSEORDER_SUCCESS, longOrderId));
		}

	}

	public Page<OrderView> findTask(int city, Principal principal, TableRequest req, TaskQueryType tqType) {
		String userid = Request.getUserId(principal);
		int page = req.getPage(), pageSize = req.getLength();
		Sort sort = req.getSort("created");

		String longId = req.getFilter("longOrderId"), userIdQuery = req.getFilter("userId"), taskKey = req.getFilter("taskKey"), customerName = req.getFilter("customerName");
		Long longOrderId = StringUtils.isBlank(longId) ? 0 : NumberUtils.toLong(longId);
		page = page + 1;
		List<Task> tasks = new ArrayList<Task>();
		List<OrderView> leaves = new ArrayList<OrderView>();
		TaskQuery countQuery = taskService.createTaskQuery().processDefinitionKey(MAIN_PROCESS);
		TaskQuery queryList = taskService.createTaskQuery().processDefinitionKey(MAIN_PROCESS);
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
			countQuery.processVariableValueEquals(ActivitiService.ORDER_ID, OrderIdSeq.checkAndGetRealyOrderId(longOrderId));
			queryList.processVariableValueEquals(ActivitiService.ORDER_ID, OrderIdSeq.checkAndGetRealyOrderId(longOrderId));
		}

		if (StringUtils.isNoneBlank(customerName)) {
			countQuery.processVariableValueLike(ActivitiService.COMPANY, "%" + customerName + "%");
			queryList.processVariableValueLike(ActivitiService.COMPANY, "%" + customerName + "%");
		}
		if (StringUtils.isNoneBlank(userIdQuery)) {
			countQuery.processVariableValueLike(ActivitiService.CREAT_USERID, "%" + userIdQuery + "%");
			queryList.processVariableValueLike(ActivitiService.CREAT_USERID, "%" + userIdQuery + "%");
		}
		if (StringUtils.isNoneBlank(taskKey) && !StringUtils.startsWith(taskKey, ActivitiService.R_DEFAULTALL)) {
			countQuery.taskDefinitionKey(taskKey);
			queryList.taskDefinitionKey(taskKey);
			//queryList.taskVariableValueEquals("paymentResult", true);
		}
		long t = System.currentTimeMillis();
		long c = countQuery.count();
		NumberPageUtil pageUtil = new NumberPageUtil((int) c, page, pageSize);

		taskOrderBy(sort, queryList);
		tasks = queryList.listPage(pageUtil.getLimitStart(), pageUtil.getPagesize());
		log.info("queryList_time:{}", System.currentTimeMillis() - t);
		//	tasks = query.orderByTaskPriority()
		//		.desc().ororderByTaskCreateTime().desc().listPage(pageUtil.getLimitStart(), pageUtil.getPagesize());

		Map<String, ProcessInstance> processMap = new HashMap<String, ProcessInstance>();

		Map<Integer, JpaOrders> orderMap = new HashMap<Integer, JpaOrders>();

		Set<String> proesccId = new HashSet<String>();
		List<Integer> orders = new ArrayList<Integer>();
		for (Task task : tasks) {
			String processInstanceId = task.getProcessInstanceId();
			proesccId.add(processInstanceId);
			Map<String, Object> var = task.getProcessVariables();
			orders.add((Integer) var.get(ORDER_ID));
		}

		if (!orders.isEmpty()) {

			List<JpaOrders> jpaLists = orderService.getJpaOrders(orders);

			for (JpaOrders jpaOrders : jpaLists) {
				orderMap.put(jpaOrders.getId(), jpaOrders);
			}
		}

		if (!proesccId.isEmpty()) {
			List<ProcessInstance> pList = runtimeService.createProcessInstanceQuery().processInstanceIds(proesccId).list();
			if (pList != null) {
				for (ProcessInstance processInstance : pList) {
					processMap.put(processInstance.getId(), processInstance);
				}
			}
		}

		for (Task task : tasks) {
			String processInstanceId = task.getProcessInstanceId();

			ProcessInstance processInstance = processMap.get(processInstanceId);
			//ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
			//		.processInstanceId(processInstanceId).singleResult();
			Map<String, Object> var = task.getProcessVariables();
			Integer orderid = (Integer) var.get(ORDER_ID);

			OrderView v = new OrderView();
			JpaOrders order = orderMap.get(orderid);//orderService.queryOrderDetail(orderid, principal);
			if (order != null) {
				JpaProduct product = order.getProduct();//productService.findById(order.getProductId());
				v.setProduct(product);
				v.setOrder(order);
				v.setTask(task);
				//log.info(this.getClass().getName() + " debug=> " + task.getTaskDefinitionKey());
				v.setProcessInstanceId(processInstance.getId());
				v.setTask_createTime(task.getCreateTime());
				boolean r = true;
				if (longOrderId > 0) {
					r = longOrderId == OrderIdSeq.getIdFromDate(order.getId(), order.getCreated());
				}

				if (var.get(ActivitiService.R_USERPAYED) == null || !BooleanUtils.toBoolean((Boolean) var.get(ActivitiService.R_USERPAYED))) {
					v.setCanClosed(true);
				}
				if (r) {
					leaves.add(v);
				}
			}
		}
		Pageable p = new PageRequest(page, pageSize, sort);
		org.springframework.data.domain.PageImpl<OrderView> r = new org.springframework.data.domain.PageImpl<OrderView>(leaves, p, pageUtil.getTotal());
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

	public List<OrderView> findMyOrders(int city, String userid, NumberPageUtil page) {
		List<OrderView> orders = new ArrayList<OrderView>();
		List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().processDefinitionKey(MAIN_PROCESS).involvedUser(userid)
				.listPage(page.getLimitStart(), page.getPagesize());
		for (ProcessInstance processInstance : processInstances) {
			List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).processVariableValueEquals(ActivitiService.CITY, city)
					.includeProcessVariables().orderByTaskCreateTime().desc().listPage(0, 1);
			Integer orderid = (Integer) tasks.get(0).getProcessVariables().get(ORDER_ID);
			OrderView v = new OrderView();
			Iterable<JpaOrders> order = orderService.selectOrderByUser(city, userid, orderid);
			if (order.iterator().hasNext()) {
				v.setOrder(order.iterator().next());
			}
			//v.setProcessInstance(processInstance);
			v.setProcessInstanceId(processInstance.getId());
			//v.setProcessDefinition(getProcessDefinition(processInstance.getProcessDefinitionId()));
			v.setTask(tasks.get(0));
			orders.add(v);
		}
		return orders;
	}

	public List<OrderView> findRunningProcessInstaces(int city, Principal principal, NumberPageUtil page) {
		List<OrderView> orders = new ArrayList<OrderView>();
		List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().variableValueEquals(ActivitiService.CITY, city).processDefinitionKey(MAIN_PROCESS)
				.listPage(page.getLimitStart(), page.getPagesize());
		for (ProcessInstance processInstance : processInstances) {
			List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).processVariableValueEquals(ActivitiService.CITY, city)
					.includeProcessVariables().orderByTaskCreateTime().desc().listPage(0, 1);
			Integer orderid = (Integer) tasks.get(0).getProcessVariables().get(ORDER_ID);
			OrderView v = new OrderView();
			v.setOrder(orderService.queryOrderDetail(orderid, principal));
			v.setProcessInstance(processInstance);
			v.setProcessInstanceId(processInstance.getId());
			v.setProcessDefinition(getProcessDefinition(processInstance.getProcessDefinitionId()));
			v.setTask(tasks.get(0));
			orders.add(v);
		}
		return orders;
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

	public Page<OrderView> finished(int city, Principal principal, TableRequest req) {
		int page = req.getPage(), pageSize = req.getLength();
		Sort sort = req.getSort("created");

		String longId = req.getFilter("longOrderId"), userIdQuery = req.getFilter("userId"), taskKey = req.getFilter("taskKey"), stateKey = req.getFilter("stateKey"), productId = req
				.getFilter("productId"), customerName = req.getFilter("customerName");
		String salesMan = req.getFilter("salesMan");
		Long longOrderId = StringUtils.isBlank(longId) ? 0 : NumberUtils.toLong(longId);

		page = page + 1;
		List<OrderView> orders = new ArrayList<OrderView>();

		HistoricProcessInstanceQuery countQuery = historyService.createHistoricProcessInstanceQuery().processDefinitionKey(MAIN_PROCESS)
				.variableValueEquals(ActivitiService.CITY, city).finished();

		HistoricProcessInstanceQuery listQuery = historyService.createHistoricProcessInstanceQuery().processDefinitionKey(MAIN_PROCESS)
				.variableValueEquals(ActivitiService.CITY, city).includeProcessVariables().finished();

		if (longOrderId > 0) {
			countQuery.variableValueEquals(ActivitiService.ORDER_ID, OrderIdSeq.checkAndGetRealyOrderId(longOrderId));
			listQuery.variableValueEquals(ActivitiService.ORDER_ID, OrderIdSeq.checkAndGetRealyOrderId(longOrderId));
		}

		if (StringUtils.isNoneBlank(stateKey) && !StringUtils.startsWith(stateKey, ActivitiService.R_DEFAULTALL)) {
			boolean isClosed = StringUtils.startsWith(stateKey, ActivitiService.R_CLOSED) ? true : false;
			countQuery.variableValueEquals(ActivitiService.CLOSED, isClosed);
			listQuery.variableValueEquals(ActivitiService.CLOSED, isClosed);
		}
		/*按商品查询 */
		if (StringUtils.isNoneBlank(productId)) {
			countQuery.variableValueEquals(ActivitiService.PRODUCT, NumberUtils.toInt(productId));
			listQuery.variableValueEquals(ActivitiService.PRODUCT, NumberUtils.toInt(productId));
		}
		//按客户查询
		if (StringUtils.isNoneBlank(customerName)) {
			countQuery.variableValueLike(ActivitiService.COMPANY, "%" + customerName + "%");
			listQuery.variableValueLike(ActivitiService.COMPANY, "%" + customerName + "%");
		}
		/*按用户查询 */
		if (StringUtils.isNoneBlank(userIdQuery)) {
			countQuery.variableValueLike(ActivitiService.CREAT_USERID, "%" + userIdQuery + "%");
			listQuery.variableValueLike(ActivitiService.CREAT_USERID, "%" + userIdQuery + "%");
		}
		/*按销售员查询 */
		if (StringUtils.isNoneBlank(salesMan)) {
			countQuery.variableValueLike(ActivitiService.CREAT_USERID, "%" + salesMan + "%");
			listQuery.variableValueLike(ActivitiService.CREAT_USERID, "%" + salesMan + "%");
		}

		int c = 0;
		if ((Request.hasAuth(principal, ActivitiConfiguration.ADVERTISER) || Request.hasAuth(principal, ActivitiConfiguration.SALES))
				&& !Request.hasAuth(principal, ActivitiConfiguration.ORDER)) {
			c = (int) countQuery.involvedUser(Request.getUserId(principal)).count();
		} else {
			c = (int) countQuery.count();
		}
		NumberPageUtil pageUtil = new NumberPageUtil((int) c, page, pageSize);
		//Request.hasAuth(principal, ActivitiConfiguration.ADVERTISER)
		List<HistoricProcessInstance> list = null;
		if ((Request.hasAuth(principal, ActivitiConfiguration.ADVERTISER) || Request.hasAuth(principal, ActivitiConfiguration.SALES))
				&& !Request.hasAuth(principal, ActivitiConfiguration.ORDER)) {
			listQuery.involvedUser(Request.getUserId(principal));
		}
		hisotrySort(sort, listQuery);
		list = listQuery.listPage(pageUtil.getLimitStart(), pageUtil.getPagesize());

		for (HistoricProcessInstance historicProcessInstance : list) {

			//---------------------
			Integer orderid = (Integer) historicProcessInstance.getProcessVariables().get(ORDER_ID);
			OrderView v = new OrderView();
			if (orderid != null && orderid > 0) {
				JpaOrders or = orderService.queryOrderDetail(orderid, principal);
				if (or != null) {
					JpaProduct product = productService.findById(or.getProductId());
					v.setProduct(product);
					v.setOrder(or);
					//orders.add(v);
					boolean r = true;
					if (longOrderId > 0) {
						r = longOrderId == OrderIdSeq.getIdFromDate(or.getId(), or.getCreated());
					}
					if (r) {
						orders.add(v);
					}
					Boolean bn = (Boolean) historicProcessInstance.getProcessVariables().get(ActivitiService.CLOSED);
					boolean b = bn == null ? false : bn;
					v.setFinishedState(b ? Constants.CLOSED_STATE : Constants.FINAL_STATE);
					v.setProcessInstanceId(historicProcessInstance.getId());
					v.setStartTime(historicProcessInstance.getStartTime());
					v.setEndTime(historicProcessInstance.getEndTime());
				}
			}
		}
		Pageable p = new PageRequest(page, pageSize, sort);
		org.springframework.data.domain.PageImpl<OrderView> r = new org.springframework.data.domain.PageImpl<OrderView>(orders, p, pageUtil.getTotal());
		return r;
	}

	public List<OrderView> findFinishedProcessInstaces(int city, Principal principal, String usertype, NumberPageUtil page) {
		List<OrderView> orders = new ArrayList<OrderView>();
		List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery().variableValueEquals(ActivitiService.CITY, city).finished()
				.processDefinitionKey(MAIN_PROCESS).includeProcessVariables().orderByProcessInstanceStartTime().desc().list();
		for (HistoricProcessInstance historicProcessInstance : list) {
			// String businessKey = historicProcessInstance.getBusinessKey();
			Integer orderid = (Integer) historicProcessInstance.getProcessVariables().get(ORDER_ID);
			OrderView v = new OrderView();
			if (null != usertype && usertype.equals("user")) {
				String userid = Request.getUserId(principal);
				Iterable<JpaOrders> order = orderService.selectOrderByUser(city, userid, orderid);
				if (order.iterator().hasNext()) {
					v.setOrder(order.iterator().next());
				}
			} else {
				if (orderid != null && orderid > 0) {
					v.setOrder(orderService.queryOrderDetail(orderid, principal));
				}
			}
			v.setHistoricProcessInstance(historicProcessInstance);
			v.setProcessDefinition(getProcessDefinition(historicProcessInstance.getProcessDefinitionId()));
			orders.add(v);
		}
		page.setTotal(orders.size());
		return orders;
	}

	/**
	 * 根据流程定义Id查询流程定义
	 */
	public ProcessDefinition getProcessDefinition(String processDefinitionId) {
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
		return processDefinition;
	}

	public void startProcess(int city, UserDetail u, Orders order) {
		// Deployment deployment = repositoryService.createDeployment()
		// .addClasspathResource("classpath*:/com/pantuo/activiti/autodeploy/order.bpmn20.xml").deploy();
		Map<String, Object> initParams = new HashMap<String, Object>();
		initParams.put(ActivitiService.OWNER, u);
		initParams.put(ActivitiService.ORDER_ID, order.getId());
		initParams.put(ActivitiService.CITY, city);
		initParams.put(ActivitiService.CLOSED, false);
		initParams.put(ActivitiService.NOW, new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date()));
		ProcessInstance process = runtimeService.startProcessInstanceByKey(MAIN_PROCESS, initParams);

		List<Task> tasks = taskService.createTaskQuery().processInstanceId(process.getId()).processVariableValueEquals(ActivitiService.CITY, city).orderByTaskCreateTime().desc()
				.listPage(0, 1);
		if (!tasks.isEmpty()) {
			Task task = tasks.get(0);
			Map<String, Object> info = taskService.getVariables(task.getId());
			if (info.containsKey(ORDER_ID) && ObjectUtils.equals(info.get(ORDER_ID), order.getId())) {
				taskService.claim(task.getId(), u.getUsername());
				//TaskDefinition nextTaskDefinition = getNextTaskByTaskOrder(task.getId());

				MailTask mailTask = new MailTask(u.getUsername(), order.getId(), null, task.getTaskDefinitionKey(), Type.sendCompleteMail);
				taskService.complete(task.getId());
				mailJob.putMailTask(mailTask);
			}
		}
		tasks = taskService.createTaskQuery().processDefinitionKey(MAIN_PROCESS).processVariableValueEquals(ActivitiService.CITY, city).taskCandidateOrAssigned(u.getUsername())
				.includeProcessVariables().orderByTaskPriority().desc().orderByTaskCreateTime().desc().list();
		if (!tasks.isEmpty()) {
			Task task = tasks.get(0);
			Map<String, Object> info = taskService.getVariables(task.getId());
			if (info.containsKey(ORDER_ID) && ObjectUtils.equals(info.get(ORDER_ID), order.getId())) {
				taskService.claim(task.getId(), u.getUsername());
			}
		}

		//debug(process.getId());
	}

	public void startTest() {
		String u = "bodysales";
		Map<String, Object> initParams = new HashMap<String, Object>();
		initParams.put("username", u);
		initParams.put("st", System.currentTimeMillis());

		ProcessInstance process = runtimeService.startProcessInstanceByKey("busFlowV2", initParams);
		List<Task> tasks = taskService.createTaskQuery().processInstanceId(process.getId()).orderByTaskCreateTime().desc().listPage(0, 5);
		if (!tasks.isEmpty()) {
			Task task = tasks.get(0);
			taskService.claim(task.getId(), u);
			taskService.complete(task.getId());
		}

		TaskQuery tb = taskService.createTaskQuery().processDefinitionKey("busFlowV2");

		tasks = tb.taskCandidateOrAssigned("contract").orderByTaskCreateTime().desc().listPage(0, 3);
		if (!tasks.isEmpty()) {
			Task task = tasks.get(0);
			taskService.claim(task.getId(), "contract");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("canSchedule", false);
			map.put("closed", true);
			map.put("closedReson", "库存不足 订单关闭");

			taskService.complete(task.getId(), map);
		}
		tasks = taskService.createTaskQuery().processInstanceId(process.getId()).orderByTaskCreateTime().desc().listPage(0, 2);
		if (!tasks.isEmpty()) {
			Task task = tasks.get(0);
			taskService.claim(task.getId(), "material");
			taskService.complete(task.getId());
		}

		tasks = taskService.createTaskQuery().processInstanceId(process.getId()).orderByTaskCreateTime().desc().listPage(0, 2);
		if (!tasks.isEmpty()) {
			Task task = tasks.get(0);
			taskService.claim(task.getId(), "financial");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("paymentResult", false);
			map.put("canelResult", true);
			taskService.complete(task.getId(), map);
		}
		//小样

		tasks = taskService.createTaskQuery().processInstanceId(process.getId()).orderByTaskCreateTime().desc().listPage(0, 2);
		if (!tasks.isEmpty()) {
			Task task = tasks.get(0);
			taskService.claim(task.getId(), "material");
			taskService.complete(task.getId());
		}

		tasks = taskService.createTaskQuery().processInstanceId(process.getId()).orderByTaskCreateTime().desc().listPage(0, 2);
		if (!tasks.isEmpty()) {
			Task task = tasks.get(0);
			taskService.claim(task.getId(), "material");
			taskService.complete(task.getId());
		}

		HistoricProcessInstanceQuery countQuery = historyService.createHistoricProcessInstanceQuery().processDefinitionKey("busFlowV2").includeProcessVariables().finished();
		List<HistoricProcessInstance> list = countQuery.list();

		for (HistoricProcessInstance historicProcessInstance : list) {

			Map<String, Object> map = historicProcessInstance.getProcessVariables();
			System.out.println(map);
			//---------------------

		}
		//debug(process.getId());
	}

	public void startProcess2(int cityId, UserDetail u, JpaOrders order, UserDetail customer) {
		Map<String, Object> initParams = new HashMap<String, Object>();
		//String ssu=u.getUsername();
		//u.setUsername(u.getCompany());
		initParams.put(ActivitiService.OWNER, u);
		initParams.put(ActivitiService.PAYPLAN, false);

		initParams.put("isContractPayed", false);

		initParams.put(ActivitiService.CREAT_USERID, u.getUsername());
		initParams.put(ActivitiService.ORDER_ID, order.getId());
		//add company for customer
		initParams.put(ActivitiService.COMPANY, customer != null ? customer.getCompany() : StringUtils.EMPTY);
		initParams.put(ActivitiService.CITY, cityId);
		initParams.put(ActivitiService.PRODUCT, order.getProductId());
		if (null != order.getSupplies()) {
			initParams.put(ActivitiService.SUPPLIEID, order.getSupplies().getId());
		} else {
			initParams.put(ActivitiService.SUPPLIEID, 0);
		}
		initParams.put(ActivitiService.NOW, new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date()));
		JpaCity city = cityService.fromId(cityId);
		if (city != null && city.getMediaType() == JpaCity.MediaType.body) {
			//车身广告不需要终审
			initParams.put("approve2Result", true);
		}
		Product product = productMapper.selectByPrimaryKey(order.getProductId());
		if (product != null && product.getIscompare() == 1) {
			initParams.put(ActivitiService.R_USERPAYED, true);
		}
		ProcessInstance process = runtimeService.startProcessInstanceByKey(MAIN_PROCESS, initParams);
		List<Task> tasks = taskService.createTaskQuery().processInstanceId(process.getId()).orderByTaskCreateTime().desc().listPage(0, 1);
		//u.setUsername(ssu);
		if (!tasks.isEmpty()) {
			Task task = tasks.get(0);
			if (StringUtils.equals("submitOrder", task.getTaskDefinitionKey())) {
				taskService.claim(task.getId(), u.getUsername());
				taskService.complete(task.getId());
			}
		}
		tasks = taskService.createTaskQuery().processInstanceId(process.getId()).orderByTaskCreateTime().desc().listPage(0, 5);
		if (!tasks.isEmpty()) {
			for (Task task : tasks) {
				if (StringUtils.equals("payment", task.getTaskDefinitionKey())) {
					taskService.claim(task.getId(), u.getUsername());
					//如果是分期
					if (order.getPayType().name().equals("dividpay")) {
						Map<String, Object> variables = new HashMap<String, Object>();
						variables.put(ActivitiService.R_USERPAYED, false);
						variables.put(ActivitiService.PAYPLAN, true);
						//variables.put("isContractPayed", true);
						taskService.complete(task.getId(), variables);

					} else if (order.getStats().equals(JpaOrders.Status.paid)) {
						Map<String, Object> variables = new HashMap<String, Object>();
						variables.put(ActivitiService.R_USERPAYED, true);
						variables.put(ActivitiService.PAYPLAN, false);
						taskService.complete(task.getId(), variables);
					}
				}
			}
		}
		tasks = taskService.createTaskQuery().processInstanceId(process.getId()).orderByTaskCreateTime().desc().listPage(0, 2);
		autoCompleteBindStatic(u, order, tasks, false);
		//debug(process.getId());
	}

	private void autoCompleteBindStatic(UserDetail u, JpaOrders order, List<Task> tasks, boolean alwaysSet) {
		if (!tasks.isEmpty()) {
			for (Task task : tasks) {
				//Task task = tasks.get(0);
				Map<String, Object> info = taskService.getVariables(task.getId());
				if (StringUtils.equals("bindstatic", task.getTaskDefinitionKey())) {
					if (info.containsKey(ORDER_ID) && ObjectUtils.equals(info.get(ORDER_ID), order.getId())) {
						//默认签收 绑定素材
						taskService.claim(task.getId(), u.getUsername());
						if (alwaysSet || (null != order.getSupplies() && order.getSupplies().getId() > 1)) {
							//如果是下单的时候 就绑定了素材 完成这一步
							//TaskDefinition nextTaskDefinition = getNextTaskByTaskOrder(task.getId());
							MailTask mailTask = new MailTask(u.getUsername(), order.getId(), null, task.getTaskDefinitionKey(), Type.sendCompleteMail);
							taskService.complete(task.getId());
							mailJob.putMailTask(mailTask);
						}
					}
				}
				/**
				 * 销售 分期自动 签收
				 */
				if (StringUtils.equals("setPayPlan", task.getTaskDefinitionKey()) && haveGroup(u, "sales")) {
					taskService.claim(task.getId(), u.getUsername());
				}

			}
		}
	}

	public boolean haveGroup(UserDetail u, String... group) {
		boolean r = true;
		if (u.getGroups().size() == 0) {
			r = false;
		}
		for (int i = 0; i < group.length; i++) {
			boolean isHitGroup = false;
			for (org.activiti.engine.identity.Group c : u.getGroups()) {
				if (c.getId().equals(group[i])) {
					isHitGroup = true;
				}
			}
			r = r && isHitGroup;
		}
		return r;
	}

	private void debug(String processid) {
		List<Task> tasks;
		tasks = taskService.createTaskQuery().processInstanceId(processid).orderByTaskCreateTime().desc().listPage(0, 1);
		for (Task task2 : tasks) {
			System.out.println(task2.getTaskDefinitionKey());
		}
	}

	public int relateContract(int orderid, int contractid, String payType, int isinvoice, InvoiceDetail invoiceDetail, String userId, String taskName) {

		Orders orders = ordersMapper.selectByPrimaryKey(orderid);
		Contract contract = contractMapper.selectByPrimaryKey(contractid);
		if (orders != null) {
			orders.setIsInvoice(isinvoice);
			if (null != invoiceDetail.getId()) {
				orders.setInvoiceId(invoiceDetail.getId());
			}

			if (contract != null && contract.getContractCode() != null && payType.equals("contract")) {
				orders.setContractId(contractid);
			}
			if (orders.getPayType() == null || orders.getPayType().intValue() != 6) {
				if (contract != null && contract.getContractCode() != null && payType.equals("contract")) {
					orders.setContractId(contractid);
					//				orders.setContractCode(contract.getContractCode());
					orders.setPayType(1);
				} else if (payType.equals("online")) {
					orders.setPayType(0);
				} else if (payType.equals("check")) {
					orders.setPayType(2);
				} else if (payType.equals("cash")) {
					orders.setPayType(4);
				} else {
					orders.setPayType(3);
				}
			}
			if (StringUtils.isNoneBlank(taskName)) {
				finishTaskByTaskName(orderid, taskName, userId);
			}
			int dbId = ordersMapper.updateByPrimaryKey(orders);
			return dbId;

		}
		return 1;
	}

	public Pair<Object, String> payment(HttpServletRequest request, int orderid, String taskid, int contractid, String payType, int isinvoice, int invoiceid, String contents,
			String receway, UserDetail u) {

		String payNextLocation = request.getParameter("payNextLocation"), payWay = request.getParameter("payWay");
		InvoiceDetail invoiceDetail = new InvoiceDetail();
		Invoice invoice = invoiceMapper.selectByPrimaryKey(invoiceid);
		Orders orders = ordersMapper.selectByPrimaryKey(orderid);
		if (invoice != null) {
			BeanUtils.copyProperties(invoice, invoiceDetail);
			invoiceDetail.setReceway(receway);
			invoiceDetail.setContents(contents);
			invoiceDetail.setMainid(invoiceid);
			invoiceDetailMapper.insert(invoiceDetail);
		}
		Pair<Object, String> r = null;
		Task task = taskService.createTaskQuery().taskId(taskid).singleResult();
		if (task != null) {
			Map<String, Object> info = taskService.getVariables(task.getId());
			UserDetail ul = (UserDetail) info.get(ActivitiService.OWNER);
			if (ul != null && ObjectUtils.equals(ul.getUsername(), u.getUsername())) {
				if (StringUtils.equals("payment", task.getTaskDefinitionKey()) || StringUtils.equals("userFristPay", task.getTaskDefinitionKey())) {
					if (relateContract(orderid, contractid, payType, isinvoice, invoiceDetail, u.getUsername(), StringUtils.EMPTY) > 0) {
						taskService.claim(task.getId(), u.getUsername());
						Map<String, Object> variables = new HashMap<String, Object>();
						//						if(){
						//							
						//						}
						variables.put(ActivitiService.R_USERPAYED, true);

						MailTask mailTask = new MailTask(u.getUsername(), orderid, null, task.getTaskDefinitionKey(), Type.sendCompleteMail);
						taskService.complete(task.getId(), variables);
						//首付
						if (StringUtils.equals("userFristPay", task.getTaskDefinitionKey())) {
							Pair<Object, String> subResult = orderService.updatePlanState(request, payWay, orderid, 0, payNextLocation, JpaPayPlan.PayState.check, u.getUsername());
							if (subResult != null) {
								return subResult;
							}
						}
						mailJob.putMailTask(mailTask);
						if (orders != null) {
							return new Pair<Object, String>(orders, String.format("订单支付成功%s!", getNextTaskInfo(mailTask.getFinishDate(), orderid)));
						}
					} else {
						return new Pair<Object, String>(null, "订单支付失败!");
					}
				}
			} else {
				r = new Pair<Object, String>(null, "任务属主不匹配!");
			}
		} else {
			if (relateContract(orderid, contractid, payType, isinvoice, invoiceDetail, u.getUsername(), "payment") > 0) {
				return new Pair<Object, String>(orders, String.format("订单支付成功%s!", getNextTaskInfo(new Date(System.currentTimeMillis() - 2500), orderid)));
			} else {
				return new Pair<Object, String>(null, "订单支付失败!");
			}
		}
		if (task != null) {
			//debug(task.getProcessInstanceId());
		}
		return r = new Pair<Object, String>(orders, "订单支付成功!");

	}

	// 根据OrderId 及taskName 完成task
	public void finishTaskByTaskName(int orderid, String taskName, String userId) {
		List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().involvedUser(userId).variableValueEquals(ORDER_ID, orderid).orderByProcessInstanceId().desc()
				.listPage(0, 1);
		for (ProcessInstance processInstance : list) {
			List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).orderByTaskCreateTime().desc().listPage(0, 3);
			if (!tasks.isEmpty()) {
				for (Task task : tasks) {
					Map<String, Object> info = taskService.getVariables(task.getId());
					if (StringUtils.equals(taskName, task.getTaskDefinitionKey())) {
						if (info.containsKey(ORDER_ID) && ObjectUtils.equals(info.get(ORDER_ID), orderid)) {
							taskService.claim(task.getId(), userId);

							Map<String, Object> variables = new HashMap<String, Object>();
							variables.put(ActivitiService.R_USERPAYED, true);
							//TaskDefinition nextTask = getNextTaskByTaskOrder(task.getId());
							MailTask mailTask = new MailTask(userId, orderid, null, task.getTaskDefinitionKey(), Type.sendCompleteMail);

							taskService.complete(task.getId(), variables);

							mailJob.putMailTask(mailTask);
						}
					}
				}
			}
		}

	}

	public String getNextTaskInfo(Date completeDate, int orderId) {
		String r = StringUtils.EMPTY;
		List<Task> tasks = taskService.createTaskQuery().processVariableValueEquals(ActivitiService.ORDER_ID, orderId).orderByTaskCreateTime().desc().list();
		for (Task task : tasks) {
			if (completeDate.before(task.getCreateTime())) {
				r = " 订单将进入到下一个操作环节[" + task.getName() + "]";
			} else {
				log.info(" complete Task and getNextInfo ,filter task:{},{},{}", completeDate.getTime(), task.getCreateTime().getTime(), task.getName());
			}
		}
		return r;
	}

	public Pair<Boolean, String> complete(String taskId, Map<String, Object> variables, Principal principal) {
		Pair<Boolean, String> r = new Pair<Boolean, String>(true, StringUtils.EMPTY);
		UserDetail u = Request.getUser(principal);
		try {
			if (variables != null) {
				Map<String, Object> taskVarMap = new HashMap<String, Object>();
				for (Map.Entry<String, Object> entry : variables.entrySet()) {
					//参照 order:1:8
					taskVarMap.put(String.format("taskVar:%s:%s", taskId, entry.getKey()), entry.getValue());
				}
				variables.putAll(taskVarMap);
			}
			Task task = findTaskById(taskId, true);
			if (StringUtils.equals(task.getAssignee(), u.getUsername())) {
				variables.put("lastModifUser", u.getUsername());
				JpaOrders.Status status = fetchStatusAfterTaskComplete(task);
				Integer orderId = (Integer) task.getProcessVariables().get(ORDER_ID);
				if (status != null && orderId != null) {
					orderService.updateStatus(orderId, principal, status);
				}
				MailTask mailTask = new MailTask(u.getUsername(), orderId, null, task.getTaskDefinitionKey(), Type.sendCompleteMail);

				//支付确认失败
				if (StringUtils.equals("financialCheck", task.getTaskDefinitionKey()) && task.getProcessVariables().containsKey("paymentResult")
						&& ((Boolean) variables.get("paymentResult")) == false) {
					variables.put(ActivitiService.R_USERPAYED, false);
				}

				taskService.complete(taskId, variables);

				if (StringUtils.equals("financialCheck", task.getTaskDefinitionKey())) {//且判断是分期的话 
					//根据页面选中值 更新分期状态
					if (task.getProcessVariables().containsKey(ActivitiService.PAYPLAN) && ((Boolean) task.getProcessVariables().get(ActivitiService.PAYPLAN)) == true) {
						if (task.getProcessVariables().containsKey("paymentResult")) {
							PayPlanExample e = new PayPlanExample();
							PayPlanExample.Criteria criteria = e.createCriteria();
							criteria.andOrderIdEqualTo(orderId).andPayStateEqualTo(JpaPayPlan.PayState.check.ordinal());
							//避免更新用户后支付的分期
							if (variables.containsKey(ActivitiService.FILTERTIME)) {
								if (variables.get(ActivitiService.FILTERTIME) instanceof Long) {
									long time = (Long) variables.get(ActivitiService.FILTERTIME);
									criteria.andUpdatedLessThan(new Date(time));
								}
							}

							boolean isPayed = (Boolean) variables.get("paymentResult");
							List<PayPlan> list = payPlanMapper.selectByExample(e);
							double payMoney = 0;
							for (PayPlan payPlan : list) {
								payPlan.setPayState(isPayed ? JpaPayPlan.PayState.payed.ordinal() : JpaPayPlan.PayState.fail.ordinal());
								if (isPayed) {
									payMoney += payPlan.getPrice();
								}
								payPlan.setRemarks((String) variables.get("financialcomment"));
								payPlan.setReduceUser(u.getUsername());
								payPlanMapper.updateByPrimaryKey(payPlan);
							}
							if (isPayed && payMoney > 0) {
								orderService.updatePayPrice(orderId, payMoney);
							}
							if (!isPayed) {//如果支付失败 让用户回到首支付

								autoClaimFristPayUser(task, orderId, "financialCheck");
							}
						}
					}
				}
				//自动签收首付款 环节
				autoClaimFristPayUser(task, orderId, "setPayPlan");

				mailJob.putMailTask(mailTask);
				r.setRight("操作成功 " + getNextTaskInfo(mailTask.getFinishDate(), orderId) + "!");
			} else {
				r = new Pair<Boolean, String>(false, "非法操作！");
				log.warn(u.getUsername() + ":" + task.toString());
			}
		} catch (Exception e) {
			log.error("Fail to complete task {}", taskId, e);
			r = new Pair<Boolean, String>(false, StringUtils.EMPTY);
		}
		return r;
	}

	private void autoClaimFristPayUser(Task task, Integer orderId, String equalTaskKey) {
		//setPayPlan
		if (StringUtils.equals(task.getTaskDefinitionKey(), equalTaskKey)) {
			List<Task> tasks = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).list();
			for (Task eachTask : tasks) {
				if (StringUtils.equals(eachTask.getTaskDefinitionKey(), "userFristPay")) {
					//默认用户签收首付款
					taskService.claim(eachTask.getId(), (String) task.getProcessVariables().get(ActivitiService.CREAT_USERID));
				}
			}
		}
	}

	public String reset(int city, String p) {
		StringBuilder sb = new StringBuilder();
		List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().processDefinitionKey(MAIN_PROCESS).variableValueEquals(ActivitiService.CITY, city)
				.includeProcessVariables().list();
		for (ProcessInstance processInstance : processInstances) {

			if (StringUtils.contains(p, "deleteAll")) {
				runtimeService.deleteProcessInstance(processInstance.getId(), "");

			} else {
				Map<String, Object> info = processInstance.getProcessVariables();
				Integer orderid = (Integer) info.get(ORDER_ID);
				if (orderid != null) {
					Orders order = orderService.selectOrderById(orderid);
					if (order == null) {
						sb.append(orderid + ",");
						runtimeService.deleteProcessInstance(processInstance.getId(), "");
					}
				}

				/**
				 *  清理完成的订单里找不到订单 数据异常的工作流
				 */

				List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery().variableValueEquals(ActivitiService.CITY, city).finished()
						.processDefinitionKey(MAIN_PROCESS).includeProcessVariables().orderByProcessInstanceStartTime().desc().list();
				for (HistoricProcessInstance historicProcessInstance : list) {
					Integer hisId = (Integer) historicProcessInstance.getProcessVariables().get(ORDER_ID);
					if (hisId != null && hisId > 0) {
						Orders or = orderService.selectOrderById(hisId);
						if (or == null) {
							historyService.deleteHistoricProcessInstance(historicProcessInstance.getId());
						}
					}
				}
			}
		}
		return sb.toString();
	}

	public OrderView findOrderViewByOrder(int orderid, Principal principal) {
		ProcessInstance instance = runtimeService.createProcessInstanceQuery().includeProcessVariables().variableValueEquals(ORDER_ID, orderid).singleResult();
		OrderView v = new OrderView();
		JpaOrders order = orderService.queryOrderDetail(orderid, principal);
		if (order != null) {
			JpaProduct product = productService.findById(order.getProductId());
			v.setProduct(product);
			v.setOrder(order);
		}
		if (instance != null) {
			v.setProcessInstance(instance);
			v.setProcessInstanceId(instance.getId());
			v.setProcessDefinition(getProcessDefinition(instance.getProcessDefinitionId()));
			v.setVariables(instance.getProcessVariables());
			v.setTask_name(getOrderState(instance.getProcessVariables()));
		}
		return v;
	}

	public OrderView findOrderViewByTaskId(String taskid, Principal principal) {
		Task task = taskService.createTaskQuery().taskId(taskid).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).includeProcessVariables().singleResult();
		OrderView v = new OrderView();
		Map<String, Object> variables = taskService.getVariables(taskid);
		int orderid = (Integer) variables.get(ORDER_ID);
		JpaOrders order = orderService.queryOrderDetail(orderid, principal);
		if (order != null) {
			JpaProduct product = productService.findById(order.getProductId());
			v.setProduct(product);
			v.setOrder(order);
		}
		v.setTask(task);
		v.setTask_id(taskid);
		v.setProcessInstance(processInstance);
		v.setProcessInstanceId(processInstance.getId());
		v.setProcessDefinition(getProcessDefinition(processInstance.getProcessDefinitionId()));
		v.setVariables(variables);
		v.setTask_name(getOrderState(processInstance.getProcessVariables()));
		return v;
	}

	public JpaOrders.Status getOrderStatus(int orderId) {
		ProcessInstance instance = runtimeService.createProcessInstanceQuery().includeProcessVariables().variableValueEquals(ORDER_ID, orderId).singleResult();
		if (instance != null) {
			Map<String, Object> vars = instance.getProcessVariables();
			Boolean approve1 = (Boolean) vars.get("approve1Result");
			Boolean approve2 = (Boolean) vars.get("approve2Result");
			Boolean payment = (Boolean) vars.get("paymentResult");
			Boolean schedule = (Boolean) vars.get("scheduleResult");
			Boolean shangbo = (Boolean) vars.get("shangboResult");
			Boolean jianbo = (Boolean) vars.get("jianboResult");
			if ((jianbo != null && jianbo) || (shangbo != null && shangbo)) {
				return JpaOrders.Status.started;
			} else if (schedule != null && schedule) {
				return JpaOrders.Status.scheduled;
			} else if (payment != null && payment) {
				return JpaOrders.Status.paid;
			} else {
				return JpaOrders.Status.unpaid;
			}
		} else {
			HistoricTaskInstance history = historyService.createHistoricTaskInstanceQuery().includeProcessVariables().processVariableValueEquals(ORDER_ID, orderId).singleResult();
			if (history != null) {
				Map<String, Object> vars = history.getProcessVariables();
				Boolean jianbo = (Boolean) vars.get("jianboResult");
				if (jianbo != null && jianbo) {
					return JpaOrders.Status.completed;
				} else {
					return JpaOrders.Status.cancelled;
				}
			} else {
				return null;
			}
		}
	}

	// 根据OrderId查找流程实例
	public ProcessInstance findProcessInstanceByOrderId(int orderid, String userId) {
		// 找到流程实例
		List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().involvedUser(userId).includeProcessVariables().variableValueEquals(ORDER_ID, orderid)
				.orderByProcessInstanceId().desc().listPage(0, 1);
		ProcessInstance processInstance = null;
		if (list != null && !list.isEmpty()) {
			processInstance = list.get(0);
		}
		return processInstance;
	}

	public ProcessView findPidByOrderid(int orderid, String userId, boolean filterUser) {
		// 找到流程实例
		ProcessInstanceQuery query = runtimeService.createProcessInstanceQuery();
		if (filterUser) {
			query = query.involvedUser(userId);
		}
		List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().includeProcessVariables().variableValueEquals(ORDER_ID, orderid).orderByProcessInstanceId().desc()
				.listPage(0, 1);
		ProcessInstance processInstance = null;
		String pid = null;
		Map<String, Object> var = null;
		if (list != null && !list.isEmpty()) {
			processInstance = list.get(0);
		}
		if (processInstance == null) {
			HistoricProcessInstanceQuery istoricQuery = historyService.createHistoricProcessInstanceQuery();
			if (filterUser) {
				istoricQuery = istoricQuery.involvedUser(userId);
			}
			HistoricProcessInstance hpie = istoricQuery.finished().processDefinitionKey(MAIN_PROCESS).includeProcessVariables().variableValueEquals(ORDER_ID, orderid)
					.singleResult();
			if (hpie != null) {
				pid = hpie.getId();
				var = hpie.getProcessVariables();
			}
		} else {
			pid = processInstance.getProcessInstanceId();
			var = processInstance.getProcessVariables();
		}
		return new ProcessView(pid, var);
	}

	class ProcessView {
		String pid = null;
		Map<String, Object> var = null;

		public ProcessView(String pid, Map<String, Object> var) {
			this.pid = pid;
			this.var = var;
		}

	}

	// 根据taskId查找流程实例
	public ProcessInstance findProcessInstanceByTaskId(String taskId) throws Exception {
		// 找到流程实例
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(findTaskById(taskId).getProcessInstanceId()).singleResult();
		if (processInstance == null) {
			throw new Exception("流程实例未找到!");
		}
		return processInstance;
	}

	// 根据taskId查找流程定义
	public ProcessDefinitionEntity findProcessDefinitionEntityByTaskId(String taskId) throws Exception {
		// 取得流程定义
		ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition(findTaskById(taskId)
				.getProcessDefinitionId());

		if (processDefinition == null) {
			throw new Exception("流程定义未找到!");
		}

		return processDefinition;
	}

	public TaskEntity findTaskById(String taskId, boolean withVariables) throws Exception {
		TaskQuery query = taskService.createTaskQuery();
		if (withVariables)
			query.includeProcessVariables();
		TaskEntity task = (TaskEntity) query.taskId(taskId).singleResult();
		if (task == null) {
			throw new Exception("任务实例未找到!");
		}
		return task;
	}

	public TaskEntity findTaskById(String taskId) throws Exception {
		TaskEntity task = (TaskEntity) taskService.createTaskQuery().taskId(taskId).singleResult();
		if (task == null) {
			throw new Exception("任务实例未找到!");
		}
		return task;
	}

	// 根据流程实例和节点ID查找历史审批记录
	public List<HistoricTaskView> findHistoricUserTask(int city, String pid, String activityId) {

		// 查询当前流程实例审批结束的历史节点
		// List<HistoricActivityInstance> historicActivityInstances =
		// historyService
		// .createHistoricActivityInstanceQuery().activityType("userTask")
		// .processInstanceId(processInstance.getId()).activityId(activityId).finished()
		// .orderByHistoricActivityInstanceEndTime().desc().list();
		// return historicActivityInstances;
		List<HistoricTaskInstance> taskInstances = historyService.createHistoricTaskInstanceQuery().processInstanceId(pid).processVariableValueEquals(ActivitiService.CITY, city)
				.includeProcessVariables().orderByTaskCreateTime().desc().orderByTaskId().desc().list();//orderByTaskId
		List<HistoricTaskView> view = new ArrayList<HistoricTaskView>();

		for (HistoricTaskInstance historicTaskInstance : taskInstances) {
			HistoricTaskView w = new HistoricTaskView();
			BeanUtils.copyProperties(historicTaskInstance, w);
			w.setVars(w.getProcessVariables());
			Map<String, Object> temp = w.getProcessVariables();

			String f = "taskVar:%s:%s";
			if (StringUtils.equals("approve1", w.getTaskDefinitionKey())) {
				String key = String.format(f, historicTaskInstance.getId(), "approve1Comments");
				String result = String.format(f, historicTaskInstance.getId(), "approve1Result");
				w.setComment((String) temp.get(key));
				w.setResult(temp.get(result));
			} else if (StringUtils.equals("approve2", w.getTaskDefinitionKey())) {
				String key = String.format(f, historicTaskInstance.getId(), "approve2Comments");
				String result = String.format(f, historicTaskInstance.getId(), "approve2Result");
				w.setComment((String) temp.get(key));
				w.setResult(temp.get(result));
			} else if (StringUtils.equals("financialCheck", w.getTaskDefinitionKey())) {
				String key = String.format(f, historicTaskInstance.getId(), "financialcomment");
				w.setComment((String) temp.get(key));
				w.setResult(temp.get(String.format(f, historicTaskInstance.getId(), "paymentResult")));
			} else if (StringUtils.equals("inputSchedule", w.getTaskDefinitionKey())) {
				String key = String.format(f, historicTaskInstance.getId(), "inputScheduleComments");
				w.setComment((String) temp.get(key));
			} else if (StringUtils.equals("shangboReport", w.getTaskDefinitionKey())) {
				String key = String.format(f, historicTaskInstance.getId(), "shangboComments");
				w.setComment((String) temp.get(key));
			} else if (StringUtils.equals("jianboReport", w.getTaskDefinitionKey())) {
				String key = String.format(f, historicTaskInstance.getId(), "jianboComments");
				w.setComment((String) temp.get(key));
			} else if (StringUtils.equals("usertask2", w.getTaskDefinitionKey())) {
				String key = String.format(f, historicTaskInstance.getId(), "ReceComments");
				w.setComment((String) temp.get(key));
			} else if (StringUtils.equals("usertask4", w.getTaskDefinitionKey())) {
				String key = String.format(f, historicTaskInstance.getId(), "shigongComments");
				w.setComment((String) temp.get(key));
			}

			view.add(w);
		}
		//extractDebug(taskInstances);
		return view;

	}

	private void extractDebug(List<HistoricTaskInstance> taskInstances) {
		for (HistoricTaskInstance hai : taskInstances) {
			System.out.print(" exceutionId:" + hai.getExecutionId() + "，");
			System.out.print(" id:" + hai.getId() + "，");
			System.out.print(" name:" + hai.getName() + "，");
			System.out.print("key :" + hai.getTaskDefinitionKey() + "，");
			System.out.print("pid:" + hai.getProcessInstanceId() + "，");
			System.out.print("assignee:" + hai.getAssignee() + "，");
			System.out.print("startTime:" + hai.getStartTime() + "，");
			System.out.print("endTime:" + hai.getEndTime() + "，");
			//List<HistoricVariableInstance> tw = historyService.createHistoricVariableInstanceQuery()
			//		.taskId( hai.getId()).list();
			//printDetail(tw);
			System.out.print("duration:" + hai.getDurationInMillis());
			System.out.println("vars:" + hai.getProcessVariables() + "，");
		}
	}

	public void queryHistoricActivityInstance(String processInstanceId) throws Exception {
		List<HistoricActivityInstance> hais = historyService.createHistoricActivityInstanceQuery()
		// 过滤条件
				.processInstanceId(processInstanceId)
				// 分页条件
				//		     .listPage(firstResult, maxResults)
				// 排序条件
				.orderByHistoricActivityInstanceStartTime().asc()
				// 执行查询
				.list();

		for (HistoricActivityInstance hai : hais) {

			List<HistoricVariableInstance> tw = historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId).excludeTaskVariables().list();
			//	printDetail(tw);
			System.out.print("taskId:" + hai.getTaskId() + "，");
			System.out.print("activitiId:" + hai.getActivityId() + "，");
			System.out.print("name:" + hai.getActivityName() + "，");
			System.out.print("type:" + hai.getActivityType() + "，");
			System.out.print("pid:" + hai.getProcessInstanceId() + "，");
			System.out.print("assignee:" + hai.getAssignee() + "，");
			System.out.print("startTime:" + hai.getStartTime() + "，");
			System.out.print("endTime:" + hai.getEndTime() + "，");
			System.out.println("duration:" + hai.getDurationInMillis());
		}
		printDetail2(processInstanceId);

	}

	public void printDetail2(String processInstanceId) {
		List<HistoricDetail> detail = historyService.createHistoricDetailQuery().processInstanceId(processInstanceId).list();
		for (HistoricDetail h : detail) {
			System.out.print(h.getTaskId() + "，");
			System.out.print(h.getExecutionId() + "，");
			System.out.print(h.getId() + "，");
			System.out.print(h.getExecutionId() + "，");
			System.out.print(h.getExecutionId() + "，");
			System.out.println(h.getActivityInstanceId() + "，");

		}
	}

	public void printDetail(List<HistoricVariableInstance> list) {
		for (HistoricVariableInstance hv : list) {
			System.out.print("1:" + hv.toString() + "，");
			System.out.print("2:" + hv.getVariableName() + "，");
			System.out.println("3:" + hv.getValue());
		}
	}

	public Pair<Object, String> modifyOrder(int city, String startdate1, int orderid, String taskid, int supplieid, UserDetail user) throws ParseException {
		Pair<Object, String> r = null;
		Orders orders = ordersMapper.selectByPrimaryKey(orderid);
		Task task = taskService.createTaskQuery().taskId(taskid).singleResult();
		if (task != null) {
			Map<String, Object> info = taskService.getVariables(task.getId());
			UserDetail ul = (UserDetail) info.get(ActivitiService.OWNER);
			if (updateSupplise(startdate1, orderid, supplieid) > 0) {
				taskService.claim(task.getId(), ul.getUsername());
				//TaskDefinition nextTaskDefinition = getNextTaskByTaskOrder(task.getId());
				MailTask mailTask = new MailTask(user.getUsername(), orderid, null, task.getTaskDefinitionKey(), Type.sendCompleteMail);
				taskService.complete(task.getId());
				mailJob.putMailTask(mailTask);

				return new Pair<Object, String>(orders, String.format("订单修改成功%s!", getNextTaskInfo(mailTask.getFinishDate(), orderid)));
			} else {
				return new Pair<Object, String>(null, "订单修改失败!");
			}
		} else {
			if (updateSupplise(startdate1, orderid, supplieid) > 0) {
				JpaOrders order = orderService.getJpaOrder(orderid);

				List<Task> tasks = taskService.createTaskQuery().processVariableValueEquals(ActivitiService.ORDER_ID, order.getId()).orderByTaskCreateTime().desc().listPage(0, 4);
				autoCompleteBindStatic(user, order, tasks, true);
				return new Pair<Object, String>(order, String.format("绑定物料成功%s!", getNextTaskInfo(new Date(System.currentTimeMillis() - 2500), orderid)));
			} else {
				return new Pair<Object, String>(null, "绑定物料失败!");
			}
		}
	}

	public int updateSupplise(String stdate, int orderid, int supplieid) throws ParseException {
		Date sDate = null;
		Date eDate = null;
		Orders o = ordersMapper.selectByPrimaryKey(orderid);
		if (StringUtils.isNotBlank(stdate)) {
			JpaProduct prod = productService.findById(o.getProductId());
			sDate = DateUtil.longDf.get().parse(stdate);
			eDate = DateUtil.dateAdd(sDate, prod.getDays());
		}
		if (o != null && supplieid > 0) {
			o.setSuppliesId(supplieid);
			if (sDate != null && eDate != null) {
				o.setStartTime(sDate);
				o.setEndTime(eDate);
			}
			return ordersMapper.updateByPrimaryKey(o);
		}
		return 0;
	}

	Map<String, Date> getOperationTime(List<HistoricTaskView> list) {
		Map<String, Date> map = new HashMap<String, Date>();
		for (HistoricTaskView historicTaskView : list) {
			map.put(historicTaskView.getTaskDefinitionKey(), historicTaskView.getEndTime());
		}
		return map;
	}

	public String showOrderDetail(int city, Model model, int orderid, String taskid, String pid, Principal principal, boolean isAutoGoto) {

		/**
		 *  自动判断是否是结束的任务
		 */
		if (isAutoGoto && orderid > 0) {
			List<Task> tasks = taskService.createTaskQuery().processVariableValueEquals(ActivitiService.ORDER_ID, orderid).processVariableValueEquals(ActivitiService.CITY, city)
					.includeProcessVariables().orderByTaskCreateTime().desc().listPage(0, 1);
			if (!tasks.isEmpty()) {
				taskid = tasks.get(0).getId();
			}
		}

		if (StringUtils.isNotBlank(taskid)) {
			try {
				Task task = taskService.createTaskQuery().taskId(taskid).singleResult();
				ExecutionEntity executionEntity = (ExecutionEntity) runtimeService.createExecutionQuery().executionId(task.getExecutionId())
						.processInstanceId(task.getProcessInstanceId()).singleResult();
				String activityId = executionEntity.getActivityId();
				ProcessInstance pe = findProcessInstanceByTaskId(taskid);
				List<HistoricTaskView> activitis = findHistoricUserTask(city, pe.getProcessInstanceId(), activityId);

				OrderView v = findOrderViewByTaskId(taskid, principal);
				JpaProduct prod = productService.findById(v.getOrder().getProductId());
				SuppliesView suppliesView = suppliesService.getSuppliesDetail(v.getOrder(), principal);
				SuppliesView quafiles = suppliesService.getQua(v.getOrder().getSuppliesId(), null);
				model.addAttribute("cpdDetail", cpdService.queryOneCpdByPid(v.getOrder().getProductId()));
				model.addAttribute("suppliesView", suppliesView);
				model.addAttribute("quafiles", quafiles);
				model.addAttribute("activitis", activitis);
				model.addAttribute("operTimeTree", getOperationTime(activitis));
				model.addAttribute("sections", orderService.getTaskSection(activitis));
				model.addAttribute("orderview", v);
				if (v != null && v.getOrder() != null) {
					model.addAttribute("contract", contractService.selectContractById(v.getOrder().getContractId()));
				}
				model.addAttribute("prod", prod);
			} catch (Exception e) {
				log.error(e.getMessage());
			}
			return "orderDetail";
		} else {
			JpaOrders order = orderService.queryOrderDetail(orderid, principal);
			JpaProduct prod = null;
			Long longorderid = null;
			List<HistoricTaskView> activitis = null;
			OrderView orderView = new OrderView();
			SuppliesView suppliesView = null;
			SuppliesView quafiles = null;

			if (order != null) {
				orderView.setOrder(order);
				prod = productService.findById(order.getProductId());
				longorderid = OrderIdSeq.getIdFromDate(order.getId(), order.getCreated());
				suppliesView = suppliesService.getSuppliesDetail(orderView.getOrder(), principal);
				quafiles = suppliesService.getQua(orderView.getOrder().getSuppliesId(), null);
				prod = productService.findById(order.getProductId());
			} else {
				throw new OrderException("订单信息丢失!");
			}
			orderView.setTask_name(StringUtils.EMPTY);
			if (StringUtils.isNoneBlank(pid)) {
				activitis = findHistoricUserTask(city, pid, null);
				HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(pid).includeProcessVariables().singleResult();
				orderView.setTask_name(Constants.FINAL_STATE);
				if (processInstance != null) {
					Map<String, Object> variables = processInstance.getProcessVariables();
					if (BooleanUtils.toBoolean((Boolean) variables.get(ActivitiService.CLOSED))) {
						orderView.setClosed(true);
						orderView.setTask_name(Constants.CLOSED_STATE);
					} else {
						orderView.setTask_name(Constants.FINAL_STATE);
					}
				}

			} else if (order != null) {
				ProcessView pw = findPidByOrderid(order.getId(), Request.getUserId(principal), true);
				if (pw.pid != null) {
					activitis = findHistoricUserTask(city, pw.pid, null);
					orderView.setTask_name(getOrderState(pw.var));
				}
			}

			model.addAttribute("activitis", activitis);
			model.addAttribute("suppliesView", suppliesView);
			model.addAttribute("quafiles", quafiles);
			model.addAttribute("order", order);
			model.addAttribute("longorderid", longorderid);
			model.addAttribute("orderview", orderView);
			if (orderView != null && orderView.getOrder() != null) {
				model.addAttribute("contract", contractService.selectContractById(orderView.getOrder().getContractId()));
			}
			model.addAttribute("prod", prod);
			return "finishedOrderDetail";
		}
	}

	@Autowired
	IcbcServiceImpl icbcService;

	public String toPlanDetail(Model model, int planId, int city, String pid, Principal principal) {
		JpaPayPlan plan = payPlanRepository.findOne(planId);
		if (plan != null) {
			OrderView orderView = gotoView(model, plan.getOrder().getId(), city, principal);
			model.addAttribute("planView", new OrderPlanView(plan));

		}
		return "toPlanDetail";
	}

	public Pair<Object, String> updatePlan(String rad, JpaPayPlan planParams, Principal principal) {
		Pair<Object, String> r = new Pair<Object, String>(true, "操作成功!");

		PayPlan plan = payPlanMapper.selectByPrimaryKey(planParams.getId());
		if (BooleanUtils.toBoolean(rad)) {
			if (plan != null) {
				Orders order = ordersMapper.selectByPrimaryKey(plan.getOrderId());
				Paycontract paycontract = paycontractMapper.selectByPrimaryKey(plan.getContractId());
				if (order != null) {
					Orders record = new Orders();
					record.setId(plan.getOrderId());
					record.setPayPrice(order.getPayPrice() + plan.getPrice());
					ordersMapper.updateByPrimaryKeySelective(record);
				} else if (paycontract != null) {

					if (plan.getPeriodNum() == 1) {//首付款确认时 订单处理
						List<String> orders = (List<String>) JSONUtils.parse(paycontract.getOrderJson());
						for (String string : orders) {
							long dbid = NumberUtils.toLong(string.trim());
							if (dbid == 0) {
								continue;
							}
							int orderid = OrderIdSeq.longOrderId2DbId(dbid);
							List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().variableValueEquals(ORDER_ID, orderid).orderByProcessInstanceId().desc()
									.listPage(0, 1);

							for (ProcessInstance processInstance : list) {

								List<Task> tasks = taskService.createTaskQuery().includeProcessVariables().processInstanceId(processInstance.getId()).orderByTaskCreateTime()
										.desc().listPage(0, 5);
								if (!tasks.isEmpty()) {
									for (Task task : tasks) {
										if (StringUtils.equals("setPayPlan", task.getTaskDefinitionKey())) {
											taskService.claim(task.getId(), (String) task.getProcessVariables().get(ActivitiService.CREAT_USERID));
												Map<String, Object> variables = new HashMap<String, Object>();
												variables.put(ActivitiService.R_USERPAYED, false);
												variables.put(ActivitiService.PAYPLAN, true);
												variables.put("isContractPayed", true);
												variables.put("paymentResult", true);
												variables.put("paymentResult_message", "财务收到第一笔");
												taskService.complete(task.getId(), variables);

											}
										}
									}
								}

							}

						}
					}

					paycontract.setPayPrice(paycontract.getPayPrice() + plan.getPrice());
					paycontractMapper.updateByPrimaryKey(paycontract);
				}
			}
		if (plan != null) {
			plan.setRemarks(planParams.getRemarks());
			plan.setPayState(BooleanUtils.toBoolean(rad) ? PayState.payed.ordinal() : PayState.fail.ordinal());
			plan.setReduceUser(Request.getUserId(principal));
			plan.setUpdated(new Date());
			payPlanMapper.updateByPrimaryKey(plan);
		}
		return r;
	}

	@Override
	public String toRestPay(Model model, int orderid, int city, String pid, Principal principal) {
		OrderView orderView = gotoView(model, orderid, city, principal);
		orderService.fullPayPlanInfo(model, "userFristPay", orderView);
		Map<String, Object> modelMap = model.asMap();
		//总金额的工商支付
		icbcService.sufficeIcbcSubmit(model, orderView.getLongOrderId(), new CardView(null, null, (Double) modelMap.get("payAll"), 1), "offline", StringUtils.EMPTY,
				"&L=".concat((String) modelMap.get("allLocation")));
		icbcService.sufficeIcbcSubmit(model, orderView.getLongOrderId(), new CardView(null, null, (Double) modelMap.get("payNext"), 1), "offline", "_plan",
				"&L=".concat((String) modelMap.get("payNextLocation")));
		model.addAttribute("orderId", orderid);
		return "restPay";
	}

	private OrderView gotoView(Model model, int orderid, int city, Principal principal) {
		JpaOrders order = orderService.selectJpaOrdersById(orderid);
		JpaProduct prod = null;
		Long longorderid = null;
		List<HistoricTaskView> activitis = null;
		OrderView orderView = new OrderView();
		SuppliesView suppliesView = null;
		SuppliesView quafiles = null;

		if (order != null) {
			orderView.setOrder(order);
			prod = productService.findById(order.getProductId());
			longorderid = OrderIdSeq.getIdFromDate(order.getId(), order.getCreated());
			suppliesView = suppliesService.getSuppliesDetail(orderView.getOrder(), principal);
			quafiles = suppliesService.getQua(orderView.getOrder().getSuppliesId(), null);
			prod = productService.findById(order.getProductId());
		} else {
			throw new OrderException("订单信息丢失!");
		}
		orderView.setTask_name(StringUtils.EMPTY);

		ProcessView pw = findPidByOrderid(order.getId(), Request.getUserId(principal), false);
		if (pw.pid != null) {
			activitis = findHistoricUserTask(city, pw.pid, null);
			orderView.setTask_name(getOrderState(pw.var));
		}

		model.addAttribute("activitis", activitis);
		model.addAttribute("suppliesView", suppliesView);
		model.addAttribute("quafiles", quafiles);
		model.addAttribute("order", order);
		model.addAttribute("longorderid", longorderid);
		model.addAttribute("orderview", orderView);
		if (orderView != null && orderView.getOrder() != null) {
			model.addAttribute("contract", contractService.selectContractById(orderView.getOrder().getContractId()));
		}
		model.addAttribute("prod", prod);
		return orderView;
	}

	@Override
	public JpaOrders.Status fetchStatusAfterTaskComplete(Task task) {
		String key = task.getTaskDefinitionKey();
		//        Map<String, Object> info = taskService.getVariables(task.getId());
		if ("financialCheck".equals(key))
			return JpaOrders.Status.paid;
		else if ("inputSchedule".equals(key))
			return JpaOrders.Status.scheduled;
		else if ("shangboReport".equals(key))
			return JpaOrders.Status.started;
		else if ("jianboReport".equals(key))
			return JpaOrders.Status.completed;
		return null;
	}

	public TaskDefinition getNextTaskByTaskOrder(String taskId) {
		if (StringUtils.isBlank(taskId))
			return null;
		long t = System.currentTimeMillis();
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		if (task == null)
			return null;
		ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition(task.getProcessDefinitionId());
		List<ActivityImpl> activitiList = def.getActivities(); //rs是指RepositoryService的实例 根据任务获取当前流程执行ID，执行实例以及当前流程节点的ID：
		String excId = task.getExecutionId();
		ExecutionEntity execution = (ExecutionEntity) runtimeService.createExecutionQuery().executionId(excId).singleResult();
		String activitiId = execution.getActivityId();
		//然后循环activitiList 并判断出当前流程所处节点，然后得到当前节点实例，根据节点实例获取所有从当前节点出发的路径，然后根据路径获得下一个节点实例：
		TaskDefinition taskDefinition = mapActivity(activitiId, activitiList);
		log.info("getNextTaskByTaskOrder time: {} ms", System.currentTimeMillis() - t);
		if (taskDefinition != null) {
			log.info("getNextTaskByTaskOrder:{},{},{}", taskDefinition.getNameExpression(), taskDefinition.getKey());
		} else {
			log.info("getNextTaskByTaskOrder:is null");
		}
		return taskDefinition;
	}

	/**
	 * 
	 * 查下一个节点 发现不靠谱 太多状态控制，比如初审后
	 * 
	 * 参考文章 
	 * http://blog.sina.com.cn/s/blog_551421220100wzl9.html
	 * http://nichtse.github.io/activiti/2014/08/08/activiti-next-userTask-groupId.html
	 * @param activitiId
	 * @param activitiList
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public TaskDefinition mapActivity(String activitiId, List<ActivityImpl> activitiList) {
		TaskDefinition r = null;
		for (ActivityImpl activityImpl : activitiList) {
			String id = activityImpl.getId();
			if (activitiId.equals(id)) {
				r = reduceActivityImpl(activitiId, activityImpl);
				//System.out.println(r.toString());
			}
			if (r != null) {
				break;
			} else {
				r = mapActivity(activitiId, activityImpl.getActivities());
			}
		}
		return r;
	}

	private TaskDefinition reduceActivityImpl(String activitiId, ActivityImpl activityImpl) {
		TaskDefinition r = null;
		System.out.println("当前任务：" + activityImpl.getProperty("name")); //输出某个节点的某种属性
		if (!activitiId.equals(activityImpl.getId()) && "userTask".equals(activityImpl.getProperty("type"))) {
			return ((UserTaskActivityBehavior) activityImpl.getActivityBehavior()).getTaskDefinition();
		}
		List<PvmTransition> outTransitions = activityImpl.getOutgoingTransitions();//获取从某个节点出来的所有线路
		for (PvmTransition tr : outTransitions) {
			PvmActivity ac = tr.getDestination(); //获取线路的终点节点
			//if ("userTask".equals(ac.getProperty("type"))) {
			System.out.println("下一步任务任务：" + ac.getProperty("name") + " _ " + ac.getProperty("type"));
			if ("exclusiveGateway".equals(ac.getProperty("type"))) {
				List<PvmTransition> outTransitionsTemp = null;
				outTransitionsTemp = ac.getOutgoingTransitions();
				if (outTransitionsTemp.size() == 1) {//如果下一步节点又是集体结点
					PvmActivity sub = ((PvmTransition) outTransitionsTemp.get(0)).getDestination();
					r = getUserTask(sub);
				} else if (outTransitionsTemp.size() > 1) {
					for (PvmTransition tr1 : outTransitionsTemp) {
						PvmActivity sub = tr1.getDestination();
						String runActId = ((ActivityImpl) sub).getId();
						if (activitiId.equals(runActId)) {
							r = getUserTask(sub);
						} else {
							r = reduceActivityImpl(activitiId, ((ActivityImpl) sub));
						}
						if (r != null)
							return r;
					}
				}
			} else if ("subProcess".equals(ac.getProperty("type"))) {//如果是子任务
				List<ActivityImpl> activitiList = ((ActivityImpl) ac).getActivities();
				for (ActivityImpl subList : activitiList) {
					r = reduceActivityImpl(activitiId, subList);
					if (r != null) {
						break;
					}
				}
			} else if ("serviceTask".equals(ac.getProperty("type"))) {//如果是serviceTask查看他完成后的下一步是不是人工任务
				List<PvmTransition> outTransitionsTemp = ac.getOutgoingTransitions();
				if (outTransitionsTemp.size() == 1) {
					PvmActivity sub = ((PvmTransition) outTransitionsTemp.get(0)).getDestination();
					ActivityImpl imp = (ActivityImpl) sub;
					if (!"userTask".equals(imp.getProperty("type"))) {
						r = reduceActivityImpl(activitiId, imp);
					} else {
						r = getUserTask(sub);
					}
				}
			} else if ("endEvent".equals(ac.getProperty("type"))) {
				//如果是工作流结尾直接结束 
				return null;
			} else if ("userTask".equals(activityImpl.getProperty("type"))) {//如果是用户要完成的人工任务
				r = getUserTask(ac);
			}
			if (r != null) {
				break;
			}
		}
		return r;
	}

	public TaskDefinition getUserTask(PvmActivity pvmActivity) {
		TaskDefinition r = null;
		ActivityBehavior ar = ((ActivityImpl) pvmActivity).getActivityBehavior();
		if (ar instanceof UserTaskActivityBehavior) {
			r = ((UserTaskActivityBehavior) ar).getTaskDefinition();
		}
		return r;
	}

}
