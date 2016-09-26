package com.pantuo.web;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
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

import com.pantuo.ActivitiConfiguration;
import com.pantuo.dao.pojo.BaseEntity;
import com.pantuo.dao.pojo.JpaBusOrderDetailV2;
import com.pantuo.dao.pojo.JpaBusOrderV2;
import com.pantuo.dao.pojo.JpaBusline;
import com.pantuo.dao.pojo.JpaCity;
import com.pantuo.dao.pojo.JpaCpd;
import com.pantuo.dao.pojo.JpaProduct;
import com.pantuo.dao.pojo.JpaProduct.FrontShow;
import com.pantuo.dao.pojo.JpaProductLocation;
import com.pantuo.dao.pojo.JpaProductV2;
import com.pantuo.dao.pojo.UserDetail;
import com.pantuo.mybatis.domain.BusOrderDetailV2;
import com.pantuo.mybatis.domain.ProductV2;
import com.pantuo.mybatis.domain.UserCpd;
import com.pantuo.pojo.DataTablePage;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.CardService;
import com.pantuo.service.CityService;
import com.pantuo.service.CpdService;
import com.pantuo.service.ProductService;
import com.pantuo.service.UserServiceInter;
import com.pantuo.service.security.Request;
import com.pantuo.util.Only1ServieUniqLong;
import com.pantuo.util.Pair;
import com.pantuo.web.view.BodyProView;
import com.pantuo.web.view.MediaSurvey;
import com.pantuo.web.view.PlanRequest;
import com.pantuo.web.view.ProductView;

/**
 * @author xl
 */
@Controller
@RequestMapping(produces = "application/json;charset=utf-8", value = "/product")
@SessionAttributes("bodyProView")
public class ProductController {
    private static Logger log = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    @Lazy
    private ProductService productService;
    @Autowired
    private CpdService cpdService;
    @Autowired
	private UserServiceInter userService;
    @Autowired
    private CityService cityService;
	@Autowired
	@Lazy
	CardService cardService;
	
    @RequestMapping("ajax-list")
    @ResponseBody
    public DataTablePage<ProductView> getAllProducts( TableRequest req,
                                                     @CookieValue(value="city", defaultValue = "-1") int city,
 Principal principal) {
		Page<JpaProduct> page = null;
		if (Request.hasAuth(principal, ActivitiConfiguration.ORDER)) {
			page = productService.getAllProducts(city, true, null, req,principal);
		} else {
			page = productService.getAllProducts(city, true, Request.getUserId(principal), req,principal);
		}
		return new DataTablePage(productService.getProductView(page), req.getDraw());
	}
    @RequestMapping("sift_data")
    @ResponseBody
    public DataTablePage<ProductView> searchPro( TableRequest req,
    		@CookieValue(value="city", defaultValue = "-1") int city,
    		Principal principal) {
    	   Page<JpaProduct> page = productService.searchProducts(city, principal, req);
    	return new DataTablePage(productService.getProductView(page), req.getDraw());
    }
    @RequestMapping("getProTags")
    @ResponseBody
    public List<JpaProductLocation> getProTags(
    		@RequestParam(value="belongTag",defaultValue="video") String belongTag) {
    	return productService.getProTags(belongTag);
    }
    @RequestMapping("ajax-productV2_list")
    @ResponseBody
    public DataTablePage<JpaBusOrderDetailV2> productV2_list( TableRequest req,
    		@CookieValue(value="city", defaultValue = "-1") int city,
    		Principal principal,HttpServletResponse response) {
    	 response.setHeader("Access-Control-Allow-Origin", "*");
    	Page<JpaBusOrderDetailV2> page = productService.findAllBusOrderDetailV2(city, principal, req);
    	return new DataTablePage(page, req.getDraw());
    }
    @RequestMapping("ajax-busOrderV2_list/{type}")
    @ResponseBody
    public DataTablePage<JpaProductV2> busOrderV2_list( TableRequest req,
    		@CookieValue(value="city", defaultValue = "-1") int city,@PathVariable("type") String type,
    		Principal principal) {
    	Page<JpaBusOrderV2> page = productService.searchBusOrderV2(city, principal, req,type);
    	return new DataTablePage(page, req.getDraw());
    }
    @RequestMapping("ajax-BusOrderDetailV2")
    @ResponseBody
    public DataTablePage<JpaBusOrderDetailV2> ajaxBusOrderDetailV2( TableRequest req,
    		@CookieValue(value="city", defaultValue = "-1") int city,@RequestParam(value = "pid",required=false ,defaultValue="0") int pid,
    		@RequestParam(value = "orderid",required=false ,defaultValue="0") int orderid,
    		@RequestParam(value="seriaNum",required=false,defaultValue="0") long seriaNum,
    		Principal principal,HttpServletResponse response) {
    	
    	Page<JpaBusOrderDetailV2> page = productService.searchBusOrderDetailV2(orderid,pid,seriaNum,city, principal, req);
    	 response.setHeader("Access-Control-Allow-Origin", "*");
    	return new DataTablePage(page, req.getDraw());
    }
    @RequestMapping("compareProduct-list")
    @ResponseBody
    public DataTablePage<JpaCpd> getCompareProducts( TableRequest req,
    		@CookieValue(value="city", defaultValue = "-1") int city,
    		Principal principal ) {
    	return new DataTablePage(cpdService.getCompareProducts(city, req, principal), req.getDraw());
    }
    @RequestMapping("myComparePro")
    @ResponseBody
    public DataTablePage<JpaCpd> myComparePro( TableRequest req,
    		@CookieValue(value="city", defaultValue = "-1") int city,
    		Principal principal ) {
    	return new DataTablePage(cpdService.getMyCompareProducts(city, req, principal), req.getDraw());
    }

