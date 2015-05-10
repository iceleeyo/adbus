package com.pantuo.web;

import com.pantuo.dao.pojo.JpaCity;
import com.pantuo.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Select/change city controller
 *
 * @author tliu
 */
@Controller
@RequestMapping("/city")
public class CityController {
    @Autowired
    private CityService cityService;

    @RequestMapping(value = "/select/{city}", produces = "application/json;charset=utf-8")
    @ResponseBody
    public JpaCity select(@PathVariable("city") int city, HttpServletRequest request, HttpServletResponse response)
    {
        JpaCity c = cityService.fromId(city);
        if (c == null) {
            Cookie cookie = new Cookie("city", null);
            cookie.setPath(request.getContextPath());
            cookie.setMaxAge(0);
            response.addCookie(cookie);
            return new JpaCity();
        }
        Cookie cookie = new Cookie("city", c.getId() + "");
        cookie.setPath(request.getContextPath());
        cookie.setMaxAge(604800);   //1 week
        response.addCookie(cookie);

        return c;
    }
}
