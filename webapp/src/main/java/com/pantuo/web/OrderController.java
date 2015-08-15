package com.pantuo.web;

import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
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

import com.pantuo.dao.pojo.BaseEntity;
import com.pantuo.dao.pojo.JpaBus;
import com.pantuo.dao.pojo.JpaCity;
import com.pantuo.dao.pojo.JpaOrderBuses;
import com.pantuo.dao.pojo.JpaOrders;
import com.pantuo.dao.pojo.JpaProduct;
import com.pantuo.dao.pojo.UserDetail;
import com.pantuo.mybatis.domain.Contract;
import com.pantuo.mybatis.domain.Invoice;
import com.pantuo.mybatis.domain.Orders;
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
import com.pantuo.service.ProductService;
import com.pantuo.service.SuppliesService;
import com.pantuo.service.UserServiceInter;
import com.pantuo.util.DateUtil;
import com.pantuo.util.NumberPageUtil;
import com.pantuo.util.Pair;
import com.pantuo.util.Request;
import com.pantuo.util.Variable;
import com.pantuo.web.view.InvoiceView;
import com.pantuo.web.view.OrderView;
import com.pantuo.web.view.SuppliesView;

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
		return activitiService.payment(Integer.parseInt(orderid), taskid, contractid, payType, isinvoice,invoiceid,contents,receway,
				Request.getUser(principal));
	}

	@RequestMapping(value = "claim")
	@ResponseBody
	public Pair<Boolean, String> claimTask(@RequestParam(value = "orderid", required = true) String orderid,
			@RequestParam(value = "taskid", required = true) String taskid, Principal principal,
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

	@RequestMapping(value = "/handleView2", produces = "text/html;charset=utf-8")
	public String HandleView2(Model model,
            @RequestParam(value = "taskid", required = true) String taskid,
			Principal principal,
            @CookieValue(value = "city", defaultValue = "-1") int cityId,
            @ModelAttribute("city") JpaCity city,
            HttpServletRequest request)
			throws Exception {
		NumberPageUtil page = new NumberPageUtil(9999, 1, 9999);
		Task task = taskService.createTaskQuery().taskId(taskid).singleResult();
		HistoricTaskInstance currTask = historyService
                .createHistoricTaskInstanceQuery().taskId(taskid)
                .singleResult();
		Date claimT=currTask.getClaimTime();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String claimTime=sdf.format(claimT);
		ExecutionEntity executionEntity = (ExecutionEntity) runtimeService.createExecutionQuery()
				.executionId(task.getExecutionId()).processInstanceId(task.getProcessInstanceId()).singleResult();
		OrderView v = activitiService.findOrderViewByTaskId(taskid, principal);
		String activityId = executionEntity.getActivityId();
		ProcessInstance pe = activitiService.findProcessInstanceByTaskId(taskid);
		List<HistoricTaskView> activitis = activitiService.findHistoricUserTask(cityId, pe.getProcessInstanceId(),
				activityId);
		JpaProduct prod = productService.findById(v.getOrder().getProductId());
		List<Contract> contracts = contractService.queryContractList(cityId, page, null, null, principal);
		List<Supplies> supplieslist = suppliesService.querySuppliesByUser(cityId, principal);
		SuppliesView suppliesView = suppliesService.getSuppliesDetail(v.getOrder().getSuppliesId(), null);
		SuppliesView quafiles = suppliesService.getQua(v.getOrder().getSuppliesId(), null);
		List<Invoice> InvoiceList = userService.queryInvoiceByUser(cityId, principal);
		
		model.addAttribute("cpdDetail", cpdService.queryOneCpdByPid(v.getOrder().getProductId()));
		model.addAttribute("suppliesView", suppliesView);
		model.addAttribute("quafiles", quafiles);
		model.addAttribute("contracts", contracts);
		model.addAttribute("supplieslist", supplieslist);
		model.addAttribute("taskid", taskid);
		model.addAttribute("orderview", v);
		model.addAttribute("prod", prod);
		model.addAttribute("claimTime", claimTime);
		model.addAttribute("InvoiceList", InvoiceList);
		model.addAttribute("activitis", activitis);
		if (StringUtils.equals(ActivitiService.R_BIND_STATIC, activityId)) {
			activityId = ActivitiService.R_MODIFY_ORDER;
		}
		model.addAttribute("activityId", activityId);

        //选车
        if (city != null && city.getMediaType() == JpaCity.MediaType.body) {
            model.addAttribute("lines", busService.getAllBuslines(cityId, prod.getLineLevel(), null, 0, 9999, null).getContent());
            model.addAttribute("models", busService.getAllBusModels(cityId, null, null, 0, 9999, null).getContent());
            model.addAttribute("companies", busService.getAllBusinessCompanies(cityId, null, null, 0, 9999, null).getContent());
            model.addAttribute("categories", JpaBus.Category.values());
        }
		return "handleView2";
	}

	@RequestMapping(value = "modifyOrder")
	@ResponseBody
	public Pair<Object, String> modifyOrder(@RequestParam(value = "orderid") String orderid,
			@CookieValue(value = "city", defaultValue = "-1") int city,
			@RequestParam(value = "supplieid") int supplieid, @RequestParam(value = "taskid") String taskid,
			Principal principal, HttpServletRequest request, HttpServletResponse response) {
		    return activitiService.modifyOrder(city, Integer.parseInt(orderid), taskid, supplieid,
				Request.getUser(principal));
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
		List<Supplies> supplieslist = suppliesService.querySuppliesByUser(cityId, principal);
		List<Invoice> InvoiceList = userService.queryInvoiceByUser(cityId, principal);
		List<Contract> contracts = contractService.queryContractList(cityId, page, null, null, principal);
		SuppliesView suppliesView = suppliesService.getSuppliesDetail(order.getSuppliesId(), null);
		SuppliesView quafiles = suppliesService.getQua(order.getSuppliesId(), null);
		JpaOrders orders = orderService.queryOrderDetail(order.getId(),principal);
		v.setOrder(orders);
		model.addAttribute("supplieslist", supplieslist);
		model.addAttribute("InvoiceList", InvoiceList);
		model.addAttribute("order", order);
		model.addAttribute("prod", prod);
		model.addAttribute("orderview", v);
		model.addAttribute("contracts", contracts);
		model.addAttribute("suppliesView", suppliesView);
		model.addAttribute("quafiles", quafiles);


        //选车
        if (city != null && city.getMediaType() == JpaCity.MediaType.body) {
            model.addAttribute("lines", busService.getAllBuslines(cityId, prod.getLineLevel(), null, 0, 9999, null).getContent());
            model.addAttribute("models", busService.getAllBusModels(cityId, null, null, 0, 9999, null).getContent());
            model.addAttribute("companies", busService.getAllBusinessCompanies(cityId, null, null, 0, 9999, null).getContent());
            model.addAttribute("categories", JpaBus.Category.values());
        }
		return "relateSup";
	}

	@RequestMapping(value = "/orderDetail/{orderid}", produces = "text/html;charset=utf-8")
	public String orderDetail(Model model, @PathVariable("orderid") int orderid,
			@RequestParam(value = "taskid", required = false) String taskid,
			@RequestParam(value = "auto", required = false) String auto,
			@RequestParam(value = "pid", required = false) String pid, Principal principal,
			@CookieValue(value = "city", defaultValue = "-1") int city, HttpServletRequest request) throws Exception {
		return activitiService.showOrderDetail(city, model, orderid, taskid, pid, principal,BooleanUtils.toBoolean(auto));
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

	@RequestMapping(value = "/myTask/{pageNum}")
	public String list() {
		return "orderList";
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
			+ "or hasRole('ShibaSuppliesManager')or hasRole('UserManager') ")
	@RequestMapping(value = "/allRuningOrders/{pageNum}")
	public String allRuningOrders(Model model, Principal principal, @PathVariable int pageNum,
			HttpServletRequest request, HttpServletResponse response) {
		return "allRuningOrders";
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

    @RequestMapping(value = "ajax-order-buses", method = RequestMethod.POST)
    @ResponseBody
    public JpaOrders orderBuses(@CookieValue(value = "city", defaultValue = "-1") int city,
                                            JpaOrderBuses orderBuses) {
        int orderId = orderBuses.getOrder().getId();
        JpaOrders order = orderService.getJpaOrder(orderId);

        if (order == null) {
            JpaOrders result =  new JpaOrders();
            result.setErrorInfo(BaseEntity.ERROR, "无效订单");
            return result;
        }

        //check order status
        JpaOrders.Status status = activitiService.getOrderStatus(orderId);
        if (status == null || status.ordinal() >= JpaOrders.Status.scheduled.ordinal()) {
            JpaOrders result =  new JpaOrders();
            result.setErrorInfo(BaseEntity.ERROR, "订单" + status.getNameStr() + ", 无法更改公交线路");
            return result;
        }

        if (orderBuses.getBusNumber() <= 0) {
            JpaOrders result =  new JpaOrders();
            result.setErrorInfo(BaseEntity.ERROR, "请填写正确的巴士数量");
            return result;
        }

        //validate
        order.getOrderBuses().add(orderBuses);
        if (order.getSelectableBusesNumber() < 0) {
            order.setErrorInfo(BaseEntity.ERROR, "选择巴士数量超过套餐总数");
        }

        orderService.saveOrderBuses(orderBuses);
        order.setErrorInfo(BaseEntity.OK, "选车成功");
        return order;
    }

    @RequestMapping(value = "ajax-orderedbuses", method = RequestMethod.GET)
    @ResponseBody
    public DataTablePage<JpaOrderBuses> getOrderedBuses(@CookieValue(value = "city", defaultValue = "-1") int city,
                                            @RequestParam("orderId") int orderId) {
        JpaOrders order = orderService.getJpaOrder(orderId);
        if (order == null) {
            return new DataTablePage(Collections.EMPTY_LIST);
        }
        return new DataTablePage(order.getOrderBusesList());
    }
    
    @PreAuthorize(" hasRole('ShibaOrderManager')")
	@RequestMapping(value = "/setOrderPrice", method = RequestMethod.POST)
	@ResponseBody
	public Pair<Boolean, String> setOrderPrice(@RequestParam("orderId") int orderId, @RequestParam("price") double price) {
		return orderService.updateOrderPrice(orderId, price);
	}


    @RequestMapping(value = "ajax-remove-order-buses", method = RequestMethod.POST)
    @ResponseBody
    public JpaOrders removeOrderedBuses(@CookieValue(value = "city", defaultValue = "-1") int city,
                                                        @RequestParam("orderId") int orderId,
                                                        @RequestParam("id") int id) {
        JpaOrders order = orderService.getJpaOrder(orderId);
        if (order == null) {
            JpaOrders result =  new JpaOrders();
            result.setErrorInfo(BaseEntity.ERROR, "没找到对应的订单");
            return result;
        }

        //check order status
        JpaOrders.Status status = activitiService.getOrderStatus(orderId);
        if (status == null || status.ordinal() >= JpaOrders.Status.scheduled.ordinal()) {
            JpaOrders result =  new JpaOrders();
            result.setErrorInfo(BaseEntity.ERROR, "订单" + status.getNameStr() + ", 无法更改公交线路");
            return result;
        }

        Set<JpaOrderBuses> orderBuses = order.getOrderBuses();
        JpaOrderBuses found = null;
        for (JpaOrderBuses b : orderBuses) {
            if (b.getId() == id) {
                found = b;
                break;
            }
        }
        if (found == null) {
            JpaOrders result =  new JpaOrders();
            result.setErrorInfo(BaseEntity.ERROR, "没找到对应的条目");
            return result;
        }

        order.getOrderBuses().remove(found);
        orderService.updateWithBuses(order);
        return order;
    }
}
