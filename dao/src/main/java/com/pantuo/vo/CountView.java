package com.pantuo.vo;

import java.util.Collection;

public class CountView {
	
	public CountView() {
		super();
	}
	Collection<ModelCountView> views;
	public int totalsalsnum;//总订购数
	public int totalalrnum;//总已上刊数
	public int totalfree;//总排队数
	public Collection<ModelCountView> getViews() {
		return views;
	}
	public void setViews(Collection<ModelCountView> views) {
		this.views = views;
	}
	public int getTotalsalsnum() {
		return totalsalsnum;
	}
	public void setTotalsalsnum(int totalsalsnum) {
		this.totalsalsnum = totalsalsnum;
	}
	public int getTotalalrnum() {
		return totalalrnum;
	}
	public void setTotalalrnum(int totalalrnum) {
		this.totalalrnum = totalalrnum;
	}
	public int getTotalfree() {
		return totalfree;
	}
	public void setTotalfree(int totalfree) {
		this.totalfree = totalfree;
	}
	
}
