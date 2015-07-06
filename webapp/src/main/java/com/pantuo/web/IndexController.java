package com.pantuo.web;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pantuo.dao.pojo.JpaProduct;
import com.pantuo.dao.pojo.JpaProduct.FrontShow;
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
		Page<JpaProduct> videoList = productService.getValidProducts(NumberUtils.INTEGER_MINUS_ONE,
				JpaProduct.Type.video, false, null, 0, 4, new Sort("id"), FrontShow.Y);
		Page<JpaProduct> imageList = productService.getValidProducts(NumberUtils.INTEGER_MINUS_ONE,
				JpaProduct.Type.image, false, null, 0, 4, new Sort("id"), FrontShow.Y);
		Page<JpaProduct> noteList = productService.getValidProducts(NumberUtils.INTEGER_MINUS_ONE,
				JpaProduct.Type.info, false, null, 0, 4, new Sort("id"), FrontShow.Y);
		model.addAttribute("auctionList", cpdService.getIndexCpdList(4));
		model.addAttribute("videoList", videoList.getContent());
		model.addAttribute("imageList", imageList.getContent());
		model.addAttribute("noteList", noteList.getContent());
		return "index";
		//return "redirect:/index.html";
	}
}
