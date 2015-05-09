package com.pantuo.web.view;

import java.util.Date;

import com.pantuo.dao.pojo.JpaMessage;
import com.pantuo.util.OrderIdSeq;

public class MessageView {

	public JpaMessage mainView;
	long longOrderId;
	Date createId;

	public MessageView(JpaMessage mainView, int longOrderId, Date createId) {
		this.mainView = mainView;

		this.createId = createId;

		if (createId != null) {
			this.longOrderId = OrderIdSeq.getIdFromDate(longOrderId, createId);
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
