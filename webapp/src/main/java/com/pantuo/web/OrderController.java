package com.pantuo.web;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pantuo.mybatis.domain.Orders;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pantuo.mybatis.domain.Orders;
import com.pantuo.service.ActivitiService;
import com.pantuo.service.ContractService;
import com.pantuo.service.OrderService;
import com.pantuo.service.ProductService;
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
	private IdentityService identityService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	ActivitiService activitiService;

	@Autowired
	private TaskService taskService;

	@RequestMapping(value = "/buypro", produces = "text/html;charset=utf-8")
	public String buypro(HttpServletRequest request) {
		return "creOrder";
	}

	@RequestMapping(value = "/payview", produces = "text/html;charset=utf-8")
	public String payview(Model model, @RequestParam(value = "taskid", required = true) String taskid,
			@RequestParam(value = "orderid", required = true) String orderid, HttpServletRequest request) {
		model.addAttribute("orderid", orderid);
		model.addAttribute("taskid", taskid);
		return "payview";
	}

	@RequestMapping(value = "/handleView", produces = "text/html;charset=utf-8")
	public String handleView(Model model, @RequestParam(value = "taskid", required = true) String taskid,
			@RequestParam(value = "orderid", required = true) String orderid, HttpServletRequest request) {
		model.addAttribute("orderid", orderid);
		model.addAttribute("taskid", taskid);
		return "handleView";
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
	public Pair<Boolean, String> payment(@RequestParam(value = "orderid", required = true) String orderid,
			@RequestParam(value = "taskid", required = true) String taskid, HttpServletRequest request,
			HttpServletResponse response) {
		return activitiService.payment(Integer.parseInt(orderid), taskid, Request.getUser(request));
	}

	@RequestMapping(value = "claim")
	@ResponseBody
	public Pair<Boolean, String> claimTask(@RequestParam(value = "orderid", required = true) String orderid,
			@RequestParam(value = "taskid", required = true) String taskid, HttpServletRequest request,
			HttpServletResponse response) {
		taskService.claim(taskid, Request.getUserId(request));
		return new Pair<Boolean, String>(true, "任务签收成功!");
	}

	@RequestMapping(value = "/handleView2", produces = "text/html;charset=utf-8")
	public String HandleView2(Model model, @RequestParam(value = "taskid", required = true) String taskid,
			HttpServletRequest request) {

		Task task = taskService.createTaskQuery().taskId(taskid).singleResult();
		ExecutionEntity executionEntity = (ExecutionEntity) runtimeService.createExecutionQuery()
				.executionId(task.getExecutionId()).processInstanceId(task.getProcessInstanceId()).singleResult();

		OrderView v = activitiService.findOrderViewByTaskId(taskid);
		String activityId = executionEntity.getActivityId();
		model.addAttribute("taskid", taskid);
		model.addAttribute("orderview", v);
		model.addAttribute("activityId", activityId);
		return "handleView2";
	}

	@RequestMapping(value = "handle")
	@ResponseBody
	public Pair<Boolean, String> handle(@RequestParam(value = "orderid", required = false) String orderid,
			@RequestParam(value = "taskid", required = false) String taskid,
			@RequestParam(value = "isok", required = false) String isok,
			@RequestParam(value = "comment", required = false) String comment, HttpServletRequest request,
			HttpServletResponse response, Authentication auth) {
		return activitiService.handle(orderid, taskid, comment, isok, Request.getUser(request));
	}

	/**
	 * 根据任务Id完成任务
	 * @return
	 */
	@RequestMapping(value = "/{taskId}/complete", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public Pair<Boolean, String> completeTask(@PathVariable("taskId") String taskId, Variable variable,
			HttpServletRequest request) {
		return activitiService.complete(taskId, variable.getVariableMap(), Request.getUser(request));

	}

	@RequestMapping(value = "creOrder", method = RequestMethod.POST)
	@ResponseBody
	public Pair<Boolean, String> saveOrder(Orders order, HttpServletRequest request, HttpServletResponse response)
			throws IllegalStateException, IOException, ParseException {

		return orderService.saveOrder(order, request);
	}

	@RequestMapping(value = "/myTask/{pageNum}", method = RequestMethod.GET)
	public String myTask(Model model, @PathVariable int pageNum, HttpServletRequest request,
			HttpServletResponse response) {
		NumberPageUtil page = new NumberPageUtil(pageNum);
		page.setPagesize(30);
		model.addAttribute("list", activitiService.findTask(Request.getUserId(request), page));
		model.addAttribute("pageNum", page);
		return "mytask";
	}
}
