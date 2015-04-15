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

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
import com.pantuo.util.GlobalMethods;
import com.pantuo.util.NumberPageUtil;
import com.pantuo.util.Pair;
import com.pantuo.util.Request;
import com.pantuo.util.Variable;
import com.pantuo.web.view.OrderView;

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
	public String buypro(Model model,@PathVariable("product_id") int product_id, Principal principal, HttpServletRequest request) {
    	JpaProduct  prod=productService.findById(product_id);
		Page<JpaProduct> products = productService.getValidProducts(0 , 9999, null);
        model.addAttribute("products", products.getContent());
        NumberPageUtil page = new NumberPageUtil(9999, 1, 9999);
        List<Supplies> supplies = suppliesService.queryMyList(page, null, null, principal);
        model.addAttribute("supplies", supplies);
        model.addAttribute("prod", prod);
        List<Contract> contracts = contractService.queryContractList(page, null, null, principal);
        model.addAttribute("contracts", contracts);
		return "creOrder";
	}
	 @RequestMapping(value = "/proDetail", produces = "text/html;charset=utf-8")
	    public String proDetail(Model model, Principal principal,@RequestParam(value = "product_id") int product_id ,HttpServletRequest request)
	    {   
		   JpaProduct  prod=productService.findById(product_id);
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
			@RequestParam(value = "code", required = false, defaultValue = "") String code, @PathVariable int pageNum,
			HttpServletRequest request) {
		int psize = 9;
		NumberPageUtil page = new NumberPageUtil(productService.countMyList(name, code, request), pageNum, psize);
		model.addAttribute("list", productService.queryContractList(page, name, code, request));
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("paginationHTML", page.showNumPageWithEmpty());
		return "product_list";
	}

	@RequestMapping(value = "payment")
	@ResponseBody
	public Pair<Boolean, String> payment(@RequestParam(value = "orderid") String orderid,@RequestParam(value = "contractid") int contractid,
			@RequestParam(value = "taskid") String taskid,
			@RequestParam(value = "payType") String payType,
            Principal principal,
            HttpServletRequest request,
			HttpServletResponse response) {
		return activitiService.payment(Integer.parseInt(orderid), taskid,contractid,payType, Request.getUser(principal));
	}

	@RequestMapping(value = "claim")
	@ResponseBody
	public Pair<Boolean, String> claimTask(@RequestParam(value = "orderid", required = true) String orderid,
			@RequestParam(value = "taskid", required = true) String taskid,
            Principal principal,
            HttpServletRequest request,
			HttpServletResponse response) {
		taskService.claim(taskid, Request.getUserId(principal));
		return new Pair<Boolean, String>(true, "任务签收成功!");
	}

	@RequestMapping(value = "/handleView2", produces = "text/html;charset=utf-8")
	public String HandleView2(Model model, @RequestParam(value = "taskid", required = true) String taskid,Principal principal,
			HttpServletRequest request) throws Exception {
		NumberPageUtil page = new NumberPageUtil(9999, 1, 9999);
		Task task = taskService.createTaskQuery().taskId(taskid).singleResult();
		ExecutionEntity executionEntity = (ExecutionEntity) runtimeService.createExecutionQuery()
				.executionId(task.getExecutionId()).processInstanceId(task.getProcessInstanceId()).singleResult();

		OrderView v = activitiService.findOrderViewByTaskId(taskid);
		
		String activityId = executionEntity.getActivityId();
		List<HistoricTaskView> activitis=activitiService.findHistoricUserTask(activitiService.findProcessInstanceByTaskId(taskid),activityId);
		JpaProduct  prod=productService.findById(v.getOrder().getProductId());
		  List<Contract> contracts = contractService.queryContractList(page, null, null, principal);
	        model.addAttribute("contracts", contracts);
		model.addAttribute("taskid", taskid);
		model.addAttribute("orderview", v);
		model.addAttribute("prod", prod);
		model.addAttribute("activitis", activitis);
		model.addAttribute("activityId", activityId);
		return "handleView2";
	}


	/**
	 * 根据任务Id完成任务
	 * @return
	 */
	@RequestMapping(value = "/{taskId}/complete", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public Pair<Boolean, String> completeTask(@PathVariable("taskId") String taskId, Principal principal, Variable variable) {
		return activitiService.complete(taskId, variable.getVariableMap(), Request.getUser(principal));

	}
	@RequestMapping(value = "creOrder2", method = RequestMethod.POST)
	@ResponseBody
	public Pair<Boolean, String> saveOrderJpa(JpaOrders order, Principal principal, HttpServletRequest request)
			throws IllegalStateException, IOException, ParseException {

        order.setCreator(Request.getUserId(principal));
        JpaProduct prod = productService.findById(order.getProductId());
        if (prod == null) {
            return new Pair<Boolean, String> (false, "找不到对应的套餐");
        }
        order.setType(prod.getType());
        String start = request.getParameter("startTime1").toString();
        if (!start.isEmpty()) {
            Date startTime = GlobalMethods.sdf.get().parse(start);
            order.setStartTime(startTime);

            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            cal.setTime(startTime);
            cal.add(prod.getDays(), Calendar.DAY_OF_MONTH);
            order.setEndTime(cal.getTime());
        } else {
            return new Pair<Boolean, String> (false, "请指定订单开播时间");
        }
		return orderService.saveOrderJpa(order, Request.getUser(principal));
	}
	@RequestMapping(value = "creOrder", method = RequestMethod.POST)
	@ResponseBody
	public Pair<Boolean, String> saveOrder(Orders order, Principal principal)
			throws IllegalStateException, IOException, ParseException {

		return orderService.saveOrder(order, principal);
	}

/*
    @RequestMapping(value = "schedule")
    @ResponseBody
    public Iterable<JpaOrders> schedule(String day)
            throws IllegalStateException, IOException, ParseException {
        Date date = sdf.get().parse(day);
        return orderService.getOrdersForSchedule(date, JpaProduct.Type.video);
    }
*/

	
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
	public DataTablePage<OrderView> getAllContracts(TableRequest req, Principal principal) {
		Page<OrderView> w = orderService.getOrderList(req.getPage(), req.getLength(), req.getSort("id"), principal);
		return new DataTablePage(w, req.getDraw());
	}

	@RequestMapping(value = "/list")
	public String list() {
		return "orderList";
	}
    @RequestMapping(value = "/myTask/{pageNum}", method = RequestMethod.GET)
	public String myTask(Model model, @PathVariable int pageNum, Principal principal) {
		NumberPageUtil page = new NumberPageUtil(pageNum);
		page.setPagesize(30);
		//model.addAttribute("list", activitiService.findTask(Request.getUserId(principal), page));
		model.addAttribute("pageNum", page);
		return "mytask";
	}
    @RequestMapping(value = "/myOrders/{pageNum}")
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
		int pagesize=8;
		int totalnum=runtimeService.createProcessInstanceQuery().processDefinitionKey("order").list().size();
		NumberPageUtil page=new NumberPageUtil(totalnum, pageNum, pagesize);
		List<OrderView> list = activitiService.findRunningProcessInstaces(Request.getUserId(principal), page);
		model.addAttribute("list", list);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("paginationHTML", page.showNumPageWithEmpty());
		return "allRuningOrders";
	}
	@RequestMapping(value="/finishedOrders/{usertype}/{pageNum}")
	public String finishedOrders(Model model,Principal principal,@PathVariable("usertype") String usertype,@PathVariable int pageNum,HttpServletRequest request, HttpServletResponse response){
		NumberPageUtil page = new NumberPageUtil(pageNum);
		page.setPagesize(30);
		List<OrderView> list = activitiService.findFinishedProcessInstaces(Request.getUserId(principal),usertype, page);
		model.addAttribute("list", list);
		model.addAttribute("pageNum", page);
		return "finishedOrders";
	}
}
