package com.pantuo.dao.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.pantuo.dao.pojo.JpaCardBoxHelper.Stats;
import com.pantuo.dao.pojo.JpaCity.MediaType;
import com.pantuo.dao.pojo.JpaOrders.PayType;

/**
 */
@Entity
@Table(name = "body_order_log")
public class JpaBodyOrderLog extends CityEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@ManyToOne
	@JoinColumn(name = "helperId")
	private JpaCardBoxHelper jpaCardBoxHelper;


	private String userid;
	private String stats;
	private String remarks;
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public JpaCardBoxHelper getJpaCardBoxHelper() {
		return jpaCardBoxHelper;
	}

	public void setJpaCardBoxHelper(JpaCardBoxHelper jpaCardBoxHelper) {
		this.jpaCardBoxHelper = jpaCardBoxHelper;
	}


	public String getStats() {
		return stats;
	}

	public void setStats(String stats) {
		this.stats = stats;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}



	 
 

}