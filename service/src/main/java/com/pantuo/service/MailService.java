package com.pantuo.service;

import org.activiti.engine.impl.task.TaskDefinition;

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
	public Pair<Boolean, String> sendRestPwdMail(UserDetail user);

	/**
	 * 
	 * 资质审核邮件通知
	 *
	 * @param u
	 * @param request
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public void sendCanCompareMail(UserDetail user);

	public void sendActivateMail(UserDetail user);

	public void sendCompleteMail(MailTask task);

	public void sendNormalMail(String tomail, String subject, String content);
}
