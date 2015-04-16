package com.pantuo.pojo.highchart;

import java.io.Serializable;

public enum SeriesType implements Serializable {

    TIMESLOT(1, "时段", ScaleType.TIME_COUNT),
    PEAK_TIMESLOT(2, "高峰时段", ScaleType.TIME_COUNT),
    TIMESLOT_PERCENT(3, "时段占比", ScaleType.PERCENT);
	
	final private int id;
	final private String desc;
	final private ScaleType scale;

	public String getDesc() {
		return desc;
	}

	private SeriesType(int id, String desc, ScaleType scale) {
		this.id = id;
		this.desc = desc;
		this.scale = scale;
	}
	
	public int getId() {
		return id;
	}
	
	public ScaleType getScaleType() {
		return scale;
	}
	
	public static SeriesType getSeriesType(int id) {
        return SeriesType.values()[id - 1];
	}
}
