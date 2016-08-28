package com.pantuo.web.schedule;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

@lombok.Getter
@lombok.Setter
@lombok.ToString
public class Between {
	@DateTimeFormat(pattern = "yyyy-MM-dd")  
	Date oStartDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd") 
	Date oEndDate;
}