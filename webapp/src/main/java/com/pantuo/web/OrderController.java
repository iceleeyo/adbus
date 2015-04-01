package com.pantuo.web;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pantuo.mybatis.domain.Order;
import com.pantuo.service.ActivitiService;
import com.pantuo.service.ContractService;
import com.pantuo.service.OrderService;
import com.pantuo.service.ProductService;
import com.pantuo.util.NumberPageUtil;
import com.pantuo.util.Pair;
import com.pantuo.util.Request;
import com.pantuo.web.view.ContractView;
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
	ActivitiService activitiService;

	@RequestMapping(value = "/buypro", produces = "text/html;charset=utf-8")
	public String buypro(HttpServletRequest request) {
		return "creOrder";
	}

	@RequestMapping(value = "/payview", produces = "text/html;charset=utf-8")
	public String payview(Model model,@RequestParam(value = "taskid", required = true) String taskid, HttpServletRequest request) {
		int order_id = Integer.parseInt(request.getParameter("order_id"));
		model.addAttribute("order_id", order_id);
		model.addAttribute("taskid", taskid);
		return "payview";
	}

	@RequestMapping(value = "pay", method = RequestMethod.POST)
	@ResponseBody
	public Pair<Boolean, String> pay(Model model,
			@RequestParam(value = "orderid", required = false, defaultValue = "") String orderid,
			HttpServletRequest request, HttpServletResponse response) {
		return orderService.pay("34");
	}

	@RequestMapping(value = "/list/{pageNum}", method = RequestMethod.GET)
	public String contralist(Model model,
			@RequestParam(value = "name", required = false, defaultValue = "") String name,
			@RequestParam(value = "code", required = false, defaultValue = "") String code, @PathVariable int pageNum,
			HttpServletRequest request) {
		int psize = 9;
		NumberPageUtil page = new NumberPageUtil(productService.countMyList(name, code, request), pageNum, psize);
		model.addAttribute("list", productService.queryContractList(page, name, code, request));
		model.addAttribute("pageNum", pageNum);
		return "product_list";
	}

	@RequestMapping(value = "payment")
	@ResponseBody
	public Pair<Boolean, String> payment(@RequestParam(value = "orderid", required = true) int orderid,
			@RequestParam(value = "taskid", required = true) String taskid, HttpServletRequest request,
			HttpServletResponse response) {
		return activitiService.payment(orderid, taskid, Request.getUser(request));
	}

	@RequestMapping(value = "creOrder", method = RequestMethod.POST)
	@ResponseBody
	public Pair<Boolean, String> saveOrder(Order order, HttpServletRequest request, HttpServletResponse response)
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
