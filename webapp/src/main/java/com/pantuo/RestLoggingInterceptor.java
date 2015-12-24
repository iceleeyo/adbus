package com.pantuo;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Log request and response
 *
 * @author tliu
 */
public class RestLoggingInterceptor extends HandlerInterceptorAdapter {

	public static Map<String, String> URL_VIEWMAP = new HashMap<String, String>();

	private static final Logger log = LoggerFactory.getLogger(RestLoggingInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		request.setAttribute("_start_ts_", System.currentTimeMillis());
		log.info("{} ==> {} {}{}{}", request.getRemoteAddr(), request.getMethod(), request.getRequestURI(),
				request.getQueryString() == null ? "" : "?",
				request.getQueryString() == null ? "" : request.getQueryString());
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if (handler != null) {
			if (handler instanceof HandlerMethod) {
				HandlerMethod method = (HandlerMethod) handler;
				if (method.getMethod() != null) {
					String key = splitCglib(method.getBean().getClass().getSimpleName()) + method.getMethod().getName();
					URL_VIEWMAP.put(key, modelAndView != null ? modelAndView.getViewName() : StringUtils.EMPTY);
				}

			}
		}
		log.info("<==> Took {} ms.", (System.currentTimeMillis() - (Long) request.getAttribute("_start_ts_")));
	}

	public String splitCglib(String str) {
		String[] s = StringUtils.split(str, "$");
		return s[0];
	}
}
