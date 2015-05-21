package com.pantuo.mybatis.domain;

/**
 * @author tliu
 *
 * BusModel with bus count
 */
public class CountableBusModel extends BusModel {
    private int busCount;

    public int getBusCount() {
        return busCount;
    }

    public void setBusCount(int busCount) {
        this.busCount = busCount;
    }
}
