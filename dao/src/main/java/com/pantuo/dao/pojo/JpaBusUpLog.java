package com.pantuo.dao.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="bus_uplog")
public class JpaBusUpLog extends CityEntity{
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
	@ManyToOne
	@JoinColumn(name = "busid")
	private JpaBus jpabus;
	@Column(length=1000) 
	private String jsonString;
	@Column(length=1000) 
	private String oldjsonString;
	
	@Column(length=16) 
	private String beSerialNumber;//修改前的自编号
	@Column(length=16) 
	private String afSerialNumber;//修改后的自编号
	
	@Column(length=16) 
	private int oldCompanyId;//原销售中心
	@Column(length=16) 
	private String oldBranch;//原分公司
	
	private String change_fileds;//修改的字段
	private String updator;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public JpaBus getJpabus() {
		return jpabus;
	}
	public void setJpabus(JpaBus jpabus) {
		this.jpabus = jpabus;
	}
	public String getJsonString() {
		return jsonString;
	}
	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}
	
	public String getOldjsonString() {
		return oldjsonString;
	}
	public void setOldjsonString(String oldjsonString) {
		this.oldjsonString = oldjsonString;
	}
	public String getUpdator() {
		return updator;
	}
	public void setUpdator(String updator) {
		this.updator = updator;
	}
	public String getBeSerialNumber() {
		return beSerialNumber;
	}
	public void setBeSerialNumber(String beSerialNumber) {
		this.beSerialNumber = beSerialNumber;
	}
	public String getAfSerialNumber() {
		return afSerialNumber;
	}
	public void setAfSerialNumber(String afSerialNumber) {
		this.afSerialNumber = afSerialNumber;
	}
	public String getChange_fileds() {
		return change_fileds;
	}
	public void setChange_fileds(String change_fileds) {
		this.change_fileds = change_fileds;
	}
	public int getOldCompanyId() {
		return oldCompanyId;
	}
	public void setOldCompanyId(int oldCompanyId) {
		this.oldCompanyId = oldCompanyId;
	}
	public String getOldBranch() {
		return oldBranch;
	}
	public void setOldBranch(String oldBranch) {
		this.oldBranch = oldBranch;
	}
	
}
