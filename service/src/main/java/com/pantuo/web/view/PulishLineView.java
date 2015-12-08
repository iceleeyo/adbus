package com.pantuo.web.view;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.pantuo.dao.pojo.JpaBusOnline;
import com.pantuo.dao.pojo.JpaPublishLine;

public class PulishLineView {
	public JpaPublishLine orders;
	public JpaBusOnline one;//只查出一个

	public List<Integer> busId;
	public StringBuilder sb = new StringBuilder();
	
	
	
	public String adtype;//: "tiaofu",
	public String print;//: "center",
	public String sktype;//: "normal"
	

	public String getAdtype() {
		return one!=null?one.getAdtype().getAdtypeName():StringUtils.EMPTY;
	}
	public String getPrint() {
		return one!=null?one.getPrint().getPrintName():StringUtils.EMPTY;
	}

	public String getSktype() {
		return one!=null?one.getSktype().getSktypeName():StringUtils.EMPTY;
	}

	public JpaPublishLine getOrders() {
		return orders;
	}

	public void setOrders(JpaPublishLine orders) {
		this.orders = orders;
	}

	public PulishLineView(JpaPublishLine orders) {
		super();
		this.orders = orders;
	}

	public JpaBusOnline getOne() {
		return one;
	}

	public void setOne(JpaBusOnline one) {
		this.one = one;
	}

	public List<Integer> getBusId() {
		return busId;
	}

	public void setBusId(List<Integer> busId) {
		this.busId = busId;
	}

	public void addBusId(Integer bus_id) {
		if (busId == null) {
			busId = new ArrayList<Integer>();
		}
		busId.add(bus_id);
	}

	public StringBuilder getSb() {
		return sb;
	}

	public void setSb(StringBuilder sb) {
		this.sb = sb;
	}
	
	public void appendBusId(String bus_id) {
		if (sb.length() != 0) {
			sb.append(",");
		}
		sb.append(bus_id);
	}

}
