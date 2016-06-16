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

}
