package com.pantuo.service;

import javax.servlet.http.HttpServletRequest;

import com.pantuo.dao.pojo.UserDetail;
import com.pantuo.util.Pair;

public interface MailService {
	/**
	 * 
	 * 重置密码
	 *
	 * @param u
	 * @param request
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public Pair<Boolean, String> addUserMailReset(UserDetail u, HttpServletRequest request);
	
	
	
	public void sendCanCompareMail(UserDetail u, HttpServletRequest request);	
}