    @RequestMapping(value = "/{productId}/{enable}", method = { RequestMethod.POST})
    @ResponseBody
    public Pair<Boolean, String> enableProduct(@PathVariable("productId") int productId,
                                 @PathVariable("enable") int enable,HttpServletRequest request,
                                 @CookieValue(value="city", defaultValue = "-1") int city) {
        return productService.changeProStats(productId, enable);
    }
    
    @RequestMapping(value = "/checkProHadBought/{productId}")
    @ResponseBody
    public Pair<Boolean, String> checkProHadBought(@PathVariable("productId") int productId){
    	return productService.checkProHadBought(productId);
    }
    
    @RequestMapping(value = "/frontshow/{productId}/{enable}", method = { RequestMethod.POST})
    @ResponseBody
    public JpaProduct frontshow(@PathVariable("productId") int productId,
                                 @PathVariable("enable") String enable,Principal principal,
                                 @CookieValue(value="city", defaultValue = "-1") int city) {
        JpaProduct product = productService.findById(productId);
        if (product == null) {
            JpaProduct p = new JpaProduct();
            p.setErrorInfo(BaseEntity.ERROR, "找不到ID为" + productId + "的套餐");
            return p;
        }
            product.setFrontShow(FrontShow.valueOf(enable));
            productService.saveProduct(city, product,null,null,principal);
        return product;
    }
    
	@RequestMapping(value = "/comparePrice", method = { RequestMethod.POST })
	@ResponseBody
	public Pair<Boolean, String> comparePrice(@RequestParam(value = "cpdid") int cpdid,
			@RequestParam(value = "myprice") double myprice, Principal principal) {
		Pair<Boolean, String> rPair = cpdService.setMyPrice(cpdid, principal, myprice);
		if (rPair.getLeft()) {
			//pdService.changeMoney(principal, cpdid, myprice);
		}
		return rPair;
	}
	
	@RequestMapping(value = "/buyBodyPro/{pid}")
	@ResponseBody
	public Pair<Boolean, String> buyBodyPro(
			@CookieValue(value = "city", defaultValue = "-1") int city, Principal principal,
			HttpServletRequest request, @PathVariable("pid") int pid) {
		return productService.buyBodyPro(pid,city, Request.getUserId(principal));
	}
	
	@RequestMapping(value = "/c/{cpdid}", produces = "text/html;charset=utf-8")
	public String goCpd(Model model, @PathVariable("cpdid") int cpdid,@RequestParam(value = "pid",required=false ,defaultValue="0") int pid) {
		toComparePage(model, cpdid, pid);
		return "comparePage";
	}
	@RequestMapping(value = "/win/{cpdid}", produces = "text/html;charset=utf-8")
	public String win(Model model, @PathVariable("cpdid") int cpdid,@RequestParam(value = "pid",required=false ,defaultValue="0") int pid,HttpServletRequest request) {
		toComparePage(model, cpdid, pid);
		request.getSession(false).setAttribute("token", UUID.randomUUID().toString());
		return "winPage";
	}

