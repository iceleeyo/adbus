package com.pantuo.web;

import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pantuo.dao.IndustryRepository;
import com.pantuo.dao.pojo.JpaBlackAd;
import com.pantuo.dao.pojo.JpaContract;
import com.pantuo.dao.pojo.JpaIndustry;
import com.pantuo.dao.pojo.JpaProduct;
import com.pantuo.dao.pojo.UserDetail;
import com.pantuo.mybatis.domain.BusContract;
import com.pantuo.mybatis.domain.Contract;
import com.pantuo.mybatis.domain.Industry;
import com.pantuo.pojo.DataTablePage;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.ContractServiceData;
import com.pantuo.service.UserServiceInter;
import com.pantuo.util.Request;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.pantuo.service.ContractService;
import com.pantuo.util.NumberPageUtil;
import com.pantuo.util.Pair;
import com.pantuo.web.view.ContractView;
import com.pantuo.web.view.InvoiceView;

/**
 * 
 *
 * @author xl
 */
@Controller
@RequestMapping(produces = "application/json;charset=utf-8", value = "/contract")
public class ContractController {
	private static Logger log = LoggerFactory.getLogger(ContractController.class);

	@Autowired
	private ContractService contractService;
	@Autowired
	private UserServiceInter userService;
	@Autowired
	private ContractServiceData contractServiceDate;
	@Autowired
	private IndustryRepository industryRepo;

	@RequestMapping(value = "contractCodeCheck", method = RequestMethod.POST)
	@ResponseBody
	public Pair<Boolean, String> contractCodeCheck(Model model,
			@RequestParam(value = "code", required = false, defaultValue = "") String code, HttpServletRequest request,
			HttpServletResponse response) {
		return contractService.queryCode(code);
	}

	@RequestMapping(value = "/creContract", produces = "text/html;charset=utf-8")
	public String creContract(HttpServletRequest request) {
		return "crecontract";
	}
	@RequestMapping(value = "/bus_contractEnter", produces = "text/html;charset=utf-8")
	public String bus_contractEnter(Model model, @CookieValue(value = "city", defaultValue = "-1") int cityId,
			Principal principal,HttpServletRequest request) {
		List<Contract> contracts = contractService.querybodyContractList(cityId);
		model.addAttribute("contracts", contracts);
		return "bus_contractEnter";
	}
	@RequestMapping(value = "saveBusContract", method = RequestMethod.POST)
	@ResponseBody
	public Pair<Boolean, String> saveBusContract(@CookieValue(value = "city", defaultValue = "-1") int city,
			@RequestParam(value = "contractid") int contractid,
			@RequestParam(value = "plateNumber") String plateNumber,
			@RequestParam(value = "startdate") String startdate,
			@RequestParam(value = "enddate") String enddate,
		 Principal principal, HttpServletRequest request) throws IllegalStateException,
			IOException, ParseException {
		return contractService.saveBusContract(city,plateNumber,contractid,startdate,enddate);
	}
	@PreAuthorize(" hasRole('ShibaOrderManager')  "+" or hasRole('bodyContractManager')")
	@RequestMapping(value = "saveContract", method = RequestMethod.POST)
	@ResponseBody
	public Pair<Boolean, String> saveContract(@CookieValue(value = "city", defaultValue = "-1") int city,
			Contract contract,Industry industry, Principal principal, HttpServletRequest request) throws IllegalStateException,
			IOException, ParseException {
		String start = request.getParameter("startDate1").toString();
		String end = request.getParameter("endDate1").toString();
		if(StringUtils.isNotBlank(industry.getName())){
			if(contractService.saveIndustry(industry)>0){
				contract.setIndustryId(industry.getId());
			}
		}
		if (start.length() > 1 && end.length() > 1) {
			contract.setStartDate((Date) new SimpleDateFormat("yyyy-MM-dd").parseObject(start));
			contract.setEndDate((Date) new SimpleDateFormat("yyyy-MM-dd").parseObject(end));
		}
		return contractService.saveContract(city, contract, Request.getUserId(principal), request);
	}
	@PreAuthorize(" hasRole('ShibaOrderManager')  ")
	@RequestMapping(value = "saveblackAd", method = RequestMethod.POST)
	@ResponseBody
	public Pair<Boolean, String> saveblackAd(@CookieValue(value = "city", defaultValue = "-1") int city,
			JpaBlackAd blackAd, Principal principal, HttpServletRequest request) throws IllegalStateException,
			IOException, ParseException {
		return contractService.saveBlackAd(city, blackAd, Request.getUserId(principal), request);
	}
	@RequestMapping(value = "/contract_edit/{contract_id}", produces = "text/html;charset=utf-8")
	public String contract_edit(Model model,@PathVariable("contract_id") int contract_id,Principal principal,
			@CookieValue(value = "city", defaultValue = "-1") int cityId,HttpServletRequest request) {
		ContractView  contractView=contractService.findContractById(contract_id,principal);
		Page<UserDetail> users = userService.getValidUsers( null ,0, 999, null);
		List<JpaIndustry> industries = industryRepo.findAll();
		List<Contract> contracts = contractService.queryParentContractList(cityId);
		model.addAttribute("contracts", contracts);
		model.addAttribute("users", users.getContent());
		model.addAttribute("industries", industries);
		model.addAttribute("contractView", contractView);
		return "contractEnter";
	}
	@RequestMapping(value = "/list")
	public String contralist() {

		return "contractlist";
	}
	
