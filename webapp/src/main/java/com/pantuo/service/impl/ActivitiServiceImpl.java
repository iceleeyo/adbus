package com.pantuo.service.impl;

import java.security.Principal;
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
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import scala.collection.mutable.StringBuilder;

import com.pantuo.dao.pojo.JpaOrders;
import com.pantuo.dao.pojo.JpaProduct;
import com.pantuo.dao.pojo.UserDetail;
import com.pantuo.mybatis.domain.Contract;
import com.pantuo.mybatis.domain.Orders;
import com.pantuo.mybatis.domain.Product;
import com.pantuo.mybatis.domain.Supplies;
import com.pantuo.mybatis.persistence.ContractMapper;
import com.pantuo.mybatis.persistence.OrdersMapper;
import com.pantuo.pojo.HistoricTaskView;
import com.pantuo.service.ActivitiService;
import com.pantuo.service.OrderService;
import com.pantuo.service.ProductService;
import com.pantuo.service.SuppliesService;
import com.pantuo.util.BeanUtils;
import com.pantuo.util.NumberPageUtil;
import com.pantuo.util.OrderIdSeq;
import com.pantuo.util.Pair;
import com.pantuo.util.Request;
import com.pantuo.web.ScheduleController;
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
	private ProductService productService;
	@Autowired
	private SuppliesService suppliesService;
	@Autowired
	private OrdersMapper ordersMapper;
	@Autowired
	private ContractMapper contractMapper;

	public Page<OrderView> running(int city, String userid, int page, int pageSize, Sort sort) {
		page = page + 1;
		List<OrderView> orders = new ArrayList<OrderView>();
		int totalnum = runtimeService.createProcessInstanceQuery().processDefinitionKey(MAIN_PROCESS)
                .variableValueEquals(ActivitiService.CITY, city).list().size();
		NumberPageUtil pageUtil = new NumberPageUtil((int) totalnum, page, pageSize);

		List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery()
				.processDefinitionKey(MAIN_PROCESS)
                .variableValueEquals(ActivitiService.CITY, city).orderByProcessInstanceId().desc()
				.listPage(pageUtil.getLimitStart(), pageUtil.getPagesize());
		for (ProcessInstance processInstance : processInstances) {
			List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId())
                    .processVariableValueEquals(ActivitiService.CITY, city).includeProcessVariables().orderByTaskCreateTime().desc().listPage(0, 1);
			Integer orderid = (Integer) tasks.get(0).getProcessVariables().get(ORDER_ID);
			OrderView v = new OrderView();
			Orders order = orderService.selectOrderById(orderid);
			if (order != null) {
				v.setOrder(order);
				Product product = productService.selectProById(order.getProductId());
				v.setProduct(product);
				v.setProcessInstanceId(processInstance.getId());
				if (!tasks.isEmpty()) {
					v.setTask(tasks.get(0));
				}
				orders.add(v);
			}
		}
		Pageable p = new PageRequest(page, pageSize, sort);
		org.springframework.data.domain.PageImpl<OrderView> r = new org.springframework.data.domain.PageImpl<OrderView>(
				orders, p, pageUtil.getTotal());
		return r;
	}

	public Page<OrderView> MyOrders(int city, String userid, int page, int pageSize, Sort sort) {
		page = page + 1;
		List<OrderView> orders = new ArrayList<OrderView>();
		int totalnum =(int) runtimeService.createProcessInstanceQuery()
                .processDefinitionKey(MAIN_PROCESS)
                .variableValueEquals(ActivitiService.CITY, city).involvedUser(userid).count();
		NumberPageUtil pageUtil = new NumberPageUtil((int) totalnum, page, pageSize);

		List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery()
				.processDefinitionKey(MAIN_PROCESS)
                .variableValueEquals(ActivitiService.CITY, city).involvedUser(userid)
				.listPage(pageUtil.getLimitStart(), pageUtil.getPagesize());
		for (ProcessInstance processInstance : processInstances) {
			List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId())
                    .processVariableValueEquals(ActivitiService.CITY, city).includeProcessVariables().orderByTaskCreateTime().desc().listPage(0, 1);
			Integer orderid = (Integer) tasks.get(0).getProcessVariables().get(ORDER_ID);
			OrderView v = new OrderView();
			Orders order = orderService.selectOrderById(orderid);
			if (order != null) {
				v.setOrder(order);
			}
			//v.setProcessInstance(processInstance);
			v.setProcessInstanceId(processInstance.getId());
			//v.setProcessDefinition(getProcessDefinition(processInstance.getProcessDefinitionId()));
			v.setTask(tasks.get(0));
			orders.add(v);
		}
		Pageable p = new PageRequest(page, pageSize, sort);
		org.springframework.data.domain.PageImpl<OrderView> r = new org.springframework.data.domain.PageImpl<OrderView>(
				orders, p, pageUtil.getTotal());
		return r;
	}

	public Page<OrderView> findTask(int city, String userid, int page, int pageSize, Sort sort) {
		page = page + 1;
		List<Task> tasks = new ArrayList<Task>();
		List<OrderView> leaves = new ArrayList<OrderView>();
		//先查得总条数
		long c = taskService.createTaskQuery().processDefinitionKey(MAIN_PROCESS).taskCandidateOrAssigned(userid)
                .processVariableValueEquals(ActivitiService.CITY, city).count();
		NumberPageUtil pageUtil = new NumberPageUtil((int) c, page, pageSize);
		tasks = taskService.createTaskQuery().processDefinitionKey(MAIN_PROCESS).taskCandidateOrAssigned(userid)
                .processVariableValueEquals(ActivitiService.CITY, city).includeProcessVariables().orderByTaskPriority().desc().orderByTaskCreateTime().desc()
				.listPage(pageUtil.getLimitStart(), pageUtil.getPagesize());
		for (Task task : tasks) {
			String processInstanceId = task.getProcessInstanceId();
			ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
					.processInstanceId(processInstanceId).singleResult();
			Integer orderid = (Integer) task.getProcessVariables().get(ORDER_ID);
			OrderView v = new OrderView();
			Orders order = orderService.selectOrderById(orderid);
			if (order != null) {
				Product product = productService.selectProById(order.getProductId());
				v.setProduct(product);
				v.setOrder(order);
				v.setTask(task);
				v.setProcessInstanceId(processInstance.getId());
				leaves.add(v);
			}
		}
		Pageable p = new PageRequest(page, pageSize, sort);
		org.springframework.data.domain.PageImpl<OrderView> r = new org.springframework.data.domain.PageImpl<OrderView>(
				leaves, p, pageUtil.getTotal());
		return r;
	}

	public List<OrderView> findMyOrders(int city, String userid, NumberPageUtil page) {
		List<OrderView> orders = new ArrayList<OrderView>();
		List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery()
				.processDefinitionKey(MAIN_PROCESS).involvedUser(userid)
				.listPage(page.getLimitStart(), page.getPagesize());
		for (ProcessInstance processInstance : processInstances) {
			List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId())
                    .processVariableValueEquals(ActivitiService.CITY, city).includeProcessVariables().orderByTaskCreateTime().desc().listPage(0, 1);
			Integer orderid = (Integer) tasks.get(0).getProcessVariables().get(ORDER_ID);
			OrderView v = new OrderView();
			List<Orders> order = orderService.selectOrderByUser(city, userid, orderid);
			if (order.size() > 0) {
				v.setOrder(order.get(0));
			}
			//v.setProcessInstance(processInstance);
			v.setProcessInstanceId(processInstance.getId());
			//v.setProcessDefinition(getProcessDefinition(processInstance.getProcessDefinitionId()));
			v.setTask(tasks.get(0));
			orders.add(v);
		}
		return orders;
	}

	public List<OrderView> findRunningProcessInstaces(int city, String userid, NumberPageUtil page) {
		List<OrderView> orders = new ArrayList<OrderView>();
		List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery()
                .variableValueEquals(ActivitiService.CITY, city)
				.processDefinitionKey(MAIN_PROCESS).listPage(page.getLimitStart(), page.getPagesize());
		for (ProcessInstance processInstance : processInstances) {
			List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId())
                    .processVariableValueEquals(ActivitiService.CITY, city).includeProcessVariables().orderByTaskCreateTime().desc().listPage(0, 1);
			Integer orderid = (Integer) tasks.get(0).getProcessVariables().get(ORDER_ID);
			OrderView v = new OrderView();
			v.setOrder(orderService.selectOrderById(orderid));
			v.setProcessInstance(processInstance);
			v.setProcessInstanceId(processInstance.getId());
			v.setProcessDefinition(getProcessDefinition(processInstance.getProcessDefinitionId()));
			v.setTask(tasks.get(0));
			orders.add(v);
		}
		return orders;
	}

	public Page<OrderView> finished(int city, Principal principal, int page, int pageSize, Sort sort) {
		page = page + 1;
		List<OrderView> orders = new ArrayList<OrderView>();
		int c = (int) historyService.createHistoricProcessInstanceQuery().variableValueEquals(ActivitiService.CITY, city)
                .finished()
				.involvedUser(Request.getUserId(principal)).processDefinitionKey(MAIN_PROCESS).count();
		NumberPageUtil pageUtil = new NumberPageUtil((int) c, page, pageSize);
		List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery()
                .variableValueEquals(ActivitiService.CITY, city).finished()
				.involvedUser(Request.getUserId(principal)).processDefinitionKey(MAIN_PROCESS)
				.includeProcessVariables().orderByProcessInstanceStartTime().desc()
				.listPage(pageUtil.getLimitStart(), pageUtil.getPagesize());
		for (HistoricProcessInstance historicProcessInstance : list) {
			//---------------------
			Integer orderid = (Integer) historicProcessInstance.getProcessVariables().get(ORDER_ID);
			OrderView v = new OrderView();
			if (orderid != null && orderid > 0) {
				Orders or = orderService.selectOrderById(orderid);
				if (or != null) {
					Product product = productService.selectProById(or.getProductId());
					v.setProduct(product);
					v.setOrder(or);
					orders.add(v);
					v.setProcessInstanceId(historicProcessInstance.getId());
				}
			}
		}
		Pageable p = new PageRequest(page, pageSize, sort);
		org.springframework.data.domain.PageImpl<OrderView> r = new org.springframework.data.domain.PageImpl<OrderView>(
				orders, p, pageUtil.getTotal());
		return r;
	}

	public List<OrderView> findFinishedProcessInstaces(int city, String userid, String usertype, NumberPageUtil page) {
		List<OrderView> orders = new ArrayList<OrderView>();
		List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery()
                .variableValueEquals(ActivitiService.CITY, city).finished()
				.processDefinitionKey(MAIN_PROCESS).includeProcessVariables().orderByProcessInstanceStartTime().desc()
				.list();
		for (HistoricProcessInstance historicProcessInstance : list) {
			// String businessKey = historicProcessInstance.getBusinessKey();
			Integer orderid = (Integer) historicProcessInstance.getProcessVariables().get(ORDER_ID);
			OrderView v = new OrderView();
			if (null != usertype && usertype.equals("user")) {
				List<Orders> order = orderService.selectOrderByUser(city, userid, orderid);
				if (order.size() > 0) {
					v.setOrder(order.get(0));
				}
			} else {
				if (orderid != null && orderid > 0) {
					v.setOrder(orderService.selectOrderById(orderid));
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
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(processDefinitionId).singleResult();
		return processDefinition;
	}

	public void startProcess(int city, UserDetail u, Orders order) {
		// Deployment deployment = repositoryService.createDeployment()
		// .addClasspathResource("classpath*:/com/pantuo/activiti/autodeploy/order.bpmn20.xml").deploy();
		Map<String, Object> initParams = new HashMap<String, Object>();
		initParams.put(ActivitiService.OWNER, u);
		initParams.put(ActivitiService.ORDER_ID, order.getId());
        initParams.put(ActivitiService.CITY, city);
		initParams.put(ActivitiService.NOW, new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date()));
		ProcessInstance process = runtimeService.startProcessInstanceByKey(MAIN_PROCESS, initParams);

		List<Task> tasks = taskService.createTaskQuery().processInstanceId(process.getId())
                .processVariableValueEquals(ActivitiService.CITY, city).orderByTaskCreateTime()
				.desc().listPage(0, 1);
		if (!tasks.isEmpty()) {
			Task task = tasks.get(0);
			Map<String, Object> info = taskService.getVariables(task.getId());
			if (info.containsKey(ORDER_ID) && ObjectUtils.equals(info.get(ORDER_ID), order.getId())) {
				taskService.claim(task.getId(), u.getUsername());
				taskService.complete(task.getId());
			}
		}
		tasks = taskService.createTaskQuery().processDefinitionKey(MAIN_PROCESS)
                .processVariableValueEquals(ActivitiService.CITY, city)
                .taskCandidateOrAssigned(u.getUsername()).includeProcessVariables().orderByTaskPriority().desc()
				.orderByTaskCreateTime().desc().list();
		if (!tasks.isEmpty()) {
			Task task = tasks.get(0);
			Map<String, Object> info = taskService.getVariables(task.getId());
			if (info.containsKey(ORDER_ID) && ObjectUtils.equals(info.get(ORDER_ID), order.getId())) {
				taskService.claim(task.getId(), u.getUsername());
			}
		}

		debug(process.getId());
	}

	public void startProcess2(int city, UserDetail u, JpaOrders order) {
		// Deployment deployment = repositoryService.createDeployment()
		// .addClasspathResource("classpath*:/com/pantuo/activiti/autodeploy/order.bpmn20.xml").deploy();
		Map<String, Object> initParams = new HashMap<String, Object>();
		initParams.put(ActivitiService.OWNER, u);
		initParams.put(ActivitiService.ORDER_ID, order.getId());
        initParams.put(ActivitiService.CITY, city);
		initParams.put(ActivitiService.NOW, new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date()));
		ProcessInstance process = runtimeService.startProcessInstanceByKey(MAIN_PROCESS, initParams);

		List<Task> tasks = taskService.createTaskQuery().processInstanceId(process.getId())
                .processVariableValueEquals(ActivitiService.CITY, city).orderByTaskCreateTime()
				.desc().listPage(0, 1);
		if (!tasks.isEmpty()) {
			Task task = tasks.get(0);
			Map<String, Object> info = taskService.getVariables(task.getId());
			if (info.containsKey(ORDER_ID) && ObjectUtils.equals(info.get(ORDER_ID), order.getId())) {
				taskService.claim(task.getId(), u.getUsername());
				taskService.complete(task.getId());
			}
		}
		tasks = taskService.createTaskQuery().processDefinitionKey(MAIN_PROCESS)
                .processVariableValueEquals(ActivitiService.CITY, city)
                .taskCandidateOrAssigned(u.getUsername()).includeProcessVariables().orderByTaskPriority().desc()
				.orderByTaskCreateTime().desc().list();
		if (!tasks.isEmpty()) {
			Task task = tasks.get(0);
			Map<String, Object> info = taskService.getVariables(task.getId());
			if (info.containsKey(ORDER_ID) && ObjectUtils.equals(info.get(ORDER_ID), order.getId())) {
				taskService.claim(task.getId(), u.getUsername());
				if(order.getPayType()!=null){
					taskService.complete(task.getId());
				}
			}
		}
		debug(process.getId());
	}

	private void debug(String processid) {
		List<Task> tasks;
		tasks = taskService.createTaskQuery().processInstanceId(processid).orderByTaskCreateTime().desc()
				.listPage(0, 1);
		for (Task task2 : tasks) {
			System.out.println(task2.getTaskDefinitionKey());
		}
	}

	public int relateContract(int orderid, int contractid, String payType) {

		Orders orders = ordersMapper.selectByPrimaryKey(orderid);
		Contract contract = contractMapper.selectByPrimaryKey(contractid);
		if (orders != null) {
			if (contract != null && contract.getContractCode() != null && payType.equals("contract")) {
				orders.setContractId(contractid);
				orders.setContractCode(contract.getContractCode());
				orders.setPayType(0);
			} else if (payType.equals("online")) {
				orders.setPayType(1);
			} else {
				orders.setPayType(2);
			}
			return ordersMapper.updateByPrimaryKey(orders);
		}
		return 1;
	}

	public Pair<Boolean, String> payment(int orderid, String taskid, int contractid, String payType, UserDetail u) {

		Pair<Boolean, String> r = null;
		Task task = taskService.createTaskQuery().taskId(taskid).singleResult();
		if (task != null) {
			Map<String, Object> info = taskService.getVariables(task.getId());
			UserDetail ul = (UserDetail) info.get(ActivitiService.OWNER);
			if (ul != null && ObjectUtils.equals(ul.getUsername(), u.getUsername())) {
				if (StringUtils.equals("payment", task.getTaskDefinitionKey())) {
					if (relateContract(orderid, contractid, payType) > 0) {
						taskService.claim(task.getId(), u.getUsername());
						taskService.complete(task.getId());
						return new Pair<Boolean, String>(true, "订单支付成功!");
					} else {
						return new Pair<Boolean, String>(false, "订单支付失败!");
					}
				}
			} else {
				r = new Pair<Boolean, String>(false, "任务属主不匹配!");
			}
		} else {
			if (relateContract(orderid, contractid, payType) > 0) {
				return new Pair<Boolean, String>(true, "订单支付成功!");
			} else {
				return new Pair<Boolean, String>(false, "订单支付失败!");
			}
		}
		if (task != null) {
			debug(task.getProcessInstanceId());
		}
		return r = new Pair<Boolean, String>(true, "订单支付成功!");

	}

	public Pair<Boolean, String> complete(String taskId, Map<String, Object> variables, UserDetail u) {
		Pair<Boolean, String> r = new Pair<Boolean, String>(true, StringUtils.EMPTY);
		try {
			// Map<String, Object> variables2 =
			// taskService.getVariables(taskId);
			// variables.putAll(variables2);
			if (variables != null) {
				Map<String, Object> taskVarMap = new HashMap<String, Object>();
				for (Map.Entry<String, Object> entry : variables.entrySet()) {
					//参照 order:1:8
					taskVarMap.put(String.format("taskVar:%s:%s", taskId, entry.getKey()), entry.getValue());
				}
				variables.putAll(taskVarMap);
			}
			variables.put("lastModifUser", u.getUsername());
			taskService.complete(taskId, variables);
		} catch (Exception e) {
			//e.printStackTrace();
			r = new Pair<Boolean, String>(false, StringUtils.EMPTY);
		}
		return r;
	}

	public String reset(int city, String p) {
		StringBuilder sb = new StringBuilder();
		List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery()
				.processDefinitionKey(MAIN_PROCESS)
                .variableValueEquals(ActivitiService.CITY, city).includeProcessVariables().list();
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

				List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery()
                        .variableValueEquals(ActivitiService.CITY, city).finished()
						.processDefinitionKey(MAIN_PROCESS).includeProcessVariables().orderByProcessInstanceStartTime()
						.desc().list();
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

	public OrderView findOrderViewByTaskId(String taskid) {
		Task task = taskService.createTaskQuery().taskId(taskid).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
		OrderView v = new OrderView();

		Map<String, Object> variables = taskService.getVariables(taskid);
		int orderid = (Integer) variables.get(ORDER_ID);
		Orders order = orderService.selectOrderById(orderid);
		Product product = productService.selectProById(order.getProductId());
		v.setProduct(product);
		v.setOrder(order);
		v.setTask(task);
		v.setProcessInstance(processInstance);
		v.setProcessInstanceId(processInstance.getId());
		v.setProcessDefinition(getProcessDefinition(processInstance.getProcessDefinitionId()));
		// Map<String, Object> variables = task.getProcessVariables();
		v.setVariables(variables);
		return v;
	}

	// 根据OrderId查找流程实例
	public String findPidByOrderid(int orderid, String userId) {
		// 找到流程实例
		List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().involvedUser(userId)
				.variableValueEquals(ORDER_ID, orderid).orderByProcessInstanceId().desc().listPage(0, 1);
		ProcessInstance processInstance = null;
		if (list != null && !list.isEmpty()) {
			processInstance = list.get(0);
		}
		String pid = null;
		if (processInstance == null) {
			HistoricProcessInstance hpie = historyService.createHistoricProcessInstanceQuery().finished()
					.involvedUser(userId).processDefinitionKey(MAIN_PROCESS).includeProcessVariables()
					.variableValueEquals(ORDER_ID, orderid).singleResult();
			if (hpie != null) {
				pid = hpie.getId();
			}

		} else {
			pid = processInstance.getProcessInstanceId();
		}
		return pid;
	}

	// 根据taskId查找流程实例
	public ProcessInstance findProcessInstanceByTaskId(String taskId) throws Exception {
		// 找到流程实例
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
				.processInstanceId(findTaskById(taskId).getProcessInstanceId()).singleResult();
		if (processInstance == null) {
			throw new Exception("流程实例未找到!");
		}
		return processInstance;
	}

	// 根据taskId查找流程定义
	public ProcessDefinitionEntity findProcessDefinitionEntityByTaskId(String taskId) throws Exception {
		// 取得流程定义
		ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(findTaskById(taskId).getProcessDefinitionId());

		if (processDefinition == null) {
			throw new Exception("流程定义未找到!");
		}

		return processDefinition;
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
		List<HistoricTaskInstance> taskInstances = historyService.createHistoricTaskInstanceQuery()
				.processInstanceId(pid)
                .processVariableValueEquals(ActivitiService.CITY, city).includeProcessVariables().orderByTaskId().desc().list();
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
				Object r = temp.get(result);
				w.setResult(r == null ? false : (Boolean) r);
			} else if (StringUtils.equals("approve2", w.getTaskDefinitionKey())) {
				String key = String.format(f, historicTaskInstance.getId(), "approve2Comments");
				String result = String.format(f, historicTaskInstance.getId(), "approve2Result");
				w.setComment((String) temp.get(key));
				Object r = temp.get(result);
				w.setResult(r == null ? false : (Boolean) r);
			} else if (StringUtils.equals("financialCheck", w.getTaskDefinitionKey())) {
				String key = String.format(f, historicTaskInstance.getId(), "financialcomment");
				w.setComment((String) temp.get(key));
			} else if (StringUtils.equals("inputSchedule", w.getTaskDefinitionKey())) {
				String key = String.format(f, historicTaskInstance.getId(), "inputScheduleComments");
				w.setComment((String) temp.get(key));
			} else if (StringUtils.equals("shangboReport", w.getTaskDefinitionKey())) {
				String key = String.format(f, historicTaskInstance.getId(), "shangboComments");
				w.setComment((String) temp.get(key));
			} else if (StringUtils.equals("jianboReport", w.getTaskDefinitionKey())) {
				String key = String.format(f, historicTaskInstance.getId(), "jianboComments");
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

			List<HistoricVariableInstance> tw = historyService.createHistoricVariableInstanceQuery()
					.processInstanceId(processInstanceId).excludeTaskVariables().list();
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
		List<HistoricDetail> detail = historyService.createHistoricDetailQuery().processInstanceId(processInstanceId)
				.list();
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

	public Pair<Boolean, String> modifyOrder(int city,int orderid, String taskid, int supplieid, UserDetail user) {
		Pair<Boolean, String> r = null;
		Task task = taskService.createTaskQuery().taskId(taskid).singleResult();
		if (task != null) {
			Map<String, Object> info = taskService.getVariables(task.getId());
			UserDetail ul = (UserDetail) info.get(ActivitiService.OWNER);
			if (updateSupplise(orderid, supplieid) > 0) {
				taskService.claim(task.getId(), ul.getUsername());
				taskService.complete(task.getId());
				return new Pair<Boolean, String>(true, "订单修改成功!");
			} else {
				return new Pair<Boolean, String>(false, "订单修改失败!");
			}
		}else{
			if (updateSupplise(orderid, supplieid) > 0){
				JpaOrders order=orderService.getJpaOrder(orderid);
				startProcess2(city, user,  order);
				return new Pair<Boolean, String>(true, "绑定物料成功!");
			}else{
				return new Pair<Boolean, String>(true, "绑定物料失败!");
			}
		}
	}

	public int updateSupplise(int orderid, int supplieid) {
		Orders o = ordersMapper.selectByPrimaryKey(orderid);
		if (o != null && supplieid > 0) {
			o.setSuppliesId(supplieid);
			return ordersMapper.updateByPrimaryKey(o);
		}
		return 1;
	}

	public String showOrderDetail(int city, Model model, int orderid, String taskid, String pid, Principal principal) {

		if (StringUtils.isNotBlank(taskid)) {
			try {
				Task task = taskService.createTaskQuery().taskId(taskid).singleResult();
				ExecutionEntity executionEntity = (ExecutionEntity) runtimeService.createExecutionQuery()
						.executionId(task.getExecutionId()).processInstanceId(task.getProcessInstanceId())
						.singleResult();
				String activityId = executionEntity.getActivityId();
				ProcessInstance pe = findProcessInstanceByTaskId(taskid);
				List<HistoricTaskView> activitis = findHistoricUserTask(city, pe.getProcessInstanceId(), activityId);
				OrderView v = findOrderViewByTaskId(taskid);
				JpaProduct prod = productService.findById(v.getOrder().getProductId());
				SuppliesView suppliesView = suppliesService.getSuppliesDetail(v.getOrder().getSuppliesId(), null);
				model.addAttribute("suppliesView", suppliesView);
				model.addAttribute("activitis", activitis);
				model.addAttribute("sections", orderService.getTaskSection(activitis));
				model.addAttribute("orderview", v);
				model.addAttribute("prod", prod);
			} catch (Exception e) {
				log.error(e.getMessage());
			}
			return "orderDetail";
		} else {
			Orders order = orderService.queryOrderDetail(orderid, principal);
			JpaProduct prod = null;
			Long longorderid = null;
			List<HistoricTaskView> activitis = null;
			OrderView orderView = new OrderView();
			SuppliesView suppliesView = null;

			if (order != null) {
				orderView.setOrder(order);
				prod = productService.findById(order.getProductId());
				longorderid = OrderIdSeq.getIdFromDate(order.getId(), order.getCreated());
				suppliesView = suppliesService.getSuppliesDetail(orderView.getOrder().getSuppliesId(), null);

			}
			if (StringUtils.isNoneBlank(pid)) {
				activitis = findHistoricUserTask(city, pid, null);
			} else if (order != null) {
				String processInstanceId = findPidByOrderid(order.getId(), Request.getUserId(principal));
				if (processInstanceId != null) {
					activitis = findHistoricUserTask(city, processInstanceId, null);
				}
			}

			model.addAttribute("activitis", activitis);
			model.addAttribute("suppliesView", suppliesView);
			model.addAttribute("order", order);
			model.addAttribute("longorderid", longorderid);
			model.addAttribute("orderview", orderView);
			model.addAttribute("prod", prod);
			return "finishedOrderDetail";
		}
	}
}
