package com.pantuo.web;

import com.pantuo.dao.pojo.JpaCity;
import com.pantuo.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author tliu
 */
@Component
public class ControllerInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private CityService cityService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //check city cookie
        Cookie[] cookies = request.getCookies();
        Cookie cityCookie = null;
        if (cookies != null) {
        for (Cookie c : cookies) {
            if ("city".equals(c.getName())) {
                if (cityCookie == null) {
                    cityCookie = c;
                    break;
                }
            }
        }
        }
        if (cityCookie != null) {
            JpaCity city = cityService.fromId(Integer.parseInt(cityCookie.getValue()));
            if (city == null) {
                cityCookie = null;
            }
        }

        if (cityCookie == null) {
            JpaCity city = cityService.list(true).isEmpty() ? null : cityService.list(true).get(0);
            cityCookie = new Cookie("city", city == null ? "-1" : city.getId() + "");
            cityCookie.setPath(request.getContextPath());
            cityCookie.setMaxAge(604800);   //1 week
            response.addCookie(cityCookie);
        }
        return true;
    }
}
