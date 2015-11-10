package com.pantuo.dao.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="modeldesc")
public class JpaModeldesc extends CityEntity{
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int type;                //类型
    private String description;         //描述
    
	public JpaModeldesc() {
		super();
	}
	public JpaModeldesc(int type, String description) {
		super();
		this.type = type;
		this.description = description;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return "JpaModeldesc [id=" + id + ", type=" + type + ", description=" + description + "]";
	}
    
}
