package com.pantuo.dao.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="invoice")
public class JpaInvoice extends CityEntity{
	 public static enum Type {
		 normal, special,other
	 }

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
	
	private String title;	
	private Type type;	
	private String taxrenum;
	private String bankname;
	private String accountnum;
	private String regisaddr;
	private String fixphone;
	private String mailaddr;
	 private String userId;
	public JpaInvoice(){
		
	}
	
	public JpaInvoice(int id, String title, Type type, String taxrenum,
			String bankname, String accountnum, String regisaddr,
			String fixphone, String mailaddr, String userId) {
		super();
		this.id = id;
		this.title = title;
		this.type = type;
		this.taxrenum = taxrenum;
		this.bankname = bankname;
		this.accountnum = accountnum;
		this.regisaddr = regisaddr;
		this.fixphone = fixphone;
		this.mailaddr = mailaddr;
		this.userId = userId;
	}

	public String getMailaddr() {
		return mailaddr;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setMailaddr(String mailaddr) {
		this.mailaddr = mailaddr;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTaxrenum() {
		return taxrenum;
	}
	public void setTaxrenum(String taxrenum) {
		this.taxrenum = taxrenum;
	}
	public String getBankname() {
		return bankname;
	}
	public void setBankname(String bankname) {
		this.bankname = bankname;
	}
	public String getAccountnum() {
		return accountnum;
	}
	public void setAccountnum(String accountnum) {
		this.accountnum = accountnum;
	}
	public String getRegisaddr() {
		return regisaddr;
	}
	public void setRegisaddr(String regisaddr) {
		this.regisaddr = regisaddr;
	}
	public String getFixphone() {
		return fixphone;
	}
	public void setFixphone(String fixphone) {
		this.fixphone = fixphone;
	}

	@Override
	public String toString() {
		return "JpaInvoice [id=" + id + ", title=" + title + ", type=" + type
				+ ", taxrenum=" + taxrenum + ", bankname=" + bankname
				+ ", accountnum=" + accountnum + ", regisaddr=" + regisaddr
				+ ", fixphone=" + fixphone + "]";
	}
	

}
