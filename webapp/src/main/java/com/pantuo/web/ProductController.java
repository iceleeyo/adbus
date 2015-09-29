package com.pantuo.web;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.pantuo.ActivitiConfiguration;
import com.pantuo.dao.pojo.BaseEntity;
import com.pantuo.dao.pojo.JpaBusOrderDetailV2;
import com.pantuo.dao.pojo.JpaBusOrderV2;
import com.pantuo.dao.pojo.JpaBusline;
import com.pantuo.dao.pojo.JpaCity;
import com.pantuo.dao.pojo.JpaCpd;
import com.pantuo.dao.pojo.JpaProduct;
import com.pantuo.dao.pojo.JpaProduct.FrontShow;
import com.pantuo.dao.pojo.JpaProductV2;
import com.pantuo.dao.pojo.UserDetail;
import com.pantuo.mybatis.domain.BusOrderDetailV2;
import com.pantuo.mybatis.domain.ProductV2;
import com.pantuo.mybatis.domain.UserCpd;
import com.pantuo.pojo.DataTablePage;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.CpdService;
import com.pantuo.service.ProductService;
import com.pantuo.service.UserServiceInter;
import com.pantuo.service.impl.ProductServiceImpl.PlanRequest;
import com.pantuo.util.Only1ServieUniqLong;
import com.pantuo.util.Pair;
import com.pantuo.util.Request;
import com.pantuo.web.view.ProductView;

/**
 * @author xl
 */
@Controller
@RequestMapping(produces = "application/json;charset=utf-8", value = "/product")
public class ProductController {
    private static Logger log = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;
    @Autowired
    private CpdService cpdService;
    @Autowired
	private UserServiceInter userService;
    
