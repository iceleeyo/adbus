package com.pantuo.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pantuo.dao.pojo.UserDetail;

public class Request {
	private static Log log = LogFactory.getLog(Request.class);

	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	public static UserDetail getUser(HttpServletRequest request) {
		UserDetail u = (UserDetail) request.getSession().getAttribute(com.pantuo.util.Constants.SESSION_U_KEY);
		return u;
	}
	public static String getUserId(HttpServletRequest request) {
		String r = StringUtils.EMPTY;
		UserDetail u = (UserDetail) request.getSession().getAttribute(com.pantuo.util.Constants.SESSION_U_KEY);
		return u == null ? r : u.getUsername();
	}
}
