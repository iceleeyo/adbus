package com.pantuo.dao.pojo;

import javax.persistence.*;

import java.util.Date;

/**
 * @author xiaoli
 *
 * 线下合同
 */
@Entity
@Table(name = "offlinecontract")
public class JpaOfflineContract extends CityEntity {


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String legalman;
	private String relateMan;  //客户名称
	private String company;
	private String companyAddr;
	private String phoneNum;
	private String contractCode;
	private String contractName;
	private String remarks;
	private String creator;
	private String amounts;
	private long seriaNum;
	private boolean isSchedule = false;
	private Date signDate;       //签订日期
	private int days;           //刊期
	private int totalNum;        //总数
	private String salesman;    //业务员
	private String adway;        //广告形式
	private String adcontent;  //广告内容
	private String linecontent;//发布线路
	private String payway;      //付款方式
	private Date payDate;      //付款日期
	private String markcenter;//营销中心
//	 @ManyToOne
//	 @JoinColumn(name = "dividPayId")
//	private JpaDividPay dividPay;
//	
//	
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

	public String getLegalman() {
		return legalman;
	}

	public void setLegalman(String legalman) {
		this.legalman = legalman;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCompanyAddr() {
		return companyAddr;
	}

	public void setCompanyAddr(String companyAddr) {
		this.companyAddr = companyAddr;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}


	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getAmounts() {
		return amounts;
	}

	public void setAmounts(String amounts) {
		this.amounts = amounts;
	}

	public String getRelateMan() {
		return relateMan;
	}

	public void setRelateMan(String relateMan) {
		this.relateMan = relateMan;
	}

	public boolean isSchedule() {
		return isSchedule;
	}

	public void setSchedule(boolean isSchedule) {
		this.isSchedule = isSchedule;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getSignDate() {
		return signDate;
	}

	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public String getSalesman() {
		return salesman;
	}

	public void setSalesman(String salesman) {
		this.salesman = salesman;
	}

	public String getAdway() {
		return adway;
	}

	public void setAdway(String adway) {
		this.adway = adway;
	}

	public String getAdcontent() {
		return adcontent;
	}

	public void setAdcontent(String adcontent) {
		this.adcontent = adcontent;
	}


	public String getLinecontent() {
		return linecontent;
	}

	public void setLinecontent(String linecontent) {
		this.linecontent = linecontent;
	}

	public String getPayway() {
		return payway;
	}

	public void setPayway(String payway) {
		this.payway = payway;
	}

	public Date getPayDate() {
		return payDate;
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}

	public String getMarkcenter() {
		return markcenter;
	}

	public void setMarkcenter(String markcenter) {
		this.markcenter = markcenter;
	}

}