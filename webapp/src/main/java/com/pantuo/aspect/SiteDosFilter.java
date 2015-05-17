package com.pantuo.aspect;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * <b><code>SiteDosFilter</code></b>
 * <p>
 * 发现dos Filter 有问题 上传文件超过30秒的给拦截掉了
 * 
 * 针对内部耗时长的http地址 需要加上?dos_authorize_token=b157f4ea25e968b0e3d646ef10ff6624
 * </p>
 * <b>Creation Time:</b> 2015年5月12日 下午6:08:42
 * @author impanxh@gmail.com
 * @since pantuotech 1.0-SNAPSHOT 
 */
public class SiteDosFilter extends org.eclipse.jetty.servlets.DoSFilter {

	private static Logger log = LoggerFactory.getLogger(SiteDosFilter.class);

	public SiteDosFilter() {
		log.info("SiteDosFilter init !");
	}

	public static final String TOKEN = "dos_authorize_token";
	public static final String TOKEN_VALUE = "b157f4ea25e968b0e3d646ef10ff6624";

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException,
			ServletException {
		System.out.println(  ((HttpServletRequest)request).getRequestURI());
		if (StringUtils.equals(request.getParameter(TOKEN), TOKEN_VALUE)) {
			try {
				filterChain.doFilter(request, response);
			} finally {
			}
		} else {
			super.doFilter((HttpServletRequest) request, (HttpServletResponse) response, filterChain);
		}

	}

}
