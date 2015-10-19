package com.pantuo.dao.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 */
@Entity
@Table(name = "cardbox_body")
public class JpaCardBoxBody extends CityEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String userId;
	@ManyToOne
	@JoinColumn(name = "productId")
	private JpaProductV2 product;
	private int needCount;//购买数量
	private double price = 0;//价格

	private long seriaNum;//表单序列号

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public JpaProductV2 getProduct() {
		return product;
	}

	public void setProduct(JpaProductV2 product) {
		this.product = product;
	}

	public int getNeedCount() {
		return needCount;
	}

	public void setNeedCount(int needCount) {
		this.needCount = needCount;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public long getSeriaNum() {
		return seriaNum;
	}

	public void setSeriaNum(long seriaNum) {
		this.seriaNum = seriaNum;
	}
 
}