package com.pantuo.web.view;

import java.util.Set;


import com.pantuo.dao.pojo.JpaBus;
import com.pantuo.dao.pojo.JpaBusModel;
import com.pantuo.dao.pojo.JpaBusUpLog;
import com.pantuo.dao.pojo.JpaBusinessCompany;
import com.pantuo.dao.pojo.JpaBusline;
import com.pantuo.dao.pojo.JpaLineUpLog;
import com.pantuo.mybatis.domain.Bus;
import com.pantuo.mybatis.domain.BusLine;

public class BusInfoView {
	BusInfo busInfo;
	JpaBus jpaBus;
	Bus bus;
	Bus oldbus;
	JpaBusModel model;
	JpaBusline line;
	String busCategory;
	String oldbusCategory;
	String levleString;
	String oldlevleString;
	JpaBusinessCompany company;
	JpaBusinessCompany oldcompany;
	JpaBusModel oldmodel;
	JpaBusline oldline;
	boolean ishaveAd=false;
	
	BusLine newLine;
	BusLine oldLine2;
	
	
	JpaBusUpLog busUpLog;
	JpaLineUpLog lineUpLog;
	
	
	  String busLevel;
	  //以下是查询条件 
	  String serinum;
	  //String serinum = req.getFilter("serinum");

	public BusInfoView() {
	}

	public BusInfo getBusInfo() {
		return busInfo;
	}


	public String getLevleString() {
		return levleString;
	}

	public String getOldlevleString() {
		return oldlevleString;
	}

	public void setOldlevleString(String oldlevleString) {
		this.oldlevleString = oldlevleString;
	}

	public void setLevleString(String levleString) {
		this.levleString = levleString;
	}

	public JpaLineUpLog getLineUpLog() {
		return lineUpLog;
	}

	public void setLineUpLog(JpaLineUpLog lineUpLog) {
		this.lineUpLog = lineUpLog;
	}

	public BusLine getNewLine() {
		return newLine;
	}

	public void setNewLine(BusLine newLine) {
		this.newLine = newLine;
	}


	public BusLine getOldLine2() {
		return oldLine2;
	}

	public void setOldLine2(BusLine oldLine2) {
		this.oldLine2 = oldLine2;
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

	public Bus getOldbus() {
		return oldbus;
	}

	public JpaBusModel getOldmodel() {
		return oldmodel;
	}

	public void setOldmodel(JpaBusModel oldmodel) {
		this.oldmodel = oldmodel;
	}

	public JpaBusline getOldline() {
		return oldline;
	}

	public void setOldline(JpaBusline oldline) {
		this.oldline = oldline;
	}

	public String getOldbusCategory() {
		return oldbusCategory;
	}

	public void setOldbusCategory(String oldbusCategory) {
		this.oldbusCategory = oldbusCategory;
	}

	public JpaBusinessCompany getOldcompany() {
		return oldcompany;
	}

	public void setOldcompany(JpaBusinessCompany oldcompany) {
		this.oldcompany = oldcompany;
	}

	public void setOldbus(Bus oldbus) {
		this.oldbus = oldbus;
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

	public String getSerinum() {
		return serinum;
	}

	public void setSerinum(String serinum) {
		this.serinum = serinum;
	}

}
