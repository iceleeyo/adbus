package com.pantuo.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.IdentityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pantuo.mybatis.domain.Contract;
import com.pantuo.service.ContractService;
import com.pantuo.util.Pair;

/**
 * 
 *
 * @author xl
 */
@Controller
public class ContractController {
    private static Logger log = LoggerFactory.getLogger(ContractController.class);

    @Autowired
    private ContractService contractService;
    @Autowired
    private IdentityService identityService;
    
    @RequestMapping(value = "/contractlist", produces = "text/html;charset=utf-8")
    public String contractlist(HttpServletRequest request)
    {   
    	List<Contract> contracts=contractService.findContracts();
    	request.getSession().setAttribute("contracts", contracts);
        return "contractlist";
    }
    @RequestMapping(value = "/creContract", produces = "text/html;charset=utf-8")
    public String creContract(HttpServletRequest request)
    {   
        return "crecontract";
    }
    @RequestMapping(value="saveContract",method = RequestMethod.POST)
    @ResponseBody
	public Pair<Boolean, String> saveContract(Contract contract,HttpServletRequest request, HttpServletResponse response)throws IllegalStateException, IOException{
    	return contractService.saveContract(contract,request);
	}
	

}
