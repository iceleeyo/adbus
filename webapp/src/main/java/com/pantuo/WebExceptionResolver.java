package com.pantuo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class WebExceptionResolver implements HandlerExceptionResolver {
    private Logger log = LoggerFactory.getLogger(WebExceptionResolver.class);

   // @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        log.error("Error processing request {}", httpServletRequest.getRequestURI() , e);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        
        Map<String, Object> model = new HashMap<String, Object> ();
        model.put("errorMsg", e.getMessage());
        model.put("errorStack", sw.toString());
        ModelAndView mav = new ModelAndView("error", model);
        return mav;
    }
}
