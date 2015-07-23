package com.pantuo.service.impl;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.activiti.engine.IdentityService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.User;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.pantuo.dao.pojo.JpaOrders;
import com.pantuo.dao.pojo.UserDetail;
import com.pantuo.service.ActivitiService;
import com.pantuo.service.MailService;
import com.pantuo.service.OrderService;
import com.pantuo.service.UserServiceInter;
import com.pantuo.util.FreeMarker;
import com.pantuo.util.GlobalMethods;
import com.pantuo.util.LinuxMailService;
import com.pantuo.util.Mail;
import com.pantuo.util.OSinfoUtils;
import com.pantuo.util.OrderIdSeq;
import com.pantuo.util.Pair;
import com.pantuo.util.Request;
import com.pantuo.util.SendMailException;

@Service
@ImportResource("classpath:/properties.xml")
public class MailServiceImpl implements MailService {
	private static Logger log = LoggerFactory.getLogger(MailServiceImpl.class);
	@Autowired
	private UserServiceInter userService;

	@Autowired
	private TaskService taskService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private OrderService orderService;

	@Value("${activiti.smtp.host}")
	private String smtpHost;

	@Value("${activiti.smtp.port}")
	private int smtpPort;

	@Value("${activiti.smtp.mailServerUsername}")
	private String mailServerUsername;

	@Value("${activiti.smtp.mailServerPassword}")
	private String mailServerPassword;

	private Mail getMailService(String toMail) {
		Mail mail = new Mail();
		/*	mail.setTo(toMail);
			mail.setFrom("ad_system@163.com");// 你的邮箱  
			mail.setHost("smtp.163.com");
			mail.setUsername("ad_system@163.com");// 用户  
			mail.setPassword("pantuo");// 密码  
		*/

		/*	mail.setTo(toMail);
			mail.setFrom("adbus@gscopetech.com");// 你的邮箱  
			mail.setHost("smtp.exmail.qq.com");
			mail.setUsername("adbus@gscopetech.com");// 用户  
			mail.setPassword("pantuo1709");// 密码 
		*/
		mail.setTo(toMail);
		mail.setFrom(mailServerUsername);// 你的邮箱  
		mail.setHost(smtpHost);
		mail.setPort(smtpPort);
		mail.setUsername(mailServerUsername);// 用户  
		mail.setPassword(mailServerPassword);// 密码  
		return mail;
	}

	public void sendCanCompareMail(UserDetail u) {

		if (u != null) {//
			String userName = u.getUsername();
			UserDetail userDetail = userService.findByUsername(userName);
			if (userDetail != null && userDetail.getUser() != null
					&& StringUtils.isNoneBlank(userDetail.getUser().getEmail())) {
				Mail mail = getMailService(userDetail.getUser().getEmail());
				mail.setSubject("[北巴广告交易系统]资质审核通过通知");
				mail.setContent(getCompareTemplete(userDetail.getUser().getLastName(),
						u.getUstats().ordinal() == UserDetail.UStats.authentication.ordinal(),
						"http://" + Request.getServerIp()));
				boolean r = mail.sendMail();
				log.info("sennd mail to notify user {} can compre, success: N|Y {}", userName, r);
				if (!r) {
					throw new SendMailException("send mail fail");
				}
			}
		}
	}

	public void sendActivateMail(UserDetail u) {
		String serverIP = Request.getServerIp();
		if (u != null) {//
			String userName = u.getUsername();
			String md5 = userService.getUserUniqCode(userName);
			UserDetail userDetail = userService.findByUsername(userName);
			if (userDetail != null && userDetail.getUser() != null
					&& StringUtils.isNoneBlank(userDetail.getUser().getEmail())) {
				Mail mail = getMailService(userDetail.getUser().getEmail());
				mail.setSubject("[北巴广告交易系统]账号激活通知");
				mail.setContent(getActiviteTemplete(
						userDetail.getUser().getFirstName(),
						String.format(StringUtils.trim("http://" + serverIP + "/user/activate?userId=%s&uuid=%s"),
								u.getUsername(), md5)));
				boolean r = mail.sendMail();
				log.info("sennd mail to notify user {} can compre, success: N|Y {}", userName, r);
			}
		}
	}

