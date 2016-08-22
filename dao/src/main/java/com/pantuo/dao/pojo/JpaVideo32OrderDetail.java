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
@Table(name = "video32OrderDetail")
public class JpaVideo32OrderDetail {

	public static enum Result {
		N, Y, Z
		//拒绝可待办,通过,初始状态
	}

	public JpaVideo32OrderDetail() {
	}

	public static enum Status {
		upload("上传素材"), auth("审核"), schedule("排期"), completed("确认");

		public final String nameStr;

		private Status(String nameStr) {
			this.nameStr = nameStr;
		}

		public String getNameStr() {
			return nameStr;
		}
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int id;

	public String userId;
	@ManyToOne
	@JoinColumn(name = "suppliesId")
	public JpaSupplies supplies;
	@ManyToOne
	@JoinColumn(name = "groupId")
	public JpaVideo32Group group;
	@ManyToOne
	@JoinColumn(name = "orderId")
	public Jpa32Order order;
	public Date startTime;
	public Date endTime;
	public Status stats;
	public long runningNum;//交易流水号
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public JpaSupplies getSupplies() {
		return supplies;
	}
	public void setSupplies(JpaSupplies supplies) {
		this.supplies = supplies;
	}
	 public void setSuppliesId(int suppliesId) {
	        if (supplies == null)
	            supplies = new JpaSupplies();
	        this.supplies.setId(suppliesId);
	    }
	public JpaVideo32Group getGroup() {
		return group;
	}
	public void setGroup(JpaVideo32Group group) {
		this.group = group;
	}
	public Jpa32Order getOrder() {
		return order;
	}
	public void setOrder(Jpa32Order order) {
		this.order = order;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Status getStats() {
		return stats;
	}
	public void setStats(Status stats) {
		this.stats = stats;
	}
	public long getRunningNum() {
		return runningNum;
	}
	public void setRunningNum(long runningNum) {
		this.runningNum = runningNum;
	}

}
