package com.pantuo.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pantuo.dao.pojo.UserDetail;
import com.pantuo.service.UserServiceInter;

/**
 * Created by tliu on 3/22/15.
 * modify by impanxh on 7/21/15.
 */
@Service
public class ActivitiUserDetailsService implements UserDetailsService {
	@Autowired
	private UserServiceInter userService;

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDetail user = userService.getByUsername(username);
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
