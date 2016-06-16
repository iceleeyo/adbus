package com.pantuo.dao.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Video32OrderStatus")
public class JpaVideo32OrderStatus extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int id;

	public static enum Result {
		N, Y, Z
		//拒绝可待办,通过,初始状态
	}

	public JpaVideo32OrderStatus() {
		super();
	}
	public static enum Status {
		paid("支付"), payed("账务确认");
		public final String nameStr;

		private Status(String nameStr) {
			this.nameStr = nameStr;
		}

		public String getNameStr() {
			return nameStr;
		}
	}

	@ManyToOne
	@JoinColumn(name = "orderId")
	public Jpa32Order order;
	public String operation;
	public String creater;
	public Result r = Result.Z;
	public String comment;

}
