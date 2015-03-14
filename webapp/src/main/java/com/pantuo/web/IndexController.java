package com.pantuo.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Index controller
 *
 * @author tliu
 */
@Controller
public class IndexController {
    @RequestMapping(value = "/", produces = "text/html;charset=utf-8")
    public String index()
    {
        return "redirect:/index.html";
    }
}
