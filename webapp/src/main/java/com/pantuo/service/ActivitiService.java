package com.pantuo.service;

import java.util.List;
import java.util.Map;

import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.runtime.ProcessInstance;

import com.pantuo.dao.pojo.JpaOrders;
import com.pantuo.dao.pojo.UserDetail;
import com.pantuo.mybatis.domain.Orders;
import com.pantuo.util.NumberPageUtil;
import com.pantuo.util.Pair;
import com.pantuo.web.view.OrderView;

public interface ActivitiService {
	
	 //工作流中的一些变量名
    public static final String MAIN_PROCESS = "order";
    public static final String ORDER_ID = "_orderId";
    public static final String OWNER = "_owner";
    public static final String THE_EMAIL = "_theEmail";
    public static final String THE_COMPANY = "_theCompany";
    public static final String TIMEOUT = "_timeout";
    public static final String NOW = "_now";
	/**
	 * 
	 * 启动工作流
	 *
	 * @param u
	 * @param order
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	public void startProcess(UserDetail u, Orders order);
	
	public void startProcess2(UserDetail u, JpaOrders order);

	public Pair<Boolean, String> payment(int orderid, String taskid, UserDetail u);

	public List<OrderView> findTask(String userid, NumberPageUtil page);

//	public Pair<Boolean, String> handle(String orderid, String taskid, String comment, String isok, UserDetail user);
	/**
	 * 
	 * 办理任务
	 *
	 * @param taskId
	 * @param variables
	 * @param u
	 * @return
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	public Pair<Boolean, String> complete(String taskId, Map<String, Object> variables, UserDetail u);

	/*
	 * 扫描工作流 去掉订单不存在的工作流
	 */
	public String reset(String p);

	public OrderView findOrderViewByTaskId(String taskid);

	public List<OrderView> findRunningProcessInstaces(String userId,
			NumberPageUtil page);

	public List<OrderView> findFinishedProcessInstaces(String userId,
			String usertype, NumberPageUtil page);

	//根据taskId查找流程实例
		 public ProcessInstance findProcessInstanceByTaskId(String taskId) throws Exception;
	public List<OrderView> findMyOrders(String userId, NumberPageUtil page);
	//根据taskId查找流程定义
		 public ProcessDefinitionEntity findProcessDefinitionEntityByTaskId(  
		            String taskId) throws Exception;
		//根据流程实例和节点ID查找历史审批记录
		   public List<HistoricActivityInstance> findHistoricUserTask (  
		            ProcessInstance processInstance, String activityId);
}
