package com.pantuo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pantuo.service.CpdService;

/**
 * Index controller
 *
 * @author tliu
 */
@Controller
public class IndexController {
	
	
	@Autowired
	CpdService cpdService;
	
	
	
    @RequestMapping(value = "/", produces = "text/html;charset=utf-8")
    public String index(Model model)
    {
    	model.addAttribute("auctionList", cpdService.getIndexCpdList(4));
    	return "index";
    	//return "redirect:/index.html";
    }
}
