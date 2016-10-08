package com.pantuo.vo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pantuo.dao.pojo.JpaTimeslot;
import com.pantuo.util.DateUtil;

public class ScheduleView {
	JpaTimeslot timeslot;
    private String bname;
    private Date startTime;
    private long duration;
    private int num;
    private int bsize;
    private String day;
    Map<String/*date*/, Integer> map=new HashMap<String, Integer>();
    Map<String/*date*/, SchObj> map2=new HashMap<String, SchObj>();
	public ScheduleView() {
	}
	public String getBname() {
		return bname;
	}
	public void setBname(String bname) {
		this.bname = bname;
	}
	public int getBsize() {
		return bsize;
	}
	public void setBsize(int bsize) {
		this.bsize = bsize;
	}
	
	 public JpaTimeslot getTimeslot() {
		return timeslot;
	}
	public void setTimeslot(JpaTimeslot timeslot) {
		this.timeslot = timeslot;
	}
	
	public Map<String, Integer> getMap() {
		return map;
	}
	public class SchObj{
		
		public SchObj(int num, int del) {
			this.num = num;
			this.del = del;
		}
		int num;
		int del;
		public int getNum() {
			return num;
		}
		public void setNum(int num) {
			this.num = num;
		}
		public int getDel() {
			return del;
		}
		public void setDel(int del) {
			this.del = del;
		}
		
		
	}
	public void setMap(Map<String, Integer> map) {
		this.map = map;
	}
	
	public Map<String, SchObj> getMap2() {
		return map2;
	}
	public void setMap2(Map<String, SchObj> map2) {
		this.map2 = map2;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public long getDuration() {
		return duration;
	}
	public void setDuration(long duration) {
		this.duration = duration;
	}
	public String getStartTimeStr() {
	        if (startTime == null)
	            return "--";
	        return DateUtil.shortDf.get().format(startTime);
	    }

	    public String getDurationStr() {
	        return DateUtil.toShortStr(duration);
	    }
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
    
} 
