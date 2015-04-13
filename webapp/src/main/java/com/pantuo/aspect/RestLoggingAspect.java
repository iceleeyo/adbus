package com.pantuo.aspect;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 *
 * @author tliu
 */
@Aspect
@Component
public class RestLoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(RestLoggingAspect.class);

    private ObjectMapper mapper = new ObjectMapper();

    public RestLoggingAspect() {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
    }

    @AfterReturning(pointcut="execution(* com.pantuo.web.AdbusController.*(..))", returning="result")
    public void afterReturning(JoinPoint joinPoint , Object result)  {

        try {
            if (logger.isDebugEnabled()) {
                String json = mapper.writeValueAsString(result);
                logger.debug("<== {}", json);
            } else {
                logger.info("<== JSON (...)");
            }
        } catch (JsonProcessingException e) {
            logger.error("<== ERROR LOGGING: {}", e);
        }
    }
}
