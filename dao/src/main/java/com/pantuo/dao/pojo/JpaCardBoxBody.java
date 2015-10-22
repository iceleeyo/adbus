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
	private JpaBusOrderDetailV2 product;
	private int needCount;//购买数量
	private double price = 0;//价格
	private double totalprice = 0;//价格
	private int isConfirm = 0;//1代表已确认
	private long seriaNum;//表单序列号
	private int days;//用户需求天数
	private int isDesign =0;//0 管理员 1用户选择

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public int getIsConfirm() {
		return isConfirm;
	}

	public void setIsConfirm(int isConfirm) {
		this.isConfirm = isConfirm;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}


	public JpaBusOrderDetailV2 getProduct() {
		return product;
	}

	public void setProduct(JpaBusOrderDetailV2 product) {
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

	public double getTotalprice() {
		return totalprice;
	}

	public void setTotalprice(double totalprice) {
		this.totalprice = totalprice;
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

	public int getIsDesign() {
		return isDesign;
	}

	public void setIsDesign(int isDesign) {
		this.isDesign = isDesign;
	}
 
}