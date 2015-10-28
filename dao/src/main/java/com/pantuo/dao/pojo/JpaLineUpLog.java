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
@Table(name="line_uplog")
public class JpaLineUpLog extends CityEntity{
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
	@ManyToOne
	@JoinColumn(name = "lineid")
	private JpaBusline jpabusline;
	@Column(length=1000) 
	private String jsonString;
	@Column(length=1000) 
	private String oldjsonString;
	private String newlinename;
	private String oldlinename;
	private String change_fileds;//修改的字段
	private String updator;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public JpaBusline getJpabusline() {
		return jpabusline;
	}
	public void setJpabusline(JpaBusline jpabusline) {
		this.jpabusline = jpabusline;
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
	public String getNewlinename() {
		return newlinename;
	}
	public void setNewlinename(String newlinename) {
		this.newlinename = newlinename;
	}
	public String getOldlinename() {
		return oldlinename;
	}
	public void setOldlinename(String oldlinename) {
		this.oldlinename = oldlinename;
	}
	public String getChange_fileds() {
		return change_fileds;
	}
	public void setChange_fileds(String change_fileds) {
		this.change_fileds = change_fileds;
	}
	
	
}
