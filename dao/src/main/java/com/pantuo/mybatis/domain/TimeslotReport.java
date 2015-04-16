package com.pantuo.mybatis.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * @author tliu
 */
public class TimeslotReport implements Serializable {
    private Date day;
    private int year = -1;
    private int month = -1;
    private long remain;
    private long size;

    public TimeslotReport() {}

    public TimeslotReport(Date day, long remain, long size) {
        this.day = day;
        this.remain = remain;
        this.size = size;
    }

    public TimeslotReport(int year, int month, long remain, long size) {
        this.year = year;
        this.month = month;
        this.remain = remain;
        this.size = size;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public long getRemain() {
        return remain;
    }

    public void setRemain(long remain) {
        this.remain = remain;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    @Override
    public String toString() {
        return "{" +
                "year:" + year +
                ", month:" + month +
                ", y:" + remain +
                ", size: " + size +
                (day == null ? "" : ", day:" + day.getTime()) +
                '}';
    }
}
