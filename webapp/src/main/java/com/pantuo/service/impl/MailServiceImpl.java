package com.pantuo.service.impl;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pantuo.dao.pojo.UserDetail;
import com.pantuo.service.MailService;
import com.pantuo.service.UserServiceInter;
import com.pantuo.util.FreeMarker;
import com.pantuo.util.GlobalMethods;
import com.pantuo.util.Mail;
import com.pantuo.util.Pair;
import com.pantuo.util.Request;

@Service
public class MailServiceImpl implements MailService {
	private static Logger log = LoggerFactory.getLogger(MailServiceImpl.class);
	@Autowired
	private UserServiceInter userService;

	public void sendCanCompareMail(UserDetail u, HttpServletRequest request) {

		if (u != null) {//
			String userName = u.getUsername();
			UserDetail userDetail = userService.findByUsername(userName);
			if (userDetail != null && userDetail.getUser() != null
					&& StringUtils.isNoneBlank(userDetail.getUser().getEmail())) {
				Mail mail = getMailService(userDetail);
				mail.setSubject("[北巴广告交易系统]资质审核通过通知");
				mail.setContent(getCompareTemplete(userDetail.getUser().getLastName(),
						u.getUstats().ordinal() == UserDetail.UStats.authentication.ordinal(),
						"http://" + Request.getServerIp(), request));
				boolean r = mail.sendMail();
				log.info("sennd mail to notify user {} can compre, success: N|Y {}", userName, r);
			}
		}
	}
	public void sendActivateMail(UserDetail u, HttpServletRequest request) {
		String serverIP = Request.getServerIp();
		if (u != null) {//
			String userName = u.getUsername();
			String md5=userService.getUserUniqCode(userName);
			UserDetail userDetail = userService.findByUsername(userName);
			if (userDetail != null && userDetail.getUser() != null
					&& StringUtils.isNoneBlank(userDetail.getUser().getEmail())) {
				Mail mail = getMailService(userDetail);
				mail.setSubject("[北巴广告交易系统]账号激活通知");
				mail.setContent(getActiviteTemplete(userDetail.getUser().getFirstName(),
						String.format(StringUtils.trim("http://" + serverIP + "/user/activate?userId=%s&uuid=%s"),
								u.getUsername(), md5), request));
				boolean r = mail.sendMail();
				log.info("sennd mail to notify user {} can compre, success: N|Y {}", userName, r);
			}
		}
	}

	public Pair<Boolean, String> addUserMailReset(UserDetail u, HttpServletRequest request) {
		String md5 = GlobalMethods.md5Encrypted(u.getUser().getId().getBytes());
		if (StringUtils.isBlank(u.getUser().getEmail())) {
			return new Pair<Boolean, String>(false, "用户未填写邮箱信息,无法通过邮件找回请联系管理员");
		}
		String serverIP = Request.getServerIp();
		;//可能会存在问题
		Mail mail = getMailService(u);
		mail.setSubject("[北巴广告交易系统]找回您的账户密码");
		mail.setContent(getMailTemplete(
				u.getUser().getLastName(),
				String.format(StringUtils.trim("http://" + serverIP + "/user/reset_pwd?userId=%s&uuid=%s"),
						u.getUsername(), md5), request));
		Pair<Boolean, String> resultPair = null;
		String email = u.getUser().getEmail();
		String regex = "(\\w{3})(\\w+)(\\w{3})(@\\w+)";
		String mailto = email.replaceAll(regex, "$1..$3$4");
		if (mail.sendMail()) {
			resultPair = new Pair<Boolean, String>(true, "您的申请已提交成功，请查看您的" + mailto + "邮箱。");
		} else {
			resultPair = new Pair<Boolean, String>(false, "往" + mailto + "发邮件操作失败，轻稍后重新尝试！");
		}
		return resultPair;
	}

	private Mail getMailService(UserDetail u) {
		Mail mail = new Mail();
		mail.setTo(u.getUser().getEmail());
		mail.setFrom("ad_system@163.com");// 你的邮箱  
		mail.setHost("smtp.163.com");
		mail.setUsername("ad_system@163.com");// 用户  
		mail.setPassword("pantuo");// 密码  
		return mail;
	}

	private String getCompareTemplete(String userId, boolean passed, String context, HttpServletRequest request) {
		StringWriter swriter = new StringWriter();
		try {
			String xmlTemplete = request.getSession().getServletContext().getRealPath("/WEB-INF/ftl/mail_templete");
			FreeMarker hf = new FreeMarker();
			hf.init(xmlTemplete);
			Map<String, String> map = new HashMap<String, String>();
			map.put("userName", userId);
			map.put("resetUrl", context);
			map.put("passed", BooleanUtils.toStringYesNo(passed));
			hf.process(map, "compare_templete.ftl", swriter);
		} catch (Exception e) {
			log.error(e.toString());
		}
		return swriter.toString();
	}
	private String getActiviteTemplete(String userId,String context, HttpServletRequest request) {
		StringWriter swriter = new StringWriter();
		try {
			String xmlTemplete = request.getSession().getServletContext().getRealPath("/WEB-INF/ftl/mail_templete");
			FreeMarker hf = new FreeMarker();
			hf.init(xmlTemplete);
			Map<String, String> map = new HashMap<String, String>();
			map.put("userName", userId);
			map.put("activatetUrl", context);
			hf.process(map, "activate_templete.ftl", swriter);
		} catch (Exception e) {
			log.error(e.toString());
		}
		return swriter.toString();
	}

	private String getMailTemplete(String userId, String resetPwd, HttpServletRequest request) {
		StringWriter swriter = new StringWriter();
		try {
			//			String xmlTemplete = FileHelper.getAbosluteDirectory("/WEB-INF/ftl/mail_templete");
			String xmlTemplete = request.getSession().getServletContext().getRealPath("/WEB-INF/ftl/mail_templete");
			FreeMarker hf = new FreeMarker();
			hf.init(xmlTemplete);
			Map<String, String> map = new HashMap<String, String>();
			map.put("userName", userId);
			map.put("resetUrl", resetPwd);
			hf.process(map, "mail_templete.ftl", swriter);
		} catch (Exception e) {
			log.error(e.toString());
		}
		return swriter.toString();
	}
}
