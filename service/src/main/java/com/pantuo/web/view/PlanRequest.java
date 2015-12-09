package com.pantuo.web.view;

public class PlanRequest {
	public String level = null;
	public Boolean doubleChecker = false;
	public int days = 0;
	public String msg;

	public PlanRequest(String level, Boolean doubleChecker, int days) {
		this.level = level;
		this.doubleChecker = doubleChecker;
		this.days = days;
	}

	public PlanRequest(String msg) {
		super();
		this.msg = msg;
	}

	public PlanRequest() {
		super();
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public Boolean getDoubleChecker() {
		return doubleChecker;
	}

	public void setDoubleChecker(Boolean doubleChecker) {
		this.doubleChecker = doubleChecker;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
