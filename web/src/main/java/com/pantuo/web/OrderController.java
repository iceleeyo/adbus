package com.pantuo.web;

import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pantuo.dao.pojo.JpaCity;
import com.pantuo.dao.pojo.JpaOrders;
import com.pantuo.dao.pojo.JpaPayPlan;
import com.pantuo.dao.pojo.JpaProduct;
import com.pantuo.dao.pojo.JpaProductV2;
import com.pantuo.dao.pojo.JpaVideo32OrderDetail;
import com.pantuo.dao.pojo.JpaVideo32OrderStatus;
import com.pantuo.mybatis.domain.Contract;
import com.pantuo.mybatis.domain.Invoice;
import com.pantuo.mybatis.domain.Orders;
import com.pantuo.mybatis.domain.PayPlan;
import com.pantuo.mybatis.domain.Supplies;
import com.pantuo.pojo.DataTablePage;
import com.pantuo.pojo.HistoricTaskView;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.ActivitiService;
import com.pantuo.service.ActivitiService.TaskQueryType;
import com.pantuo.service.BusService;
import com.pantuo.service.ContractService;
import com.pantuo.service.CpdService;
import com.pantuo.service.OrderService;
import com.pantuo.service.PayContractService;
import com.pantuo.service.ProductService;
import com.pantuo.service.SuppliesService;
import com.pantuo.service.UserServiceInter;
import com.pantuo.service.impl.IcbcServiceImpl;
import com.pantuo.service.security.Request;
import com.pantuo.util.DateUtil;
import com.pantuo.util.NumberPageUtil;
import com.pantuo.util.Pair;
import com.pantuo.util.Variable;
import com.pantuo.web.view.CardView;
import com.pantuo.web.view.InvoiceView;
import com.pantuo.web.view.OrderPlanView;
import com.pantuo.web.view.OrderView;
import com.pantuo.web.view.SuppliesView;

import scala.collection.generic.BitOperations.Int;

/**
 * 
 *
 * @author xl
 */
@Controller
@RequestMapping(produces = "application/json;charset=utf-8", value = "/order")
public class OrderController {

	@Autowired
	private ContractService contractService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	@Lazy
	private ProductService productService;
	@Autowired
	private SuppliesService suppliesService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	ActivitiService activitiService;
    @Autowired
    BusService busService;
    @Autowired
    HistoryService historyService;
    @Autowired
	private UserServiceInter userService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private CpdService cpdService;
	@Autowired
	IcbcServiceImpl icbcService;
	
	
	@RequestMapping(value = "/ibus/{product_id}", produces = "text/html;charset=utf-8")
	public String ibus(Model model, @PathVariable("product_id") int product_id,
                         Principal principal,@RequestParam(value="cpdid",required = false ,defaultValue = "0") int cpdid,
			@CookieValue(value = "city", defaultValue = "-1") int cityId,
            @ModelAttribute("city") JpaCity city,
            HttpServletRequest request) {
		JpaProductV2 prod = productService.findV2ById(product_id);
		List<Orders> logsList=orderService.queryLogByProId(prod.getId());
		int logCount=orderService.queryLogCountByProId(prod.getId());
		request.getSession(false).setAttribute("token", UUID.randomUUID().toString());
		model.addAttribute("logCount", logCount);
		model.addAttribute("prod", prod);
		model.addAttribute("logsList", logsList);
		return "commonBusPage";
	}
	
	
	@RequestMapping(value = "/iwant/{product_id}", produces = "text/html;charset=utf-8")
	public String buypro(Model model, @PathVariable("product_id") int product_id,
                         Principal principal,@RequestParam(value="cpdid",required = false ,defaultValue = "0") int cpdid,
			@CookieValue(value = "city", defaultValue = "-1") int cityId,
            @ModelAttribute("city") JpaCity city,
            HttpServletRequest request) {
		JpaProduct prod = productService.findById(product_id);
		NumberPageUtil page = new NumberPageUtil(9999, 1, 9999);
		List<Supplies> supplies = suppliesService.queryMyList(cityId, page, null, prod.getType(), principal);
		List<Orders> logsList=orderService.queryLogByProId(prod.getId());
		int logCount=orderService.queryLogCountByProId(prod.getId());
		request.getSession(false).setAttribute("token", UUID.randomUUID().toString());
		model.addAttribute("logCount", logCount);
		model.addAttribute("supplies", supplies);
		model.addAttribute("prod", prod);
		model.addAttribute("logsList", logsList);
		List<Contract> contracts = contractService.queryContractList(cityId, page, null, null, principal);
		model.addAttribute("contracts", contracts);
		model.addAttribute("cpdid", cpdid);

		return "commonPage";
	}

