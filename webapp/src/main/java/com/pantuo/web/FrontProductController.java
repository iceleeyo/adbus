package com.pantuo.web;

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
        Page<JpaProduct> list = productService.getValidProducts(-1, type, false, null, 0, 999, new Sort("id"));
        model.addAttribute("type", type.ordinal());
        model.addAttribute("typeStr", type.getTypeName());
        model.addAttribute("list", list.getContent());
        return "front_product_list";
    }

}
