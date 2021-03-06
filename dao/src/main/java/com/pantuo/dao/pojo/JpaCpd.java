package com.pantuo.dao.pojo;

import javax.persistence.*;

import java.util.Date;

/**
 * 
 * <b><code>JpaCpd</code></b>
 * <p>
 * cpd 规则
 * </p>
 * <b>Creation Time:</b> 2015年7月1日 上午10:45:23
 * @author impanxh@gmail.com
 * @since pantuo 1.0-SNAPSHOT
 */
@Entity
@Table(name = "role_cpd")
public class JpaCpd extends BaseEntity {
	//是否扣费
	public static enum OverType {
		wait, over;
	}

	public static enum State {
		online, offline;
	}
	//检查是否下过订单
	public static enum CheckOrder {
		Y, N;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@OneToOne
	@JoinColumn(name = "productId")
	private JpaProduct product;
	private double saleprice; //售价
	private double comparePrice;//当前用户竞价
	private String userId;
	private Date startDate;
	private Date biddingDate;
	private OverType ispay;
	private State state;
	private CheckOrder checkOrder;
	private int pv;
	private int setcount;
	private int orderid;

	public JpaCpd() {
	}

	public JpaCpd(int id, JpaProduct product, double saleprice, double comparePrice, String userId, Date startDate,
			Date biddingDate, OverType ispay, State state, CheckOrder checkOrder, int pv, int setcount, int orderid) {
		super();
		this.id = id;
		this.product = product;
		this.saleprice = saleprice;
		this.comparePrice = comparePrice;
		this.userId = userId;
		this.startDate = startDate;
		this.biddingDate = biddingDate;
		this.ispay = ispay;
		this.state = state;
		this.checkOrder = checkOrder;
		this.pv = pv;
		this.setcount = setcount;
		this.orderid = orderid;
	}

	public JpaCpd(String name) {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public JpaProduct getProduct() {
		return product;
	}

	public void setProduct(JpaProduct product) {
		this.product = product;
	}

	public double getSaleprice() {
		return saleprice;
	}

	public void setSaleprice(double saleprice) {
		this.saleprice = saleprice;
	}

	public double getComparePrice() {
		return comparePrice;
	}

	public void setComparePrice(double comparePrice) {
		this.comparePrice = comparePrice;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getBiddingDate() {
		return biddingDate;
	}

	public void setBiddingDate(Date biddingDate) {
		this.biddingDate = biddingDate;
	}


	public OverType getIspay() {
		return ispay;
	}

	public void setIspay(OverType ispay) {
		this.ispay = ispay;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public int getPv() {
		return pv;
	}

	public void setPv(int pv) {
		this.pv = pv;
	}

	public int getSetcount() {
		return setcount;
	}

	public void setSetcount(int setcount) {
		this.setcount = setcount;
	}

	public CheckOrder getCheckOrder() {
		return checkOrder;
	}

	public void setCheckOrder(CheckOrder checkOrder) {
		this.checkOrder = checkOrder;
	}

	public int getOrderid() {
		return orderid;
	}

	public void setOrderid(int orderid) {
		this.orderid = orderid;
	}

	@Override
	public String toString() {
		return "JpaCpd [id=" + id + ", product=" + product + ", saleprice=" + saleprice + ", comparePrice="
				+ comparePrice + ", userId=" + userId + ", startDate=" + startDate + ", biddingDate=" + biddingDate
				+ ", ispay=" + ispay + ", state=" + state + ", checkOrder=" + checkOrder + ", pv=" + pv + ", setcount="
				+ setcount + ", orderid=" + orderid + "]";
	}

}