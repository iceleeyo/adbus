package com.pantuo.web.view;

import org.springframework.beans.factory.annotation.Autowired;

import com.pantuo.dao.pojo.JpaBusAdjustLog;
import com.pantuo.simulate.QueryBusInfo;

public class AdjustLogView {
	
	
	
	public AdjustLogView(QueryBusInfo queryBusInfo) {
		this.queryBusInfo = queryBusInfo;
	}

	QueryBusInfo queryBusInfo;
	
	
	BusInfo busInfo;

	JpaBusAdjustLog log;

	String oldBusLevel;
	String busLevel;
	
	boolean ishaveAd=false;
	
	

	public boolean isIshaveAd() {
		return queryBusInfo.ishaveAd(log.getJpabus().getId());
	}

	public void setIshaveAd(boolean ishaveAd) {
		this.ishaveAd = ishaveAd;
	}

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
