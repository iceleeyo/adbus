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
import com.pantuo.dao.pojo.JpaContract;
import com.pantuo.dao.pojo.JpaIndustry;
import com.pantuo.dao.pojo.JpaProduct;
import com.pantuo.dao.pojo.UserDetail;
import com.pantuo.mybatis.domain.Contract;
import com.pantuo.pojo.DataTablePage;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.ContractServiceData;
import com.pantuo.service.UserServiceInter;
import com.pantuo.util.Request;

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

	@PreAuthorize(" hasRole('ShibaOrderManager')  ")
	@RequestMapping(value = "saveContract", method = RequestMethod.POST)
	@ResponseBody
	public Pair<Boolean, String> saveContract(@CookieValue(value = "city", defaultValue = "-1") int city,
			Contract contract, Principal principal, HttpServletRequest request) throws IllegalStateException,
			IOException, ParseException {
		String start = request.getParameter("startDate1").toString();
		String end = request.getParameter("endDate1").toString();
		if (start.length() > 1 && end.length() > 1) {
			contract.setStartDate((Date) new SimpleDateFormat("yyyy-MM-dd").parseObject(start));
			contract.setEndDate((Date) new SimpleDateFormat("yyyy-MM-dd").parseObject(end));
		}
		return contractService.saveContract(city, contract, Request.getUserId(principal), request);
	}

	@RequestMapping(value = "/list")
	public String contralist() {

		return "contractlist";
	}

	@RequestMapping("ajax-list")
	@ResponseBody
	public DataTablePage<JpaContract> getAllContracts(TableRequest req,
			@CookieValue(value = "city", defaultValue = "-1") int city, Principal principal) {
		return new DataTablePage(contractServiceDate.getAllContracts(city, req, principal), req.getDraw());
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
	@PreAuthorize(" hasRole('ShibaOrderManager')  ")
	@RequestMapping(value = "/contractEnter", produces = "text/html;charset=utf-8")
	public String contractEnter(Model model, HttpServletRequest request) {
		Page<UserDetail> users = userService.getValidUsers(0, 999, null);
		model.addAttribute("users", users.getContent());
		List<JpaIndustry> industries = industryRepo.findAll();
		model.addAttribute("industries", industries);
		return "contractEnter";
	}

	@RequestMapping(value = "/contractDetail/{contract_id}", produces = "text/html;charset=utf-8")
	public String contractDetail(Model model, @PathVariable("contract_id") int contract_id, Principal principal,
			HttpServletRequest request) {
		//    	int contract_id=Integer.parseInt(request.getParameter("contract_id"));
		ContractView view = contractService.getContractDetail(contract_id, principal);
		model.addAttribute("view", view);
		return "contractDetail";
	}

	public String paginationHTML;

}
