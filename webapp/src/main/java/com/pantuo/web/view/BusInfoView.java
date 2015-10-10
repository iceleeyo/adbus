package com.pantuo.web.view;

import com.pantuo.dao.pojo.JpaBus;
import com.pantuo.dao.pojo.JpaBus.Category;
import com.pantuo.dao.pojo.JpaBusModel;
import com.pantuo.dao.pojo.JpaBusUpLog;
import com.pantuo.dao.pojo.JpaBusinessCompany;
import com.pantuo.dao.pojo.JpaBusline;
import com.pantuo.mybatis.domain.Bus;

public class BusInfoView {
	BusInfo busInfo;
	JpaBus jpaBus;
	Bus bus;
	JpaBusModel model;
	JpaBusline line;
	String busCategory;
	JpaBusinessCompany company;
	boolean ishaveAd=false;
	
	
	JpaBusUpLog busUpLog;
	
	
	  String busLevel;

	public BusInfoView() {
	}

	public BusInfo getBusInfo() {
		return busInfo;
	}


	public boolean isIshaveAd() {
		return ishaveAd;
	}

	public void setIshaveAd(boolean ishaveAd) {
		this.ishaveAd = ishaveAd;
	}

	public void setBusInfo(BusInfo busInfo) {
		this.busInfo = busInfo;
	}

	public JpaBus getJpaBus() {
		return jpaBus;
	}

	public void setJpaBus(JpaBus jpaBus) {
		this.jpaBus = jpaBus;
	}

	public JpaBusUpLog getBusUpLog() {
		return busUpLog;
	}

	public void setBusUpLog(JpaBusUpLog busUpLog) {
		this.busUpLog = busUpLog;
	}

	public Bus getBus() {
		return bus;
	}

	public JpaBusModel getModel() {
		return model;
	}

	public void setModel(JpaBusModel model) {
		this.model = model;
	}

	public JpaBusline getLine() {
		return line;
	}

	public void setLine(JpaBusline line) {
		this.line = line;
	}

	public JpaBusinessCompany getCompany() {
		return company;
	}

	public void setCompany(JpaBusinessCompany company) {
		this.company = company;
	}

	public void setBus(Bus bus) {
		this.bus = bus;
	}

	public String getBusCategory() {
		return busCategory;
	}

	public void setBusCategory(String busCategory) {
		this.busCategory = busCategory;
	}

	public String getBusLevel() {
		return busLevel;
	}

	public void setBusLevel(String busLevel) {
		this.busLevel = busLevel;
	}

}
