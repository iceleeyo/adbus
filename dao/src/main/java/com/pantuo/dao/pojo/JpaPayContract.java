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
	private String contractCode;
	private Double price;
	private long seriaNum;
	boolean delFlag=false;
	/*
	 * list<integer>
	 * {11,22,33,44}
	 */
	@Column(length = 1024)
	private String orderJson;

	@Lob
	private String orderUserJson;//用户信息保存
	@Lob
	private String customerJson;//客户信息保存

}