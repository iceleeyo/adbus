package com.pantuo.web;

import com.pantuo.dao.IndustryRepository;
import com.pantuo.dao.pojo.JpaSupplies;
import com.pantuo.mybatis.domain.Supplies;
import com.pantuo.pojo.DataTablePage;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.SuppliesService;
import com.pantuo.service.SuppliesServiceData;
import com.pantuo.util.Pair;
import com.pantuo.web.view.SuppliesView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;

@Controller
@RequestMapping(produces = "application/json;charset=utf-8", value = "/supplies")
public class SuppliesController {

    @Autowired
    IndustryRepository industryRepo;

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String r(Model model) {
        model.addAttribute("industries", industryRepo.findAll());
		return "supplies_test";
	}

	@Autowired
	SuppliesService suppliesService;
	@Autowired
	SuppliesServiceData suppliesDataService;

	@RequestMapping(value = "put", method = RequestMethod.POST)
	@ResponseBody
	public Pair<Boolean, String> put(Supplies obj, Principal principal,
                                     @CookieValue(value="city", defaultValue = "-1") int city,
                                     HttpServletRequest request)
			throws IllegalStateException, IOException {
		return suppliesService.addSupplies(city, obj, principal, request);
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
	public DataTablePage<JpaSupplies> getAllContracts(TableRequest req,
                                                      Principal principal,
                                                      @CookieValue(value="city", defaultValue = "-1") int city) {
		return new DataTablePage(suppliesDataService.getAllSupplies(city, principal,req.getFilter("name"),
                req.getPage(), req.getLength(), req.getSort("id")), req.getDraw());
	}
}
