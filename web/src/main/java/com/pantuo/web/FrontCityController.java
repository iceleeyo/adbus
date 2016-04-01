package com.pantuo.web;

import com.pantuo.dao.pojo.JpaCity;
import com.pantuo.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Select/change city controller
 *
 * @author tliu
 */
@Controller
@RequestMapping("/f/city")
public class FrontCityController {
    @Autowired
    private CityService cityService;

    @RequestMapping(value = "/select", produces = "application/json;charset=utf-8")
    @ResponseBody
    public JpaCity select(@RequestParam("c") String city, @RequestParam("m") String media,
                          HttpServletRequest request, HttpServletResponse response)
    {
        JpaCity c = cityService.fromName(city, media);
        if (c == null) {
            List<JpaCity> list = cityService.listMedias(city);
            if (!list.isEmpty())
                c = list.get(0);
        }
        if (c == null) {
            Cookie cookie = new Cookie("city", null);
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
            return new JpaCity();
        }
        Cookie cookie = new Cookie("city", c.getId() + "");
        cookie.setPath("/");
        cookie.setMaxAge(604800);   //1 week
        response.addCookie(cookie);

        return c;
    }
}
