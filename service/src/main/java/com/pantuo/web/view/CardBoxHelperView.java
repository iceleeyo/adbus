package com.pantuo.web.view;

import com.pantuo.dao.pojo.JpaCardBoxHelper;

public class CardBoxHelperView {

	JpaCardBoxHelper r;

	public double totalMoney;
	public int product_count;
	private int media_type = -1;

	public JpaCardBoxHelper getR() {
		return r;
	}

	public void setR(JpaCardBoxHelper r) {
		this.r = r;
	}

	public double getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(double totalMoney) {
		this.totalMoney = totalMoney;
	}

	public int getProduct_count() {
		return product_count;
	}

	public void setProduct_count(int product_count) {
		this.product_count = product_count;
	}

	public CardBoxHelperView(JpaCardBoxHelper r) {
		this.r = r;
	}

	public int getMedia_type() {
		return media_type;
	}

	public void setMedia_type(int media_type) {
		this.media_type = media_type;
	}

}
