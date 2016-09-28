package com.pantuo.service;

import java.security.Principal;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;

import com.pantuo.dao.pojo.JpaOrders;
import com.pantuo.dao.pojo.JpaPayPlan;
import com.pantuo.dao.pojo.UserDetail;
import com.pantuo.mybatis.domain.Orders;
import com.pantuo.pojo.HistoricTaskView;
import com.pantuo.pojo.TableRequest;
import com.pantuo.util.BusinessException;
import com.pantuo.util.NumberPageUtil;
import com.pantuo.util.Pair;
import com.pantuo.web.view.OrderView;

public interface ActivitiService {

	//工作流中的一些变量名
	public static final String MAIN_PROCESS = "order";
	public static final String ORDER_ID = "_orderId";
	public static final String PRONAME = "_proName";
	public static final String COMPANY = "_company";
	public static final String CITY = "_city";
	public static final String PRODUCT = "_product";
	public static final String CLOSED = "_isClosed";
	public static final String ISUPLOADXY = "_isuploadxy";
	public static final String CONTRACT_ENABLE = "canSchedule";
	public static final String SUPPLIEID = "_supplieId";
	public static final String OWNER = "_owner";
	public static final String CREAT_USERID = "_creatUserId";
	public static final String THE_EMAIL = "_theEmail";
	public static final String THE_COMPANY = "_theCompany";
	public static final String TIMEOUT = "_timeout";
	public static final String NOW = "_now";
	public static final String GROUP = "_GRUOP";

	public static String R_BIND_STATIC = "bindstatic";
	public static String R_MODIFY_ORDER = "modifyOrder";
	public static String R_SCHEDULERESULT = "scheduleResult";
	public static String R_SHANGBORESULT = "shangboResult";

	public static String R_DEFAULTALL = "defaultAll";
	public static String R_USERPAYED = "_userPayed";
	public static String PAYPLAN = "payPlan";  //是否分期
	public static String INDEXPAY = "indexpay";  //首付是否付过
	
	public static String FILTERTIME = "filterTime";  //财务确认页面的时间 
	
	public static String U_ADVERTISER = "advertiser";
	
	public static String R_FINISHED = "finished";
	public static String R_CLOSED = "closed";
	
	
	public static enum SystemRoles {
		advertiser,ShibaOrderManager;
	}

	public static enum TaskQueryType {
		task, process, all_running /*查所有订单*/,my/*查我的订单*/
	}

	public static enum OrderStatus {
		payment, auth, report, over,be_contractEnable/*待合同生效*/,be_workcomple/*待施工完成*/,setPayPlan,userFristPay
	}

	public static String paymentString = "待支付", authString = "已支付待审核", reportString = "已排期待上播", overString = "已上播";
	
	public static String bodyWaitAuth = "待确认";

	/**
	 * 
	 * 启动工作流
	 *
	 * @param u
	 * @param order
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	public void startProcess(int city, UserDetail u, Orders order);

	public void startProcess2(int city, UserDetail u, JpaOrders order,UserDetail customer);
	
	public void startTest();

	public Pair<Object, String> payment(HttpServletRequest request,int orderid, String taskid, int contractid, String payType, int isinvoice,int invoiceid,
			String contents,String receway,UserDetail u);

	public Page<OrderView> findTask(int city, Principal principal, TableRequest req, TaskQueryType tqType);
	
	
	public long findTaskCount(int city, Principal principal, TableRequest req, TaskQueryType tqType);

	public Page<OrderView> finished(int city, Principal principal, TableRequest req);

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
	public Pair<Boolean, String> complete(String taskId, Map<String, Object> variables, Principal principal);

	/*
	 * 扫描工作流 去掉订单不存在的工作流
	 */
	public String reset(int city, String p);
	

	public OrderView findOrderViewByTaskId(String taskid,Principal principal);
	
	public OrderView findOrderViewByOrder(int orderId,Principal principal);

    public JpaOrders.Status getOrderStatus(int orderId);

	/**
	 * @deprecated
	 *
	 * @param userId
	 * @param page
	 * @return
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	public List<OrderView> findRunningProcessInstaces(int city,Principal principal, NumberPageUtil page);

	public List<OrderView> findFinishedProcessInstaces(int city, Principal principal, String usertype, NumberPageUtil page);

	//根据taskId查找流程实例
	public ProcessInstance findProcessInstanceByTaskId(String taskId) throws Exception;

	public List<OrderView> findMyOrders(int city, String userId, NumberPageUtil page);

	//根据taskId查找流程定义
	public ProcessDefinitionEntity findProcessDefinitionEntityByTaskId(String taskId) throws Exception;
	/**
	 * @deprecated 
	 * Comment here.
	 *
	 * @param city
	 * @param userid
	 * @param req
	 * @return
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	public Page<OrderView> running(int city, Principal principal, TableRequest req);

	public Page<OrderView> queryOrders(int city, Principal principal, TableRequest req,TaskQueryType tqType);

	//根据流程实例和节点ID查找历史审批记录
	public List<HistoricTaskView> findHistoricUserTask(int city, String processInstanceId, String activityId);

	public Pair<Object, String> modifyOrder(int city, String startdate1,int orderid, String taskid, int supplieid, UserDetail user)throws ParseException;

	public String showOrderDetail(int city, Model model, int orderid, String taskid, String pid, Principal principal, boolean isAutoGoto);

    public JpaOrders.Status fetchStatusAfterTaskComplete(Task task);
	
	/**
	 * 
	 * 
	 * 关闭订单 已支付的订单不能关闭 操作员限定为广告主和世巴管理员
	 * @param orderid
	 * @param taskid
	 * @param principal
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public Pair<Boolean, String> closeOrder(int orderid,String closeRemark, String taskid, Principal principal);
	public ProcessInstance findProcessInstanceByOrderId(int orderid, String userId);




	public Pair<Boolean, String> checkApproveResult(String orderid);

	public String toRestPay(Model model, int orderid, int city, String pid, Principal principal);
	//财务 分期确认
	public String toPlanDetail(Model model, int planId, int city, String pid, Principal principal);
	
	public Pair<Object, String> updatePlan(String rad,JpaPayPlan plan,  Principal principal);
	
	
	
	
	
	
	
	
}
