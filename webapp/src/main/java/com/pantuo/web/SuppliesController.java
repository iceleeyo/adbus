package com.pantuo.web;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pantuo.dao.pojo.JpaContract;
import com.pantuo.dao.pojo.JpaProduct;
import com.pantuo.dao.pojo.JpaSupplies;

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

	@RequestMapping(value = "/list/{pageNum}")
	public String config(Model model, @RequestParam(value = "name", required = false, defaultValue = "") String name,
			@RequestParam(value = "type", required = false) JpaProduct.Type type, @PathVariable int pageNum,
			Principal principal) {
		/*int psize = 9;
		NumberPageUtil page = new NumberPageUtil(suppliesService.countMyList(name, type, principal), pageNum, psize);
		model.addAttribute("list", suppliesService.queryMyList(page, name, type, principal));
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("paginationHTML", page.showNumPageWithEmpty());
		model.addAttribute("name", name);*/
		return "supplies_list";
	}
	@RequestMapping(value = "/suppliesDetail", produces = "text/html;charset=utf-8")
    public String suppliesDetail(Model model, Principal principal, HttpServletRequest request)
    {   
    	int supplies_id=Integer.parseInt(request.getParameter("supplies_id"));
    	SuppliesView view=suppliesService.getSuppliesDetail(supplies_id, principal);
    	model.addAttribute("view",view);
        return "suppliesDetail";
    }
	
	@RequestMapping("ajax-list")
	@ResponseBody
	public DataTablePage<JpaSupplies> getAllContracts(
			@RequestParam(value = "start", required = false, defaultValue = "0") int start,
			@RequestParam(value = "length", required = false, defaultValue = "10") int length,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "draw", required = false, defaultValue = "1") int draw) {

		if (length < 1)
			length = 1;

		return new DataTablePage(suppliesDataService.getAllSupplies(name, start/ length, length), draw);
	}
}
