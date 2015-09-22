package com.pantuo.dao.pojo;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="bus_online")
public class JpaBusOnline extends CityEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
	@ManyToOne
	@JoinColumn(name = "busid")
	private JpaBus jpabus;
	@ManyToOne
	@JoinColumn(name = "contractid")
	private JpaOfflineContract offlineContract;
	private int days;
	private Date startDate;
	private Date endDate; //预计下刊时间
	private Date real_endDate;//实际下刊时间
	private String userid;
	private String editor;
	private boolean enable=true;
	private int publish_lineId;//JpaPublishLine.id;//此次上刊的需求及批次信息
	
	public boolean isEnable() {
		return enable;
	}
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	public JpaBus getJpabus() {
		return jpabus;
	}
	public void setJpabus(JpaBus jpabus) {
		this.jpabus = jpabus;
	}
	public JpaOfflineContract getOfflineContract() {
		return offlineContract;
	}
	public void setOfflineContract(JpaOfflineContract offlineContract) {
		this.offlineContract = offlineContract;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public Date getReal_endDate() {
		return real_endDate;
	}
	public void setReal_endDate(Date real_endDate) {
		this.real_endDate = real_endDate;
	}
	public int getDays() {
		return days;
	}
	public void setDays(int days) {
		this.days = days;
	}
	public String getEditor() {
		return editor;
	}
	public void setEditor(String editor) {
		this.editor = editor;
	}
	public int getPublish_lineId() {
		return publish_lineId;
	}
	public void setPublish_lineId(int publish_lineId) {
		this.publish_lineId = publish_lineId;
	}
	
}
