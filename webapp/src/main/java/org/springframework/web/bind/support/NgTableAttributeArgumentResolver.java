package org.springframework.web.bind.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * 
 * <b><code>NgTableAttributeArgumentResolver</code></b>
 * <p>
 * 参数解析 
 * </p>
 * <b>Creation Time:</b> 2016年1月13日 下午5:26:27
 * @author impanxh@gmail.com
 * @since pantuo 1.0-SNAPSHOT
 */
public class NgTableAttributeArgumentResolver extends AbstractNamedValueMethodArgumentResolver implements
		WebArgumentResolver {
	private static Logger logger = LoggerFactory.getLogger(NgTableAttributeArgumentResolver.class);

	public NgTableAttributeArgumentResolver() {
		super(null);
	}

	public boolean supportsParameter(MethodParameter parameter) {

		if (parameter.hasParameterAnnotation(NgTable.class)) {
			return true;
		}
		return false;
	}

	@Override
	protected NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
		NgTable annotation = parameter.getParameterAnnotation(NgTable.class);
		return new RequestJsonParamNamedValueInfo(annotation);

	}

	private ObjectMapper mapper = new ObjectMapper();

	@Override
	protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest webRequest) throws Exception {
		Object attribute_value = null;
		Annotation[] param_annots = parameter.getParameterAnnotations();

		Class<?> parameter_clazz = parameter.getParameterType();
		Map<String, String[]> parameter_map = webRequest.getParameterMap();
		for (Annotation param_annot : param_annots) {
			if (param_annot.annotationType() == NgTable.class) {
				attribute_value = parameter_clazz.newInstance();
				Field[] fields = parameter_clazz.getDeclaredFields();
				for (Field field : fields) {
					String field_name = field.getName();
					NgTableMapField field_annos = field.getAnnotation(NgTableMapField.class);
					try {
						if (field_annos != null) {//判断是否是natagle map
							String[] paramValues = parameter_map.get(field_name);
							if (paramValues != null && paramValues.length == 1
									&& StringUtils.isNoneBlank(paramValues[0])) {
								Object map = mapper.readValue(paramValues[0], Map.class);
								BeanUtils.setProperty(attribute_value, field_name, map);
							}
						} else {//一般的参数 如page page_size
							if (parameter_map.containsKey(field_name)) {
								String[] values = parameter_map.get(field_name);
								BeanUtils.setProperty(attribute_value, field_name, values[0]);
							}
						}
					} catch (Exception e) {
						logger.warn("presolveName:{}", e);
					}
				}
				param_annots = new Annotation[] {};
			}
		}
		if (ObjectUtils.notEqual(attribute_value, null)) {
			return attribute_value;
		} else {
			return UNRESOLVED;
		}
	}

	@Override
	protected void handleMissingValue(String paramName, MethodParameter parameter) throws ServletException {
		String paramType = parameter.getParameterType().getName();
		throw new ServletRequestBindingException("Missing request json parameter '" + paramName
				+ "' for method parameter type [" + paramType + "]");
	}

	private class RequestJsonParamNamedValueInfo extends NamedValueInfo {
		private RequestJsonParamNamedValueInfo(NgTable annotation) {
			super(annotation.value(), annotation.required(), null);
		}
	}

	/**
	 * spring 3.1之前
	 */
	@Override
	public Object resolveArgument(MethodParameter methodParameter, NativeWebRequest request) throws Exception {
		if (!supportsParameter(methodParameter)) {
			return WebArgumentResolver.UNRESOLVED;
		}
		return resolveArgument(methodParameter, null, request, null);
	}

	private BeanFactory beanFactory;

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

}
