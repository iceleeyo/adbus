package com.pantuo.web.schedule;

import java.util.List;

@lombok.Getter
@lombok.Setter
@lombok.ToString
public class OrderRequest {
	public int oProductId;
	public int oplayNumbers;
	public List<Between> area;
	public List<DayRule> dayRule;
	Type type;

	public static enum Type {
		all, split1, selectTime, select_timeAndNumber;
	}
	 
	//每个包默认播1次
	int packageMaxPlayNumber = 1;
}
