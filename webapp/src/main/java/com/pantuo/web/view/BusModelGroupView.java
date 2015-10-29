package com.pantuo.web.view;

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
public class BusModelGroupView {

	public BusModelGroupView(String modelName) {
		this.modelName = modelName;
	}

	public String modelName;
	public int total;//总数
	public int online;//占用车辆
	public int free;//可用车辆
	public int nowDown;//到期未上刊

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public int getTotal() {
		return total;
	}

	public void ascTotal() {
		this.total = total + 1;
	}

	public void ascOnline() {
		this.online = online + 1;
	}

	public void ascFree() {
		this.free = free + 1;
	}

	public void ascNowDown() {
		this.nowDown = nowDown + 1;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getOnline() {
		return online;
	}

	public void setOnline(int online) {
		this.online = online;
	}

	public int getFree() {
		return free;
	}

	public void setFree(int free) {
		this.free = free;
	}

	public int getNowDown() {
		return nowDown;
	}

	public void setNowDown(int nowDown) {
		this.nowDown = nowDown;
	}

	public String getString() {
		return "BusModelGroupView [modelName=" + modelName + ", total=" + total + ", online=" + online + ", free="
				+ free + ", nowDown=" + nowDown + "]";
	}

}
