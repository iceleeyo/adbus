package com.pantuo.web.view;

import java.util.Date;

import com.pantuo.mybatis.domain.Offlinecontract;

public class BusInfo {
	public int busid;
	public String contractCode;
	public Offlinecontract offlinecontract;
	public Date startD;
	public Date endD;

	public static enum Stats {
		empty, past, now, future;
	}

	private Stats stats = Stats.empty;

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

	public Offlinecontract getOfflinecontract() {
		return offlinecontract;
	}

	public void setOfflinecontract(Offlinecontract offlinecontract) {
		this.offlinecontract = offlinecontract;
	}

	public Stats getStats() {
		return stats;
	}

	public void setStats(Stats stats) {
		this.stats = stats;
	}

}
