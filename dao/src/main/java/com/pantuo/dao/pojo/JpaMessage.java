package com.pantuo.dao.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author panxh
 * 
 */
@Entity
@Table(name = "message")
public class JpaMessage extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	public static enum Main_type {
		unread, read, del
	}

	public static enum Sub_type {
		payFail, pass, deny, jianbo, shangbo, effective;
	}

	int orderid;
	String sendID;//发送者编号；
	String recID;//接受者编号（如为0，则接受者为所有人
	Main_type main_type; //站内信的查看状态；
	Sub_type sub_type; //站内信的查看状态；
	String message;// 站内信内容

	public JpaMessage() {
		//for serialization
	}

	public JpaMessage(String sendID, String recID, String message, Main_type main_type, int orderid) {
		this.main_type = main_type;
		this.sendID = sendID;
		this.message = message;
		this.recID = recID;

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSendID() {
		return sendID;
	}

	public void setSendID(String sendID) {
		this.sendID = sendID;
	}

	public String getRecID() {
		return recID;
	}

	public void setRecID(String recID) {
		this.recID = recID;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Main_type getMain_type() {
		return main_type;
	}

	public void setMain_type(Main_type main_type) {
		this.main_type = main_type;
	}

	public Sub_type getSub_type() {
		return sub_type;
	}

	public void setSub_type(Sub_type sub_type) {
		this.sub_type = sub_type;
	}

	public int getOrderid() {
		return orderid;
	}

	public void setOrderid(int orderid) {
		this.orderid = orderid;
	}
 

}