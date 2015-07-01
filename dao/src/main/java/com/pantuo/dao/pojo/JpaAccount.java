package com.pantuo.dao.pojo;

import javax.persistence.*;

import java.util.Date;

/**
 * 
 * <b><code>JpaCpd</code></b>
 * <p>
 * </p>
 * <b>Creation Time:</b> 2015年7月1日 上午10:45:23
 * @author impanxh@gmail.com
 * @since pantuo 1.0-SNAPSHOT
 */
@Entity
@Table(name = "account")
public class JpaAccount extends BaseEntity {
	 

	@Id
	private String userId;
	private double totalMoney;//当前用户竞价

	public JpaAccount() {
	}

	public JpaAccount(String name) {

	}

	public double getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(double totalMoney) {
		this.totalMoney = totalMoney;
	}
 

}