package com.pantuo.dao.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.pantuo.dao.pojo.JpaOrders.PayType;

/**
 */
@Entity
@Table(name = "cardbox_helper")
public class JpaCardBoxHelper extends CityEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@ManyToOne
	@JoinColumn(name = "suppliesId")
	private JpaSupplies supplies;

	@ManyToOne
	@JoinColumn(name = "invoiceId")
	private JpaInvoiceDetail invoiceDetail;

	private String fengqi;//1,3,6,9,12期
	private PayType payType;
	private String mediaMark;

	private long seriaNum;//表单序列号

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public JpaSupplies getSupplies() {
		return supplies;
	}

	public void setSupplies(JpaSupplies supplies) {
		this.supplies = supplies;
	}

	public JpaInvoiceDetail getInvoiceDetail() {
		return invoiceDetail;
	}

	public void setInvoiceDetail(JpaInvoiceDetail invoiceDetail) {
		this.invoiceDetail = invoiceDetail;
	}

	public String getFengqi() {
		return fengqi;
	}

	public void setFengqi(String fengqi) {
		this.fengqi = fengqi;
	}

	public PayType getPayType() {
		return payType;
	}

	public void setPayType(PayType payType) {
		this.payType = payType;
	}

	public long getSeriaNum() {
		return seriaNum;
	}

	public void setSeriaNum(long seriaNum) {
		this.seriaNum = seriaNum;
	}

	public String getMediaMark() {
		return mediaMark;
	}

	public void setMediaMark(String mediaMark) {
		this.mediaMark = mediaMark;
	}

}