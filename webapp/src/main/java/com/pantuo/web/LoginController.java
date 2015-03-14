package com.pantuo.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * Index controller
 *
 * @author tliu
 */
@Controller
public class LoginController {
    private static Logger log = LoggerFactory.getLogger(LoginController.class);


    @RequestMapping(value = "/login", produces = "text/html;charset=utf-8")
    public String login(HttpServletRequest request)
    {
        return "login";
    }

    @RequestMapping(value = "/logout", produces = "text/html;charset=utf-8")
    public String logout(HttpServletRequest request)
    {
        try {
            request.logout();
        } catch (ServletException e) {
            log.error("Failed to logout.", e);
        }

        return "redirect:/login";
    }

}
