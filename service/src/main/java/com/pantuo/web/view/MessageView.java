package com.pantuo.web.view;

import java.util.Date;

import com.pantuo.dao.pojo.JpaMessage;
import com.pantuo.dao.pojo.JpaOrders;
import com.pantuo.util.OrderIdSeq;

public class MessageView {

	public JpaMessage mainView;
	long longOrderId;
	Date createId;
	double TotalMoney;//金额
	long runningNum;//交易流水号
	String msg;

	public MessageView(JpaMessage mainView, int longOrderId, JpaOrders order) {
		this.mainView = mainView;
		if (order != null) {
			this.createId = order.getCreated();
			this.longOrderId = OrderIdSeq.getIdFromDate(longOrderId, order.getCreated());
		}
	}


	public MessageView(double totalMoney, long runningNum, String msg) {
		super();
		TotalMoney = totalMoney;
		this.runningNum = runningNum;
		this.msg = msg;
	}

	public JpaMessage getMainView() {
		return mainView;
	}

	public void setMainView(JpaMessage mainView) {
		this.mainView = mainView;
	}

	public long getLongOrderId() {
		return longOrderId;
	}

	public void setLongOrderId(long longOrderId) {
		this.longOrderId = longOrderId;
	}

	public double getTotalMoney() {
		return TotalMoney;
	}

	public void setTotalMoney(double totalMoney) {
		TotalMoney = totalMoney;
	}

	public long getRunningNum() {
		return runningNum;
	}

	public void setRunningNum(long runningNum) {
		this.runningNum = runningNum;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Date getCreateId() {
		return createId;
	}

	public void setCreateId(Date createId) {
		this.createId = createId;
	}

}
