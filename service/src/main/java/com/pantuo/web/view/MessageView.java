package com.pantuo.web.view;

import java.util.Date;

import com.pantuo.dao.pojo.JpaMessage;
import com.pantuo.dao.pojo.JpaOrders;
import com.pantuo.util.OrderIdSeq;

public class MessageView {

	public JpaMessage mainView;
	long longOrderId;
	Date createId;

	public MessageView(JpaMessage mainView, int longOrderId, JpaOrders order) {
		this.mainView = mainView;
		if (order != null) {
			this.createId = order.getCreated();
			this.longOrderId = OrderIdSeq.getIdFromDate(longOrderId, order.getCreated());
		}
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

	public Date getCreateId() {
		return createId;
	}

	public void setCreateId(Date createId) {
		this.createId = createId;
	}

}
