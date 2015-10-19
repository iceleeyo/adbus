package com.pantuo.web.view;

import java.util.List;

import com.pantuo.dao.pojo.JpaCardBoxBody;
import com.pantuo.dao.pojo.JpaCardBoxMedia;

public class CardView {
	List<JpaCardBoxMedia> media;
	List<JpaCardBoxBody> body;
	
	
	double totalPrice;
	int totalnum;
	public List<JpaCardBoxMedia> getMedia() {
		return media;
	}
	public void setMedia(List<JpaCardBoxMedia> media) {
		this.media = media;
	}
	public List<JpaCardBoxBody> getBody() {
		return body;
	}
	public void setBody(List<JpaCardBoxBody> body) {
		this.body = body;
	}
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public int getTotalnum() {
		return totalnum;
	}
	public void setTotalnum(int totalnum) {
		this.totalnum = totalnum;
	}
	public CardView(List<JpaCardBoxMedia> media, List<JpaCardBoxBody> body, double totalPrice, int totalnum) {
		super();
		this.media = media;
		this.body = body;
		this.totalPrice = totalPrice;
		this.totalnum = totalnum;
	}
	
	
	
	
	

}
