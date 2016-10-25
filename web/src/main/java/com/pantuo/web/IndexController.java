package com.pantuo.web;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.pantuo.dao.pojo.JpaBusOrderDetailV2;
import com.pantuo.dao.pojo.JpaCity;
import com.pantuo.dao.pojo.JpaProduct;
import com.pantuo.dao.pojo.JpaProduct.FrontShow;
import com.pantuo.dynamic.service.GroovySimpleInterface;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.CardService;
import com.pantuo.service.CityService;
import com.pantuo.service.CpdService;
import com.pantuo.service.OrderService;
import com.pantuo.service.ProductService;
import com.pantuo.service.ScheduleService;
import com.pantuo.service.impl.IcbcServiceImpl;
import com.pantuo.util.Only1ServieUniqLong;
import com.pantuo.util.image.ValidateCode;
import com.pantuo.web.schedule.SchedUltResult;
import com.pantuo.web.view.CardView;

/**
 * Index controller
 *
 * @author tliu
 */
@Controller

@SessionAttributes("_cardSelect")
public class IndexController {
	
	private static Logger logger = LoggerFactory.getLogger(IndexController.class);
	@Autowired
	private CityService cityService;
	@Autowired
	@Lazy
	CpdService cpdService;
	  @Autowired
	    private OrderService orderService;
	@Autowired
	@Lazy
	private ProductService productService;
	@Autowired
	private ScheduleService scheduleService;
	
	@Autowired
	IcbcServiceImpl icbcService;
	
	
	  @Value("${sys.type}")
		private String isBodySys;

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
	
	
	 @RequestMapping(value = "/testReport")
	    public String testReport(Model model, HttpServletRequest request, HttpServletResponse response
			) {
	    	return "report/test";
	    }
	 @RequestMapping(value = "/secondLevelPage")
	 public String secondLevelPage(Model model, HttpServletRequest request, HttpServletResponse response,
			 @CookieValue(value = "city", defaultValue = "-1") int city) {
//		 makeCookieValueRight(city == -1 ? 1 : (city % 2 == 0 ? city - 1 : city), response);
		 model.addAttribute("auctionList", cpdService.getIndexCpdList(city, 4));
		 return "secondLevelPage";
	 }
	 @RequestMapping(value = "/secondLevelPageBus")
	    public String secondLevelPageBus(Model model, HttpServletRequest request, HttpServletResponse response,
				@CookieValue(value = "city", defaultValue = "-1") int city) {
//		  makeCookieValueRight(city == -1 ? 2 : (city % 2 == 1 ? city + 1 : city), response);
		  model.addAttribute("auctionList", cpdService.getIndexCpdList(city, 4));
	    	return "secondLevelPageBus";
	    }
	 @RequestMapping(value = "/caseMore")
	    public String caseMore(Model model, HttpServletRequest request, HttpServletResponse response,
				@CookieValue(value = "city", defaultValue = "-1") int city) {
//		  makeCookieValueRight(city == -1 ? 2 : (city % 2 == 1 ? city + 1 : city), response);
		  model.addAttribute("auctionList", cpdService.getIndexCpdList(city, 4));
	    	return "index_menu/caseMorePage";
	    }
	 @RequestMapping(value = "/caseMore1")
	    public String caseMore1(Model model, HttpServletRequest request, HttpServletResponse response,
				@CookieValue(value = "city", defaultValue = "-1") int city) {
//		  makeCookieValueRight(city == -1 ? 2 : (city % 2 == 1 ? city + 1 : city), response);
		  model.addAttribute("auctionList", cpdService.getIndexCpdList(city, 4));
	    	return "caseMore1";
	    }
	 @RequestMapping(value = "/caseMore2")
	    public String caseMore2(Model model, HttpServletRequest request, HttpServletResponse response,
				@CookieValue(value = "city", defaultValue = "-1") int city) {
//		  makeCookieValueRight(city == -1 ? 2 : (city % 2 == 1 ? city + 1 : city), response);
		  model.addAttribute("auctionList", cpdService.getIndexCpdList(city, 4));
	    	return "caseMore2";
	    }
	 @RequestMapping(value = "/caseMore3")
	    public String caseMore3(Model model, HttpServletRequest request, HttpServletResponse response,
				@CookieValue(value = "city", defaultValue = "-1") int city) {
//		  makeCookieValueRight(city == -1 ? 2 : (city % 2 == 1 ? city + 1 : city), response);
		  model.addAttribute("auctionList", cpdService.getIndexCpdList(city, 4));
	    	return "caseMore3";
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
		//if(!StringUtils.contains(request.getServerName(), "busme")){
		if(StringUtils.contains(isBodySys, "body")){ 
			try {
				request.getSession().removeAttribute("reLoginMsg");
			} catch (Exception e) {
			}
			return "redirect:/login_bus"; 
		}
		return "index";
	}
	
