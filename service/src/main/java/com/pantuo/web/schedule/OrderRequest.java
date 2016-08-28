package com.pantuo.web.schedule;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

@lombok.Getter
@lombok.Setter
@lombok.ToString
public class OrderRequest {
	public int _productId;
	public int _playNumbers;
	public List<Between> area;
	public List<DayRule> dayRule;
	Type type;

	public static enum Type {
		all, split1, selectTime, select_timeAndNumber;
	}

	@lombok.Getter
	@lombok.Setter
	@lombok.ToString
	class Between {
		@DateTimeFormat(pattern = "yyyy-MM-dd")  
		Date _startDate, _endDate;
	}

	@lombok.Getter
	@lombok.Setter
	@lombok.ToString
	class DayRule {
		@DateTimeFormat(pattern = "HH:mm")  
		Date _startTime, _endTime;
		//播放次数
		int _playNumber;
	}

	//每个包默认播1次
	int packageMaxPlayNumber = 1;
}
