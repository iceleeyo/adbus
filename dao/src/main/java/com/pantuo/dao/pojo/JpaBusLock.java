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
@Table(name = "bus_lock")
public class JpaBusLock extends CityEntity {

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

	//private int modelid;
	private int contractId;
	private int salesNumber;//合同生效时购买的线路车辆数量
	private int remainNuber;//实行贴车进行后的 的数量 ,贴车回执处理时减这个数量
	private long seriaNum;//合同唯一号
	private Date startDate;
	private Date endDate;
	
	private Date lockExpiredTime;//锁定截止日期 这个日期一过 锁定失效
	private String user_id;
	private Status stats = Status.ready;

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

}
