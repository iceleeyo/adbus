package com.pantuo.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pantuo.dao.pojo.JpaProduct;
import com.pantuo.dao.pojo.JpaProduct.FrontShow;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.CpdService;
import com.pantuo.service.ProductService;

/**
 * Index controller
 *
 * @author tliu
 */
@Controller
public class IndexController {

	@Autowired
	CpdService cpdService;
	@Autowired
	private ProductService productService;

	@RequestMapping(value = "/", produces = "text/html;charset=utf-8")
	public String index(Model model) {

		TableRequest req = new TableRequest();
		req.setStart(0);
		req.setLength(4);
		List<Map<String, String>> sort = new ArrayList<Map<String, String>>(1);
		Map<String, String> default_sort = new HashMap<String, String>(1);
		default_sort.put("id", "desc");
		sort.add(default_sort);

		Page<JpaProduct> videoList = productService.getValidProducts(NumberUtils.INTEGER_MINUS_ONE,
				JpaProduct.Type.video, false, null, req, FrontShow.Y);
		Page<JpaProduct> imageList = productService.getValidProducts(NumberUtils.INTEGER_MINUS_ONE,
				JpaProduct.Type.image, false, null, req, FrontShow.Y);
		Page<JpaProduct> noteList = productService.getValidProducts(NumberUtils.INTEGER_MINUS_ONE,
				JpaProduct.Type.info, false, null, req, FrontShow.Y);
		model.addAttribute("auctionList", cpdService.getIndexCpdList(4));
		model.addAttribute("videoList", videoList.getContent());
		model.addAttribute("imageList", imageList.getContent());
		model.addAttribute("noteList", noteList.getContent());
		return "index";
		//return "redirect:/index.html";
	}
}
