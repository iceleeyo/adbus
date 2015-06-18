package com.pantuo.dao.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pantuo.util.DateUtil;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 车辆刊期信息
 */

@Entity
@Table(name="bus_schedule",
        uniqueConstraints = {@UniqueConstraint(name="bus_schedule_uniq", columnNames = {"busId", "orderId", "startDay", "endDay"})})
public class JpaBusSchedule extends CityEntity{
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name = "busId")
    private JpaBus bus;

    @ManyToOne
    @JoinColumn(name = "orderId")
    @JsonIgnore
    private JpaOrders order;

    private Date startDay;

    private Date endDay;

	public JpaBusSchedule(){

	}

	public JpaBusSchedule(JpaBus bus, JpaOrders order) {
		super(bus.getCity());
        this.bus = bus;
        this.order = order;
        this.startDay = DateUtil.trimDate(order.getStartTime());
        this.endDay = DateUtil.dateAdd(this.startDay, order.getProduct().getDays());
	}


    public JpaBusSchedule(int city, int busId, int orderId, Date startDay, Date endDay) {
        super(city);
        this.bus = new JpaBus(city, busId);
        this.order = new JpaOrders(city, orderId);
        this.startDay = DateUtil.trimDate(startDay);
        this.endDay = DateUtil.trimDate(endDay);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public JpaBus getBus() {
        return bus;
    }

    public void setBus(JpaBus bus) {
        this.bus = bus;
    }

    public JpaOrders getOrder() {
        return order;
    }

    public void setOrder(JpaOrders order) {
        this.order = order;
    }

    public Date getStartDay() {
        return startDay;
    }

    public void setStartDay(Date startDay) {
        this.startDay = startDay;
    }

    public Date getEndDay() {
        return endDay;
    }

    public void setEndDay(Date endDay) {
        this.endDay = endDay;
    }
}
