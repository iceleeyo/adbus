package com.pantuo.dao.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.pantuo.dao.pojo.BaseEntity;
 
@Entity
@Table(name = "customer_history")
public class JpaCustomerHistory extends BaseEntity {
	
	
	
	
	
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
	
	public int userDetailId;
	public String fieldViewName;
	
	public String oldValue;
	public String newValue;
	public String operationUser;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserDetailId() {
		return userDetailId;
	}
	public void setUserDetailId(int userDetailId) {
		this.userDetailId = userDetailId;
	}
	public String getFieldViewName() {
		return fieldViewName;
	}
	public void setFieldViewName(String fieldViewName) {
		this.fieldViewName = fieldViewName;
	}
	public String getOldValue() {
		return oldValue;
	}
	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}
	public String getNewValue() {
		return newValue;
	}
	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}
	public String getOperationUser() {
		return operationUser;
	}
	public void setOperationUser(String operationUser) {
		this.operationUser = operationUser;
	}
	public JpaCustomerHistory() {
		super();
	}
	

}
