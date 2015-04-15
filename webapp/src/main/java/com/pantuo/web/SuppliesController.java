package com.pantuo.web;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pantuo.dao.pojo.JpaContract;
import com.pantuo.dao.pojo.JpaProduct;
import com.pantuo.dao.pojo.JpaSupplies;

import com.pantuo.pojo.TableRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pantuo.mybatis.domain.Supplies;
import com.pantuo.pojo.DataTablePage;
import com.pantuo.service.SuppliesService;
import com.pantuo.service.SuppliesServiceData;
import com.pantuo.util.NumberPageUtil;
import com.pantuo.util.Pair;
import com.pantuo.web.view.ContractView;
import com.pantuo.web.view.SuppliesView;

@Controller
@RequestMapping(produces = "application/json;charset=utf-8", value = "/supplies")
public class SuppliesController {

	@Autowired
	@RequestMapping(value = "/supplies_test", method = RequestMethod.GET)
	public String r() {
		return "supplies_test";
	}

	@Autowired
	SuppliesService suppliesService;
	@Autowired
	SuppliesServiceData suppliesDataService;

	@RequestMapping(value = "put", method = RequestMethod.POST)
	@ResponseBody
	public Pair<Boolean, String> put(Supplies obj, Principal principal, HttpServletRequest request)
			throws IllegalStateException, IOException {
		return suppliesService.addSupplies(obj, principal, request);
	}

	@RequestMapping(value = "/list")
	public String list() {
		return "supplies_list";
	}
	@RequestMapping(value = "/suppliesDetail/{supplies_id}", produces = "text/html;charset=utf-8")
    public String suppliesDetail(Model model, @PathVariable("supplies_id") int supplies_id,Principal principal, HttpServletRequest request)
    {   
//    	int supplies_id=Integer.parseInt(request.getParameter("supplies_id"));
    	SuppliesView view=suppliesService.getSuppliesDetail(supplies_id, principal);
    	model.addAttribute("view",view);
        return "suppliesDetail";
    }
	
	@RequestMapping("ajax-list")
	@ResponseBody
	public DataTablePage<JpaSupplies> getAllContracts(TableRequest req) {
		return new DataTablePage(suppliesDataService.getAllSupplies(req.getFilter("name"), req.getPage(), req.getLength(), req.getSort("id")), req.getDraw());
	}
}
