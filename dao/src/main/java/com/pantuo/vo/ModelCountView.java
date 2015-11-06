package com.pantuo.vo;

/**
 * 
 * <b><code>BusModelGroupView</code></b>
 * <p>
 * 订单及车辆查询 按车型统计
 * </p>
 * <b>Creation Time:</b> 2015年10月29日 下午2:20:57
 * @author impanxh@gmail.com
 * @since pantuo 1.0-SNAPSHOT
 */
public class ModelCountView {

	public ModelCountView(String modelName) {
		this.modelName = modelName;
	}

	public ModelCountView() {
	}

	public String modelName;
	public int salsnum;//订购数
	public int alrnum;//已上刊数
	public int free;//排队数
	
	public int leval;
	public int days;
	public int busnum;
	

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}


	public void ascFree() {
		this.free = free + 1;
	}


	public int getFree() {
		return free;
	}

	public int getLeval() {
		return leval;
	}

	public void setLeval(int leval) {
		this.leval = leval;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	public int getBusnum() {
		return busnum;
	}

	public void setBusnum(int busnum) {
		this.busnum = busnum;
	}

	public void setFree(int free) {
		this.free = free;
	}

	public int getSalsnum() {
		return salsnum;
	}

	public void setSalsnum(int salsnum) {
		this.salsnum = salsnum;
	}

	public int getAlrnum() {
		return alrnum;
	}

	public void setAlrnum(int alrnum) {
		this.alrnum = alrnum;
	}


}
