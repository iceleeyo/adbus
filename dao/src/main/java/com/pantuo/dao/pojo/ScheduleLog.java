package com.pantuo.dao.pojo;

import javax.persistence.*;
import java.util.Date;

/**
 * @author tliu
 *
 * 排期日志
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"city", "day", "orderId"}))
public class ScheduleLog extends CityEntity {
    public static enum Status {
        scheduling,
        scheduled,
        racing,     //有订单正在排期，需要等待
        duplicate,  //错误同一个订单重复排期
        failed
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private Status status = Status.scheduling;
    private Date day;
    private int orderId;
    private String description;

    public ScheduleLog(int city, Date day, int orderId) {
        super(city);
        this.day = day;
        this.orderId = orderId;
    }

    public ScheduleLog(int city, Date day, int orderId, Status status, String description) {
        super(city);
        this.day = day;
        this.orderId = orderId;
        this.status = status;
        this.description = description;
    }

    public ScheduleLog() {
        super();
    }

    public long getId() {
        return id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    @Override
    public String toString() {
        return "ScheduleLog{" +
                ", orderId=" + orderId +
                ", day=" + day +
                ", status=" + status +
                "description='" + description + '\'' +
                '}';
    }
}