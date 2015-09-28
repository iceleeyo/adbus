package com.pantuo.dao.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "product_V2")
public class JpaProductV2 extends CityEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private String name; //套餐名称
	private double price; //套餐价格
	private String remarks;//备注
	private String creater;//创建人

	public JpaProductV2() {
		super();
	}

	JpaProductV2(int cityId) {
		super(cityId);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

}
