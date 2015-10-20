package com.pantuo.web;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mysema.commons.lang.Pair;
import com.pantuo.dao.pojo.JpaCity;
import com.pantuo.dao.pojo.JpaProduct;
import com.pantuo.dao.pojo.JpaProduct.FrontShow;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.BusLineCheckService;
import com.pantuo.service.CardService;
import com.pantuo.service.CityService;
import com.pantuo.service.CpdService;
import com.pantuo.service.ProductService;
import com.pantuo.web.view.CardView;

/**
 * Index controller
 *
 * @author tliu
 */
@Controller
public class IndexController {
	
	private static Logger logger = LoggerFactory.getLogger(IndexController.class);
	@Autowired
	private CityService cityService;
	@Autowired
	CpdService cpdService;
	@Autowired
	private ProductService productService;
	@Autowired
	private BusLineCheckService busLineCheckService;

	public  int makeCookieValueRight(int city, HttpServletResponse response) {
		JpaCity r = cityService.fromId(city);
		
		if(r==null){
			logger.warn("city:{} is ",city); 
			
			Cookie cookie = new Cookie("city", String.valueOf(city));
			cookie.setPath("/");
			cookie.setMaxAge(0);
			response.addCookie(cookie);
		}
		
		int w = r == null ? ControllerSupport.defaultCookieValue : r.getId();
		w=w>6?ControllerSupport.defaultCookieValue:r.getId();
		try {
			Cookie cookie = new Cookie("city", String.valueOf(w));
			cookie.setPath("/");
			cookie.setMaxAge(604800);
			response.addCookie(cookie);
		} catch (Exception e) {
		}
		return w;
	}
	 @RequestMapping(value = "/secondLevelPage")
	    public String secondLevelPage() {
	    	return "secondLevelPage";
	    }
	 @RequestMapping(value = "/secondLevelPageBus")
	    public String secondLevelPageBus() {
	    	return "secondLevelPageBus";
	    }
	@RequestMapping(value = "/body", produces = "text/html;charset=utf-8")
	public String body(Model model, HttpServletRequest request, HttpServletResponse response,
			@CookieValue(value = "city", defaultValue = "-1") int city) {
		city = makeCookieValueRight(city == -1 ? 2 : (city % 2 == 1 ? city + 1 : city), response);
		return commonData(model, request, city, "body_index", "body");
	}

	@RequestMapping(value = "/screen", produces = "text/html;charset=utf-8")
	public String screen(Model model, HttpServletRequest request, HttpServletResponse response,
			@CookieValue(value = "city", defaultValue = "-1") int city) {
		city = makeCookieValueRight(city == -1 ? 1 : (city % 2 == 0 ? city - 1 : city), response);
				return commonData(model, request, city, "index", "screen");
	}

	@RequestMapping(value = "/", produces = "text/html;charset=utf-8")
	public String index(Model model, HttpServletRequest request, HttpServletResponse response,
			@CookieValue(value = "city", defaultValue = "-1") int city) {
		//city = makeCookieValueRight(city == -1 ? 1 : (city % 2 == 0 ? city - 1 : city), response);
			//		return commonData(model, request, city, "index", "screen");
		
		return "redirect:/index.html";
	}

	private String commonData(Model model, HttpServletRequest request, int city, String pageName, String medetype) {
        Integer[] ids={2504,2313,1786,1804};
        Map map=new HashMap<Integer, String>();
        map.put(2504, "/imgs/_300.jpg");
        map.put(2313, "/imgs/_44.jpg");
        map.put(1786, "/imgs/_b12.jpg");
        map.put(1804, "/imgs/_8.jpg");
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
		model.addAttribute("buslineList", busLineCheckService.getlines(ids,map));
		request.getSession().setAttribute("medetype", medetype);
		return pageName;
		//return "redirect:/index.html";
	}

	@Autowired
	CardService cardService;
	
	@RequestMapping(value = "/toCard", produces = "text/html;charset=utf-8")
	public String toCard(Model model,HttpServletRequest request,Principal principal) {
		model.addAttribute("infos", cardService.getMediaList(principal,0,null));
		return "secondCart_step1";
	}

	@RequestMapping(value = "/toCard2", produces = "text/html;charset=utf-8")
	public String toCard2(Model model,HttpServletRequest request,Principal principal,@RequestParam(value="ids") String ids) {
		model.addAttribute("infos", cardService.getMediaList(principal,0,ids));
		model.addAttribute("seriaNum", cardService.getCardBingSeriaNum(principal));
		model.addAttribute("ids", ids);
		return "secondCart_step2";
	}
//	@RequestMapping(value = "/confirmBox", produces = "text/html;charset=utf-8")
//	@ResponseBody
//	public Boolean confirmBox(Model model,HttpServletRequest request,Principal principal,@RequestParam(value="ids") String ids) {
//		cardService.confirmByids(principal,ids);
//		return true;
//	}
	
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
	@RequestMapping(value = "/intro-notice")
	public String notice() {
		return "intro/web-notice";
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
