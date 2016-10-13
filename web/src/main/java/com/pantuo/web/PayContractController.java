package com.pantuo.web;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pantuo.dao.pojo.JpaPayContract;
import com.pantuo.dao.pojo.JpaPayPlan;
import com.pantuo.mybatis.domain.PaycontractWithBLOBs;
import com.pantuo.pojo.DataTablePage;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.ContractService;
import com.pantuo.service.PayContractService;
import com.pantuo.service.impl.TaskCountService;
import com.pantuo.util.Only1ServieUniqLong;
import com.pantuo.util.Pair;
import com.pantuo.web.view.AutoCompleteView;
import com.pantuo.web.view.OrderPlanView;
import com.pantuo.web.view.OrderView;

@Controller
@RequestMapping("/payContract")
public class PayContractController {
	@Autowired
	PayContractService payContractService;
	@Autowired
	ContractService contractService;
	@Autowired
	TaskCountService taskCountService;
	
	@RequestMapping(value = "/newPayContract", produces = "text/html;charset=utf-8")
	public String createPayContract(Model model, HttpServletRequest request) {
//		model.addAttribute("contractCode", contractService.getContractId());
		model.addAttribute("seriaNum",Only1ServieUniqLong.getUniqLongNumber());
		return "payContract/newPayContract";
	}

	@RequestMapping(value = "/list")
	public String contralist() {
		return "payContract/payContractList";
	}
	@RequestMapping(value = "/notPayContract")
	public String notPayContract() {
		return "payContract/notPayContract";
	}
	
	@RequestMapping("ajax-taskCount")
	@ResponseBody
	public Map<String, Long> taskCount(@RequestParam(value = "types", required = false) String types, TableRequest req, Principal principal) {
		return taskCountService.countTaskByType(types, req, principal);
	}
	
	@RequestMapping("ajax-list")
	@ResponseBody
	public DataTablePage<JpaPayContract> getAllContracts(TableRequest req,
			 Principal principal) {
		return new DataTablePage(payContractService.getAllContracts(req, principal), req.getDraw());
	}
	@RequestMapping("ajax-notPayContract")
	@ResponseBody
	public DataTablePage<JpaPayContract> getAllNotPayContracts(TableRequest req,
			Principal principal) {
		return new DataTablePage(payContractService.getAllNotPayContracts(req, principal), req.getDraw());
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
	@RequestMapping(value = "/savePayContract",method = RequestMethod.POST)
	@ResponseBody
	public Pair<Boolean, String> savePayContract( Principal principal,PaycontractWithBLOBs contract,HttpServletRequest request
			) {
		return payContractService.savePayContract(principal,contract,request);
		
	}
	@RequestMapping(value = "/delPayContract/{id}",method = RequestMethod.POST)
	@ResponseBody
	public Pair<Boolean, String> delPayContract(Principal principal,@PathVariable("id") int contractId
			) {
		return payContractService.delPayContract(principal,contractId);
		
	}
	@RequestMapping(value = "colseContract",method = RequestMethod.GET)
	@ResponseBody
	public Pair<Boolean, String> colseContract(Principal principal,@RequestParam("id") int contractId
			,@RequestParam("agreement") String agreement
			) {
		return payContractService.colseContract(contractId,agreement,principal);
		
	}
	@RequestMapping(value = "/toEditPayContract/{id}")
	public String toEditPayContract(Model model, Principal principal,@PathVariable("id") int id
			) {
		JpaPayContract contract= payContractService.getPayContractById(id);
		model.addAttribute("isUsed", payContractService. queryContractUse(  id) );
		model.addAttribute("jpaPayContract", contract);
		return "payContract/editPayContract";
		
	}
	@RequestMapping(value = "/toRestPayContract/{id}")
	public String toRestPayContract(Model model, Principal principal,@PathVariable("id") int contarctId
			) {
		return  payContractService.toRestPayContract(contarctId,model,principal);
		
	}
	
	@RequestMapping(value = "/updatePayContractPlanState/{planId}")
	public String updatePayContractPlanState(Model model, Principal principal, @PathVariable("planId") int planId) {

		JpaPayPlan plan = payContractService.queryByPlanId(planId);
		if (plan != null) {
			model.addAttribute("jpaPayContract", plan.getContract());
			model.addAttribute("planView",  new OrderPlanView (plan));
		}

		return "payContract/updatePayContractPlanState";
	}
	
	

}