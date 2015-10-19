package com.pantuo.dao.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cardbox_user")
public class JpaCardBoxUser extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String userId;

	private long seriaNum;//表单序列号
	//1表示付款
	private int isPay = 0;

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

	public long getSeriaNum() {
		return seriaNum;
	}

	public void setSeriaNum(long seriaNum) {
		this.seriaNum = seriaNum;
	}

	public int getIsPay() {
		return isPay;
	}

	public void setIsPay(int isPay) {
		this.isPay = isPay;
	}

	public JpaCardBoxUser() {
	}

}
