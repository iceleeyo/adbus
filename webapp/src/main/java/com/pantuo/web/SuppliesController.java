package com.pantuo.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pantuo.mybatis.domain.Supplies;
import com.pantuo.service.SuppliesService;
import com.pantuo.util.NumberPageUtil;
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

	@RequestMapping(value = "/list/{pageNum}", method = RequestMethod.GET)
	public String config(Model model, @RequestParam(value = "name", required = false, defaultValue = "") String name,
			@RequestParam(value = "type", required = false, defaultValue = "") String type, @PathVariable int pageNum,
			HttpServletRequest request) {
		int psize = 9;
		NumberPageUtil page = new NumberPageUtil(suppliesService.countMyList(name, type, request), pageNum, psize);
		model.addAttribute("list", suppliesService.queryMyList(page, name, type, request));
		model.addAttribute("pageNum", pageNum);
		return "supplies_list";
	}
}
