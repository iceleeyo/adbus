package com.pantuo.web.schedule;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

@lombok.Getter
@lombok.Setter
@lombok.ToString
class DayRule {
	@DateTimeFormat(pattern = "HH:mm")
	Date  oStartTime;
	@DateTimeFormat(pattern = "HH:mm")
	Date oEndTime;
	//播放次数
	int oPlayNumber;
}