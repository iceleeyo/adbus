package com.pantuo.web;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pantuo.service.ContractService;
import com.pantuo.service.PayContractService;
import com.pantuo.web.view.AutoCompleteView;
import com.pantuo.web.view.OrderView;

@Controller
@RequestMapping("/payContract")
public class PayContractController {
	@Autowired
	PayContractService payContractService;
	@Autowired
	ContractService contractService;
	
	@RequestMapping(value = "/newPayContract", produces = "text/html;charset=utf-8")
	public String createPayContract(Model model, HttpServletRequest request) {
		model.addAttribute("contractCode", contractService.getContractId());
		return "payContract/newPayContract";
	}


	@RequestMapping(value = "/OrderIdComplete")
	@ResponseBody
	public List<AutoCompleteView> OrderIdComplete(Model model, Principal principal,
			@RequestParam(value = "term") String name,HttpServletRequest request) {
		return payContractService.OrderIdComplete(principal,name,request);
		
	}
	@RequestMapping(value = "/showOrderDetail")
	@ResponseBody
	public List<OrderView> showOrderDetail(Model model, Principal principal,
			@RequestParam(value = "orderIds") String orderIds) {
		return payContractService.showOrderDetail(orderIds);
		
	}
	

}