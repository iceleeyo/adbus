package com.pantuo.web.schedule;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SchedUltResult {
	private static Logger log = LoggerFactory.getLogger(SchedUltResult.class);
	public String reqType;
	public String msg;
	public boolean isScheduled;
	Date notSchedultDay;
	public boolean isFrist;
	public boolean isLock = false;//是否有其他用户正在排期
	public boolean isScheduleOver = false;

	int slotNum;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean isScheduled() {
		return isScheduled;
	}

	public void setScheduled(boolean isScheduled) {
		this.isScheduled = isScheduled;
	}

	public Date getNotSchedultDay() {
		return notSchedultDay;
	}

	public void setNotSchedultDay(Date notSchedultDay) {
		this.notSchedultDay = notSchedultDay;
	}

	public boolean isFrist() {
		return isFrist;
	}

	public void setFrist(boolean isFrist) {
		this.isFrist = isFrist;
	}

	public SchedUltResult(String msg, Boolean isScheduled, Date notSchedultDay, Boolean isFrist) {
		this.msg = msg;
		this.isScheduled = isScheduled;
		this.notSchedultDay = notSchedultDay;
		this.isFrist = isFrist;
	}

	public SchedUltResult(String msg, Boolean isScheduled, int slotNum) {
		this.msg = msg;
		this.isScheduled = isScheduled;
		this.slotNum=slotNum;
	}

	public boolean isLock() {
		return isLock;
	}

	public void setLock(boolean isLock) {
		this.isLock = isLock;
	}

	public int getSlotNum() {
		return slotNum;
	}

	public void setSlotNum(int slotNum) {
		this.slotNum = slotNum;
	}

	/**
	 * @see java.lang.Object#toString()
	 * @since pantuo 1.0-SNAPSHOT
	 */
	@Override
	public String toString() {
		return "SchedUltResult [reqType=" + reqType + ", msg=" + msg + ", isScheduled=" + isScheduled
				+ ", notSchedultDay=" + notSchedultDay + ", isFrist=" + isFrist + "]";
	}

	public String getReqType() {
		return reqType;
	}

	public void setReqType(String reqType) {
		this.reqType = reqType;
	}

	public boolean isScheduleOver() {
		return isScheduleOver;
	}

	public void setScheduleOver(boolean isScheduleOver) {
		this.isScheduleOver = isScheduleOver;
	}


}