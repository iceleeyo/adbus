package com.pantuo.web.view;


import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class ScheduleRequest {
	
	@DateTimeFormat(pattern = "HH:mm")  
	private Date createTime;

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}  
	
	

}
