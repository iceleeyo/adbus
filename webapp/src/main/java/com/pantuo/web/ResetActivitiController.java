package com.pantuo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pantuo.service.ActivitiService;
@Controller
@RequestMapping(produces = "application/json;charset=utf-8")
public class ResetActivitiController {

	@Autowired
	ActivitiService activitiService;

	@RequestMapping(value = "resetActiviti")
	@ResponseBody
	public String saveOrder(String p) {
		return activitiService.reset(p);
	}

}
