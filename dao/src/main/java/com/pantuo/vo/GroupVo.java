package com.pantuo.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * <b><code>GroupVo</code></b>
 * <p>
 * 统计结果
 * </p>
 * <b>Creation Time:</b> 2014年12月15日 上午10:23:13
 * @author impanxh
 * @since pantuo 1.0-SNAPSHOT
 */
public class GroupVo implements Serializable {

	private static final long serialVersionUID = -2669019626889682762L;
	private int count; //user

	private int gn1;
	private int gn2;
	private int gn3;
	private long gn4;
	private Date gn5;
	private double gnd1;

	//select count(1) as count from table group by gp1,gp2;
	private String gp1;//group 第1个字段   
	private String gp2;//group 第2个字段 
	private String gp3;//group 第2个字段 
	private String gp4;//group 第2个字段 

	public Date getGn5() {
		return gn5;
	}

	public void setGn5(Date gn5) {
		this.gn5 = gn5;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getGp1() {
		return gp1;
	}

	public void setGp1(String gp1) {
		this.gp1 = gp1;
	}

	public String getGp2() {
		return gp2;
	}

	public void setGp2(String gp2) {
		this.gp2 = gp2;
	}

	public String getGp3() {
		return gp3;
	}

	public void setGp3(String gp3) {
		this.gp3 = gp3;
	}

	public String getGp4() {
		return gp4;
	}

	public void setGp4(String gp4) {
		this.gp4 = gp4;
	}

	public int getGn1() {
		return gn1;
	}

	public void setGn1(int gn1) {
		this.gn1 = gn1;
	}

	public int getGn2() {
		return gn2;
	}

	public void setGn2(int gn2) {
		this.gn2 = gn2;
	}

	public int getGn3() {
		return gn3;
	}

	public void setGn3(int gn3) {
		this.gn3 = gn3;
	}

	public long getGn4() {
		return gn4;
	}

	public void setGn4(long gn4) {
		this.gn4 = gn4;
	}

	public double getGnd1() {
		return gnd1;
	}

	public void setGnd1(double gnd1) {
		this.gnd1 = gnd1;
	}

}
