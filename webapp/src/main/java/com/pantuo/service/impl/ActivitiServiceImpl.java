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
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import scala.collection.mutable.StringBuilder;

//import com.dumbster.smtp.SimpleSmtpServer;
import com.pantuo.dao.pojo.UserDetail;
import com.pantuo.mybatis.domain.Order;
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
		//根据当前用户的id查询代办任务列表(已经签收)
			List<Task> taskAssignees = taskService.createTaskQuery().processDefinitionKey(MAIN_PROCESS)
					.taskAssignee(userid).includeProcessVariables().orderByTaskPriority().desc().orderByTaskCreateTime().desc().list();
		//根据当前用户id查询未签收的任务列表
		List<Task> taskCandidates = taskService.createTaskQuery().processDefinitionKey(MAIN_PROCESS)
				.taskCandidateUser(userid).includeProcessVariables().orderByTaskPriority().desc()
				.orderByTaskCreateTime().desc().list();
		tasks.addAll(taskAssignees);//添加已签收准备执行的任务(已经分配到任务的人)
		tasks.addAll(taskCandidates);//添加还未签收的任务(任务的候选者)

		for (Task task : tasks) {
			String processInstanceId = task.getProcessInstanceId();
			ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
					.processInstanceId(processInstanceId).singleResult();
			Integer orderid = (Integer) task.getProcessVariables().get(ORDER_ID);
			OrderView v = new OrderView();
			Order order = orderService.selectOrderById(orderid);
			v.setOrder(order);
			v.setTask(task);
			v.setProcessInstance(processInstance);
			v.setProcessInstanceId(processInstance.getId());
			v.setProcessDefinition(getProcessDefinition(processInstance.getProcessDefinitionId()));

			leaves.add(v);
		}

		return leaves;
	}

	/**
	 * 根据流程定义Id查询流程定义
	 */
	public ProcessDefinition getProcessDefinition(String processDefinitionId) {
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(processDefinitionId).singleResult();
		return processDefinition;
	}

	public void startProcess(UserDetail u, Order order) {
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
			r = new Pair<Boolean, String>(false, "任务状态不存在!");
		}
		if (task != null) {
			debug(task.getProcessInstanceId());
		}
		return r;

	}

	public Pair<Boolean, String> handle(String orderid, String taskid,
			String comment, String isok, UserDetail user) {
		Pair<Boolean, String> r = null;
		Task task = taskService.createTaskQuery().taskId(taskid).singleResult();
		if (task != null) {
		    Map<String, Object> info = taskService.getVariables(task.getId());
		    info.put("paymentResult",true);
			taskService.claim(task.getId(), user.getUsername());
		    taskService.complete(task.getId(),info);
		    r = new Pair<Boolean, String>(true, "操作成功!");
		} else {
			r = new Pair<Boolean, String>(false, "任务状态不存在!");
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
					Order order = orderService.selectOrderById(orderid);
					if (order == null) {
						sb.append(orderid + ",");
						runtimeService.deleteProcessInstance(processInstance.getId(), "");
					}
				}
			}
		}
		return sb.toString();
	}
}
