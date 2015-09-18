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
	private Date endDate;
	private String userid;
	private boolean enable=true;
	
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
	public int getDays() {
		return days;
	}
	public void setDays(int days) {
		this.days = days;
	}
	
}
