package com.pantuo.util;

import javax.servlet.http.HttpServletRequest;

import com.pantuo.service.security.ActivitiUserDetails;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pantuo.dao.pojo.UserDetail;
import org.springframework.security.core.Authentication;

import java.security.Principal;

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
	public static UserDetail getUser(Principal principal) {
        return principal == null ? null : ((ActivitiUserDetails)((Authentication)principal).getPrincipal()).getUserDetail();
	}
	public static String getUserId(Principal principal) {
        return principal == null ? "" : principal.getName();
	}
}
