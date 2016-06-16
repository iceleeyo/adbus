package com.pantuo.dao.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="invoice_detail")
public class JpaInvoiceDetail extends CityEntity{
	 public static enum Type {
		 normal, special,other
	 }
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
	private int mainid;
	private String title;	
	private Type type;	
	private String taxrenum;
	private String bankname;
	private String accountnum;
	private String regisaddr;
	private String fixphone;
	private String mailaddr;
	 private String contactman;
	 private String phonenum;
	 private String userId;
	private String contents;
	 private String receway;
	 @Column(length=1000) 
		public String qulifijsonstr;
	public JpaInvoiceDetail() {
	}
	
	public JpaInvoiceDetail(int id, int mainid, String title, Type type, String taxrenum, String bankname,
			String accountnum, String regisaddr, String fixphone, String mailaddr, String contactman, String phonenum,
			String userId, String contents, String receway) {
		super();
		this.id = id;
		this.mainid = mainid;
		this.title = title;
		this.type = type;
		this.taxrenum = taxrenum;
		this.bankname = bankname;
		this.accountnum = accountnum;
		this.regisaddr = regisaddr;
		this.fixphone = fixphone;
		this.mailaddr = mailaddr;
		this.contactman = contactman;
		this.phonenum = phonenum;
		this.userId = userId;
		this.contents = contents;
		this.receway = receway;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getContactman() {
		return contactman;
	}
	public void setContactman(String contactman) {
		this.contactman = contactman;
	}
	
	public String getQulifijsonstr() {
		return qulifijsonstr;
	}

	public void setQulifijsonstr(String qulifijsonstr) {
		this.qulifijsonstr = qulifijsonstr;
	}

	public String getPhonenum() {
		return phonenum;
	}
	public void setPhonenum(String phonenum) {
		this.phonenum = phonenum;
	}
	public int getMainid() {
		return mainid;
	}
	public void setMainid(int mainid) {
		this.mainid = mainid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
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
	public String getMailaddr() {
		return mailaddr;
	}
	public void setMailaddr(String mailaddr) {
		this.mailaddr = mailaddr;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public String getReceway() {
		return receway;
	}
	public void setReceway(String receway) {
		this.receway = receway;
	}

	@Override
	public String toString() {
		return "JpaInvoiceDetail [id=" + id + ", mainid=" + mainid + ", title=" + title + ", type=" + type
				+ ", taxrenum=" + taxrenum + ", bankname=" + bankname + ", accountnum=" + accountnum + ", regisaddr="
				+ regisaddr + ", fixphone=" + fixphone + ", mailaddr=" + mailaddr + ", contactman=" + contactman
				+ ", phonenum=" + phonenum + ", userId=" + userId + ", contents=" + contents + ", receway=" + receway
				+ "]";
	}
	 
}