    @RequestMapping("ajax-list")
    @ResponseBody
    public DataTablePage<ProductView> getAllProducts( TableRequest req,
                                                     @CookieValue(value="city", defaultValue = "-1") int city,
 Principal principal) {
		Page<JpaProduct> page = null;
		if (Request.hasAuth(principal, ActivitiConfiguration.ORDER)) {
			page = productService.getAllProducts(city, true, null, req);
		} else {
			page = productService.getAllProducts(city, true, Request.getUserId(principal), req);
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
    @RequestMapping("ajax-productV2_list")
    @ResponseBody
    public DataTablePage<JpaProductV2> productV2_list( TableRequest req,
    		@CookieValue(value="city", defaultValue = "-1") int city,
    		Principal principal) {
    	Page<JpaProductV2> page = productService.searchProductV2s(city, principal, req);
    	return new DataTablePage(page, req.getDraw());
    }
    @RequestMapping("ajax-busOrderV2_list")
    @ResponseBody
    public DataTablePage<JpaProductV2> busOrderV2_list( TableRequest req,
    		@CookieValue(value="city", defaultValue = "-1") int city,
    		Principal principal) {
    	Page<JpaBusOrderV2> page = productService.searchBusOrderV2(city, principal, req,"all");
    	return new DataTablePage(page, req.getDraw());
    }
    @RequestMapping("ajax-BusOrderDetailV2")
    @ResponseBody
    public DataTablePage<JpaBusOrderDetailV2> ajaxBusOrderDetailV2( TableRequest req,
    		@CookieValue(value="city", defaultValue = "-1") int city,@RequestParam(value = "pid",required=false ,defaultValue="0") int pid,@RequestParam(value="seriaNum",required=false,defaultValue="0") long seriaNum,
    		Principal principal) {
    	Page<JpaBusOrderDetailV2> page = productService.searchBusOrderDetailV2(pid,seriaNum,city, principal, req);
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
    public JpaProduct enableProduct(@PathVariable("productId") int productId,
                                 @PathVariable("enable") String enable,
                                 @CookieValue(value="city", defaultValue = "-1") int city) {
        boolean en = "enable".equals(enable);
        JpaProduct product = productService.findById(productId);
        if (product == null) {
            JpaProduct p = new JpaProduct();
            p.setErrorInfo(BaseEntity.ERROR, "找不到ID为" + productId + "的套餐");
            return p;
        }

        if (product.isEnabled() != en) {
            product.setEnabled(en);
            productService.saveProduct(city, product);
        }
        return product;
    }
    
    @RequestMapping(value = "/frontshow/{productId}/{enable}", method = { RequestMethod.POST})
    @ResponseBody
    public JpaProduct frontshow(@PathVariable("productId") int productId,
                                 @PathVariable("enable") String enable,
                                 @CookieValue(value="city", defaultValue = "-1") int city) {
        JpaProduct product = productService.findById(productId);
        if (product == null) {
            JpaProduct p = new JpaProduct();
            p.setErrorInfo(BaseEntity.ERROR, "找不到ID为" + productId + "的套餐");
            return p;
        }
            product.setFrontShow(FrontShow.valueOf(enable));
            productService.saveProduct(city, product);
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
	@RequestMapping(value = "/saveProductV2")
	@ResponseBody
	public Pair<Boolean, String> saveProductV2(ProductV2 productV2,
			@CookieValue(value = "city", defaultValue = "-1") int city, Principal principal,
			HttpServletRequest request, @RequestParam(value = "seriaNum", required = true) long seriaNum) {
		productV2.setCity(city);
		return productService.saveProductV2(productV2, seriaNum, Request.getUserId(principal));
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
		model.addAttribute("prod", prod);
		model.addAttribute("jpaCpd", jpaCpd);
		model.addAttribute("userCpdList", userCpdList);
	}
    @PreAuthorize(" hasRole('ShibaOrderManager') ")
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
    @RequestMapping(value = "/newBodyPro", produces = "text/html;charset=utf-8")
    public String newBodyPro(Model model) {
    	model.addAttribute("seriaNum", Only1ServieUniqLong.getUniqLongNumber());
    	model.addAttribute("lineLevels", JpaBusline.Level.values());
    	return "newBodyPro";
    }
   
    @PreAuthorize(" hasRole('ShibaOrderManager')  ")
    @RequestMapping(value = "/{id}", produces = "text/html;charset=utf-8")
    public String updateProduct(@PathVariable int id,
                                @ModelAttribute("city") JpaCity city,
                                Model model, HttpServletRequest request) {
        Page<UserDetail> users = userService.getValidUsers(null,0, 999, null);
        model.addAttribute("users", users.getContent());
    	model.addAttribute("prod", productService.findById(id));
        if (city != null) {
            model.addAttribute("types", JpaProduct.productTypesForMedia.get(city.getMediaType()));
            if (city.getMediaType() == JpaCity.MediaType.body) {
                model.addAttribute("lineLevels", JpaBusline.Level.values());
            }
        }
//        model.addAttribute("types", JpaProduct.Type.values());
        return "newProduct";
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
		model.addAttribute("view", view);
		return "template/proDetail";
	}
    
    @RequestMapping(value = "isMyCompare/{cpdid}")
    @ResponseBody
    public Pair<Boolean, String> isMyCompare(@PathVariable int cpdid,
    		Model model, Principal principal,HttpServletRequest request) {
    	return  cpdService.isMycompare(cpdid,principal);
    }

    @PreAuthorize(" hasRole('ShibaOrderManager')  ")
    @RequestMapping(value = "/save", method = { RequestMethod.POST})
    @ResponseBody
    public JpaProduct createProduct(
            JpaProduct prod,JpaCpd jpacpd,
            @CookieValue(value="city", defaultValue = "-1") int city,
            HttpServletRequest request) {
        if (prod.getId() > 0) {
            log.info("Updating product {}", prod.getName());
        } else {
            log.info("Creating new product {}", prod.getName());
        }
        try {
        	prod.setFrontShow(FrontShow.N);
            productService.saveProduct(city, prod);
            if(prod.getIscompare()==1){
            	String biddingDate1 = request.getParameter("biddingDate1").toString();
            	String startDate1 = request.getParameter("startDate1").toString();
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
        return prod;
    }
    @RequestMapping(value = "/saveBusOrderDetail", method = { RequestMethod.POST})
    @ResponseBody
    public Pair<Boolean, Long> saveBusOrderDetail(
    		JpaBusOrderDetailV2 prod,@CookieValue(value="city", defaultValue = "-1") int city,
    		HttpServletRequest request, @RequestParam(value = "seriaNum", required = true) long seriaNum) {
    	  prod.setCity(city);
    	  prod.setSeriaNum(seriaNum);
    	  return productService.saveBusOrderDetail(prod);
    }
    @RequestMapping(value = "ajax-remove-busOrderDetail", method = RequestMethod.POST)
	@ResponseBody
	public Pair<Boolean, String> removepublishLine(Principal principal, @CookieValue(value = "city", defaultValue = "-1") int city,
			 @RequestParam("id") int id) {
		return productService.removeBusOrderDetail(principal, city, id);
	}

    @RequestMapping(value = "/list")
    public String contralist() {
        return "product_list2";
    }
    @RequestMapping(value = "/showProV2Detail/{id}", produces = "text/html;charset=utf-8")
    public String showProV2Detail(Model model, @PathVariable("id") int id,HttpServletResponse response) {
    	response.setHeader("X-Frame-Options", "SAMEORIGIN");
    	model.addAttribute("pid", id);
    	return "busOrderDetail_list";
    }
    @RequestMapping(value = "/busOrderV2_list")
    public String busOrderV2_list() {
    	return "BusOrderV2_list";
    }
    @RequestMapping(value = "/productV2_list")
    public String productV2_list() {
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
    public String sift() {
    	return "sift";
    }
    @RequestMapping(value = "/sift_bus")
    public String bus_sift(@CookieValue(value = "city", defaultValue = "-1") int city,Principal principal,Model model) {
    	model.addAttribute("seriaNum", Only1ServieUniqLong.getUniqLongNumber());
    	TableRequest r = new TableRequest();
    	r.setLength(4);
    	Page<JpaProductV2> page = productService.searchProductV2s(city, principal, r);
    	model.addAttribute("siftList", page.getContent());
    	return "sift_bus";
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
	
	 
	
	@RequestMapping(value = "sift_SelectBodyPrice", method = RequestMethod.GET)
	@ResponseBody
	public Double querySelectPrice(Model model, @CookieValue(value = "city", defaultValue = "-1") int city,
			String select,Principal principal) {
		return productService.querySelectPrice(  city,select);
	}

}
