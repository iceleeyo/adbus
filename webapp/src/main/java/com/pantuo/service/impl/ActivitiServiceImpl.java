package com.pantuo.service.impl;

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
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scala.collection.mutable.StringBuilder;
import com.pantuo.dao.pojo.JpaOrders;
import com.pantuo.dao.pojo.UserDetail;
import com.pantuo.service.ActivitiService;
import com.pantuo.service.OrderService;
import com.pantuo.util.NumberPageUtil;
import com.pantuo.util.Pair;
import com.pantuo.web.view.OrderView;

@Service
public class ActivitiServiceImpl implements ActivitiService {

	public final String ORDER_ID = "_orderId";
	public final String MAIN_PROCESS = "order";
	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private HistoryService historyService;

	private ProcessInstance instance;
	//private SimpleSmtpServer mailServer;
	@Autowired
	private OrderService orderService;

	public List<OrderView> findTask(String userid, NumberPageUtil page) {
		List<Task> tasks = new ArrayList<Task>();
		List<OrderView> leaves = new ArrayList<OrderView>();
		long c = taskService.createTaskQuery().processDefinitionKey(MAIN_PROCESS).taskCandidateUser(userid).count();
		page.setTotal((int) c);
		 tasks = taskService.createTaskQuery().processDefinitionKey(MAIN_PROCESS)
				.taskCandidateOrAssigned(userid).includeProcessVariables().orderByTaskPriority().desc()
				.orderByTaskCreateTime().desc().list();
		for (Task task : tasks) {
			String processInstanceId = task.getProcessInstanceId();
			ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
					.processInstanceId(processInstanceId).singleResult();
			Integer orderid = (Integer) task.getProcessVariables().get(ORDER_ID);
			OrderView v = new OrderView();
			Orders order = orderService.selectOrderById(orderid);
			v.setOrder(order);
			v.setTask(task);
			v.setProcessInstance(processInstance);
			v.setProcessInstanceId(processInstance.getId());
			v.setProcessDefinition(getProcessDefinition(processInstance.getProcessDefinitionId()));
			leaves.add(v);
		}

		return leaves;
	}
	public List<OrderView> findRunningProcessInstaces(String userid, String usertype,NumberPageUtil page){
//		int totalnum=runtimeService.createProcessInstanceQuery().processDefinitionKey(MAIN_PROCESS).list().size();
//		page=new NumberPageUtil(totalnum, page.getCurrpage(), page.getPagesize());
		List<OrderView> orders=new ArrayList<OrderView>();
		List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().processDefinitionKey(MAIN_PROCESS).listPage(page.getLimitStart(), page.getPagesize());
		for (ProcessInstance processInstance : processInstances) {
			List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).includeProcessVariables().orderByTaskCreateTime().desc().listPage(0, 1);
			Integer orderid =  (Integer)tasks.get(0).getProcessVariables().get(ORDER_ID);
			OrderView v = new OrderView();
			if(null!=usertype&&usertype.equals("user")){
				List<Order> order = orderService.selectOrderByUser(userid,orderid);
				if(order.size()>0){
					
					v.setOrder(order.get(0));
				}
			}else{
				v.setOrder(orderService.selectOrderById(orderid));
			}
			v.setProcessInstance(processInstance);
			v.setProcessInstanceId(processInstance.getId());
			v.setProcessDefinition(getProcessDefinition(processInstance.getProcessDefinitionId()));
			v.setTask(tasks.get(0));
			orders.add(v);
		}
		return orders;
	}
	public List<OrderView> findFinishedProcessInstaces(String userid, String usertype,NumberPageUtil page){
		List<OrderView> orders=new ArrayList<OrderView>();
		List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery().finished().processDefinitionKey(MAIN_PROCESS).includeProcessVariables().orderByProcessInstanceStartTime().desc().list();
		for (HistoricProcessInstance historicProcessInstance : list) {
//        	String businessKey = historicProcessInstance.getBusinessKey();
        	Integer orderid=(Integer) historicProcessInstance.getProcessVariables().get(ORDER_ID);
        	OrderView v = new OrderView();
        	if(null!=usertype&&usertype.equals("user")){
				List<Order> order = orderService.selectOrderByUser(userid,orderid);
				if(order.size()>0){
					v.setOrder(order.get(0));
				}
			}else{
				if(orderid!=null&&orderid>0){
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

	public void startProcess(UserDetail u, Orders order) {
	//	Deployment deployment = repositoryService.createDeployment()
	//			.addClasspathResource("classpath*:/com/pantuo/activiti/autodeploy/order.bpmn20.xml").deploy();
		Map<String, Object> initParams = new HashMap<String, Object>();
		initParams.put("_owner", u);
		initParams.put(ORDER_ID, order.getId());
		initParams.put("_now", new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date()));
		ProcessInstance process = runtimeService.startProcessInstanceByKey(MAIN_PROCESS, initParams);

		List<Task> tasks = taskService.createTaskQuery().processInstanceId(process.getId()).orderByTaskCreateTime()
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
	
	public void startProcess2(UserDetail u, JpaOrders order) {
		//	Deployment deployment = repositoryService.createDeployment()
		//			.addClasspathResource("classpath*:/com/pantuo/activiti/autodeploy/order.bpmn20.xml").deploy();
			Map<String, Object> initParams = new HashMap<String, Object>();
			initParams.put("_owner", u);
			initParams.put(ORDER_ID, order.getId());
			initParams.put("_now", new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date()));
			ProcessInstance process = runtimeService.startProcessInstanceByKey(MAIN_PROCESS, initParams);

			List<Task> tasks = taskService.createTaskQuery().processInstanceId(process.getId()).orderByTaskCreateTime()
					.desc().listPage(0, 1);
			if (!tasks.isEmpty()) {
				Task task = tasks.get(0);
				Map<String, Object> info = taskService.getVariables(task.getId());
				if (info.containsKey(ORDER_ID) && ObjectUtils.equals(info.get(ORDER_ID), order.getId())) {
					taskService.claim(task.getId(), u.getUsername());
					taskService.complete(task.getId());
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

	public Pair<Boolean, String> payment(int orderid, String taskid, UserDetail u) {
		Pair<Boolean, String> r = null;
		Task task = taskService.createTaskQuery().taskId(taskid).singleResult();
		if (task != null) {
			Map<String, Object> info = taskService.getVariables(task.getId());
			UserDetail ul = (UserDetail) info.get("_owner");
			if (ul != null && ObjectUtils.equals(ul.getUsername(), u.getUsername())) {
				if (StringUtils.equals("payment", task.getTaskDefinitionKey())) {
					taskService.claim(task.getId(), u.getUsername());
					taskService.complete(task.getId());
				}
			} else {
				r = new Pair<Boolean, String>(false, "任务属主不匹配!");
			}
		} else {
			r = new Pair<Boolean, String>(false, "任务已完成!");
		}
		if (task != null) {
			debug(task.getProcessInstanceId());
		}
		return r = new Pair<Boolean, String>(false, "办理成功!");

	}


	public Pair<Boolean, String> complete(String taskId, Map<String, Object> variables, UserDetail u) {
		Pair<Boolean, String> r = new Pair<Boolean, String>(true, StringUtils.EMPTY);
		try {
			//Map<String, Object> variables2 = taskService.getVariables(taskId);
		//	variables.putAll(variables2);
			
			taskService.complete(taskId, variables);
		} catch (Exception e) {
			r = new Pair<Boolean, String>(false, StringUtils.EMPTY);
		}
		return r;
	}

	public String reset(String p) {
		StringBuilder sb = new StringBuilder();
		List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery()
				.processDefinitionKey(MAIN_PROCESS).includeProcessVariables().list();
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
		v.setOrder(order);
		v.setTask(task);
		v.setProcessInstance(processInstance);
		v.setProcessInstanceId(processInstance.getId());
		v.setProcessDefinition(getProcessDefinition(processInstance.getProcessDefinitionId()));
		//Map<String, Object> variables = task.getProcessVariables();
		v.setVariables(variables);
		return v;
	}

}
