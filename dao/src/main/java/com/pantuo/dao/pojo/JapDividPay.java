package com.pantuo.dao.pojo;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * <b><code>JapDividPay</code></b>
 * <p>
 * 分期付款表
 * </p>
 * <b>Creation Time:</b> 2015年9月14日 下午1:43:55
 * @author xiaoli
 * @since pantuo 1.0-SNAPSHOT
 */
@Entity
@Table(name = "dividpay")
public class JapDividPay extends CityEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private long seriaNum;
	private String name;
	private String amounts;
	private Date payDate;
	private int stats;
	private String updator;
	private String remarks;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getSeriaNum() {
		return seriaNum;
	}
	public void setSeriaNum(long seriaNum) {
		this.seriaNum = seriaNum;
	}
	public String getAmounts() {
		return amounts;
	}
	public void setAmounts(String amounts) {
		this.amounts = amounts;
	}
	public int getStats() {
		return stats;
	}
	public void setStats(int stats) {
		this.stats = stats;
	}
	
	public String getUpdator() {
		return updator;
	}
	public void setUpdator(String updator) {
		this.updator = updator;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getPayDate() {
		return payDate;
	}
	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}
	
	
}
