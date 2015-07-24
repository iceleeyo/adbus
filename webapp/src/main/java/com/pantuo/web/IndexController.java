package com.pantuo.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.pantuo.dao.pojo.JpaCity;
import com.pantuo.dao.pojo.JpaProduct;
import com.pantuo.dao.pojo.JpaProduct.FrontShow;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.CityService;
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
	private CityService cityService;
	@Autowired
	CpdService cpdService;
	@Autowired
	private ProductService productService;

	public int makeCookieValueRight(int city, HttpServletResponse response) {
		JpaCity r = cityService.fromId(city);
		int w = r == null ? ControllerSupport.defaultCookieValue : r.getId();
		try {
			Cookie cookie = new Cookie("city", String.valueOf(w));
			cookie.setPath("/");
			cookie.setMaxAge(604800);
			response.addCookie(cookie);
		} catch (Exception e) {
		}
		return w;
	}

	@RequestMapping(value = "/body", produces = "text/html;charset=utf-8")
	public String body(Model model, HttpServletRequest request, HttpServletResponse response,
			@CookieValue(value = "city", defaultValue = "-1") int city) {
		city = makeCookieValueRight(city == -1 ? 2 : (city % 2 == 1 ? city + 1 : city), response);
		return commonData(model, request, city, "body_index", "body");
	}

	@RequestMapping(value = "/", produces = "text/html;charset=utf-8")
	public String index(Model model, HttpServletRequest request, HttpServletResponse response,
			@CookieValue(value = "city", defaultValue = "-1") int city) {
		city = makeCookieValueRight(city == -1 ? 1 : (city % 2 == 0 ? city - 1 : city), response);
		return commonData(model, request, city, "index", "screen");
	}

	private String commonData(Model model, HttpServletRequest request, int city, String pageName, String medetype) {

		TableRequest req = new TableRequest();
		req.setStart(0);
		req.setLength(4);
		List<Map<String, String>> sort = new ArrayList<Map<String, String>>(1);
		Map<String, String> default_sort = new HashMap<String, String>(1);
		default_sort.put("id", "desc");
		sort.add(default_sort);

		Page<JpaProduct> videoList = productService.getValidProducts(city, JpaProduct.Type.video, false, null, req,
				FrontShow.Y);
		Page<JpaProduct> imageList = productService.getValidProducts(city, JpaProduct.Type.image, false, null, req,
				FrontShow.Y);
		Page<JpaProduct> noteList = productService.getValidProducts(city, JpaProduct.Type.info, false, null, req,
				FrontShow.Y);

		Page<JpaProduct> bodyList = productService.getValidProducts(city, JpaProduct.Type.body, false, null, req,
				FrontShow.Y);
		model.addAttribute("auctionList", cpdService.getIndexCpdList(city, 4));
		model.addAttribute("videoList", videoList.getContent());
		model.addAttribute("imageList", imageList.getContent());
		model.addAttribute("noteList", noteList.getContent());
		model.addAttribute("bodyList", bodyList.getContent());
		request.getSession().setAttribute("medetype", medetype);
		return pageName;
		//return "redirect:/index.html";
	}

	@RequestMapping(value = "/intro-video")
	public String video() {
		return "intro/intro-video";
	}

	@RequestMapping(value = "/intro-txt")
	public String txt() {
		return "intro/intro-txt";
	}

	@RequestMapping(value = "/intro-price")
	public String price() {
		return "intro/intro-price";
	}

	@RequestMapping(value = "/intro-ywzn")
	public String ywzn() {
		return "intro/intro-ywzn";
	}

	@RequestMapping(value = "/about-me")
	public String aboutme() {
		return "intro/about-me";
	}
}
