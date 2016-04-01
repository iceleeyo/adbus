package com.pantuo;

import java.lang.annotation.Annotation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.pantuo.util.OpenApiV1;
/**
 * 
 * <b><code>OpenApiInterceptor</code></b>
 * <p>
 * open api 参数验证
 * </p>
 * <b>Creation Time:</b> 2016年3月10日 下午3:38:03
 * @author impanxh@gmail.com
 * @since pantuo 1.0-SNAPSHOT
 */
public class OpenApiInterceptor extends HandlerInterceptorAdapter {

	private static final Logger log = LoggerFactory.getLogger(OpenApiInterceptor.class);

	public boolean isOPenApi(HandlerMethod method) {
		boolean r = false;
		Annotation[] list = method.getMethod().getAnnotations();
		for (Annotation annotation : list) {
			if (annotation.annotationType().isAssignableFrom(OpenApiV1.class)) {
				r = true;
			}
		}
		return r;
	}

	long timeBetween = 1000 * 60 * 15;
	String SOLT = "timeBetween";

	boolean setFailIfNotPass(HttpServletRequest request, HttpServletResponse response) {
		String qs = request.getParameter("qs");//md5
		String k = request.getParameter("k");//time
		String i = request.getParameter("i");//id // id=1&time=139399393393&md5=md5  timeBetween1393993933931
		boolean r = false;
		long t = NumberUtils.toLong(k, 0);
		if (System.currentTimeMillis() - t < timeBetween) {
			if (StringUtils.isNoneBlank(qs)) {
				if (qs.equals(DigestUtils.md5Hex(SOLT + k + i).toLowerCase())) {
					r = true;
				}
			}
		}
		if (!r) {
			 response.setHeader("Access-Control-Allow-Origin", "*");
			response.setStatus(203);
		}
		return r;

	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		if (handler != null) {
			if (handler instanceof HandlerMethod) {
				HandlerMethod method = (HandlerMethod) handler;
				if (method.getMethod() != null) {
					if (isOPenApi(method)) {
						return setFailIfNotPass(request, response);

					}
				}
			}
		}

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

}
