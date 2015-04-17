package com.pantuo.web.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;

import antlr.StringUtils;

import com.pantuo.mybatis.domain.Orders;
import com.pantuo.mybatis.domain.Product;
import com.pantuo.mybatis.domain.Supplies;

import freemarker.template.utility.StringUtil;

public class OrderView {

	Orders order;
	Product product;
	Supplies supplies;
	// -- 临时属性 --//
	int id;
	long longOrderId = 0;
	String payTypeString;
	// 流程任务
	private Task task;//task 对象转json时 jpa 延迟加载有问题

	private String task_id;
	private String task_name;
	private String executionId;
	private String task_assignee;

	//
	private String processInstanceId;
	private Map<String, Object> variables;

	// 运行中的流程实例
	private ProcessInstance processInstance;

	// 历史的流程实例
	private HistoricProcessInstance historicProcessInstance;

	// 流程定义
	private ProcessDefinition processDefinition;

	public Orders getOrder() {
		return order;
	}

	long maxId = 100000L, split = maxId * 10L;

	public long getIdFromDate(int id, Date date) {
		SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
		return NumberUtils.toLong(sd.format(date)) * split + maxId + id;
	}

	public int longOrderId2DbId(long longOrderId) {
		return (int) (longOrderId - (longOrderId / split) * split - maxId);
	}

	public void setOrder(Orders order) {
		if (ObjectUtils.notEqual(order, null) && ObjectUtils.notEqual(order.getId(), null)) {
			longOrderId = getIdFromDate(order.getId(), order.getCreated());
			this.id = order.getId();
		}
		this.order = order;

	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Supplies getSupplies() {
		return supplies;
	}

	public void setSupplies(Supplies supplies) {
		this.supplies = supplies;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		setTask(task, true);
	}

	/**
	 * 
	 * task 对象转json时 jpa 延迟加载有问题
	 *
	 * @param task
	 * @param setTaskEmpty
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	public void setTask(Task task, boolean setTaskEmpty) {
		this.task = task;
		task_id = task.getId();
		task_name = task.getName();
		executionId = task.getExecutionId();
		task_assignee = task.getAssignee();
		if (setTaskEmpty) {
			this.task = null;
		}
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
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

	public long getLongOrderId() {
		return longOrderId;
	}

	public void setLongOrderId(long longOrderId) {
		this.longOrderId = longOrderId;
	}

	public String getPayTypeString() {
		if (order == null || order.getPayType() == null) {
			return org.apache.commons.lang3.StringUtils.EMPTY;
		}
		switch (order.getPayType()) {
		case 0:
			return "关联合同";
		case 1:
			return "线上支付";
		case 2:
			return "其他";
		default:
			return "";
		}
	}

	public void setPayTypeString(String payTypeString) {
		this.payTypeString = payTypeString;
	}

	public String getTask_id() {
		return task_id;
	}

	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}

	public String getTask_name() {
		return task_name;
	}

	public void setTask_name(String task_name) {
		this.task_name = task_name;
	}

	public String getExecutionId() {
		return executionId;
	}

	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}

	public long getMaxId() {
		return maxId;
	}

	public void setMaxId(long maxId) {
		this.maxId = maxId;
	}

	public long getSplit() {
		return split;
	}

	public void setSplit(long split) {
		this.split = split;
	}

	public String getTask_assignee() {
		return task_assignee;
	}

	public void setTask_assignee(String task_assignee) {
		this.task_assignee = task_assignee;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
