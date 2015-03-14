package com.pantuo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Log request and response
 *
 * @author tliu
 */
public class RestLoggingInterceptor extends HandlerInterceptorAdapter {
    private static final Logger log = LoggerFactory.getLogger(RestLoggingInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute("_start_ts_", System.currentTimeMillis());
        log.info("{} ==> {} {}{}{}",
                request.getRemoteAddr(),
                request.getMethod(),
                request.getRequestURI(),
                request.getQueryString() == null ? "" : "?",
                request.getQueryString() == null ? "" : request.getQueryString());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("<==> Took {} ms.", (System.currentTimeMillis() - (Long)request.getAttribute("_start_ts_")));
    }
}
