package com.pantuo.dao.pojo;

import javax.persistence.*;
import java.util.Date;

/**
 * @author tliu
 *
 * 时段
 */
@Entity
@Table(name="timeslot")
public class JpaTimeslot extends BaseEntity {
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

    public JpaTimeslot(String name, Date startTime, long duration, boolean peak) {
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