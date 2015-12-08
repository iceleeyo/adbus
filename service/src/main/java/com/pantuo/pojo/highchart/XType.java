package com.pantuo.pojo.highchart;

public enum XType {
	HOUR("小时"), DATE ("日期"), WEEK("周"), MONTH("月");

    final private String desc;
    private XType(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
