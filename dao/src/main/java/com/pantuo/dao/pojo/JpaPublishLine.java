package com.pantuo.dao.pojo;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "publish_line")
public class JpaPublishLine extends CityEntity {

	public static enum Status {
		ready, invalid, enable, close,
		/*初始化,无库存,生效,关闭状态*/
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@ManyToOne
	@JoinColumn(name = "lineId")
	private JpaBusline line;

	@ManyToOne
	@JoinColumn(name = "model_id")
	private JpaBusModel model;
	private int contractId;
	private int salesNumber;//合同生效时购买的线路车辆数量
	private int remainNuber;//实行贴车进行后的 的数量 ,贴车回执处理时减这个数量
	private long seriaNum;//合同唯一号
	private Date startDate;
	private Date endDate;
	private Date lockExpiredTime;//锁定截止日期 这个日期一过 锁定失效
	private String user_id;
	private Status stats = Status.ready;
	 private int days;
     private String  batch;//批次
     private String  unitPrice;//发布费单价
     private String  publishValue;//发布价值
     private String  discountrate;//折扣率
     private String  discountPrice;//优惠后金额
     

	public long getSeriaNum() {
		return seriaNum;
	}

	public JpaBusModel getModel() {
		return model;
	}

	public void setModel(JpaBusModel model) {
		this.model = model;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public void setSeriaNum(long seriaNum) {
		this.seriaNum = seriaNum;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public JpaBusline getLine() {
		return line;
	}

	public void setLine(JpaBusline line) {
		this.line = line;
	}

	public int getContractId() {
		return contractId;
	}

	public void setContractId(int contractId) {
		this.contractId = contractId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getSalesNumber() {
		return salesNumber;
	}

	public void setSalesNumber(int salesNumber) {
		this.salesNumber = salesNumber;
	}

	public int getRemainNuber() {
		return remainNuber;
	}

	public void setRemainNuber(int remainNuber) {
		this.remainNuber = remainNuber;
	}

	 
	public Status getStats() {
		return stats;
	}

	public void setStats(Status stats) {
		this.stats = stats;
	}

	public Date getLockExpiredTime() {
		return lockExpiredTime;
	}

	public void setLockExpiredTime(Date lockExpiredTime) {
		this.lockExpiredTime = lockExpiredTime;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	public String getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getPublishValue() {
		return publishValue;
	}

	public void setPublishValue(String publishValue) {
		this.publishValue = publishValue;
	}

	public String getDiscountrate() {
		return discountrate;
	}

	public void setDiscountrate(String discountrate) {
		this.discountrate = discountrate;
	}

	public String getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(String discountPrice) {
		this.discountPrice = discountPrice;
	}
   
}