	@RequestMapping(value = "/to_comparePage/{cpdid}", produces = "text/html;charset=utf-8")
	public String to_comparePage(Model model, @ModelAttribute("city") JpaCity city, @PathVariable("cpdid") int cpdid) {
		toComparePage(model, cpdid, 0);
		return "comparePage";
	}

	private void toComparePage(Model model, int cpdid, int productId) {
		//如果有Pid根据商品id查cpd 信息
		JpaCpd jpaCpd = productId > 0 ? cpdService.queryOneCpdByPid(productId) : cpdService.queryOneCpdDetail(cpdid);
		List<UserCpd> userCpdList = cpdService.queryLogByCpdId(cpdid);
		JpaProduct prod = jpaCpd.getProduct();
		productService.fillImg(prod);
		model.addAttribute("prod", prod);
		model.addAttribute("jpaCpd", jpaCpd);
		model.addAttribute("userCpdList", userCpdList);
	}
    @PreAuthorize(" hasRole('ShibaOrderManager')  or hasRole('sales') ")
    @RequestMapping(value = "/new", produces = "text/html;charset=utf-8")
    public String newProduct(Model model, @ModelAttribute("city") JpaCity city) {
    	Page<UserDetail> users = userService.getValidUsers(null,0, 999, null);
        model.addAttribute("users", users.getContent());
        if (city != null) {
            model.addAttribute("types", JpaProduct.productTypesForMedia.get(city.getMediaType()));
            if (city.getMediaType() == JpaCity.MediaType.body) {
                model.addAttribute("lineLevels", JpaBusline.Level.values());
            }
        }
        return "newProduct";
    }
   
   
    @RequestMapping(value = "editComparePro/{id}", produces = "text/html;charset=utf-8")
    public String editComparePro(@PathVariable int id,
    		@ModelAttribute("city") JpaCity city,
    		Model model, HttpServletRequest request) {
    	 model.addAttribute("types", JpaProduct.productTypesForMedia.get(city.getMediaType()));
    	model.addAttribute("prod", productService.findCpdById(id));
    	model.addAttribute("jsonView", cardService.getJsonfromJsonStr(productService.findCpdById(id).getProduct().getJsonString()));
    	return "editComparePro";
    }
    @RequestMapping(value = "/d/{id}", produces = "text/html;charset=utf-8")
    public String showdetail(@PathVariable int id,
                                Model model, HttpServletRequest request) {
        model.addAttribute("prod", productService.findById(id));
        return "productView";
    }
    
    @RequestMapping(value = "/ajaxdetail/{id}")
    @ResponseBody
    public JpaProduct ajaxdetail(@PathVariable int id,
                                Model model, HttpServletRequest request) {
       return  productService.findById(id);
    }
    
    @RequestMapping(value = "/prodetail/{id}", produces = "text/html;charset=utf-8")
	public String productDetail(Model model, @PathVariable("id") int productId,
			HttpServletRequest request,HttpServletResponse response) {
		response.setHeader("X-Frame-Options", "SAMEORIGIN");
		//    	int supplies_id=Integer.parseInt(request.getParameter("supplies_id"));
		JpaProduct view =  productService.findById(productId);
		JpaProduct j=cardService.getJpaProductByid(productId);
		model.addAttribute("view", view);
		model.addAttribute("jsonView", cardService.getJsonfromJsonStr(j.getJsonString()));
		return "template/proDetail";
	}
    
    @RequestMapping(value = "isMyCompare/{cpdid}")
    @ResponseBody
    public Pair<Boolean, String> isMyCompare(@PathVariable int cpdid,
    		Model model, Principal principal,HttpServletRequest request) {
    	return  cpdService.isMycompare(cpdid,principal);
    }

