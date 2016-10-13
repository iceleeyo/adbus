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
 * 排期取消历史 
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "schedule_changlog")
public class JpaScheduleChangeLog extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private int orderid; 
	private String userId;
	@Column(length = 16)
	private String startDate;
	@Column(length = 4)
	private String isCallAfterDayAll;
	
	@Lob
	String remark;//备注

}