	public Pair<Boolean, String> sendRestPwdMail(UserDetail u) {
		String md5 = GlobalMethods.md5Encrypted(u.getUser().getId().getBytes());
		if (StringUtils.isBlank(u.getUser().getEmail())) {
			return new Pair<Boolean, String>(false, "用户未填写邮箱信息,无法通过邮件找回请联系管理员");
		}
		String serverIP = Request.getServerIp();
		;//可能会存在问题
		Mail mail = getMailService(u.getUser().getEmail());
		mail.setSubject("[北巴广告交易系统]找回您的账户密码");
		mail.setContent(getMailTemplete(
				u.getUser().getLastName(),
				String.format(StringUtils.trim("http://" + serverIP + "/user/reset_pwd?userId=%s&uuid=%s"),
						u.getUsername(), md5)));
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

	private String getCompareTemplete(String userId, boolean passed, String context) {
		StringWriter swriter = new StringWriter();
		try {
			//String xmlTemplete = request.getSession().getServletContext().getRealPath("/WEB-INF/ftl/mail_templete");
			String xmlTemplete = getTempletePath("/WEB-INF/ftl/mail_templete");
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

	public String getTempletePath(String templetePath) {
		WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
		ServletContext servletContext = webApplicationContext.getServletContext();
		return servletContext.getRealPath(templetePath);
	}

	private String getActiviteTemplete(String userId, String context) {
		StringWriter swriter = new StringWriter();
		try {
			//String xmlTemplete = request.getSession().getServletContext().getRealPath("/WEB-INF/ftl/mail_templete");
			String xmlTemplete = getTempletePath("/WEB-INF/ftl/mail_templete");
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

	private String getMailTemplete(String userId, String resetPwd) {
		StringWriter swriter = new StringWriter();
		try {
			//			String xmlTemplete = FileHelper.getAbosluteDirectory("/WEB-INF/ftl/mail_templete");
			//String xmlTemplete = request.getSession().getServletContext().getRealPath("/WEB-INF/ftl/mail_templete");
			String xmlTemplete = getTempletePath("/WEB-INF/ftl/mail_templete");
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

	private String getCompleteTemplete(String userId, int orderId, String resetPwd) {
		StringWriter swriter = new StringWriter();
		try {
			//String xmlTemplete = request.getSession().getServletContext().getRealPath("/WEB-INF/ftl/mail_templete");
			String xmlTemplete = getTempletePath("/WEB-INF/ftl/mail_templete");
			FreeMarker hf = new FreeMarker();
			hf.init(xmlTemplete);
			Map<String, String> map = new HashMap<String, String>();
			map.put("userName", userId);
			JpaOrders order = orderService.getJpaOrder(orderId);
			map.put("orderId", String.valueOf(OrderIdSeq.getLongOrderId(order)));
			map.put("resetUrl", resetPwd);
			hf.process(map, "task_templete.ftl", swriter);
		} catch (Exception e) {
			log.error(e.toString());
		}
		return swriter.toString();
	}

	@Override
	public void sendCompleteMail(String userName, Integer orderId) {
		List<Task> tasks = taskService.createTaskQuery().processVariableValueEquals(ActivitiService.ORDER_ID, orderId)
				.orderByTaskCreateTime().desc().list();
		for (Task task : tasks) {
			List<IdentityLink> rs = taskService.getIdentityLinksForTask(task.getId());
			for (IdentityLink identityLink : rs) {
				//如果不是自己 发邮件
				if (!StringUtils.equals(userName, identityLink.getUserId())) {
					if (StringUtils.isNoneBlank(identityLink.getGroupId())) {
						List<User> activitiUser = identityService.createUserQuery()
								.memberOfGroup(identityLink.getGroupId()).list();
						for (User user2 : activitiUser) {
							try {
								Thread.sleep(300);
							} catch (InterruptedException e) {
							}
							String context = getCompleteTemplete(user2.getFirstName(), orderId,
									"http://" + Request.getServerIp() + "/order/myTask/1");
							if (OSinfoUtils.isMacOS() || OSinfoUtils.isWindows()) {
								Mail mail = getMailService(user2.getEmail());
								mail.setSubject("[北巴广告交易系统]待办事项通知");
								mail.setContent(context);
								boolean r = mail.sendMail();
								log.info("sendCompleteMail to{} , success: N|Y {}", user2.getEmail(), r);
								if (!r) {
									throw new SendMailException("send mail fail:" + user2.getEmail());
								}
							} else if (OSinfoUtils.isLinux()) {
								LinuxMailService.sendMail(user2.getEmail(), "[北巴广告交易系统]待办事项通知", context);
								log.info("sendCompleteMail to{} , success: N|Y {true}", user2.getEmail());
							}
						}
					}
				}
			}
		}

	}
}
