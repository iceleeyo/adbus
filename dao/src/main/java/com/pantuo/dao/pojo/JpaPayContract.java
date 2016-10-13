package com.pantuo.dao.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * 支付 合同 
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "paycontract")
public class JpaPayContract extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String userId;
	private String updator;
	private String contractCode;
	private Double price;
	private double payPrice = 0;//已支付金额
	private long seriaNum;
	boolean delFlag=false;   //1为已删除
	boolean closeFlag=false;  //1为关闭
	@Lob
	private String agreement;//补充协议
	/*
	 * list<integer>
	 * {11,22,33,44}
	 */
	@Column(length = 1024)
	private String orderJson;
	@Column(length = 32)
	private String salesName;// 销售名字
	@Lob
	private String remark;//备注

	@Lob
	private String orderUserJson;//用户信息保存
	@Lob
	private String customerJson;//客户信息保存

}