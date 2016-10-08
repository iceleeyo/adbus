package com.pantuo.vo;

import java.util.Date;

import com.pantuo.util.DateUtil;

public class MediaInventory {
    private String bname;
    private Date startTime;
    private Date day;
    private long duration;
    private int num;
    private int sotid;
    private int bsize;
    private int normalremain;
    private int fremain;
    int isDelete;
    
	public MediaInventory() {
	}
	public MediaInventory(String bname, int bsize, int normalremain, int fremain) {
		super();
		this.bname = bname;
		this.bsize = bsize;
		this.normalremain = normalremain;
		this.fremain = fremain;
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
	
	public Date getDay() {
		return day;
	}
	public void setDay(Date day) {
		this.day = day;
	}
	
	public int getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}
	public void setBsize(int bsize) {
		this.bsize = bsize;
	}
	
	 public int getSotid() {
		return sotid;
	}
	public void setSotid(int sotid) {
		this.sotid = sotid;
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
	public int getNormalremain() {
		return normalremain;
	}
	public void setNormalremain(int normalremain) {
		this.normalremain = normalremain;
	}
	public int getFremain() {
		return fremain;
	}
	public void setFremain(int fremain) {
		this.fremain = fremain;
	}
    
} 
