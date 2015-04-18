package com.pantuo.web.view;

public class SectionView {

	public String date;
	public String time;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public SectionView(String date, String time) {
		super();
		this.date = date;
		this.time = time;
	}

}
