package com.pantuo.dao.pojo;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.pantuo.dao.pojo.JpaOrders.PayType;

/**
 * 
 * <b><code>JpaPayPlan</code></b>
 * <p>
 * 分期表
 * </p>
 * <b>Creation Time:</b> 2016年4月6日 上午9:48:02
 * @author impanxh@gmail.com
 * @since pantuo 1.0-SNAPSHOT
 */
@Entity
@Table(name = "payPlan")
public class JpaPayPlan extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private Date day;//分期付款日  4.5  4.8     delete if payUser==null
	private double price = 0;

	@ManyToOne
	@JoinColumn(name = "orderId")
	private JpaOrders order;
	private PayType payType;
	private PayState PayState; 
	private String setPlanUser;//分期设置人

	private String payUser;//支付人

	public static enum PayState {
		init/*未支付*/, payed/*已支付*/, check/*等待款项检查*/
	}

	public JpaPayPlan() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDay() {
		return day;
	}

	public void setDay(Date day) {
		this.day = day;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public JpaOrders getOrder() {
		return order;
	}

	public void setOrder(JpaOrders order) {
		this.order = order;
	}

	public PayType getPayType() {
		return payType;
	}

	public void setPayType(PayType payType) {
		this.payType = payType;
	}

	public String getSetPlanUser() {
		return setPlanUser;
	}

	public void setSetPlanUser(String setPlanUser) {
		this.setPlanUser = setPlanUser;
	}

	public String getPayUser() {
		return payUser;
	}

	public void setPayUser(String payUser) {
		this.payUser = payUser;
	}

}