	@RequestMapping(value = "/proDetail", produces = "text/html;charset=utf-8")
	public String proDetail(Model model, Principal principal, @RequestParam(value = "product_id") int product_id,
			HttpServletRequest request) {
		JpaProduct prod = productService.findById(product_id);
		model.addAttribute("prod", prod);
		return "proDetail";
	}

	@RequestMapping(value = "/invoiceDetail/{orderid}")
	@ResponseBody
	public InvoiceView invoice_detail(Model model,@PathVariable int orderid, Principal principal,HttpServletRequest request) {
		    InvoiceView invoiceView = suppliesService.getInvoiceDetail(orderid, principal);
		   return invoiceView;
	}
	@RequestMapping(value = "/eleContract/{orderid}")
	@ResponseBody
	public Pair<Object, String> eleContract(Model model,@PathVariable int orderid, Principal principal,HttpServletRequest request) {
		return  orderService.geteleContract(orderid);
	}
	@RequestMapping(value = "/payview", produces = "text/html;charset=utf-8")
	public String payview(Model model, @RequestParam(value = "taskid", required = true) String taskid,
			@RequestParam(value = "orderid", required = true) String orderid, HttpServletRequest request) {
		model.addAttribute("orderid", orderid);
		model.addAttribute("taskid", taskid);
		return "payview";
	}

	@RequestMapping(value = "/list/{pageNum}")
	public String contralist(Model model,
			@RequestParam(value = "name", required = false, defaultValue = "") String name,
			@RequestParam(value = "code", required = false, defaultValue = "") String code, @PathVariable int pageNum,
			@CookieValue(value = "city", defaultValue = "-1") int city, HttpServletRequest request) {
		int psize = 9;
		NumberPageUtil page = new NumberPageUtil(productService.countMyList(city, name, code, request), pageNum, psize);
		model.addAttribute("list", productService.queryContractList(city, page, name, code, request));
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("paginationHTML", page.showNumPageWithEmpty());
		return "product_list";
	}

	@RequestMapping(value = "payment")
	@ResponseBody
	public Pair<Object, String> payment(@RequestParam(value = "orderid") String orderid,
			@RequestParam(value = "contractid") int contractid, @RequestParam(value = "taskid") String taskid,
			@RequestParam(value = "payType") String payType, @RequestParam(value = "isinvoice") int isinvoice, @RequestParam(value = "invoiceid") int invoiceid,
			@RequestParam(value = "contents") String contents,@RequestParam(value = "receway") String receway,
			Principal principal, HttpServletRequest request, HttpServletResponse response) {
		return activitiService.payment(request,NumberUtils.toInt(orderid), taskid, contractid, payType, isinvoice,invoiceid,contents,receway,
				Request.getUser(principal));
	}
	@RequestMapping(value = "updatePlanState")
	@ResponseBody
	public Pair<Object, String> updatePlanState(
			@RequestParam(value = "orderid",required=false,defaultValue="0") int orderid,
			@RequestParam(value = "payWay",required=false) String payWay,
			@RequestParam(value = "payNextLocation",required=false) String payNextLocation,
			@RequestParam(value = "payContractId",required=false,defaultValue="0") int payContractId,
			PayPlan payPlan,Principal principal, HttpServletRequest request) {
		if(orderid>0 && StringUtils.isNoneBlank(payWay)){
			return orderService.updatePlanState(request,payWay,orderid,payContractId, payNextLocation,
					JpaPayPlan.PayState.check,Request.getUserId(principal));
		}
		
		return orderService.savePayPlan(payPlan, Request.getUserId(principal), request);
	}
	@RequestMapping(value = "video32OrderPay")
	@ResponseBody
	public Pair<Object, String> video32OrderPay(
			Principal principal, HttpServletRequest request) {
		return orderService.video32OrderPay(request,principal);
	}

