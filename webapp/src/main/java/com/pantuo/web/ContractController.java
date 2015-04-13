package com.pantuo.web;

import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pantuo.dao.pojo.JpaContract;
import com.pantuo.dao.pojo.JpaProduct;
import com.pantuo.dao.pojo.UserDetail;
import com.pantuo.mybatis.domain.Contract;
import com.pantuo.pojo.DataTablePage;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.ContractServiceData;
import com.pantuo.service.UserService;
import com.pantuo.util.Request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
	private UserService userService;
	@Autowired
	private ContractServiceData contractServiceDate;
	

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

	@RequestMapping(value = "saveContract", method = RequestMethod.POST)
	@ResponseBody
	public Pair<Boolean, String> saveContract(Contract contract, Principal principal, HttpServletRequest request) throws IllegalStateException, IOException, ParseException {
		String start = request.getParameter("startDate1").toString();
		String end = request.getParameter("endDate1").toString();
		if (start.length() > 1 && end.length() > 1) {
			contract.setStartDate((Date) new SimpleDateFormat("yyyy-MM-dd").parseObject(start));
			contract.setEndDate((Date) new SimpleDateFormat("yyyy-MM-dd").parseObject(end));
		}
		return contractService.saveContract(contract, Request.getUserId(principal), request);
	}

	@RequestMapping(value = "/list")
	public String contralist() {
		
		return "contractlist";
	}
	
	@RequestMapping("ajax-list")
	@ResponseBody
	public DataTablePage<JpaContract> getAllContracts(TableRequest req) {
		return new DataTablePage(
                contractServiceDate.getAllContracts(req.getFilter("contractName"), req.getFilter("contractCode"),
                        req.getPage(), req.getLength(), req.getSort("id")), req.getDraw());
	}

	@RequestMapping(value = "/contractEnter", produces = "text/html;charset=utf-8")
	public String contractEnter(Model model, HttpServletRequest request) {
        Page<UserDetail> users = userService.getValidUsers(0, 999, null);
        model.addAttribute("users", users.getContent());
		return "contractEnter";
	}
    @RequestMapping(value = "/contractDetail", produces = "text/html;charset=utf-8")
    public String contractDetail(Model model, Principal principal, HttpServletRequest request)
    {   
    	int contract_id=Integer.parseInt(request.getParameter("contract_id"));
    	ContractView view=contractService.getContractDetail(contract_id, principal);
    	model.addAttribute("view",view);
        return "contractDetail";
    }
    public String paginationHTML;

    
}
