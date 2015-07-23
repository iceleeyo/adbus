package com.pantuo.service;

import com.pantuo.dao.pojo.UserDetail;

public class MailTask {

	public static enum Type {
		sendRestPwdMail("发送重置密码邮件"), sendCanCompareMail("发送资质审核通过邮件"), sendActivateMail("发送激活成功"), sendCompleteMail(
				"待办事项完成通知下一个节点", 1);

		private final String name;
		private int ptype = 0;//发邮件时的参数类型

		private Type(String name) {
			this.name = name;
		}

		private Type(String name, int ptype) {
			this.name = name;
			this.ptype = ptype;
		}

		public String getTypeName() {
			return name;
		}

		public int getPtype() {
			return ptype;
		}
	}

	public MailTask(String userName, int orderId, Enum<Type> type) {
		this.userName = userName;
		this.orderId = orderId;
		this.mailType = type;
	}

	public MailTask(UserDetail user, Enum<Type> type) {
		this.user = user;
		this.mailType = type;
	}

	private String userName;
	private int orderId;
	private UserDetail user;
	private Enum<Type> mailType;
	private int reSendCount = 0;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public UserDetail getUser() {
		return user;
	}

	public void setUser(UserDetail user) {
		this.user = user;
	}

	public Enum<Type> getMailType() {
		return mailType;
	}

	public void setMailType(Enum<Type> mailType) {
		this.mailType = mailType;
	}

	public int getReSendCount() {
		return reSendCount;
	}

	public void setReSendCount(int reSendCount) {
		this.reSendCount = reSendCount;
	}

	public boolean isReOver() {
		if (reSendCount >= 5) {
			return true;
		} else {
			this.reSendCount = reSendCount + 1;
			return false;
		}

	}

}
