package com.pantuo.web.view;

import com.pantuo.dao.pojo.JpaBusAdjustLog;

public class AdjustLogView {
	BusInfo busInfo;

	JpaBusAdjustLog log;

	String oldBusLevel;
	String busLevel;

	public AdjustLogView() {
	}

	public BusInfo getBusInfo() {
		return busInfo;
	}

	public void setBusInfo(BusInfo busInfo) {
		this.busInfo = busInfo;
	}

	 
	public String getOldBusLevel() {
		return oldBusLevel;
	}

	public void setOldBusLevel(String oldBusLevel) {
		this.oldBusLevel = oldBusLevel;
	}

	public JpaBusAdjustLog getLog() {
		return log;
	}

	public void setLog(JpaBusAdjustLog log) {
		this.log = log;
	}

	public String getBusLevel() {
		return busLevel;
	}

	public void setBusLevel(String busLevel) {
		this.busLevel = busLevel;
	}

}
