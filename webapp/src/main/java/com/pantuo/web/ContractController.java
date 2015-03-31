package com.pantuo.web;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.IdentityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pantuo.mybatis.domain.Contract;
import com.pantuo.service.ContractService;
import com.pantuo.util.NumberPageUtil;
import com.pantuo.util.Pair;

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
    private IdentityService identityService;
    
    @RequestMapping(value = "/creContract", produces = "text/html;charset=utf-8")
    public String creContract(HttpServletRequest request)
    {   
        return "crecontract";
    }
    @RequestMapping(value = "saveContract",method = RequestMethod.POST)
    @ResponseBody
	public Pair<Boolean, String> saveContract(Contract contract,HttpServletRequest request, HttpServletResponse response)throws IllegalStateException, IOException, ParseException{
    	String start=request.getParameter("startDate1").toString();
    	String end=request.getParameter("endDate1").toString();
    	if(start!=null&&start!=""&&end!=null&&end!=""){
    		contract.setStartDate((Date) new SimpleDateFormat("yyyy-MM-dd").parseObject(start)); 
    		contract.setEndDate((Date) new SimpleDateFormat("yyyy-MM-dd").parseObject(end));
    	}
    	    return contractService.saveContract(contract,request);
	}
    
    @RequestMapping(value = "/list/{pageNum}", method = RequestMethod.GET)
	public String contralist(Model model, @RequestParam(value = "name", required = false, defaultValue = "") String name,
			@RequestParam(value = "code",required = false, defaultValue = "") String code, @PathVariable int pageNum,
			HttpServletRequest request) {
		int psize = 9;
		NumberPageUtil page = new NumberPageUtil(contractService.countMyList(name, code, request), pageNum, psize);
		model.addAttribute("list", contractService.queryContractList(page, name, code, request));
		model.addAttribute("pageNum", pageNum);
		return "contractlist";
	}
    @RequestMapping(value = "/contractEnter", produces = "text/html;charset=utf-8")
    public String contractEnter(HttpServletRequest request)
    {   
        return "contractEnter";
    }
}
