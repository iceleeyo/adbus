package com.pantuo.web.view;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import com.pantuo.dao.pojo.JpaBodyContract;
import com.pantuo.dao.pojo.JpaOrderBuses;
import com.pantuo.dao.pojo.JpaOrders;
import com.pantuo.dao.pojo.JpaProduct;
import com.pantuo.dao.pojo.JpaSupplies;
import com.pantuo.util.OrderIdSeq;

public class OrderView {

    JpaOrders order;
    Set<JpaOrderBuses> orderBuses;
    JpaProduct product;
	JpaSupplies supplies;
	// -- 临时属性 --//
	int id;
	long longOrderId = 0;
	String payTypeString;
	// 流程任务
	private Task task;//task 对象转json时 jpa 延迟加载有问题
	
	private int haveTasks;//当前订单 当前需要处理的 待办事项个数

	private String task_id;
	private String task_name;
	private String definitionKey;//xml定义的英文说明 
	private String executionId;
	private String task_assignee;
	private boolean approve1Result;
	private Date task_createTime;
	
	//=====================================
	private JpaBodyContract jpaBodyContract;
	
	private int need_cars;//合同需要车辆总数
	private int done_cars;//已安装车辆总数
	
	
	
	
	//--------- 订单历史信息
	boolean canClosed = false;
	//订单是否关闭,目前只在历史订单里显示订单关闭原因时使用
	boolean closed = false;
	private String finishedState = StringUtils.EMPTY;
	public Date startTime;
	public Date endTime;
	//
	private String processInstanceId;
	private Map<String, Object> variables;

	// 运行中的流程实例
	private ProcessInstance processInstance;

	// 历史的流程实例
	private HistoricProcessInstance historicProcessInstance;

	// 流程定义
	private ProcessDefinition processDefinition;

	public JpaOrders getOrder() {
		return order;
	}

	public void setOrder(JpaOrders order) {
		if (ObjectUtils.notEqual(order, null) && ObjectUtils.notEqual(order.getId(), null)) {
			longOrderId = OrderIdSeq.getIdFromDate(order.getId(), order.getCreated());
			this.id = order.getId();
		}
		this.order = order;
        this.orderBuses = order.getOrderBuses();

	}

    public Set<JpaOrderBuses> getOrderBuses() {
        return orderBuses;
    }

    public void setOrderBuses(Set<JpaOrderBuses> orderBuses) {
        this.orderBuses = orderBuses;
    }


	public boolean isApprove1Result() {
		return approve1Result;
	}

	public void setApprove1Result(boolean approve1Result) {
		this.approve1Result = approve1Result;
	}

	public JpaProduct getProduct() {
		return product;
	}

	public void setProduct(JpaProduct product) {
		this.product = product;
	}

	public JpaSupplies getSupplies() {
		return supplies;
	}

	public void setSupplies(JpaSupplies supplies) {
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
		definitionKey = task.getTaskDefinitionKey();
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
		case contract:
			return "关联合同";
		case online:
			return "线上支付";
		case check:
			return "支票支付";
		case remit:
			return "汇款";
		case cash:
			return "现金交易";
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

	public Date getTask_createTime() {
		return task_createTime;
	}

	public void setTask_createTime(Date task_createTime) {
		this.task_createTime = task_createTime;
	}

	public int getHaveTasks() {
		return haveTasks;
	}

	public void setHaveTasks(int haveTasks) {
		this.haveTasks = haveTasks;
	}

	public boolean isCanClosed() {
		return canClosed;
	}

	public void setCanClosed(boolean canClosed) {
		this.canClosed = canClosed;
	}

	public String getFinishedState() {
		return finishedState;
	}

	public void setFinishedState(String finishedState) {
		this.finishedState = finishedState;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public boolean isClosed() {
		return closed;
	}

	public void setClosed(boolean closed) {
		this.closed = closed;
	}

	public String getDefinitionKey() {
		return definitionKey;
	}

	public void setDefinitionKey(String definitionKey) {
		this.definitionKey = definitionKey;
	}

	public JpaBodyContract getJpaBodyContract() {
		return jpaBodyContract;
	}

	public void setJpaBodyContract(JpaBodyContract jpaBodyContract) {
		this.jpaBodyContract = jpaBodyContract;
	}

	public int getNeed_cars() {
		return need_cars;
	}

	public void setNeed_cars(int need_cars) {
		this.need_cars = need_cars;
	}

	public int getDone_cars() {
		return done_cars;
	}

	public void setDone_cars(int done_cars) {
		this.done_cars = done_cars;
	}

	 
}
