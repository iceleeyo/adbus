package com.pantuo.activiti.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.pantuo.ActivitiConfiguration;
import com.pantuo.dao.DaoBeanConfiguration;

/**
 * 
 * 1:先执行test.activiti.sql 创建用户
 * 2:调整dev.jdbc.properties 在webapp目录下
 * 	jdbc.database=MYSQL
	jdbc.driverClassName=com.mysql.jdbc.Driver
	jdbc.url=jdbc:mysql://112.124.14.72:3306/ad_bus?characterEncoding=UTF-8
	jdbc.username=root
	jdbc.password=pantuo1709

 *3：然后运行测试类
 * 
 * @author pxh
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DaoBeanConfiguration.class, ActivitiConfiguration.class })
public class MainprocessTest {
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private HistoryService historyService;

	private ProcessInstance instance;
	/**
	 * 模拟订单
	 */
	static Map<Long, Order> order = new HashMap<Long, Order>();

	static class Order {
		String userid;
		long orderid;

		Order(String userid) {
			this.userid = userid;
		}

		//-- 临时属性 --//

		// 流程任务
		private Task task;
		private String processInstanceId;
		private Map<String, Object> variables;

		// 运行中的流程实例
		private ProcessInstance processInstance;

		// 历史的流程实例
		private HistoricProcessInstance historicProcessInstance;

		// 流程定义
		private ProcessDefinition processDefinition;

		public String getProcessInstanceId() {
			return processInstanceId;
		}

		public void setProcessInstanceId(String processInstanceId) {
			this.processInstanceId = processInstanceId;
		}

		public long getOrderid() {
			return orderid;
		}

		public void setOrderid(long orderid) {
			this.orderid = orderid;
		}

		public Task getTask() {
			return task;
		}

		public void setTask(Task task) {
			this.task = task;
		}

		public Map<String, Object> getVariables() {
			return variables;
		}

		public void setVariables(Map<String, Object> variables) {
			this.variables = variables;
		}

		public ProcessInstance getProcessInstance() {
			return processInstance;
		}

		public void setProcessInstance(ProcessInstance processInstance) {
			this.processInstance = processInstance;
		}

		public HistoricProcessInstance getHistoricProcessInstance() {
			return historicProcessInstance;
		}

		public void setHistoricProcessInstance(HistoricProcessInstance historicProcessInstance) {
			this.historicProcessInstance = historicProcessInstance;
		}

		public ProcessDefinition getProcessDefinition() {
			return processDefinition;
		}

		public void setProcessDefinition(ProcessDefinition processDefinition) {
			this.processDefinition = processDefinition;
		}

		@Override
		public String toString() {
			return "JpaOrders [userid=" + userid + ", orderid=" + orderid + ", task=" + task + ", processInstanceId="
					+ processInstanceId + ", variables=" + variables + ", processInstance=" + processInstance
					+ ", historicProcessInstance=" + historicProcessInstance + ", processDefinition="
					+ processDefinition + "]\n";
		}

		public String getUserid() {
			return userid;
		}

		public void setUserid(String userid) {
			this.userid = userid;
		}

	}

	static {
		for (long i = 0; i < 20; i++) {
			order.put(i, new Order("userid_" + i));
		}

	}

	@Before
	public void before() throws Exception {

		Deployment deployment = repositoryService.createDeployment().addClasspathResource("Adprocess.bpmn20.xml")
				.deploy();

		//	deploymentIdList.add(deployment.getId());
	}

	//	@Override
	protected void destroy() throws Exception {
		/*for (String deployment : deploymentIdList) {
				repositoryService.deleteDeployment(deployment, true);
			}*/
	}

	@Test
	public void testSubClean() {
		/**
		 * 清空当前所有流程，可能有更好的方法
		 */
		String processKey = "ADProcess";
		List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery()
				.processDefinitionKey(processKey).list();
		for (ProcessInstance processInstance : processInstances) {
			runtimeService.deleteProcessInstance(processInstance.getId(), "");
		}
	}

	@Test
	public void testSubProcess() {

		identityService.setAuthenticatedUserId("panxh");
		String processKey = "ADProcess";
		//先保存订单和素材等信息 产生一个订单号对应数据库一条记录 然后进入 工作流
		// 
		String orderId = String.valueOf(1);
		Map<String, Object> variables = new HashMap<String, Object>();
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processKey, orderId, variables);
		System.out.println("流程已启动，流程ID：" + processInstance.getId());
		//查询当前的流程
		System.out.println(findRunningProcessInstaces(processKey));

		//step2 查linda 下面的任务列表

		System.out.println("取linda用户下面的待办列表");
		List<Order> list = findTask("linda", processKey);
		System.out.println(list);
		List<Order> list4 = findTask("yqj", processKey);
		System.out.println("取yqj用户下面的待办列表" + list4);

		List<Order> list2 = findTask("guodan", processKey);
		System.out.println("取guodan用户下面的待办列表" + list2);
		System.out.println("linda 签收任务");
		for (Order order : list) {
			//linda 签收任务
			taskService.claim(order.getTask().getId(), "linda");
		}
		List<Order> list3 = findTask("yqj", processKey);
		System.out.println("任务被linda签收了,yqj没任务了" + list3 + "  这里应该为空");

		for (Order order : list) {
			//linda 签收任务
			Map<String, Object> v = new HashMap<String, Object>();
			//我同意了 -linda
			v.put("fristpass", true);
			v.put("r1", "初审通过无不正常内容!");
			taskService.complete(order.getTask().getId(), v);
		}
		list2 = findTask("guodan", processKey);
		System.out.println("guodan用户下面的待办列表,并且查看一下任务的上一步意见," + list2);
		for (Order order : list) {
			String t=order.getTask().getId();
			Map<String, Object> info = taskService.getVariables(t);
			System.out.println(info);
		}
		
	}

	/**
	 * 根据流程定义Id查询流程定义
	 */
	public ProcessDefinition getProcessDefinition(String processDefinitionId) {
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(processDefinitionId).singleResult();
		return processDefinition;
	}

	public List<Order> findTask(String userid, String processDefinitionKey) {

		//存放当前用户的所有任务
		List<Task> tasks = new ArrayList<Task>();

		List<Order> leaves = new ArrayList<Order>();

		//根据当前用户的id查询代办任务列表(已经签收)
		List<Task> taskAssignees = taskService.createTaskQuery().processDefinitionKey(processDefinitionKey)
				.taskAssignee(userid).orderByTaskPriority().desc().orderByTaskCreateTime().desc().list();
		//根据当前用户id查询未签收的任务列表
		List<Task> taskCandidates = taskService.createTaskQuery().processDefinitionKey(processDefinitionKey)
				.taskCandidateUser(userid).orderByTaskPriority().desc().orderByTaskCreateTime().desc().list();

		tasks.addAll(taskAssignees);//添加已签收准备执行的任务(已经分配到任务的人)
		tasks.addAll(taskCandidates);//添加还未签收的任务(任务的候选者)

		//遍历所有的任务列表,关联实体
		for (Task task : tasks) {
			String processInstanceId = task.getProcessInstanceId();
			//根据流程实例id查询流程实例
			ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
					.processInstanceId(processInstanceId).singleResult();
			//获取业务id
			String businessKey = processInstance.getBusinessKey();
			//查询请假实体
			Long orderid = Long.parseLong(businessKey);
			Order v = order.get(orderid);
			//设置属性
			v.setTask(task);
			v.setProcessInstance(processInstance);
			v.setProcessInstanceId(processInstance.getId());
			v.setProcessDefinition(getProcessDefinition(processInstance.getProcessDefinitionId()));

			leaves.add(v);
		}

		return leaves;
	}

	public List<Order> findRunningProcessInstaces(String processDefinitionKey) {

		List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery()
				.processDefinitionKey(processDefinitionKey).list();
		List<Order> list = new ArrayList<Order>();
		for (ProcessInstance processInstance : processInstances) {

			String businessKey = processInstance.getBusinessKey();
			Long orderid = Long.parseLong(businessKey);
			Order v = order.get(orderid);
			if (!order.containsKey(orderid)) {
				runtimeService.deleteProcessInstance(processInstance.getId(), "");
				continue;
			}
			v.setOrderid(orderid);
			v.setProcessInstance(processInstance);
			v.setProcessInstanceId(processInstance.getId());
			v.setProcessDefinition(getProcessDefinition(processInstance.getProcessDefinitionId()));

			//设置当前任务信息
			//根据流程实例id,按照任务创建时间降序排列,查询一条任务信息
			List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId())
					.orderByTaskCreateTime().desc().listPage(0, 1);
			v.setTask(tasks.get(0));
			list.add(v);
		}
		return list;
	}

}