    @PreAuthorize(" hasRole('ShibaOrderManager') or hasRole('sales') ")
    @RequestMapping(value = "/save", method = { RequestMethod.POST})
    public String createProduct(
            JpaProduct prod,JpaCpd jpacpd,MediaSurvey survey,@RequestParam(value="startDate1")  String startDate1,@RequestParam(value="biddingDate1")  String biddingDate1,
            @CookieValue(value="city", defaultValue = "-1") int city,Principal principal,
            HttpServletRequest request) {
        if (prod.getId() > 0) {
            log.info("Updating product {}", prod.getName());
        } else {
            log.info("Creating new product {}", prod.getName());
        }
        try {
        	prod.setFrontShow(FrontShow.N);
            productService.saveProduct(city, prod,survey,request,principal);
            if(prod.getIscompare()==1){
            	if (biddingDate1.length() > 1 && startDate1.length()>1 ) {
            		jpacpd.setStartDate((Date) new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parseObject(startDate1));
            		jpacpd.setBiddingDate((Date) new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parseObject(biddingDate1));
            	}
            	jpacpd.setProduct(prod);
            	cpdService.saveOrUpdateCpd(jpacpd);
            }
        } catch (Exception e) {
            prod.setErrorInfo(BaseEntity.ERROR, e.getMessage());
        }
        return "product_list2";
    }
    @RequestMapping(value = "/saveCompareProduct", method = { RequestMethod.POST})
    public String saveCompareProduct(
    		JpaProduct product,JpaCpd cpd,MediaSurvey survey,@RequestParam(value="productid") int productid,@RequestParam(value="cpdid") int cpdid,
    		@RequestParam(value="startDate1") String startDate1,@RequestParam(value="biddingDate1")  String biddingDate1,
    		Principal principal,
    		@CookieValue(value="city", defaultValue = "-1") int city,
    		HttpServletRequest request) {
    	try {
    		product.setId(productid);
    		product.setFrontShow(FrontShow.N);
    		productService.saveProduct(city, product,survey,request,principal);
    			if (biddingDate1.length() > 1 && startDate1.length()>1 ) {
    				cpd.setStartDate((Date) new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parseObject(startDate1));
    				cpd.setBiddingDate((Date) new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parseObject(biddingDate1));
    			}
    			cpd.setId(cpdid);
    			cpd.setProduct(product);
    			cpdService.saveOrUpdateCpd(cpd);
    	} catch (Exception e) {
    	}
    	return "compareProduct_list";
    }
    @RequestMapping(value = "/newBodyPro", produces = "text/html;charset=utf-8")
    public String newBodyPro(Model model) {
    	model.addAttribute("seriaNum", Only1ServieUniqLong.getUniqLongNumber());
    	model.addAttribute("lineLevels", JpaBusline.Level.values());
    	return "newBodyPro";
    }
    
