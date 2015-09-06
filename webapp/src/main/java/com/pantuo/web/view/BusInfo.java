package com.pantuo.web.view;

import java.util.Date;

public class BusInfo {
   public int busid;
   public String contractCode;
   public Date startD;
   public Date endD;
public int getBusid() {
	return busid;
}
public void setBusid(int busid) {
	this.busid = busid;
}
public String getContractCode() {
	return contractCode;
}
public void setContractCode(String contractCode) {
	this.contractCode = contractCode;
}
public Date getStartD() {
	return startD;
}
public void setStartD(Date startD) {
	this.startD = startD;
}
public Date getEndD() {
	return endD;
}
public void setEndD(Date endD) {
	this.endD = endD;
}
public BusInfo(int busid) {
	super();
	this.busid = busid;
}
public BusInfo() {
}
   
}
