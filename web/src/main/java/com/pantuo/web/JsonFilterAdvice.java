package com.pantuo.web;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.codehaus.groovy.runtime.wrappers.FloatWrapper;
import org.codehaus.jackson.map.ser.FilterProvider;
import org.codehaus.jackson.map.ser.impl.SimpleBeanPropertyFilter;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.pantuo.dao.pojo.UserDetail;
import com.pantuo.pojo.DataTablePage;
import com.pantuo.spring.JsonFilter;

@ControllerAdvice
public class JsonFilterAdvice implements ResponseBodyAdvice<Object> {

	private static Logger logger = LoggerFactory.getLogger(JsonFilterAdvice.class);

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		List<Annotation> annotations = Arrays.asList(returnType.getMethodAnnotations());
		return annotations.stream().anyMatch(annotation -> annotation.annotationType().equals(JsonFilter.class));
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
			ServerHttpRequest request, ServerHttpResponse response) {
		JsonFilter annotation = returnType.getMethodAnnotation(JsonFilter.class);
		List<String> possibleFilters = Arrays.asList(annotation.keys());

		if (body instanceof DataTablePage) {
			for (Object bean : ((DataTablePage<?>) body).getContent()) {
				for (String propertyName : possibleFilters) {
					setNestedPropertyValue(bean, propertyName, null);
				}
			}
		}
		return body;
	}

	public void setNestedPropertyValue(Object bean, String propertyName, Object value) {
		PropertyUtilsBean beanUtil = new PropertyUtilsBean();
		String[] propertyLevels = propertyName.split("\\.");
		String propertyNameWithParent = "";
		for (int i = 0; i < propertyLevels.length; i++) {
			String p = propertyLevels[i];
			propertyNameWithParent = (propertyNameWithParent.equals("") ? p : propertyNameWithParent + "." + p);

			try {
				if (i < (propertyLevels.length - 1) && beanUtil.getProperty(bean, propertyNameWithParent) != null) {
					continue;
				}
				Class pType = beanUtil.getPropertyType(bean, propertyNameWithParent);
				if (i < (propertyLevels.length - 1)) {
					org.apache.commons.beanutils.BeanUtils.setProperty(bean, propertyNameWithParent, pType.getConstructor().newInstance());
				} else {
					org.apache.commons.beanutils.BeanUtils.setProperty(bean, propertyNameWithParent, value);
				}
			} catch (Exception e) {
				logger.warn("error to set property value ", e);
			}
		}
	}

}