    //打开添加车身套餐页面
    @RequestMapping(value = "/newBodyCombo", produces = "text/html;charset=utf-8")
    public String newBodyCombo(Model model) {
    	model.addAttribute("seriaNum", Only1ServieUniqLong.getUniqLongNumber());
    	model.addAttribute("lineLevels", JpaBusline.Level.values());
    	return "newBodyCombo";
    }
    //添加车身套餐
    @RequestMapping(value = "/saveBodyCombo")
    @ResponseBody
    public Pair<Boolean, String> saveBodyCombo(ProductV2 productV2,MediaSurvey survey,JpaBusOrderDetailV2 detailV2,
    		@CookieValue(value = "city", defaultValue = "-1") int cityID, Principal principal,
    		@RequestParam(value="orderDetailV2Id",defaultValue = "-1") int orderDetailV2Id,
    		@RequestParam(value="productV2Id",defaultValue = "-1") int productV2Id,
    		HttpServletRequest request,HttpServletResponse response) {
    	 int city=cityID==-1?2:cityID;
    	 String userID=principal==null?productV2.getCreater():Request.getUserId(principal);
    	 productV2.setCity(city);
    	 response.setHeader("Access-Control-Allow-Origin", "*");
    	return productService.saveBodyCombo(productV2,detailV2, survey,userID,city,orderDetailV2Id,productV2Id);
    }
    //根据车身产品ID获取json串
    @RequestMapping(value = "/getBodyProViewJson/{id}")
    @ResponseBody
    public String getBodyProViewJson(@PathVariable("id") int id,
                                Model model, HttpServletRequest request,HttpServletResponse response) {
    	response.setHeader("Access-Control-Allow-Origin", "*");
        return productService.getBodyProViewJson(id);
    }
    @RequestMapping(value = "BefEditBodyCombo", produces = "text/html;charset=utf-8")
    public String BefEditBodyCombo(
    		@RequestParam(value="jsonStr",required = false) String jsonStr,
    		Model model, HttpServletRequest request) {
    	model.addAttribute("bodyProView", cardService.getBodyProViewfromJsonStr(jsonStr));
    	return "redirect:/product/editBodyCombo";
    }
    @RequestMapping(value = "/editBodyCombo", produces = "text/html;charset=utf-8")
    public String editBodyCombo(
    		@ModelAttribute("bodyProView") BodyProView bodyProView,
    		Model model, HttpServletRequest request) {
    	//String jString=productService.getBodyProViewJson(id);
    	model.addAttribute("lineLevels", JpaBusline.Level.values());
    	model.addAttribute("jsonView", bodyProView);
    	return "newBodyCombo";
    }
    @PreAuthorize(" hasRole('ShibaOrderManager')  ")
    @RequestMapping(value = "/{id}", produces = "text/html;charset=utf-8")
    public String updateProduct(@PathVariable int id,
    		@ModelAttribute("city") JpaCity city,
    		Model model, HttpServletRequest request) {
    	Page<UserDetail> users = userService.getValidUsers(null,0, 999, null);
    	JpaProduct product=productService.findById(id);
    	model.addAttribute("users", users.getContent());
    	model.addAttribute("prod",product);
    	model.addAttribute("locationIds", productService.selectLocationIdsByProId(id));
    	model.addAttribute("jsonView", cardService.getJsonfromJsonStr(productService.findById(id).getJsonString()));
    	if (city != null) {
    		model.addAttribute("types", JpaProduct.productTypesForMedia.get(city.getMediaType()));
    		if (city.getMediaType() == JpaCity.MediaType.body) {
    			model.addAttribute("lineLevels", JpaBusline.Level.values());
    		}
    	}
    	return "newProduct";
    }
    @RequestMapping(value = "/saveProductV2")
	@ResponseBody
	public Pair<Boolean, String> saveProductV2(ProductV2 productV2,MediaSurvey survey,
			@CookieValue(value = "city", defaultValue = "-1") int city, Principal principal,
			HttpServletRequest request,HttpServletResponse response, @RequestParam(value = "seriaNum", required = true) long seriaNum) {
		productV2.setCity(city);
		response.setHeader("Access-Control-Allow-Origin", "*");
		return productService.saveProductV2(productV2, survey,seriaNum, Request.getUserId(principal));
	}
    @RequestMapping(value = "/saveBusOrderDetail", method = { RequestMethod.POST})
    @ResponseBody
    public Pair<Boolean, Long> saveBusOrderDetail(
    		JpaBusOrderDetailV2 prod,@CookieValue(value="city", defaultValue = "-1") int city,
    		HttpServletRequest request, @RequestParam(value = "seriaNum", required = true) long seriaNum,HttpServletResponse response) {
    	  prod.setCity(city);
    	  prod.setSeriaNum(seriaNum);
    	  
    	  response.setHeader("Access-Control-Allow-Origin", "*");
    	  return productService.saveBusOrderDetail(prod);
    }
    @RequestMapping(value = "/acountPrice", method = { RequestMethod.POST})
    @ResponseBody
    public Long acountPrice(JpaBusOrderDetailV2 prod,HttpServletResponse response) {
    	 response.setHeader("Access-Control-Allow-Origin", "*");
    	return productService.acountPrice(prod);
    }
    @RequestMapping(value = "ajax-remove-busOrderDetail", method = RequestMethod.POST)
	@ResponseBody
	public Pair<Boolean, String> removepublishLine(Principal principal, @CookieValue(value = "city", defaultValue = "-1") int city,
			 @RequestParam("id") int id) {
		return productService.removeBusOrderDetail(principal, city, id);
	}

	@RequestMapping(value = "/list")
	public String contralist(Model model, @ModelAttribute("city") JpaCity city) {
		model.addAttribute("types", JpaProduct.productTypesForMedia.get(city.getMediaType()));
		return "product_list2";
	}
    @RequestMapping(value = "/showProV2Detail/{id}", produces = "text/html;charset=utf-8")
    public String showProV2Detail(Model model, @PathVariable("id") int id,HttpServletResponse response) {
    	response.setHeader("X-Frame-Options", "SAMEORIGIN");
    	model.addAttribute("pid", id);
    	return "busOrderDetail_list";
    }
    @RequestMapping(value = "/showProV2DetailByOrderID/{id}", produces = "text/html;charset=utf-8")
    public String showProV2DetailByOrderID(Model model, @PathVariable("id") int id,HttpServletResponse response) {
    	response.setHeader("X-Frame-Options", "SAMEORIGIN");
    	model.addAttribute("orderid", id);
    	return "busOrderDetail_list";
    }
    @RequestMapping(value = "/busOrderV2_list/{type}")
    public String busOrderV2_list(Model model,@PathVariable("type") String type) {
    	model.addAttribute("type", type);
    	model.addAttribute("currMenu", StringUtils.equals(type, "all")?"车身订单列表":"我的订单");
    	return "BusOrderV2_list";
    }
    @RequestMapping(value = "/productV2_list")
    public String productV2_list(HttpServletResponse response) {
    	 response.setHeader("Access-Control-Allow-Origin", "*");
    	return "productV2_list";
    }
    @RequestMapping(value = "/auction")
    public String comparelist() {
    	return "compareProduct_list";
    }
    @RequestMapping(value = "/myAuctionList")
    public String toMyCompare() {
    	return "myCompare";
    }
    
