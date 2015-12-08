package com.pantuo.web.view;

import java.util.concurrent.atomic.AtomicInteger;

public class LineDateCount {

	public int line_id;
	public AtomicInteger today = new AtomicInteger(0);//今天的上刑数量
	public AtomicInteger month_1_day = new AtomicInteger(0);//1个月后天的今天  上刑量
	public AtomicInteger month_2_day = new AtomicInteger(0);
	public AtomicInteger month_3_day = new AtomicInteger(0);

	public LineDateCount(int line_id) {
		super();
		this.line_id = line_id;
	}

	public LineDateCount() {
	}

	public int getLine_id() {
		return line_id;
	}

	public void setLine_id(int line_id) {
		this.line_id = line_id;
	}

	public AtomicInteger getToday() {
		return today;
	}

	public void setToday(AtomicInteger today) {
		this.today = today;
	}

	public AtomicInteger getMonth_1_day() {
		return month_1_day;
	}

	public void setMonth_1_day(AtomicInteger month_1_day) {
		this.month_1_day = month_1_day;
	}

	public AtomicInteger getMonth_2_day() {
		return month_2_day;
	}

	public void setMonth_2_day(AtomicInteger month_2_day) {
		this.month_2_day = month_2_day;
	}

	public AtomicInteger getMonth_3_day() {
		return month_3_day;
	}

	public void setMonth_3_day(AtomicInteger month_3_day) {
		this.month_3_day = month_3_day;
	}

}
