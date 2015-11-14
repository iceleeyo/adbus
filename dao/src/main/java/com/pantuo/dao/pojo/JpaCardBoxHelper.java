package com.pantuo.dao.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.pantuo.dao.pojo.JpaCity.MediaType;
import com.pantuo.dao.pojo.JpaOrders.PayType;

/**
 */
@Entity
@Table(name = "cardbox_helper")
public class JpaCardBoxHelper extends CityEntity {
	  public static enum Stats {
	        init ("待审核"),
	        pass ("审核通过"),
	        refu ("订单已拒绝");
	        private final String nameStr;
	        private Stats(String nameStr) {
	            this.nameStr = nameStr;
	        }
	        public String getNameStr () {
	            return nameStr;
	        }
	    }
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
	private int isdivid=0;  //默认不分期
	private PayType payType;
	private String mediaMark;
	private String userid;
	private String remarks;
	private long seriaNum;//表单序列号

	private double totalMoney;
	private int productCount;
	private int isPay;
	private MediaType mediaType;
	private Stats stats;
	
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public JpaSupplies getSupplies() {
		return supplies;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public int getIsdivid() {
		return isdivid;
	}

	public void setIsdivid(int isdivid) {
		this.isdivid = isdivid;
	}

	public Stats getStats() {
		return stats;
	}

	public void setStats(Stats stats) {
		this.stats = stats;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
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

	public double getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(double totalMoney) {
		this.totalMoney = totalMoney;
	}

	public int getProductCount() {
		return productCount;
	}

	public void setProductCount(int productCount) {
		this.productCount = productCount;
	}

	public int getIsPay() {
		return isPay;
	}

	public void setIsPay(int isPay) {
		this.isPay = isPay;
	}

	public MediaType getMediaType() {
		return mediaType;
	}

	public void setMediaType(MediaType mediaType) {
		this.mediaType = mediaType;
	}

	 
 

}