	@RequestMapping(value = "/sift")
	public String sift(HttpServletRequest request, HttpServletResponse response,
			@CookieValue(value = "city", defaultValue = "-1") int city, Principal principal, Model model) {
		JpaCity jpaCity = cityService.fromId(city);
		if (jpaCity != null && jpaCity.getMediaType() == JpaCity.MediaType.body) {
			return bus_sift(request, response, city, principal, model);
		}
		return "sift";
	}

	@RequestMapping(value = "/sift_bus")
	public String bus_sift(HttpServletRequest request, HttpServletResponse response,
			@CookieValue(value = "city", defaultValue = "-1") int city, Principal principal, Model model) {
		request.getSession().setAttribute("medetype", "body");
		city = makeCookieValueRight(city == -1 ? 2 : (city % 2 == 1 ? city + 1 : city), response);

		model.addAttribute("seriaNum", Only1ServieUniqLong.getUniqLongNumber());
		TableRequest r = new TableRequest();
		r.setLength(4);
		Page<JpaProductV2> page = productService.searchProductV2s(city, principal, r);
		model.addAttribute("siftList", page.getContent());
		return "sift_bus";
	}

	public int makeCookieValueRight(int city, HttpServletResponse response) {
		JpaCity r = cityService.fromId(city);

		if (r == null) {
			log.warn("city:{} is ", city);

			Cookie cookie = new Cookie("city", String.valueOf(city));
			cookie.setPath("/");
			cookie.setMaxAge(0);
			response.addCookie(cookie);
		}

		int w = r == null ? ControllerSupport.defaultCookieValue : r.getId();
		w = w > 6 ? ControllerSupport.defaultCookieValue : r.getId();
		try {
			Cookie cookie = new Cookie("city", String.valueOf(w));
			cookie.setPath("/");
			cookie.setMaxAge(604800);
			response.addCookie(cookie);
		} catch (Exception e) {
		}
		return w;
	}

	@RequestMapping(value = "/sift_addPlan")
	@ResponseBody
	public Pair<Boolean, PlanRequest> addPlan(@CookieValue(value = "city", defaultValue = "-1") int city, Model model,
			String select, int number, String startDate1,long seriaNum, Principal principal) {
		return productService.addPlan(city,seriaNum, select, number, startDate1, principal);
	}

	@RequestMapping(value = "/ajax-delPlan")
	@ResponseBody
	public Pair<Boolean, String> delPlan(int id, Principal principal) {
		return productService.delPlan(id, principal);
	}
	
	@RequestMapping(value = "/ajax-sift_buildPlan")
	@ResponseBody
	public Pair<Boolean, String> sift_buildPlan(@CookieValue(value = "city", defaultValue = "-1") int city,long seriaNum, Principal principal) {
		return productService.buildPlan(city,seriaNum, principal);
	}
	
	
	@RequestMapping(value = "sift_orderdetailV2", method = RequestMethod.GET)
	@ResponseBody
	public List<BusOrderDetailV2> sift_orderdetailV2(Model model, @CookieValue(value = "city", defaultValue = "-1") int city,
			@RequestParam("seriaNum") long seriaNum,Principal principal) {
		return productService.getOrderDetailV2BySeriNum(seriaNum,  principal);
	}
	
	@RequestMapping(value = "/changeStats/{proId}/{enable}", method = { RequestMethod.POST})
    @ResponseBody
    public Pair<Boolean, String> changeStats(@PathVariable("proId") int proId,HttpServletResponse response,
    		@PathVariable("enable") String enable,
                                 @CookieValue(value="city", defaultValue = "-1") int city) {
		 response.setHeader("Access-Control-Allow-Origin", "*");
		 return productService.changeProV2Stats(proId,enable);
    }
	
	@RequestMapping(value = "sift_SelectBodyPrice", method = RequestMethod.GET)
	@ResponseBody
	public Double querySelectPrice(Model model, @CookieValue(value = "city", defaultValue = "-1") int city,
			String select,Principal principal) {
		return productService.querySelectPrice(  city,select);
	}

}
