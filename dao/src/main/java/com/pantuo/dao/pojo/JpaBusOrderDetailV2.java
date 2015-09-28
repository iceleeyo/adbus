package com.pantuo.dao.pojo;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
@Entity
@Table(name = "busOrderDetail_V2")
public class JpaBusOrderDetailV2 extends CityEntity {

	public JpaBusOrderDetailV2() {
	}

	public JpaBusOrderDetailV2(int cityId) {
		super(cityId);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@ManyToOne
	@JoinColumn(name = "productId")
	private JpaBusOrderV2 JpaProductV2;
	
	
	@ManyToOne
	@JoinColumn(name = "orderid")
	private JpaBusOrderV2 jpaBusOrderV2;

	private JpaBusline.Level leval;
	private boolean doubleDecker = false;//true双层
	private double price; 
	
	
	private int busNumber;//车辆数
	private Date startTime;//时间 
	private long seriaNum;//表单序列号
	private int days;
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public JpaBusOrderV2 getJpaProductV2() {
		return JpaProductV2;
	}

	public void setJpaProductV2(JpaBusOrderV2 jpaProductV2) {
		JpaProductV2 = jpaProductV2;
	}

	public JpaBusline.Level getLeval() {
		return leval;
	}

	public void setLeval(JpaBusline.Level leval) {
		this.leval = leval;
	}

	public boolean isDoubleDecker() {
		return doubleDecker;
	}

	public void setDoubleDecker(boolean doubleDecker) {
		this.doubleDecker = doubleDecker;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getBusNumber() {
		return busNumber;
	}

	public void setBusNumber(int busNumber) {
		this.busNumber = busNumber;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public long getSeriaNum() {
		return seriaNum;
	}

	public void setSeriaNum(long seriaNum) {
		this.seriaNum = seriaNum;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	public JpaBusOrderV2 getJpaBusOrderV2() {
		return jpaBusOrderV2;
	}

	public void setJpaBusOrderV2(JpaBusOrderV2 jpaBusOrderV2) {
		this.jpaBusOrderV2 = jpaBusOrderV2;
	}
	
	
	
	
	
	
	
	

}
