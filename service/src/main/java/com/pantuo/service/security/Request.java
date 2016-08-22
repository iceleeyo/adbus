package com.pantuo.service.security;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import com.google.common.collect.Lists;
import com.pantuo.dao.pojo.JpaVideo32OrderStatus;
import com.pantuo.dao.pojo.UserDetail;
import com.pantuo.service.UserServiceInter;
import com.pantuo.service.security.ActivitiUserDetails;

public class Request {
	private static Log log = LogFactory.getLog(Request.class);

	public static final String HOST_IP = "busme.cn";
	public static final boolean IS_ONLINE = true;
	
	
	
	public static boolean isMobileRequest(HttpServletRequest request) {
		String s1 = request.getHeader("user-agent");
		if (StringUtils.equals(request.getParameter("from"), "mobile")) {
			return true;
		} else {
			if (StringUtils.equals(request.getParameter("from"), "pc")) {
				return false;
			}
		}
		boolean r = false;
		if (s1.contains("Android")) {
			r = true;
		} else if (s1.contains("iPhone")) {
			r = true;
		} else if (s1.contains("iPad")) {
			r = true;
		} else {
		}
		return r;
	}

	/**
	 * 
	 * 取的服务器端ip 
	 *
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public static String getServerIp() {
		String SERVER_IP = HOST_IP;
		if (!IS_ONLINE) {
			try {
				Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
				InetAddress ip = null;
				while (netInterfaces.hasMoreElements()) {
					NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
					ip = (InetAddress) ni.getInetAddresses().nextElement();
					SERVER_IP = ip.getHostAddress();
					if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {
						SERVER_IP = ip.getHostAddress();
						break;
					} else {
						ip = null;
					}
				}
			} catch (SocketException e) {
				log.error("get serverip SocketException{}", e);
			} catch (Exception e) {
				log.error("get serverip Exception{}", e);
			}
		}
		return StringUtils.contains(SERVER_IP, "0:0:0:0:0") ? HOST_IP : SERVER_IP;//fe80:0:0:0:0:0:0:1%1
	}

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
		UserDetail detail = principal == null ? null : ((ActivitiUserDetails) ((Authentication) principal)
				.getPrincipal()).getUserDetail();
		if (detail != null) {
			if (ActivitiUserDetailsService.staticUserService != null) {
				detail = ActivitiUserDetailsService.staticUserService.getByUsername(detail.getUsername());
			}
		}
		return detail;
	}
	public static String getUserId(Principal principal) {
		return principal == null ? "" : principal.getName();
	}

	public static boolean hasAuth(Principal principal, String group) {
		return ((ActivitiUserDetails) ((Authentication) principal).getPrincipal()).hasAuthority(group);
	}

	/**
	 * 
	 * 判断只有一个角色,如删除物料时,广告主可以删除自己的物料, 超级管理员包括广告主权限又可以删除所有人的物料
	 *
	 * @param principal
	 * @param group
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public static boolean hasOnlyAuth(Principal principal, String... group) {
		return ((ActivitiUserDetails) ((Authentication) principal).getPrincipal()).hasOnlyAuthority(group);
	}
	
	public static String getCookieValue(HttpServletRequest request, String keyName) {
		String r = null;
		Cookie[] cookies = request.getCookies();
		if (null != cookies) {
			for (Cookie cookie : cookies) {
				if (keyName.equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return r;
	}
}
