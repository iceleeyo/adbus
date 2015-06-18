package com.pantuo.pojo.highchart;

import java.io.Serializable;

public enum SeriesType implements Serializable {

    TIMESLOT(1, "时段", YType.TIME_COUNT),
    LONG_TIMESLOT(3, "时段", YType.LONG_TIME_COUNT),
    TIMESLOT_PERCENT(5, "时段占比", YType.PERCENT),
    BUSCOUNT_PERCENT(7, "巴士占比", YType.BUS_COUNT),
    ;
	
	final private int id;
	final private String desc;
	final private YType yType;

	public String getDesc() {
		return desc;
	}

	private SeriesType(int id, String desc, YType yType) {
		this.id = id;
		this.desc = desc;
		this.yType = yType;
	}
	
	public int getId() {
		return id;
	}
	
	public YType getyType() {
		return yType;
	}
	
	public static SeriesType getSeriesType(int id) {
        return SeriesType.values()[id - 1];
	}
}
