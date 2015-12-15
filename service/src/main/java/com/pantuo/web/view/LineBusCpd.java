package com.pantuo.web.view;

import com.pantuo.dao.pojo.JpaBusline;
import com.pantuo.mybatis.domain.Bus;
import com.pantuo.mybatis.domain.BusContract;
import com.pantuo.mybatis.domain.BusLock;

public class LineBusCpd {

	public String serialNumber;
	public Bus bus;
	JpaBusline line;
	
	BusLock buslock;
	String impSite;
	
	//已安装车辆显示
	private BusContract busContract;

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Bus getBus() {
		return bus;
	}

	public void setBus(Bus bus) {
		this.bus = bus;
	}

	public JpaBusline getLine() {
		return line;
	}

	public void setLine(JpaBusline line) {
		this.line = line;
	}

	public String getImpSite() {
		return impSite;
	}

	public void setImpSite(String impSite) {
		this.impSite = impSite;
	}

	public BusContract getBusContract() {
		return busContract;
	}

	public void setBusContract(BusContract busContract) {
		this.busContract = busContract;
	}

	public BusLock getBuslock() {
		return buslock;
	}

	public void setBuslock(BusLock buslock) {
		this.buslock = buslock;
	}

}
