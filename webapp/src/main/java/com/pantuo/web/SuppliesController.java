package com.pantuo.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pantuo.mybatis.domain.Supplies;
import com.pantuo.service.SuppliesService;
import com.pantuo.util.Pair;

@Controller
@RequestMapping(produces = "application/json;charset=utf-8", value = "/supplies")
public class SuppliesController {

	@Autowired
	@RequestMapping(value = "/supplies_test", method = RequestMethod.GET)
	public String r(HttpServletRequest request, HttpServletResponse response) {
		return "supplies_test";
	}

	@Autowired
	SuppliesService suppliesService;

	@RequestMapping(value = "put", method = RequestMethod.POST)
	@ResponseBody
	public Pair<Boolean, String> put(Supplies obj, HttpServletRequest request, HttpServletResponse response)
			throws IllegalStateException, IOException {
		return suppliesService.addSupplies(obj, request);
	}
}
