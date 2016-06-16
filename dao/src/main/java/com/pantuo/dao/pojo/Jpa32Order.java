package com.pantuo.dao.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "video32Order")
public class Jpa32Order extends BaseEntity {
	/**
	 * Comment here.
	 *
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public static final long serialVersionUID = 3501204647397695580L;

	public static enum PayType {
		online, contract, check, remit, cash, offline, dividpay
	}

	public static enum Status {
		ings, over, colse;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int id;

	public String userId;

	@ManyToOne
	@JoinColumn(name = "invoiceId")
	public JpaInvoiceDetail invoiceDetail;
	public PayType payType;
	public Status stats;
	public String ordRemark;
	public String closeRemark;
	public String creator;
	@Column(length = 2000)
	public String orderUserJson;//用户信息保存
	@Column(length = 2000)
	public String customerJson;//客户信息保存
	public long runningNum;//交易流水号
	public double price = 0; //订单价格 管理员可以根据套餐价格调整订单的价格 比如打折 促销
	public double payPrice = 0;//已支付金额

	public Jpa32Order() {
		//for serialization
	}

}
