package com.pantuo.web;

import javax.servlet.http.HttpServletRequest;

import com.pantuo.dao.pojo.BaseEntity;
import com.pantuo.dao.pojo.JpaProduct;
import com.pantuo.dao.pojo.UserDetail;
import com.pantuo.pojo.DataTablePage;
import org.activiti.engine.IdentityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.pantuo.service.ProductService;
import com.pantuo.util.NumberPageUtil;

import java.util.Random;
import java.util.UUID;

/**
 * @author xl
 */
@Controller
@RequestMapping(produces = "application/json;charset=utf-8", value = "/product")
public class ProductController {
    private static Logger log = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @RequestMapping("ajax-list")
    @ResponseBody
    public DataTablePage<JpaProduct> getAllProducts( @RequestParam(value = "start", required = false, defaultValue = "0") int start,
                                                     @RequestParam(value = "length", required = false, defaultValue = "10") int length,
                                                     @RequestParam(value = "name", required = false) String name,
                                                     @RequestParam(value = "draw", required = false, defaultValue = "1") int draw) {

        /** test **/
        Random r = new Random();
        for (int i=0; i<Math.abs(r.nextInt(5)) + 2; i++) {
            JpaProduct p = new JpaProduct(JpaProduct.Type.video, UUID.randomUUID().toString(),
                    (long)(Math.abs(r.nextInt(120301))), Math.abs(r.nextInt(5)), Math.abs(r.nextInt(5)),
                    Math.abs(r.nextInt(5)), Math.abs(Math.random()), Math.abs(r.nextInt(5)), Math.abs(r.nextInt(1000000)/100.0),
                    (Math.random() > 0));
            productService.saveProduct(p);
        }
        /** test end **/

        if (length < 1)
            length = 1;

        return new DataTablePage(productService.getAllProducts(name, start/length, length), draw);
    }

    @RequestMapping(value = "/{productId}/{enable}", method = { RequestMethod.POST})
    @ResponseBody
    public JpaProduct enableProduct(@PathVariable("productId") int productId,
                                 @PathVariable("enable") String enable) {
        boolean en = "enable".equals(enable);
        JpaProduct product = productService.findById(productId);
        if (product == null) {
            JpaProduct p = new JpaProduct();
            p.setErrorInfo(BaseEntity.ERROR, "找不到ID为" + productId + "的套餐");
            return p;
        }

        if (product.isEnabled() != en) {
            product.setEnabled(en);
            productService.saveProduct(product);
        }
        return product;
    }

    @RequestMapping(value = "/new", produces = "text/html;charset=utf-8")
    public String newProduct(HttpServletRequest request) {
        return "newProduct";
    }

    @RequestMapping(value = "/{id}", produces = "text/html;charset=utf-8")
    public String updateProduct(@PathVariable int id,
                                Model model, HttpServletRequest request) {
        JpaProduct prod  = productService.findById(id);
        model.addAttribute("prod", prod);
        return "newProduct";
    }

    @RequestMapping(value = "/save", method = { RequestMethod.POST})
    @ResponseBody
    public JpaProduct createProduct(
            JpaProduct prod,
/*            @RequestParam(value = "id", required = false) Integer id,
            @RequestParam(value = "type", required = true) JpaProduct.Type type,
            @RequestParam(value = "name", required = true) String name,
            @RequestParam(value = "duration", required = true) long duration,
            @RequestParam(value = "playNumber", required = true) int playNumber,
            @RequestParam(value = "firstNumber", required = true) int firstNumber,
            @RequestParam(value = "lastNumber", required = true) int lastNumber,
            @RequestParam(value = "hotRatio", required = true) double hotRatio,
            @RequestParam(value = "days", required = true) int days,
            @RequestParam(value = "price", required = true) double price,*/
            HttpServletRequest request) {
//        JpaProduct prod = new JpaProduct(type, name, duration, playNumber, firstNumber, lastNumber, hotRatio, days, price, false);
        if (prod.getId() > 0) {
            log.info("Updating product {}", prod.getName());
        } else {
            log.info("Creating new product {}", prod.getName());
        }
        try {
            productService.saveProduct(prod);
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

}
