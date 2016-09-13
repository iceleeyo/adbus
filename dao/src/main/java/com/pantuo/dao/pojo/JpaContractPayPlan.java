package com.pantuo.dao.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.pantuo.dao.pojo.JpaOrders.PayType;

/**
 * 
 * <b><code>JpaPayPlan</code></b>
 * <p>
 * 分期表
 * 
 * 
 * 注意点：设置分期时候 后期设置的时间不能小于前期的时间 
 * 删除的时候判断用户是否已支付 payUser==null表示未有人支付
 * </p>
 * <b>Creation Time:</b> 2016年4月6日 上午9:48:02
 * @author impanxh@gmail.com
 * @since pantuo 1.0-SNAPSHOT
 */
@Entity
@Table(name = "pay_plan")
@NoArgsConstructor
@Getter
@Setter
public class JpaContractPayPlan extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private Date day;//分期付款日  4.5  4.8     delete if payUser==null
	private double price = 0;
	private int periodNum; //期数
	@ManyToOne
	@JoinColumn(name = "orderId")
	private JpaPayContract order;
	private PayType payType;//付款方式 
	private PayState payState; //付款状态 从 init 到check 到payed
	@Column(length = 32)
	private String setPlanUser;//分期设置人
	@Column(length = 32)
	private String payUser;//支付人
	private String reduceUser;//分期确认人
	private String remarks;

	public static enum PayState {
		payed/*已支付*/, init/*未支付*/, fail/*未收到款项*/, check/*等待款项检查*/
	}

}