	@RequestMapping(value = "/blackAdlist")
	public String blackAdlist() {

		return "blackAd_list";
	}
	@RequestMapping("ajax-list")
	@ResponseBody
	public DataTablePage<JpaContract> getAllContracts(TableRequest req,
			@CookieValue(value = "city", defaultValue = "-1") int city, Principal principal) {
		return new DataTablePage(contractServiceDate.getAllContracts(city, req, principal), req.getDraw());
	}
	@RequestMapping("blackAd-ajax-list")
	@ResponseBody
	public DataTablePage<JpaBlackAd> getAllblackAd(TableRequest req,
			@CookieValue(value = "city", defaultValue = "-1") int city, Principal principal) {
		return new DataTablePage(contractServiceDate.getAllblackAd(city, req, principal), req.getDraw());
	}
	/**
	 * 
	 * 只允许订单管理员增加合同
	 *
	 * @param model
	 * @param request
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	@PreAuthorize(" hasRole('ShibaOrderManager')  "+ " or hasRole('bodyContractManager')")
	@RequestMapping(value = "/contractEnter", produces = "text/html;charset=utf-8")
	public String contractEnter(Model model, @CookieValue(value = "city", defaultValue = "-1") int cityId,HttpServletRequest request) {
		Page<UserDetail> users = userService.getValidUsers(null , 0, 999, null);
		List<Contract> contracts = contractService.queryParentContractList(cityId);
		List<JpaIndustry> industries = industryRepo.findAll();
		model.addAttribute("contracts", contracts);
		model.addAttribute("users", users.getContent());
		model.addAttribute("industries", industries);
		return "contractEnter";
	}
	@PreAuthorize(" hasRole('ShibaOrderManager')  ")
	@RequestMapping(value = "/blackAdEnter", produces = "text/html;charset=utf-8")
	public String blackAdEnter(Model model, HttpServletRequest request) {
		return "blackAdEnter";
	}
	@RequestMapping(value = "/contractDetail/{contract_id}", produces = "text/html;charset=utf-8")
	public String contractDetail(Model model, @PathVariable("contract_id") int contract_id, Principal principal,
			HttpServletRequest request,HttpServletResponse response) {
		response.setHeader("X-Frame-Options", "SAMEORIGIN");
		//    	int contract_id=Integer.parseInt(request.getParameter("contract_id"));
		ContractView view = contractService.getContractDetail(contract_id, principal);
		model.addAttribute("view", view);
		return "contractDetail";
	}
	@RequestMapping(value = "/ajax-contractDetail/{contract_id}")
	@ResponseBody
	public ContractView ajaxcontractDetail(Model model, @PathVariable("contract_id") int contract_id, Principal principal,
			HttpServletRequest request) {
		return  contractService.getContractDetail(contract_id, principal);
	}
	@PreAuthorize(" hasRole('ShibaOrderManager')  ")
	@RequestMapping(value = "/delContract/{contract_id}")
	@ResponseBody
	public Pair<Boolean, String> delContract(Model model,
			@PathVariable("contract_id") int contract_id, Principal principal,
				HttpServletRequest request) {
		return contractService.delContract(contract_id);
	}
	public String paginationHTML;

}
