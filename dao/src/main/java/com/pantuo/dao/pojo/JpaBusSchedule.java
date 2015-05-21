package com.pantuo.dao.pojo;

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
@Table(name="bus_schedule")
public class JpaBusSchedule extends CityEntity{
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name = "busId")
    private JpaBus bus;

    private Date publishFrom;      //刊期开始, inclusive
    private Date publishTo;        //刊期结束, exclusive

	public JpaBusSchedule(){

	}

	public JpaBusSchedule(JpaBus bus, Date publishFrom, Date publishTo) {
		super(bus.getCity());
        this.bus = bus;
        this.publishFrom = DateUtil.trimDate(publishFrom);
        this.publishTo = DateUtil.trimDate(publishTo);
	}


    public JpaBusSchedule(JpaBus bus, Date publishFrom, int days) {
        super(bus.getCity());
        this.bus = bus;
        this.publishFrom = DateUtil.trimDate(publishFrom);
        Calendar c = DateUtil.newCalendar();
        c.setTime(this.publishFrom);
        c.add(Calendar.DATE, days);
        this.publishTo = c.getTime();
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

    public Date getPublishFrom() {
        return publishFrom;
    }

    public void setPublishFrom(Date publishFrom) {
        this.publishFrom = publishFrom;
    }

    public Date getPublishTo() {
        return publishTo;
    }

    public void setPublishTo(Date publishTo) {
        this.publishTo = publishTo;
    }
}
