package com.pantuo.web.view;

import java.util.Map;

import com.pantuo.dao.pojo.JpaBodyContract;
import com.pantuo.mybatis.domain.Bus;

public class LineBusCpd {

	public String serialNumber;
	public Bus bus;

	public Map<String, JpaBodyContract> map;

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Map<String, JpaBodyContract> getMap() {
		return map;
	}

	public void setMap(Map<String, JpaBodyContract> map) {
		this.map = map;
	}

	public Bus getBus() {
		return bus;
	}

	public void setBus(Bus bus) {
		this.bus = bus;
	}

}
