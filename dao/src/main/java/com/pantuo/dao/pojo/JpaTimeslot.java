package com.pantuo.dao.pojo;

import com.pantuo.util.DateUtil;

import javax.persistence.*;
import java.util.Date;

/**
 * @author tliu
 *
 * 时段
 */
@Entity
@Table(name="timeslot")
public class JpaTimeslot extends CityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;
    private Date startTime;
    private long duration;
    private boolean peak;
    private boolean enabled = true;

    public JpaTimeslot() {
        //for serialization
    }

    public JpaTimeslot(int city, String name, Date startTime, long duration, boolean peak) {
        super(city);
        this.name = name;
        this.startTime = startTime;
        this.duration = duration;
        this.peak = peak;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public boolean isPeak() {
        return peak;
    }

    public void setPeak(boolean peak) {
        this.peak = peak;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getStartTimeStr() {
        if (startTime == null)
            return "--";
        return DateUtil.shortDf.get().format(startTime);
//        sb.append(" - ");
//        Date end = new Date(startTime.getTime() + duration * 1000);
//        sb.append(shortDf.get().format(end));
    }

    public String getDurationStr() {
        return DateUtil.toShortStr(duration);
    }


    @Override
    public String toString() {
        return "JpaTimeslot{" +
                "name='" + name + '\'' +
                ", startTime=" + startTime +
                ", duration=" + duration +
                ", peak=" + peak +
                ", enabled=" + enabled +
                '}';
    }
}