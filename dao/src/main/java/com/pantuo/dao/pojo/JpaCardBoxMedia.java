package com.pantuo.dao.pojo;

import java.util.Date;

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
@Table(name = "cardbox_media")
public class JpaCardBoxMedia extends CityEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String userId;
	@ManyToOne
	@JoinColumn(name = "productId")
	private JpaProduct product;
	private int needCount;//购买数量
	private JpaProduct.Type type;//购买产品类型
	private double price = 0;//价格
	private int isConfirm = 0;//1代表已确认
	private double totalprice = 0;//价格
	private Date startTime;
	private boolean isChangeOrder = false;
	@ManyToOne
	@JoinColumn(name = "groupId")
	public JpaVideo32Group group;

	private long seriaNum;//表单序列号

	public int getId() {
		return id;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
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

	public long getSeriaNum() {
		return seriaNum;
	}

	public void setSeriaNum(long seriaNum) {
		this.seriaNum = seriaNum;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public JpaProduct getProduct() {
		return product;
	}

	public void setProduct(JpaProduct product) {
		this.product = product;
	}

	public int getNeedCount() {
		return needCount;
	}

	public void setNeedCount(int needCount) {
		this.needCount = needCount;
	}

	public JpaProduct.Type getType() {
		return type;
	}

	public void setType(JpaProduct.Type type) {
		this.type = type;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getTotalprice() {
		return totalprice;
	}

	public void setTotalprice(double totalprice) {
		this.totalprice = totalprice;
	}

	public JpaCardBoxMedia() {
	}

	public JpaVideo32Group getGroup() {
		return group;
	}

	public void setGroup(JpaVideo32Group group) {
		this.group = group;
	}

	public boolean getIsChangeOrder() {
		return isChangeOrder;
	}

	public void setIsChangeOrder(boolean isChangeOrder) {
		this.isChangeOrder = isChangeOrder;
	}
}