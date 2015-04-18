package com.pantuo.mybatis.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pantuo.Reportable;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 * @author tliu
 */
public class TimeslotReport implements Serializable, Reportable {
    private static final ObjectMapper mapper = new ObjectMapper();
    static {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @JsonIgnore
    private Date day;
    private Long dayInMills;
    private Integer year;
    private Integer month;
    private Integer hour;
    private Long remain;
    private Long size;
    private Long paid;
    private Long notPaid;
    private Integer industryId;

    public TimeslotReport() {}

    public TimeslotReport(Date day, long remain, long size) {
        setDay(day);
        this.remain = remain;
        this.size = size;
    }

    public TimeslotReport(int industryId, Date day, long paid, long notPaid, long size) {
        this(day, paid, notPaid, size);
        this.industryId = industryId;
    }

    public TimeslotReport(Date day, long paid, long notPaid, long size) {
        setDay(day);
        this.paid = paid;
        this.notPaid = notPaid;
        this.size = size;
        this.remain = size - paid - notPaid;
    }

    public TimeslotReport(int year, int month, long remain, long size) {
        this.year = year;
        this.month = month;
        this.remain = remain;
        this.size = size;
    }

    public TimeslotReport(int hour, long remain, long size) {
        this.hour = hour;
        this.remain = remain;
        this.size = size;
    }

    public Long getOrdered () {
        if (remain == null || size == null)
            return null;
        return size - remain;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
        if (this.day != null)
            this.dayInMills = day.getTime();
    }

    public Long getRemain() {
        return remain;
    }

    public void setRemain(Long remain) {
        this.remain = remain;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Long getDayInMills() {
        return dayInMills;
    }

    public void setDayInMills(long dayInMills) {
        this.dayInMills = dayInMills;
    }

    public Long getPaid() {
        return paid;
    }

    public void setPaid(Long paid) {
        this.paid = paid;
    }

    public Long getNotPaid() {
        return notPaid;
    }

    public void setNotPaid(Long notPaid) {
        this.notPaid = notPaid;
    }

    public Integer getIndustryId() {
        return industryId;
    }

    public void setIndustryId(Integer industryId) {
        this.industryId = industryId;
    }

    public TimeslotReport clone() {
        TimeslotReport r = new TimeslotReport();
        BeanUtils.copyProperties(this, r);
        return r;
    }

    @Override
    public String toString(String xValue, String yKey) {
        StringBuilder sb = new StringBuilder("{");
        if (xValue != null) {
            sb.append("x:").append(xValue).append(",");
        }
        Map<String, Object> props = mapper.convertValue(this, Map.class);
        Iterator<Map.Entry<String, Object>> iter = props.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, Object> e = iter.next();
            String key = e.getKey();
            if (key.equals("dayInMills"))
                key = "day";
            if (key.equals(yKey)) {
                key = "y";
            }
            sb.append(key).append(":");
            if (e.getValue() instanceof Number) {
                sb.append(e.getValue());
            } else {
                sb.append("\"").append(e.getValue()).append("\"");
            }
            sb.append(",");
        }
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 1);
        }
        sb.append("}");
        return sb.toString();
    }
}
