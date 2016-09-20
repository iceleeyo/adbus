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

import com.pantuo.dao.pojo.JpaOrders.PayType;

/**
 * 
 * <b><code>JpaPayPlan</code></b>
 * <p>
 * 分期表
 * 
 * 
 * 注意点：设置分期时候 后期设置的时间不能小于前期的时间 
 * 删除的时候判断用户是否已支付 payUser==null表示未有人支付
 * </p>
 * <b>Creation Time:</b> 2016年4月6日 上午9:48:02
 * @author impanxh@gmail.com
 * @since pantuo 1.0-SNAPSHOT
 */
@Entity
@Table(name = "pay_plan")
public class JpaPayPlan extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private Date day;//分期付款日  4.5  4.8     delete if payUser==null
	private double price = 0;
     private int periodNum;  //期数
     private long seriaNum;
     @ManyToOne
 	@JoinColumn(name = "contractId")
 	private JpaPayContract contract;
     
	@ManyToOne
	@JoinColumn(name = "orderId")
	private JpaOrders order;
	private PayType payType;//付款方式 
	private Type type;//类型
	private PayState payState; //付款状态 从 init 到check 到payed
	@Column(length=32) 
	private String setPlanUser;//分期设置人
	@Column(length=32) 
	private String payUser;//支付人
	private String reduceUser;//分期确认人
	private String remarks;
	

	public static enum PayState {
		payed/*已支付*/,	init/*未支付*/, fail/*未收到款项*/, check/*等待款项检查*/
	}
	public static enum Type {
		order/*订单*/,	contract/*合同*/
	}

	public JpaPayPlan() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPeriodNum() {
		return periodNum;
	}

	public void setPeriodNum(int periodNum) {
		this.periodNum = periodNum;
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

	public long getSeriaNum() {
		return seriaNum;
	}

	public void setSeriaNum(long seriaNum) {
		this.seriaNum = seriaNum;
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

	public PayState getPayState() {
		return payState;
	}

	public void setPayState(PayState payState) {
		this.payState = payState;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getReduceUser() {
		return reduceUser;
	}

	public void setReduceUser(String reduceUser) {
		this.reduceUser = reduceUser;
	}

	public JpaPayContract getContract() {
		return contract;
	}

	public void setContract(JpaPayContract contract) {
		this.contract = contract;
	}

	 
}