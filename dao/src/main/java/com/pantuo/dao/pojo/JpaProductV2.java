package com.pantuo.dao.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.pantuo.dao.pojo.JpaSupplies.Status;

@Entity
@Table(name = "product_V2")
public class JpaProductV2 extends CityEntity {
	public static enum Status {
		online, offline
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private String name; //套餐名称
	private double price; //套餐价格
	private String remarks;//备注
	private String creater;//创建人
	private Status stats;//上下架
	@Column(length = 128)
	private String addressList;//商区
	@Column(length = 128)
	private String smallAdressList;//城区
	@Column(length = 12)
	private String personAvg;//人次
	@Column(length=1000) 
	private String jsonString;
	
	

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

	public Status getStats() {
		return stats;
	}

	public void setStats(Status stats) {
		this.stats = stats;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getJsonString() {
		return jsonString;
	}

	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
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

	public String getAddressList() {
		return addressList;
	}

	public void setAddressList(String addressList) {
		this.addressList = addressList;
	}

	public String getSmallAdressList() {
		return smallAdressList;
	}

	public void setSmallAdressList(String smallAdressList) {
		this.smallAdressList = smallAdressList;
	}

	public String getPersonAvg() {
		return personAvg;
	}

	public void setPersonAvg(String personAvg) {
		this.personAvg = personAvg;
	}

}
