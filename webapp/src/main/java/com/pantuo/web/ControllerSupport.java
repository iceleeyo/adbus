package com.pantuo.web;

import com.pantuo.dao.pojo.JpaCity;
import com.pantuo.service.CityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

/**
 * @author tliu
 */
@ControllerAdvice
public class ControllerSupport {
    private static Logger logger = LoggerFactory.getLogger(ControllerSupport.class);

    @Autowired
    private CityService cityService;

    @ModelAttribute("cities")
    public List<JpaCity> cities() {
        return cityService.list();
    }

    @ModelAttribute("city")
    public JpaCity city(@CookieValue(value="city", defaultValue = "-1") int city) {
        return cityService.fromId(city);
    }
}
