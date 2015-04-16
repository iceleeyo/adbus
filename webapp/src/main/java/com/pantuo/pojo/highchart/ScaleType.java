package com.pantuo.pojo.highchart;

public enum ScaleType {
	PERCENT ("百分比"), USER_COUNT("用户数"), TIME_COUNT("时长");

    final private String desc;
    private ScaleType (String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
