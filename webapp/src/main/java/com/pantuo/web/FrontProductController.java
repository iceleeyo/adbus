package com.pantuo.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pantuo.dao.pojo.BaseEntity;
import com.pantuo.dao.pojo.JpaProduct;
import com.pantuo.pojo.DataTablePage;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.ProductService;
import com.pantuo.service.UserServiceInter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author tliu
 */
@Controller
@RequestMapping(value = "/f/prod")
public class FrontProductController {
	private static Logger log = LoggerFactory.getLogger(FrontProductController.class);

	@Autowired
	private ProductService productService;
	@Autowired
	private UserServiceInter userService;

	@RequestMapping(value = "/list/{type}")
	public String prolist(@PathVariable("type") JpaProduct.Type type, Model model) {

		TableRequest req = new TableRequest();
		req.setStart(0);
		req.setLength(4);
		List<Map<String, String>> sort = new ArrayList<Map<String, String>>(1);
		Map<String, String> default_sort = new HashMap<String, String>(1);
		default_sort.put("id", "desc");
		sort.add(default_sort);

		Page<JpaProduct> list = productService.getValidProducts(-1, type, false, null, req);
		model.addAttribute("type", type.ordinal());
		model.addAttribute("typeStr", type.getTypeName());
		model.addAttribute("list", list.getContent());
		return "front_product_list";
	}

}
