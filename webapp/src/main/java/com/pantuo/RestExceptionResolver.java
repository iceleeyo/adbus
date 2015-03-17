package com.pantuo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pantuo.dao.pojo.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class RestExceptionResolver implements HandlerExceptionResolver {
    private Logger log = LoggerFactory.getLogger(RestExceptionResolver.class);

    private ObjectMapper mapper;

    public RestExceptionResolver() {
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

  //  @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        log.error("Error processing REST request {}", httpServletRequest.getRequestURI() , e);

        Map error = new HashMap<String, Object>();
        error.put("error", BaseEntity.ERROR);
        error.put("errorMessage", e.getMessage());

        MappingJackson2JsonView jsonView = new MappingJackson2JsonView();
        jsonView.setObjectMapper(mapper);
        ModelAndView mav = new ModelAndView(jsonView);
        mav.addAllObjects(error);
        return mav;
    }
}