	@RequestMapping(value = "/index", produces = "text/html;charset=utf-8")
	public String toindex(Model model, HttpServletRequest request, HttpServletResponse response,
			@CookieValue(value = "city", defaultValue = "-1") int city) {
		//city = makeCookieValueRight(city == -1 ? 1 : (city % 2 == 0 ? city - 1 : city), response);
			//		return commonData(model, request, city, "index", "screen");
		
		return "index";
	}
	
	@RequestMapping(value = "/media", produces = "text/html;charset=utf-8")
	public String tomedia(Model model, HttpServletRequest request, HttpServletResponse response,
			@CookieValue(value = "city", defaultValue = "-1") int city) {
		//city = makeCookieValueRight(city == -1 ? 1 : (city % 2 == 0 ? city - 1 : city), response);
			//		return commonData(model, request, city, "index", "screen");
		
		return "index_menu/media_production";
	}
	
	@RequestMapping(value = "/effect", produces = "text/html;charset=utf-8")
	public String toeffect(Model model, HttpServletRequest request, HttpServletResponse response,
			@CookieValue(value = "city", defaultValue = "-1") int city) {
		//city = makeCookieValueRight(city == -1 ? 1 : (city % 2 == 0 ? city - 1 : city), response);
			//		return commonData(model, request, city, "index", "screen");
		
		return "index_menu/effect";
	}

	@RequestMapping(value = "report/public/{pageName}", produces = "text/html;charset=utf-8")
	public String publicView(@PathVariable("pageName") String pageName, Model model, HttpServletRequest request,
			HttpServletResponse response, @CookieValue(value = "city", defaultValue = "-1") int city) {
		return "report/" + pageName;
	}
	@RequestMapping(value = "/partner", produces = "text/html;charset=utf-8")
	public String topartner(Model model, HttpServletRequest request, HttpServletResponse response,
			@CookieValue(value = "city", defaultValue = "-1") int city) {
		//city = makeCookieValueRight(city == -1 ? 1 : (city % 2 == 0 ? city - 1 : city), response);
			//		return commonData(model, request, city, "index", "screen");
		
		return "index_menu/partner";
	}
	
	@RequestMapping(value = "/aboutme", produces = "text/html;charset=utf-8")
	public String toaboutme(Model model, HttpServletRequest request, HttpServletResponse response,
			@CookieValue(value = "city", defaultValue = "-1") int city) {
		//city = makeCookieValueRight(city == -1 ? 1 : (city % 2 == 0 ? city - 1 : city), response);
			//		return commonData(model, request, city, "index", "screen");
		
		return "index_menu/aboutme";
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
		request.getSession().setAttribute("medetype", medetype);
		return pageName;
		//return "redirect:/index.html";
	}

	@Autowired
	CardService cardService;
	
	@RequestMapping(value = "/toCard", produces = "text/html;charset=utf-8")
	public String toCard(Model model,HttpServletRequest request,Principal principal) {
		model.addAttribute("infos", cardService.getMediaList(principal,0,null,null));
		return "secondCart_step1";
	}
public class CardSelect{
	
	String meids;
	String boids;
	public CardSelect(String meids, String boids) {
		super();
		this.meids = meids;
		this.boids = boids;
	}
	 
 

	
}

	@RequestMapping(value = "/b/public_detail/{id}", produces = "text/html;charset=utf-8")
	public String _detail(Model model, HttpServletRequest request, Principal principal,
			@PathVariable("id") int id
			 ) {
		JpaBusOrderDetailV2 j=cardService.getJpaBusOrderDetailV2Byid(id);
		model.addAttribute("busOrderDetailV2", j);
		model.addAttribute("jsonView", cardService.getJsonfromJsonStr(j.getJpaProductV2().getJsonString()));
		return "thirdCar";
	}
	@RequestMapping(value = "/m/public_detail/{id}", produces = "text/html;charset=utf-8")
	public String mdetail(Model model, HttpServletRequest request, Principal principal,
			@PathVariable("id") int id
			) {
		JpaProduct j=cardService.getJpaProductByid(id);
		model.addAttribute("jpaProduct", j);
		model.addAttribute("jsonView", cardService.getJsonfromJsonStr(j.getJsonString()));
		if(j!=null && j.getType()==JpaProduct.Type.inchof32){
			model.addAttribute("lines", productService.getAllVideo32Group());
		}
		return "thirdCarMedia";
	}
	
	
	
	

