package com.pantuo.web;

import javax.servlet.http.HttpServletRequest;

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

import com.pantuo.service.ProductService;
import com.pantuo.util.NumberPageUtil;

/**
 * 
 *
 * @author xl
 */
@Controller
@RequestMapping(produces = "application/json;charset=utf-8", value = "/product")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private IdentityService identityService;
    
     
    
    @RequestMapping(value = "/list/{pageNum}", method = RequestMethod.GET)
	public String contralist(Model model, @RequestParam(value = "name", required = false, defaultValue = "") String name,
			@RequestParam(value = "code",required = false, defaultValue = "") String code, @PathVariable int pageNum,
			HttpServletRequest request) {
		int psize = 9;
		NumberPageUtil page = new NumberPageUtil(productService.countMyList(name, code, request), pageNum, psize);
		model.addAttribute("list", productService.queryContractList(page, name, code, request));
		model.addAttribute("pageNum", pageNum);
		return "product_list";
	}
     
}
