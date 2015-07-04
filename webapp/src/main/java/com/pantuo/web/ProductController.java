package com.pantuo.web;

import javax.servlet.http.HttpServletRequest;

import com.pantuo.ActivitiConfiguration;
import com.pantuo.dao.pojo.*;
import com.pantuo.mybatis.domain.UserCpd;
import com.pantuo.pojo.DataTablePage;
import com.pantuo.pojo.TableRequest;
import com.pantuo.util.Pair;
import com.pantuo.util.Request;
import com.pantuo.web.view.ProductView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.pantuo.service.CpdService;
import com.pantuo.service.ProductService;
import com.pantuo.service.UserServiceInter;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
                                                     Principal principal ) {
        Page<JpaProduct> page = null;
        if (Request.hasAuth(principal, ActivitiConfiguration.ORDER)) {
            page = productService.getAllProducts(city, req.getFilter("name"),
                    true, null,
                    req.getPage(), req.getLength(), req.getSort("id"));
        } else {
            page = productService.getAllProducts(city, req.getFilter("name"),
                    true, Request.getUserId(principal),
                    req.getPage(), req.getLength(), req.getSort("id"));
        }
        return new DataTablePage(productService.getProductView(page), req.getDraw());
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
    @RequestMapping(value = "/to_comparePage/{cpdid}", produces = "text/html;charset=utf-8")
    public String to_comparePage(Model model, @ModelAttribute("city") JpaCity city,@PathVariable("cpdid") int cpdid) {
    	JpaCpd jpaCpd = cpdService.queryOneCpdDetail(cpdid);
    	List<UserCpd> userCpdList=cpdService.queryLogByCpdId(cpdid);
    	model.addAttribute("jpaCpd", jpaCpd);
    	model.addAttribute("userCpdList", userCpdList);
    	return "comparePage";
    }
    @PreAuthorize(" hasRole('ShibaOrderManager') ")
    @RequestMapping(value = "/new", produces = "text/html;charset=utf-8")
    public String newProduct(Model model, @ModelAttribute("city") JpaCity city) {
    	Page<UserDetail> users = userService.getValidUsers(0, 999, null);
        model.addAttribute("users", users.getContent());
        if (city != null) {
            model.addAttribute("types", JpaProduct.productTypesForMedia.get(city.getMediaType()));
            if (city.getMediaType() == JpaCity.MediaType.body) {
                model.addAttribute("lineLevels", JpaBusline.Level.values());
            }
        }
        return "newProduct";
    }
   
    
    @PreAuthorize(" hasRole('ShibaOrderManager')  ")
    @RequestMapping(value = "/{id}", produces = "text/html;charset=utf-8")
    public String updateProduct(@PathVariable int id,
                                @ModelAttribute("city") JpaCity city,
                                Model model, HttpServletRequest request) {
        Page<UserDetail> users = userService.getValidUsers(0, 999, null);
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
            productService.saveProduct(city, prod);
            if(prod.getIscompare()==1){
            	String biddingDate1 = request.getParameter("biddingDate1").toString();
            	if (biddingDate1.length() > 1 ) {
            		jpacpd.setBiddingDate((Date) new SimpleDateFormat("yyyy-MM-dd").parseObject(biddingDate1));
            	}
            	jpacpd.setProduct(prod);
            	cpdService.saveOrUpdateCpd(jpacpd);
            }
        } catch (Exception e) {
            prod.setErrorInfo(BaseEntity.ERROR, e.getMessage());
        }
        return prod;
    }


    @RequestMapping(value = "/list")
    public String contralist() {
//        int psize = 9;
//        NumberPageUtil page = new NumberPageUtil(productService.countMyList(name, code, request), pageNum, psize);
//        model.addAttribute("list", productService.queryContractList(page, name, code, request));
//        model.addAttribute("pageNum", pageNum);
//        model.addAttribute("paginationHTML", page.showNumPageWithEmpty());
        return "product_list2";
    }
    @RequestMapping(value = "/auction")
    public String comparelist() {
    	return "compareProduct_list";
    }
    @RequestMapping(value = "/myAuctionList")
    public String toMyCompare() {
    	return "myCompare";
    }

}
