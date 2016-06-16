package com.pantuo.dao.pojo;

import javax.persistence.*;
import java.util.Date;

/**
 * @author tliu
 *
 * 排期
 */
@Entity
@Table(name="schedule")
public class JpaSchedule extends CityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int userId;     //用户名
    private int orderId;
    private Date date;      //播出日期
    private int timeslotId; //排期表ID
    private Date playTime;  //开播时间
    private long duration;  //广告时长(s)

    public JpaSchedule() {
        //for serialization
    }

    public JpaSchedule(int city, int userId, int orderId, Date date, int timeslotId, Date playTime, long duration) {
        super(city);
        this.userId = userId;
        this.orderId = orderId;
        this.date = date;
        this.timeslotId = timeslotId;
        this.playTime = playTime;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getTimeslotId() {
        return timeslotId;
    }

    public void setTimeslotId(int timeslotId) {
        this.timeslotId = timeslotId;
    }

    public Date getPlayTime() {
        return playTime;
    }

    public void setPlayTime(Date playTime) {
        this.playTime = playTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "JpaSchedule{" +
                ", userId=" + userId +
                ", orderId=" + orderId +
                ", date=" + date +
                ", timeslotId=" + timeslotId +
                ", playTime=" + playTime +
                ", duration=" + duration +
                '}';
    }
}