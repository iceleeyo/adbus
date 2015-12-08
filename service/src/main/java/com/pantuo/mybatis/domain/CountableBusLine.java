package com.pantuo.mybatis.domain;

/**
 * @author tliu
 *
 * BusLine with bus count
 */
public class CountableBusLine extends BusLine {
    private int busCount;

    public int getBusCount() {
        return busCount;
    }

    public void setBusCount(int busCount) {
        this.busCount = busCount;
    }
}
