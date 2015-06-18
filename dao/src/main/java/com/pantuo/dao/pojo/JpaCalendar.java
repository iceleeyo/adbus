package com.pantuo.dao.pojo;

import com.pantuo.util.DateUtil;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

/**
 * @author tliu
 *
 * 记录日历，用于辅助生成报表
 */
@Entity
@Table(name="calendar", uniqueConstraints = {@UniqueConstraint(name="calendar_uniq", columnNames = {"day"})},
indexes = {@Index(name="calendar_idx", columnList = "day, year, month", unique = true)})
public class JpaCalendar extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private Date day;
    private int week;   //in year
    private int year;
    private int month;

    public JpaCalendar() {
        //for serialization
    }

    public JpaCalendar(Date day) {
        Calendar cal = DateUtil.newCalendar();
        cal.setTime(day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        this.day = cal.getTime();
        this.month = cal.get(Calendar.MONTH);
        this.week = cal.get(Calendar.WEEK_OF_YEAR);
        this.year = cal.get(Calendar.YEAR);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
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
}