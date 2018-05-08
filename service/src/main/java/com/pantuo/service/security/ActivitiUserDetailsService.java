package com.pantuo.service.security;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pantuo.dao.pojo.UserDetail;
import com.pantuo.dao.pojo.UserDetail.UType;
import com.pantuo.service.UserServiceInter;

/**
 * Created by tliu on 3/22/15.
 * modify by impanxh on 7/21/15.
 */
@Service
public class ActivitiUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserServiceInter userService;      
	
	 
	public static UserServiceInter staticUserService;
	
	
	
	@Autowired
	private HttpServletRequest request;//重点在里这里  需要xml配置lister

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		String referer = request.getHeader("referer");
		if (StringUtils.contains(referer, "/login") && !StringUtils.contains(referer, "/login_bus")) {
			//检查验证码
			String checkCode = request.getParameter("code");
			if (StringUtils.isBlank(checkCode)) {
				throw new UsernameNotFoundException("请输入验证码!");
			}
			String code = request.getSession().getAttribute("code").toString();
			if (!StringUtils.equals(StringUtils.lowerCase(checkCode), StringUtils.lowerCase(code))
					&& StringUtils.isNoneBlank(code)) {
				throw new UsernameNotFoundException("验证码不符!");
			}
		}
		//以下是认证部分
		UserDetail user = userService.getByUsername(username);
		staticUserService = userService;
		if (user == null)
			throw new UsernameNotFoundException("用户名不存在!");
		if (user.getIsActivate() == 0) {
			throw new UsernameNotFoundException("帐户未激活,请先进行邮件激活!");
		}
		if (user.getUser() == null)
			throw new UsernameNotFoundException("fail to find Activiti user for username " + username);
		return new ActivitiUserDetails(user);
	}
}
