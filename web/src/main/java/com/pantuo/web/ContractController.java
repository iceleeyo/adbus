package com.pantuo.web;

import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.pantuo.dao.IndustryRepository;
import com.pantuo.dao.pojo.JpaBlackAd;
import com.pantuo.dao.pojo.JpaCity;
import com.pantuo.dao.pojo.JpaContract;
import com.pantuo.dao.pojo.JpaIndustry;
import com.pantuo.dao.pojo.JpaProduct;
import com.pantuo.dao.pojo.UserDetail;
import com.pantuo.mybatis.domain.Contract;
import com.pantuo.mybatis.domain.Industry;
import com.pantuo.pojo.DataTablePage;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.ContractService;
import com.pantuo.service.ContractServiceData;
import com.pantuo.service.UserServiceInter;
import com.pantuo.service.security.Request;
import com.pantuo.util.Pair;
import com.pantuo.web.view.ContractView;

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
	@PreAuthorize(" hasRole('ShibaOrderManager')  "+" or hasRole('bodyContractManager')")
	@RequestMapping(value = "saveContract", method = RequestMethod.POST)
	@ResponseBody
	public Pair<Boolean, String> saveContract(@CookieValue(value = "city", defaultValue = "-1") int city,
			@RequestParam(value="startDate1") String startDate1,
			@RequestParam(value="endDate1") String endDate1,
			@RequestParam(value="signDate1") String signDate1,
			JpaContract contract,Industry industry, Principal principal, HttpServletRequest request) throws IllegalStateException,
			IOException, ParseException {
		if(StringUtils.isNotBlank(industry.getName())){
			if(contractService.saveIndustry(industry)>0){
				contract.setIndustryId(industry.getId());
			}
		}
		return contractService.saveContract(startDate1,endDate1,signDate1,contract, Request.getUserId(principal),city,request);
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
			@CookieValue(value = "city", defaultValue = "-1") int cityId,
			@ModelAttribute("city") JpaCity city,
			HttpServletRequest request) {
		ContractView  contractView=contractService.findContractById(contract_id,principal);
		Page<UserDetail> users = userService.getValidUsers( null ,0, 999, null);
		List<JpaIndustry> industries = industryRepo.findAll();
		List<Contract> contracts = contractService.queryParentContractList(cityId);
		model.addAttribute("contracts", contracts);
		model.addAttribute("users", users.getContent());
		model.addAttribute("industries", industries);
		 model.addAttribute("types", JpaProduct.productTypesForMedia.get(city.getMediaType()));
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
//	@DynaFilter(value = JpaContract.class, fields = { "contractName","industry.id","industry.description","upload" })

//	@JsonFilter(keys = {"contractName", "industry.id","industry.description","upload"})
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
	public String contractEnter(Model model, @CookieValue(value = "city", defaultValue = "-1") int cityId,
			@ModelAttribute("city") JpaCity city,HttpServletRequest request) {
		Page<UserDetail> users = userService.getValidUsers(null , 0, 999, null);
		List<Contract> contracts = contractService.queryParentContractList(cityId);
		List<JpaIndustry> industries = industryRepo.findAll();
		model.addAttribute("contracts", contracts);
		model.addAttribute("users", users.getContent());
		model.addAttribute("industries", industries);
		 model.addAttribute("types", JpaProduct.productTypesForMedia.get(city.getMediaType()));
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
		List<ContractView> views = contractService.getContractDetail(contract_id, principal);
		model.addAttribute("views", views);
		return "contractDetail";
	}
	@RequestMapping(value = "/ajax-contractDetail/{contract_id}")
	@ResponseBody
	public ContractView ajaxcontractDetail(Model model, @PathVariable("contract_id") int contract_id, Principal principal,
			HttpServletRequest request) {
		List<ContractView> views = contractService.getContractDetail(contract_id, principal);
		return  views.get(0);
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
