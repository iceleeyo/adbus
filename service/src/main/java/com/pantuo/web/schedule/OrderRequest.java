package com.pantuo.web.schedule;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

@lombok.Getter
@lombok.Setter
public class OrderRequest {
	public int productId;
	public int playNumbers;
	public List<Between> area;
	public List<DayRule> dayRule;
	Type type;

	public static enum Type {
		all, split1, selectTime, select_timeAndNumber;
	}

	@lombok.Getter
	@lombok.Setter
	class Between {
		@DateTimeFormat(pattern = "yyyy-MM-dd")  
		Date startDate, endDate;
	}

	@lombok.Getter
	@lombok.Setter
	class DayRule {
		@DateTimeFormat(pattern = "HH:mm")  
		Date startTime, endTime;
		//播放次数
		int playNumber;
	}

	//每个包默认播1次
	int packageMaxPlayNumber = 1;
}
