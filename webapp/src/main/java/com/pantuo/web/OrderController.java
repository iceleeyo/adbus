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

import com.pantuo.mybatis.domain.Contract;
import com.pantuo.mybatis.domain.Order;
import com.pantuo.service.OrderService;
import com.pantuo.service.ProductService;
import com.pantuo.util.NumberPageUtil;
import com.pantuo.util.Pair;

/**
 * 
 *
 * @author xl
 */
@Controller
@RequestMapping(produces = "application/json;charset=utf-8", value = "/order")
public class OrderController {

    @Autowired
    private ProductService productService;
    @Autowired
    private IdentityService identityService;
    @Autowired
    private OrderService orderService;
     
    @RequestMapping(value = "/buypro", produces = "text/html;charset=utf-8")
    public String buypro(HttpServletRequest request)
    {   
        return "creOrder";
    }
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
    @RequestMapping(value = "creOrder",method = RequestMethod.POST)
    @ResponseBody
	public Pair<Boolean, String> saveOrder(Order order,HttpServletRequest request, HttpServletResponse response)throws IllegalStateException, IOException, ParseException{
    	
    	    return orderService.saveOrder(order,request);
	}
     
}
