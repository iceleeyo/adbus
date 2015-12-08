package com.pantuo.web.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.pantuo.dao.pojo.JpaBusOnline.Adtype;
import com.pantuo.dao.pojo.JpaBusOnline.Print;
import com.pantuo.dao.pojo.JpaBusOnline.Sktype;
import com.pantuo.mybatis.domain.BusOnline;
import com.pantuo.mybatis.domain.Offlinecontract;
import com.pantuo.util.DateConverter;

public class BusInfo {
	public int busid;
	public String contractCode = StringUtils.EMPTY;
	public Offlinecontract offlinecontract;
	public Date startD;
	public Date endD;
	public BusOnline busOnline;

	public String _sktype;
	public String _adtype;
	public String _print;
	
	public String get_print() {
		return busOnline == null ? StringUtils.EMPTY
				: (busOnline.getPrint() >= Print.values().length ? StringUtils.EMPTY : Print.values()[busOnline
						.getPrint()].getPrintName());
	}

	public String get_sktype() {
		return busOnline == null ? StringUtils.EMPTY
				: (busOnline.getSktype() >= Sktype.values().length ? StringUtils.EMPTY : Sktype.values()[busOnline
						.getSktype()].getSktypeName());
	}

	public String get_adtype() {

		return busOnline == null ? StringUtils.EMPTY
				: (busOnline.getAdtype() >= Adtype.values().length ? StringUtils.EMPTY : Adtype.values()[busOnline
						.getAdtype()].getAdtypeName());
	}

	public String getStartDStr() {
		return startD != null ? DateConverter.doConvertToString(startD, DateConverter.DATE_PATTERN) : StringUtils.EMPTY;
	}

	public String getEndDStr() {
		return endD != null ? DateConverter.doConvertToString(endD, DateConverter.DATE_PATTERN) : StringUtils.EMPTY;
	}

	/**
	 * 这个列表如果系统跑久了 数据会较大，需要再优化
	 */
	List<BusOnline> allPlan = null;

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

	public BusOnline getBusOnline() {
		return busOnline;
	}

	public void setBusOnline(BusOnline busOnline) {
		this.busOnline = busOnline;
	}

	public List<BusOnline> getAllPlan() {
		return allPlan;
	}

	public void addPlan(BusOnline plan) {
		if (allPlan == null) {
			this.allPlan = new ArrayList<BusOnline>();
		}
		this.allPlan.add(plan);
	}

	public void setAllPlan(List<BusOnline> allPlan) {
		this.allPlan = allPlan;
	}

}
