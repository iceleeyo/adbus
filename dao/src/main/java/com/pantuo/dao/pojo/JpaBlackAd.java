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
@Table(name = "black_ad")
public class JpaBlackAd extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	public static enum Main_type {
		online, offline, del
	}

	private String seqNumber;//审核号
	private String adName;//底版名字
	private long duration; //套餐时长(S)
	private String createrUser; //创建用户
	private long sortNumber = 0;//权重 越大越在前 

	public JpaBlackAd() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSeqNumber() {
		return seqNumber;
	}

	public void setSeqNumber(String seqNumber) {
		this.seqNumber = seqNumber;
	}

	public String getAdName() {
		return adName;
	}

	public void setAdName(String adName) {
		this.adName = adName;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public String getCreaterUser() {
		return createrUser;
	}

	public void setCreaterUser(String createrUser) {
		this.createrUser = createrUser;
	}

	public long getSortNumber() {
		return sortNumber;
	}

	public void setSortNumber(long sortNumber) {
		this.sortNumber = sortNumber;
	}

}