	@RequestMapping(value = "/checkFree", method = RequestMethod.GET)
	@ResponseBody
	public SchedUltResult checkFree(String start, int productId,
			@RequestParam(value = "city", required = false, defaultValue = "1") int city, HttpServletRequest request,
			Principal principal) {
		return scheduleService.checkForWeb(start, productId, city, request, principal);
	}
	@RequestMapping(value = "/updateCardMeida", method = RequestMethod.GET)
	@ResponseBody
	public boolean updateCardMeida(String start, int mediaId,String isChangeOrder, 
			@RequestParam(value = "city", required = false, defaultValue = "1") int city, HttpServletRequest request,
			Principal principal) {
		return cardService.updateCardMeida(start,mediaId,isChangeOrder);
	}
	
	@RequestMapping(value = "/buy/{type}",produces = "text/html;charset=utf-8")
	public String saveCardBoxMedia(Model model,@PathVariable("type") String type,
			@CookieValue(value = "city", defaultValue = "-1") int cityjpa, Principal principal,
			@RequestParam(value = "proid", required = true) int proid,
			@RequestParam(value = "needCount", required = false) int needCount) {
		  cardService.saveCard(proid, needCount, principal, cityjpa, type,0);
		  String boidString=String.valueOf(proid)+",";
		  model.addAttribute("_cardSelect", new CardSelect("", boidString));
		  return "redirect:/selected";
	}
	@RequestMapping(value = "/select", produces = "text/html;charset=utf-8")
	public String toCard3(Model model,HttpServletRequest request,Principal principal,
			@RequestParam(value="meids" , required = false) String meids,@RequestParam(value="startdate1" , required = false) String startdate1,@RequestParam(value="boids" , required = false) String boids) {
		model.addAttribute("_cardSelect", new CardSelect(meids, boids));
		model.addAttribute("startdate1", startdate1);
		return "redirect:/selected";
	}

	@RequestMapping(value = "/selected", produces = "text/html;charset=utf-8")
	public String toCard2(Model model, HttpServletRequest request, Principal principal,
			@RequestParam(value = "startdate1", required = false) String startdate1,
			@ModelAttribute("_cardSelect") CardSelect cardselect) {
		CardView carid = null;
		//@RequestParam(value="meids" , required = false) String meids,@RequestParam(value="boids" , required = false) String boids
		model.addAttribute("infos", carid = cardService.getMediaList(principal, 0, cardselect.meids, cardselect.boids));
		model.addAttribute("seriaNum", cardService.getCardBingSeriaNum(principal));
		model.addAttribute("meids", cardselect.meids);
		model.addAttribute("boids", cardselect.boids);
		model.addAttribute("startdate1", startdate1);
		icbcService.sufficeIcbcSubmit(model, Only1ServieUniqLong.getUniqLongNumber(), carid,"online");
		return "secondCart_step2";
	}
	
	
	
	@RequestMapping("/code")
	public void getCode(HttpServletRequest reqeust, HttpServletResponse response) throws IOException {
        response.setContentType("image/jpeg");  
        response.setHeader("Pragma", "no-cache");  
        response.setHeader("Cache-Control", "no-cache");  
        response.setDateHeader("Expires", 0);  
        
        response.setHeader("Expires", "Sat, 16 Jan 1980 12:00:00 GMT"); 
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate"); 
        response.addHeader("Cache-Control", "post-check=0, pre-check=0"); 
        response.addHeader("Content-Type","image/jpeg");  
        
        
        HttpSession session = reqeust.getSession();  
        ValidateCode vCode = new ValidateCode(120,40,5,30); //,100); 
        session.setAttribute("code", vCode.getCode());  
        vCode.write(response.getOutputStream());  
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
	
	@RequestMapping(value = "/intro-groovy")
	public String test() {
		//	Foo foo2 = (Foo) GroovyFactory.context.getBean("Foo");
		//	Foo foo = GroovyFactory.context.getBean(Foo.class);
		foo.execute();
		t.add(System.currentTimeMillis());
		System.out.println(foo.executew(t));
		//System.currentTimeMillis();
		return "intro/about-me";
	}
	static List<Long > t  =new ArrayList<Long>();
	@Autowired
	@Lazy
	GroovySimpleInterface foo;
	
	


	@RequestMapping(value = "report/public_log")
	public String public_log() {
		return "report/log";
	}

	
	
}
