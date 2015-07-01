package com.pantuo.dao.pojo;

import javax.persistence.*;

import java.util.Date;

/**
 * 
 * <b><code>JpaCpd</code></b>
 * <p>
 * cpd 用户竞价记录表
 * </p>
 * <b>Creation Time:</b> 2015年7月1日 上午10:45:23
 * @author impanxh@gmail.com
 * @since pantuo 1.0-SNAPSHOT
 */
@Entity
@Table(name = "user_cpd")
public class JpaCpdLog extends BaseEntity {
	//是否扣费
	public static enum OverType {
		wait, over;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@ManyToOne  
	@JoinColumn(name = "cpdid")
	private JpaCpd jpaCpd;
	private double price; //售价
	private double comparePrice;//当前用户竞价
	private String userId;
	private Date create_date;
	private OverType type;

	public JpaCpdLog() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public JpaCpd getJpaCpd() {
		return jpaCpd;
	}

	public void setJpaCpd(JpaCpd jpaCpd) {
		this.jpaCpd = jpaCpd;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getComparePrice() {
		return comparePrice;
	}

	public void setComparePrice(double comparePrice) {
		this.comparePrice = comparePrice;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public OverType getType() {
		return type;
	}

	public void setType(OverType type) {
		this.type = type;
	}

	 
}