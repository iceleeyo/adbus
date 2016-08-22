package com.pantuo.dao.pojo;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.pantuo.dao.pojo.JpaBusline.Level;

@Entity
@Table(name = "Video32OrderStatus")
public class JpaVideo32OrderStatus extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int id;

	public static enum Result {
		N, Y, Z
		// 拒绝可待办,通过,初始状态
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
	 @Transient
	 public static Map<String, Status> statusMap = new HashMap<String, Status>();
	 static {
		 statusMap.put("advertiser", Status.paid);
		 statusMap.put("ShibaFinancialManager", Status.payed);
	 }
	@ManyToOne
	@JoinColumn(name = "orderId")
	public Jpa32Order order;
	public String operation;
	public String creater;
	public Result r = Result.Z;
	public Status status;
	public String comment;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Jpa32Order getOrder() {
		return order;
	}

	public void setOrder(Jpa32Order order) {
		this.order = order;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public Result getR() {
		return r;
	}

	public void setR(Result r) {
		this.r = r;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
