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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author tliu
 */
@ControllerAdvice
public class ControllerSupport {
	private static Logger logger = LoggerFactory.getLogger(ControllerSupport.class);
	public static int defaultCookieValue = 1;
	@Autowired
	private CityService cityService;

	@ModelAttribute("cities")
	public List<JpaCity> cities() {
		return cityService.list(true);
	}

	@ModelAttribute("city")
	public JpaCity city(@CookieValue(value = "city", defaultValue = "1") int city, HttpServletResponse response) {
		JpaCity r = cityService.fromId(city);
		if (r == null) {
			logger.info("cookie_value_error:{}", city);
			Cookie cookie = new Cookie("city", String.valueOf(city));
			cookie.setPath("/");
			cookie.setMaxAge(0);
			response.addCookie(cookie);

			r = cityService.fromId(defaultCookieValue);
			bcity(response, defaultCookieValue);
		}
		return r;
	}

	public static void bcity(HttpServletResponse response, int city) {
		try {
			Cookie cookie = new Cookie("city", String.valueOf(city));
			cookie.setPath("/");
			cookie.setMaxAge(604800); //1 week
			response.addCookie(cookie);
		} catch (Exception e) {
			logger.error("ControllerSupport ", e);
		}
	}

	@ModelAttribute("medias")
	public List<JpaCity> medias(@CookieValue(value = "city", defaultValue = "1") int city) {
		return cityService.listMedias(city);
	}
}
