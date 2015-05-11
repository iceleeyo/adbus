package com.pantuo.web;

import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pantuo.util.*;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.google.common.base.Suppliers;
import com.pantuo.dao.pojo.JpaOrders;
import com.pantuo.dao.pojo.JpaProduct;
import com.pantuo.mybatis.domain.Contract;
import com.pantuo.mybatis.domain.Orders;
import com.pantuo.mybatis.domain.Supplies;
import com.pantuo.pojo.DataTablePage;
import com.pantuo.pojo.HistoricTaskView;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.ActivitiService;
import com.pantuo.service.ContractService;
import com.pantuo.service.OrderService;
import com.pantuo.service.ProductService;
import com.pantuo.service.SuppliesService;
import com.pantuo.web.view.OrderView;
import com.pantuo.web.view.SuppliesView;

import freemarker.template.utility.StringUtil;

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
	private TaskService taskService;

	@RequestMapping(value = "/buypro/{product_id}", produces = "text/html;charset=utf-8")
	public String buypro(Model model,@PathVariable("product_id") int product_id,
                         Principal principal, @CookieValue(value="city", defaultValue = "-1") int city, HttpServletRequest request) {
    	JpaProduct  prod=productService.findById(product_id);
		//Page<JpaProduct> products = productService.getValidProducts(0 , 9999, null);
        //model.addAttribute("products", products.getContent());
        NumberPageUtil page = new NumberPageUtil(9999, 1, 9999);
        List<Supplies> supplies = suppliesService.queryMyList(city, page, null, prod.getType(), principal);
        model.addAttribute("supplies", supplies);
        model.addAttribute("prod", prod);
        List<Contract> contracts = contractService.queryContractList(city, page, null, null, principal);
        model.addAttribute("contracts", contracts);
		return "creOrder";
	}

	 @RequestMapping(value = "/proDetail", produces = "text/html;charset=utf-8")
	public String proDetail(Model model, Principal principal, @RequestParam(value = "product_id") int product_id,
			HttpServletRequest request) {
		JpaProduct prod = productService.findById(product_id);
		   model.addAttribute("prod", prod);
	        return "proDetail";
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
			@RequestParam(value = "code", required = false, defaultValue = "") String code,
            @PathVariable int pageNum,
            @CookieValue(value="city", defaultValue = "-1") int city,
			HttpServletRequest request) {
		int psize = 9;
		NumberPageUtil page = new NumberPageUtil(productService.countMyList(city, name, code, request), pageNum, psize);
		model.addAttribute("list", productService.queryContractList(city, page, name, code, request));
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("paginationHTML", page.showNumPageWithEmpty());
		return "product_list";
	}

	@RequestMapping(value = "payment")
	@ResponseBody
	public Pair<Boolean, String> payment(@RequestParam(value = "orderid") String orderid,
			@RequestParam(value = "contractid") int contractid, @RequestParam(value = "taskid") String taskid,
			@RequestParam(value = "payType") String payType, Principal principal, HttpServletRequest request,
			HttpServletResponse response) {
		return activitiService.payment(Integer.parseInt(orderid), taskid, contractid, payType,
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

	@RequestMapping(value = "/handleView2", produces = "text/html;charset=utf-8")
	public String HandleView2(Model model,
                              @RequestParam(value = "taskid", required = true) String taskid,
                              Principal principal,
                              @CookieValue(value="city", defaultValue = "-1") int city,
			                  HttpServletRequest request) throws Exception {
		NumberPageUtil page = new NumberPageUtil(9999, 1, 9999);
		Task task = taskService.createTaskQuery().taskId(taskid).singleResult();
		ExecutionEntity executionEntity = (ExecutionEntity) runtimeService.createExecutionQuery()
				.executionId(task.getExecutionId()).processInstanceId(task.getProcessInstanceId()).singleResult();
		OrderView v = activitiService.findOrderViewByTaskId(taskid);
		String activityId = executionEntity.getActivityId();
		ProcessInstance pe = activitiService.findProcessInstanceByTaskId(taskid);
		List<HistoricTaskView> activitis = activitiService.findHistoricUserTask(city, pe.getProcessInstanceId(), activityId);
		JpaProduct prod = productService.findById(v.getOrder().getProductId());
		List<Contract> contracts = contractService.queryContractList(city, page, null, null, principal);
		List<Supplies> supplieslist=suppliesService.querySuppliesByUser(city, principal);
		SuppliesView suppliesView=suppliesService.getSuppliesDetail(v.getOrder().getSuppliesId(), null);
		model.addAttribute("suppliesView", suppliesView);
		model.addAttribute("contracts", contracts);
		model.addAttribute("supplieslist", supplieslist);
		model.addAttribute("taskid", taskid);
		model.addAttribute("orderview", v);
		model.addAttribute("prod", prod);
		model.addAttribute("activitis", activitis);
		model.addAttribute("activityId", activityId);
		return "handleView2";
	}
	
	@RequestMapping(value = "modifyOrder")
	@ResponseBody
	public Pair<Boolean, String> modifyOrder(@RequestParam(value = "orderid") String orderid,@CookieValue(value="city", defaultValue = "-1") int city,
			@RequestParam(value = "supplieid") int supplieid, @RequestParam(value = "taskid") String taskid,
			Principal principal, HttpServletRequest request, HttpServletResponse response) {
		return activitiService.modifyOrder(city,Integer.parseInt(orderid), taskid, supplieid, Request.getUser(principal));
	}
	
	@RequestMapping(value = "creOrder2", method = RequestMethod.POST, produces = "text/html;charset=utf-8")
	public String saveOrderJpa(Model model,JpaOrders order, Principal principal,
                                              @CookieValue(value="city", defaultValue = "-1") int city,
                                              HttpServletRequest request)
			throws IllegalStateException, IOException, ParseException {
		NumberPageUtil page = new NumberPageUtil(9999, 1, 9999);
        order.setCreator(Request.getUserId(principal));
        order.setStats(JpaOrders.Status.unpaid);
        JpaProduct prod = productService.findById(order.getProductId());
//        if (prod == null) {
//			return new Pair<Boolean, String>(false, "找不到对应的套餐");
//        }
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
		 orderService.saveOrderJpa(city, order, Request.getUser(principal));
		 List<Supplies> supplieslist=suppliesService.querySuppliesByUser(city, principal);
		 List<Contract> contracts = contractService.queryContractList(city, page, null, null, principal);
		 SuppliesView suppliesView=suppliesService.getSuppliesDetail(order.getSuppliesId(), null);
		 Orders orders = orderService.selectOrderById(order.getId());
		 v.setOrder(orders);
		 model.addAttribute("supplieslist", supplieslist);
		 model.addAttribute("order", order);
		 model.addAttribute("prod", prod);
		 model.addAttribute("orderview", v);
		 model.addAttribute("contracts", contracts);
		 model.addAttribute("suppliesView", suppliesView);
		 return "relateSup";
	}
	@RequestMapping(value = "/orderDetail/{orderid}", produces = "text/html;charset=utf-8")
	public String orderDetail(Model model, @PathVariable("orderid") int orderid,
			@RequestParam(value = "taskid", required = false) String taskid,
			@RequestParam(value = "pid", required = false) String pid, Principal principal, 
			@CookieValue(value="city", defaultValue = "-1") int city,
			HttpServletRequest request)
			throws Exception {
		return activitiService.showOrderDetail(city, model, orderid, taskid, pid, principal);
	}

	@Deprecated
	private String showOrderDetail(int city, Model model, int orderid, String taskid, String pid, Principal principal)
			throws Exception {
		if (StringUtils.isNotBlank(taskid)) {
			Task task = taskService.createTaskQuery().taskId(taskid).singleResult();
			ExecutionEntity executionEntity = (ExecutionEntity) runtimeService.createExecutionQuery()
					.executionId(task.getExecutionId()).processInstanceId(task.getProcessInstanceId()).singleResult();
			String activityId = executionEntity.getActivityId();
			ProcessInstance pe = activitiService.findProcessInstanceByTaskId(taskid);
			List<HistoricTaskView> activitis = activitiService.findHistoricUserTask(city, pe.getProcessInstanceId(),
					activityId);
			OrderView v = activitiService.findOrderViewByTaskId(taskid);
			JpaProduct prod = productService.findById(v.getOrder().getProductId());
			SuppliesView suppliesView = suppliesService.getSuppliesDetail(v.getOrder().getSuppliesId(), null);
			model.addAttribute("suppliesView", suppliesView);
			model.addAttribute("activitis", activitis);
			model.addAttribute("sections", orderService.getTaskSection(activitis));
			model.addAttribute("orderview", v);
			model.addAttribute("prod", prod);
			return "orderDetail";
		} else {
			Orders order = orderService.queryOrderDetail(orderid, principal);
			JpaProduct prod = null;
			Long longorderid = null;
			OrderView orderView = new OrderView();
			if (order != null) {
			   orderView.setOrder(order);
				prod = productService.findById(order.getProductId());
				longorderid = OrderIdSeq.getIdFromDate(order.getId(), order.getCreated());
		  }
			List<HistoricTaskView> activitis = activitiService.findHistoricUserTask(city, pid, null);
			SuppliesView suppliesView = suppliesService.getSuppliesDetail(orderView.getOrder().getSuppliesId(), null);
			model.addAttribute("activitis", activitis);
		  model.addAttribute("suppliesView", suppliesView);
		  model.addAttribute("order", order);
		  model.addAttribute("longorderid", longorderid);
		  model.addAttribute("orderview", orderView);
		  model.addAttribute("prod", prod);
		  return "finishedOrderDetail";
		}
	}

	/**
	 * 根据任务Id完成任务
	 * @return
	 */
	@RequestMapping(value = "/{taskId}/complete", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public Pair<Boolean, String> completeTask(@PathVariable("taskId") String taskId,
                                              Principal principal,
                                              Supplies supplies,
                                              Variable variable,
                                              @CookieValue(value="city", defaultValue = "-1") int city) {
		if(null!=supplies && null!=supplies.getSeqNumber() && !supplies.getSeqNumber().equals("")){
			suppliesService.updateSupplies(city, supplies);
		}
		return activitiService.complete(taskId, variable.getVariableMap(), Request.getUser(principal));
	}



	@RequestMapping(value = "creOrder", method = RequestMethod.POST)
	@ResponseBody
	public Pair<Boolean, String> saveOrder(Orders order, Principal principal,
                                           @CookieValue(value="city", defaultValue = "-1") int city)
			throws IllegalStateException, IOException, ParseException {

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
                                                    @CookieValue(value="city", defaultValue = "-1") int city) {
		Page<OrderView> w = orderService.getOrderList(city, req.getPage(), req.getLength(), req.getSort("id"), principal);
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

    /*@RequestMapping(value = "/myOrders/{pageNum}")
	public String myOrders(Model model, Principal principal,@PathVariable int pageNum, HttpServletRequest request,
			HttpServletResponse response) {
		int pagesize=8;
		int totalnum=runtimeService.createProcessInstanceQuery().processDefinitionKey("order").involvedUser(Request.getUserId(principal)).list().size();
		NumberPageUtil page=new NumberPageUtil(totalnum, pageNum, pagesize);
		List<OrderView> list = activitiService.findMyOrders(Request.getUserId(principal), page);
		
		model.addAttribute("list", list);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("paginationHTML", page.showNumPageWithEmpty());
		return "myOrders";
	}
	@RequestMapping(value = "/allRuningOrders/{pageNum}")
	public String allRuningOrders(Model model,Principal principal, @PathVariable int pageNum, HttpServletRequest request,
			HttpServletResponse response) {
		return "allRuningOrders";
	}*/

	@RequestMapping("ajax-runningAjax")
	@ResponseBody
	public DataTablePage<OrderView> runningAjax(TableRequest req, Principal principal,
                                                @CookieValue(value="city", defaultValue = "-1") int city) {
		Page<OrderView> w = activitiService.running(city, Request.getUserId(principal), req.getPage(), req.getLength(),
				req.getSort("id"));
		return new DataTablePage<OrderView>(w, req.getDraw());
	}

	@RequestMapping(value = "/allRuningOrders/{pageNum}")
	public String allRuningOrders(Model model, Principal principal, @PathVariable int pageNum,
			HttpServletRequest request, HttpServletResponse response) {
		return "allRuningOrders";
	}

	@RequestMapping(value = "/myOrders/{pageNum}")
	public String myOrders(Model model) {
		model.addAttribute("orderMenu", "我的订单");
		return "myOrders";
	}

	@RequestMapping(value = "/join/{pageNum}")
	public String joinOrder(Model model) {
		model.addAttribute("orderMenu", "我参与的订单");
		return "myOrders";
	}

	@RequestMapping("ajax-myOrders")
	@ResponseBody
	public DataTablePage<OrderView> myOrders(TableRequest req, Principal principal,
                                             @CookieValue(value="city", defaultValue = "-1") int city) {
		Page<OrderView> w = activitiService.MyOrders(city, Request.getUserId(principal), req.getPage(), req.getLength(),
				req.getSort("id"));
		return new DataTablePage<OrderView>(w, req.getDraw());
	}

	//	@RequestMapping(value="/finishedOrders/{usertype}/{pageNum}")
	//	public String finishedOrders(Model model,Principal principal,@PathVariable("usertype") String usertype,@PathVariable int pageNum,HttpServletRequest request, HttpServletResponse response){
	//		NumberPageUtil page = new NumberPageUtil(pageNum);
	//		page.setPagesize(30);
	//		List<OrderView> list = activitiService.findFinishedProcessInstaces(Request.getUserId(principal),usertype, page);
	//		model.addAttribute("list", list);
	//		model.addAttribute("pageNum", page);
	//		return "finishedOrders";
	//	}
	@RequestMapping(value = "/finished")
	public String finishedOrders() {
		/*NumberPageUtil page = new NumberPageUtil(pageNum);
		page.setPagesize(30);
		List<OrderView> list = activitiService.findFinishedProcessInstaces(Request.getUserId(principal),usertype, page);
		model.addAttribute("list", list);
		model.addAttribute("pageNum", page);*/
		return "finishedOrders";
	}

	@RequestMapping("ajax-finishedOrders")
	@ResponseBody
	public DataTablePage<OrderView> finishedAjax(TableRequest req, Principal principal,
                                                 @CookieValue(value="city", defaultValue = "-1") int city) {
		Page<OrderView> w = activitiService.finished(city, principal, req.getPage(), req.getLength(), req.getSort("id"));
		return new DataTablePage<OrderView>(w, req.getDraw());
	}
}
