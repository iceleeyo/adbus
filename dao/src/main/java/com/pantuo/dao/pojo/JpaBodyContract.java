package com.pantuo.dao.pojo;

import javax.persistence.*;
import java.util.Date;

/**
 * @author tliu
 *
 * 合同
 */
@Entity
@Table(name = "bodycontract")
public class JpaBodyContract extends CityEntity {

	public static enum Status {
		ready, enable, close,
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private int contractid;
	private String legalman;
	private String relateMan;
	private String company;
	private String companyAddr;
	private String phoneNum;
	private String contractCode;
	private String contractName;
	private String remark;
	private String creator;
	private String amounts;
	private long seriaNum;
	Status stats = Status.ready;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getSeriaNum() {
		return seriaNum;
	}

	public int getContractid() {
		return contractid;
	}

	public void setContractid(int contractid) {
		this.contractid = contractid;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public Status getStats() {
		return stats;
	}

	public void setStats(Status stats) {
		this.stats = stats;
	}

	 
}