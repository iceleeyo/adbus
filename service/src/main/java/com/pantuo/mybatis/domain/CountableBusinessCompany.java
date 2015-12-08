package com.pantuo.mybatis.domain;

/**
 * @author tliu
 *
 * BusinessCompany with bus count
 */
public class CountableBusinessCompany extends BusinessCompany {
    private int busCount;

    public int getBusCount() {
        return busCount;
    }

    public void setBusCount(int busCount) {
        this.busCount = busCount;
    }
}