	@RequestMapping(value = "claim/{taskid}")
	@ResponseBody
	public Pair<Boolean, String> claimTask(
			@PathVariable("taskid") String taskid, Principal principal,
			HttpServletRequest request, HttpServletResponse response) {
		taskService.claim(taskid, Request.getUserId(principal));
		return new Pair<Boolean, String>(true, "任务签收成功!");
	}
	
	@RequestMapping(value = "/closeOrder/{taskid}")
	@ResponseBody
	public Pair<Boolean, String> closeOrder(@RequestParam(value = "orderid", required = true) String orderid,
			@RequestParam(value = "closeRemark", required = false) String closeRemark,
			@PathVariable String taskid, Principal principal, HttpServletRequest request, HttpServletResponse response) {
		return	activitiService.closeOrder(org.apache.commons.lang.math.NumberUtils.toInt(orderid),closeRemark, taskid, principal);
	}

	public static Date addDay(Date date, int n) {
		Calendar calendar = DateUtil.newCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, n);
		return (calendar.getTime());
	}

	@RequestMapping(value = "/handl/{taskid}", produces = "text/html;charset=utf-8")
	public String HandleView2(Model model,
            @PathVariable("taskid") String taskid,
			Principal principal,
            @CookieValue(value = "city", defaultValue = "-1") int cityId,
            @ModelAttribute("city") JpaCity city,
            HttpServletRequest request)
			throws Exception {
		NumberPageUtil page = new NumberPageUtil(9999, 1, 9999);
		Task task = taskService.createTaskQuery().includeProcessVariables().taskId(taskid).singleResult();
		HistoricTaskInstance currTask = historyService
                .createHistoricTaskInstanceQuery().taskId(taskid)
                .singleResult();
		Date claimT=currTask.getClaimTime();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String claimTime=sdf.format(claimT);
		ExecutionEntity executionEntity = (ExecutionEntity) runtimeService.createExecutionQuery()
				.executionId(task.getExecutionId()).processInstanceId(task.getProcessInstanceId()).singleResult();
		OrderView v = activitiService.findOrderViewByTaskId(taskid, principal);
		if (v.getOrder() != null && v.getOrder().getEndTime() != null) {
			v.getOrder().setEndTime(addDay(v.getOrder().getEndTime(), -1));
		}
		String activityId = executionEntity.getActivityId();
		ProcessInstance pe = activitiService.findProcessInstanceByTaskId(taskid);
		List<HistoricTaskView> activitis = activitiService.findHistoricUserTask(cityId, pe.getProcessInstanceId(),
				activityId);
		JpaProduct prod = productService.findById(v.getOrder().getProductId());
		List<Contract> contracts = contractService.queryContractList(cityId, page, null, null, principal);
		List<Supplies> supplieslist = suppliesService.querySuppliesByUser(cityId, principal,v.getProduct().getType().ordinal());
		SuppliesView suppliesView = suppliesService.getSuppliesDetail(v.getOrder(), principal);
		SuppliesView quafiles = suppliesService.getQua(v.getOrder().getSuppliesId(), null);
		List<Invoice> InvoiceList = userService.queryInvoiceByUser(cityId, principal);
		
		model.addAttribute("cpdDetail", cpdService.queryOneCpdByPid(v.getOrder().getProductId()));
		model.addAttribute("suppliesView", suppliesView);
		model.addAttribute("quafiles", quafiles);
		model.addAttribute("contracts", contracts);
		model.addAttribute("supplieslist", supplieslist);
		model.addAttribute("taskid", taskid);
		model.addAttribute("paymentResult", task.getProcessVariables().get("paymentResult"));
		model.addAttribute("orderview", v);
		model.addAttribute("prod", prod);
		model.addAttribute("claimTime", claimTime);
		
		orderService.fullFristPayInfo(model, activityId, v);
		orderService.fullPayPlanInfo(model, activityId, v);
		if(v!=null && v.getOrder()!=null){
		model.addAttribute("contract", contractService.selectContractById(v.getOrder().getContractId()));
			if (StringUtils.equals(task.getName(), "支付") || StringUtils.equals(activityId, "userFristPay")) {
				if (StringUtils.equals(activityId, "userFristPay")) {
					Map<String, Object> modelMap = model.asMap();
					//总金额的工商支付
					icbcService.sufficeIcbcSubmit(model, v.getLongOrderId(),
							new CardView(null, null, (Double) modelMap.get("payAll"), 1), "offline", StringUtils.EMPTY,
							"&L=".concat((String) modelMap.get("allLocation")));
					icbcService.sufficeIcbcSubmit(model, v.getLongOrderId(),
							new CardView(null, null, (Double) modelMap.get("payNext"), 1), "offline",
							"_plan", "&L=".concat((String) modelMap.get("payNextLocation")));
				} else {
					icbcService.sufficeIcbcSubmit(model, v.getLongOrderId(), new CardView(null, null, v.getOrder()
							.getPrice(), 1), "offline");
				}
			}
		}
		model.addAttribute("InvoiceList", InvoiceList);
		model.addAttribute("activitis", activitis);
		if (StringUtils.equals(ActivitiService.R_BIND_STATIC, activityId)) {
			activityId = ActivitiService.R_MODIFY_ORDER;
		}
		model.addAttribute("activityId", activityId);
		return "handleView2";
	}
	@RequestMapping(value = "/hand32Order/{orderStatusId}", produces = "text/html;charset=utf-8")
	public String hand32Order(Model model,
			@PathVariable("orderStatusId") int orderStatusId,
			Principal principal,HttpServletRequest request){
				JpaVideo32OrderStatus video32OrderStatus=orderService.findJpaVideo32OrderStatus(principal,orderStatusId);
				if(video32OrderStatus!=null){
					model.addAttribute("video32OrderStatus", video32OrderStatus);
					model.addAttribute("activityId", video32OrderStatus.getStatus().name());
				}
		return "video32OrderHandle";
	}
	@RequestMapping(value = "/orderDetail/{orderid}", produces = "text/html;charset=utf-8")
	public String orderDetail(Model model, @PathVariable("orderid") int orderid,
			@RequestParam(value = "taskid", required = false) String taskid,
			@RequestParam(value = "auto", required = false) String auto,
			@RequestParam(value = "pid", required = false) String pid, Principal principal,
			@CookieValue(value = "city", defaultValue = "-1") int city, HttpServletRequest request) throws Exception {
		return activitiService.showOrderDetail(city, model, orderid, taskid, pid, principal,BooleanUtils.toBoolean(auto),request);
	}
	
	
	
	@RequestMapping(value = "/toPlanDetail/{planId}", produces = "text/html;charset=utf-8")
	public String to1PlanDetail(Model model, @PathVariable("planId") int planId,Principal principal,
			@RequestParam(value = "pid", required = false) String pid,
			@CookieValue(value = "city", defaultValue = "-1") int city,
			 HttpServletRequest request)  {
		model.addAttribute("planId", planId);
		return activitiService.toPlanDetail(model,planId,city,pid,principal);
	}
	
	@RequestMapping(value = "updatePlan")
	@ResponseBody
	public Pair<Object, String> updatePlan(JpaPayPlan plan, @RequestParam(value = "rad", required = false) String rad,
			Principal principal, HttpServletRequest request, HttpServletResponse response) {
		return activitiService.updatePlan(rad, plan, principal);
	}
	
	@RequestMapping(value = "/toRestPay/{orderid}", produces = "text/html;charset=utf-8")
	public String toRestPay(Model model, @PathVariable("orderid") int orderid,Principal principal,
			@RequestParam(value = "pid", required = false) String pid,
			@CookieValue(value = "city", defaultValue = "-1") int city,
			 HttpServletRequest request)  {
		return activitiService.toRestPay(model,orderid,city,pid,principal);
	}
	@RequestMapping(value = "modifyOrder")
	@ResponseBody
	public Pair<Object, String> modifyOrder(@RequestParam(value = "orderid") String orderid,
			@CookieValue(value = "city", defaultValue = "-1") int city,@RequestParam(value="startdate1",required=false) String startdate1,
			@RequestParam(value = "supplieid") int supplieid, @RequestParam(value = "taskid") String taskid,
			Principal principal, HttpServletRequest request, HttpServletResponse response) throws NumberFormatException, ParseException {
		    return activitiService.modifyOrder(city, startdate1,Integer.parseInt(orderid), taskid, supplieid,
				Request.getUser(principal));
	}
	@RequestMapping(value = "checkApproveResult")
	@ResponseBody
	public Pair<Boolean, String> checkApproveResult(@RequestParam(value = "orderid") String orderid
			)  {
		return activitiService.checkApproveResult(orderid);
	}
	@RequestMapping(value = "checkNeedPay")
	@ResponseBody
	public Boolean checkNeedPay(@RequestParam(value = "orderId" ,defaultValue="0") int  orderid,
			@RequestParam(value = "payContractId" ,defaultValue="0") int  payContractId
			)  {
		return orderService.checkNeedPay(orderid,payContractId);
	}
	@RequestMapping(value = "findOrderAndSup/{orderid}")
	@ResponseBody
	public Pair<Object, Object> findOrderAndSup(@PathVariable("orderid") int orderid,
			@CookieValue(value = "city", defaultValue = "-1") int city,Principal principal
			)  {
		return orderService.findOrderAndSup(city, principal,orderid);
	}
	@RequestMapping(value = "editOrderStartTime/{orderid}")
	@ResponseBody
	public Pair<Boolean, String> editOrderStartTime(@PathVariable("orderid") int orderid,@RequestParam(value="supid") int supid,
			@CookieValue(value = "city", defaultValue = "-1") int city,
			@RequestParam(value="startD") String startD,@RequestParam(value="ordRemark") String ordRemark,Principal principal
			)  {
		return orderService.editOrderStartTime(orderid,supid,startD,ordRemark,city,principal);
	}

	@RequestMapping(value = "confirm", method = RequestMethod.POST, produces = "text/html;charset=utf-8")
	public String saveOrderJpa(Model model, JpaOrders order, Principal principal,
           @CookieValue(value = "city", defaultValue = "-1") int cityId,
           @RequestParam(value="cpdid",required = false ,defaultValue = "0") int cpdid,
           @RequestParam(value="token",required = false) String token,
           @ModelAttribute("city") JpaCity city,
            HttpServletRequest request)
			throws IllegalStateException, IOException, ParseException,Exception {
		if(StringUtils.isNotBlank(token) && StringUtils.equals((CharSequence) request.getSession().getAttribute("token"), token)){
			request.getSession().removeAttribute("token");
		}else{
			throw new Exception("不能重复下单!");
		}
		NumberPageUtil page = new NumberPageUtil(9999, 1, 9999);
		order.setCreator(Request.getUserId(principal));
		order.setStats(JpaOrders.Status.unpaid);
		JpaProduct prod = productService.findById(order.getProductId());
		
		if (prod != null) {
			order.setPrice(prod.getPrice());
		}
		order.setType(prod.getType());
		String start = request.getParameter("startTime1").toString();
		if (!start.isEmpty()) {
			Date startTime = DateUtil.longDf.get().parse(start);
			order.setStartTime(startTime);
			Calendar cal = DateUtil.newCalendar();
			cal.setTime(startTime);
			cal.add(Calendar.DAY_OF_MONTH, prod.getDays());
			order.setEndTime(cal.getTime());
		} else {
			//			return new Pair<Boolean, String>(false, "请指定订单开播时间");
		}
		OrderView v = new OrderView();
		orderService.saveOrderJpa(cityId, order, Request.getUser(principal),cpdid,prod);
		List<Supplies> supplieslist = suppliesService.querySuppliesByUser(cityId, principal,0);
		List<Invoice> InvoiceList = userService.queryInvoiceByUser(cityId, principal);
		List<Contract> contracts = contractService.queryContractList(cityId, page, null, null, principal);
		SuppliesView suppliesView = suppliesService.getSuppliesDetail(order, principal);
		SuppliesView quafiles = suppliesService.getQua(order.getSuppliesId(), null);
		JpaOrders orders = orderService.queryOrderDetail(order.getId(),principal);
		v.setOrder(orders);
		model.addAttribute("supplieslist", supplieslist);
		model.addAttribute("InvoiceList", InvoiceList);
		model.addAttribute("order", order);
		model.addAttribute("prod", prod);
		model.addAttribute("orderview", v);
		model.addAttribute("contract", contractService.selectContractById(v.getOrder().getContractId()));
		model.addAttribute("contracts", contracts);
		model.addAttribute("suppliesView", suppliesView);
		model.addAttribute("quafiles", quafiles);


		return "relateSup";
	}


	 
	/**
	 * 根据任务Id完成任务
	 * @return
	 */
	@RequestMapping(value = "/{taskId}/complete", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public Pair<Boolean, String> completeTask(@PathVariable("taskId") String taskId, Principal principal,
			Supplies supplies, Variable variable, @CookieValue(value = "city", defaultValue = "-1") int city) {
		if (null != supplies && null != supplies.getSeqNumber() && !supplies.getSeqNumber().equals("")) {
			suppliesService.updateSupplies(city, supplies);
		}
		return activitiService.complete(taskId, variable.getVariableMap(), principal);
	}

	@RequestMapping(value = "creOrder", method = RequestMethod.POST)
	@ResponseBody
	public Pair<Boolean, String> saveOrder(Orders order, Principal principal,
			@CookieValue(value = "city", defaultValue = "-1") int city) throws IllegalStateException, IOException,
			ParseException {

		return orderService.saveOrder(city, order, principal);
	}

	/**
	 * 
	 * author:impanxh
	 *
	 * @param req
	 * @param principal
	 * @return
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	@RequestMapping("ajax-orderlist")
	@ResponseBody
	public DataTablePage<OrderView> getAllContracts(TableRequest req, Principal principal,
			@CookieValue(value = "city", defaultValue = "-1") int city) {
		Page<OrderView> w = orderService.getOrderList(city, req, principal);
		return new DataTablePage(w, req.getDraw());
	}
	@RequestMapping("ajax-video32Orderlist")
	@ResponseBody
	public DataTablePage<JpaVideo32OrderStatus> getVideo32Orderlist(TableRequest req, Principal principal,
			@CookieValue(value = "city", defaultValue = "-1") int city) {
		Page<JpaVideo32OrderStatus> page = orderService.getVideo32Orderlist(city, req, principal);
		return new DataTablePage(page, req.getDraw());
	}

	@RequestMapping(value = "/myTask/{pageNum}")
	public String list() {
		return "orderList";
	}
	@RequestMapping(value = "/to32OrderList/{pageNum}")
	public String to32OrderList() {
		return "video32OrderList";
	}

	@RequestMapping(value = "/customer/{orderid}", produces = "text/html;charset=utf-8")
	public String productDetail(@RequestParam("fname") String fname, Model model, @PathVariable("orderid") int orderid,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("X-Frame-Options", "SAMEORIGIN");
		JpaOrders order = orderService.getJpaOrder(orderid);
		OrderView view = new OrderView();
		view.setOrder(order);
		model.addAttribute("view", view);
		return "template/" + fname;
	}
	 

	@RequestMapping(value = "/myTaskbak/{pageNum}", method = RequestMethod.GET)
	public String myTask(Model model, @PathVariable int pageNum, Principal principal) {
		NumberPageUtil page = new NumberPageUtil(pageNum);
		page.setPagesize(30);
		//model.addAttribute("list", activitiService.findTask(Request.getUserId(principal), page));
		model.addAttribute("pageNum", page);
		return "mytask";
	}

	@RequestMapping("ajax-runningAjax")
	@ResponseBody
	public DataTablePage<OrderView> runningAjax(TableRequest req, Principal principal,
			@CookieValue(value = "city", defaultValue = "-1") int city) {
		//Page<OrderView> w = activitiService.running(city, Request.getUserId(principal), req);
		Page<OrderView> w = activitiService.queryOrders(city, principal, req,TaskQueryType.all_running);
		return new DataTablePage<OrderView>(w, req.getDraw());
	}
	@PreAuthorize( " hasRole('ShibaOrderManager')" + " or hasRole('ShibaFinancialManager')"
			+ "or hasRole('BeiguangMaterialManager')" + "or hasRole('BeiguangScheduleManager')"
			+ "or hasRole('ShibaSuppliesManager') or hasRole('UserManager') or hasRole('salesManager')")
	@RequestMapping(value = "/allRuningOrders/{pageNum}")
	public String allRuningOrders(Model model, Principal principal, @PathVariable int pageNum,
			HttpServletRequest request, HttpServletResponse response) {
		return "allRuningOrders";
	}
	
	
	
	@RequestMapping(value = "/planOrders")
	public String planOrders() {
		return "planOrders";
	}
	
	@RequestMapping(value = "/planContract")
	public String planContract() {
		return "planContract";
	}

	@RequestMapping("ajax-planOrders")
	@ResponseBody
	public Page<OrderPlanView> ajaxPlanOrders(TableRequest req, Principal principal,
			@CookieValue(value = "city", defaultValue = "-1") int city) {
		return orderService.queryAllPlan(req);
	}
	
	@RequestMapping(value = "/product/{productid}/{pageNum}")
	public String productOrders(Model model, Principal principal, @PathVariable int productid, @PathVariable int pageNum,
			HttpServletRequest request, HttpServletResponse response) {
		JpaProduct prod = productService.findById(productid);;
		model.addAttribute("productId", productid);
		model.addAttribute("prod", prod);
		return "productOrders";
	}


	@RequestMapping(value = "/myOrders/{pageNum}")
	public String myOrders(Model model) {
		model.addAttribute("orderMenu", "我的订单");
		return "myOrders";
	}
	@PreAuthorize( " hasRole('ShibaOrderManager')" + " or hasRole('ShibaFinancialManager')"
			+ "or hasRole('BeiguangMaterialManager')" + "or hasRole('BeiguangScheduleManager')"
			+ "or hasRole('ShibaSuppliesManager')or hasRole('UserManager') ")
	@RequestMapping(value = "/join/{pageNum}")
	public String joinOrder(Model model) {
		model.addAttribute("orderMenu", "我参与订单");
		return "myOrders";
	}

	@RequestMapping("ajax-myOrders")
	@ResponseBody
	public DataTablePage<OrderView> myOrders(TableRequest req, Principal principal,
			@CookieValue(value = "city", defaultValue = "-1") int city) {
		Page<OrderView> w = activitiService.queryOrders(city, (principal), req,TaskQueryType.my);
		return new DataTablePage<OrderView>(w, req.getDraw());
	}

	@RequestMapping(value = "/finished")
	public String finishedOrders() {
		return "finishedOrders";
	}
	@RequestMapping(value = "/scheduleAdjust")
	public String scheduleAdjust() {
		return "scheduleAdjust";
	}
	
	
	@RequestMapping(value = "/over/{productid}")
	public String product_finish(Model model, Principal principal, @PathVariable int productid,  
			HttpServletRequest request, HttpServletResponse response) {
		JpaProduct prod = productService.findById(productid);;
		model.addAttribute("productId", productid);
		model.addAttribute("prod", prod);
		return "productFinishedOrders";
	}
	
	@RequestMapping("ajax-finishedOrders")
	@ResponseBody
	public DataTablePage<OrderView> finishedAjax(TableRequest req, Principal principal,
			@CookieValue(value = "city", defaultValue = "-1") int city) {
		Page<OrderView> w = activitiService
				.finished(city, principal,req);
		return new DataTablePage<OrderView>(w, req.getDraw());
	}
	@RequestMapping("ajax-scheduleAdjust")
	@ResponseBody
	public DataTablePage<OrderView> scheduleAdjust(TableRequest req, Principal principal
			) {
		Page<OrderView> w = activitiService.scheduleAdjust(principal,req);
				
		return new DataTablePage<OrderView>(w, req.getDraw());
	}

    
    @PreAuthorize(" hasRole('ShibaOrderManager')")
	@RequestMapping(value = "/setOrderPrice", method = RequestMethod.POST)
	@ResponseBody
	public Pair<Boolean, String> setOrderPrice(@RequestParam("orderId") int orderId, @RequestParam("price") double price) {
		return orderService.updateOrderPrice(orderId, price);
	}

	@RequestMapping(value = "ajax-getPayPlan", method = RequestMethod.GET)
	@ResponseBody
	public List<PayPlan> getPayPlan(HttpServletResponse response,TableRequest req
			) {
		 response.setHeader("Access-Control-Allow-Origin", "*");
		return orderService.getPayPlan(req);
	}
	/**
	 * 获取32寸屏订单批次
	 * @param response
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value = "ajax-getOrder32Detail", method = RequestMethod.GET)
	@ResponseBody
	public List<JpaVideo32OrderDetail> getOrder32Detail(HttpServletResponse response,
			@RequestParam("orderId") int orderId) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		return orderService.getOrder32Detail(orderId);
	}
	@PreAuthorize( "hasRole('sales')")
	@RequestMapping(value = "/deletePayPlan/{id}")
	@ResponseBody
	public Pair<Boolean, String> deletePayPlan(@PathVariable("id") int id
			){
		
		return orderService.deletePayPlan(id);
	}
	@RequestMapping(value = "/checkOrderPrice/{id}")
	@ResponseBody
	public Pair<Boolean, String> checkOrderPrice(@PathVariable("id") int id
			){
		
		return orderService.checkOrderPrice(id);
	}
	@RequestMapping(value = "/queryPayPlanByid/{id}")
	@ResponseBody
	public JpaPayPlan queryPayPlanByid(Model model,
			@PathVariable("id") int id, HttpServletRequest request) {
		   return orderService.queryPayPlanByid(id);
	} 
	@RequestMapping(value = "/savePayPlan")
	@ResponseBody
	public Pair<Object, String> savePayPlan(PayPlan payPlan,
			 Principal principal,@RequestParam(value = "payDate") String payDate,
			HttpServletRequest request, @RequestParam(value = "orderId", defaultValue = "0") int orderId)  {
		return orderService.savePayPlan(payPlan,Request.getUserId(principal),request);
	}
	@RequestMapping(value = "/queryPayPlanDetail/{orderId}")
	public String toPayPlanDetail(Model model, @PathVariable("orderId") int orderId,
			@RequestParam("type") String type,
			HttpServletResponse response) {
		response.setHeader("X-Frame-Options", "SAMEORIGIN");
		model.addAttribute("orderId", orderId);
		model.addAttribute("type", type);
		return "payPlanDetail";
	}
	@RequestMapping(value = "/payPlanOrders")
	public String payPlanOrders() {
		return "payPlanOrders";
	}
	@RequestMapping("ajax-payPlanOrders")
	@ResponseBody
	public DataTablePage<OrderView> getPayPlanOrders(TableRequest req,Principal principal)
			{
		Page<OrderView> page = orderService.getPayPlanOrders(req ,principal);
		return new DataTablePage(page, req.getDraw());
	}
	@RequestMapping("ajax-queryPayPlanDetail")
	@ResponseBody
	public DataTablePage<JpaPayPlan> queryPayPlanDetail(TableRequest req,Principal principal)
	{
		Page<JpaPayPlan> jpabuspage = orderService.queryPayPlanDetail(req ,req.getPage(), req.getLength());
		return new DataTablePage(jpabuspage, req.getDraw());
	}
}
