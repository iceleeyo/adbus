package com.pantuo.web.view;


/**
 * 
 * <b><code>CountMonthView</code></b>
 * <p>
 * 月发布统计报表辅助类
 * </p>
 * <b>Creation Time:</b> 2015年11月6日 下午3:26:10
 * @author xiaoli
 * @since pantuo 1.0-SNAPSHOT
 */
public class CountMonthView {
     public String companyName;//营销中心名称
     public String modelName; //单双层
     public int contractNum;  //合同数
     public int totalcontractNum;//合同数合计
     public int nor_Snum;//正常上刊特级车辆数
     public int nor_APPnum;//A++车辆数
     public int nor_APnum;//A+车辆数
     public int nor_Anum;//A车辆数
     public int nor_xiaoji;//小计
     
     public int xu_Snum;//续刊特级车辆数
     public int xu_APPnum;//A++车辆数
     public int xu_APnum;//A+车辆数
     public int xu_Anum;//A车辆数
     public int xu_xiaoji;//小计
     
     public int one_num;//刊期为1个月的车辆数
     public int three_num;//刊期为3个月的车辆数
     public int six_num;//刊期为6个月的车辆数
     public int twelve_num;//刊期为12月的车辆数
     public int other_num;//其他
     public int days_xiaoji;//小计
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public int getContractNum() {
		return contractNum;
	}
	public void setContractNum(int contractNum) {
		this.contractNum = contractNum;
	}
	public int getTotalcontractNum() {
		return totalcontractNum;
	}
	public void setTotalcontractNum(int totalcontractNum) {
		this.totalcontractNum = totalcontractNum;
	}
	public int getNor_Snum() {
		return nor_Snum;
	}
	public void setNor_Snum(int nor_Snum) {
		this.nor_Snum = nor_Snum;
	}
	public int getNor_APPnum() {
		return nor_APPnum;
	}
	public void setNor_APPnum(int nor_APPnum) {
		this.nor_APPnum = nor_APPnum;
	}
	public int getNor_APnum() {
		return nor_APnum;
	}
	public void setNor_APnum(int nor_APnum) {
		this.nor_APnum = nor_APnum;
	}
	public int getNor_Anum() {
		return nor_Anum;
	}
	public void setNor_Anum(int nor_Anum) {
		this.nor_Anum = nor_Anum;
	}
	public int getNor_xiaoji() {
		return nor_xiaoji;
	}
	public void setNor_xiaoji(int nor_xiaoji) {
		this.nor_xiaoji = nor_xiaoji;
	}
	public int getXu_Snum() {
		return xu_Snum;
	}
	public void setXu_Snum(int xu_Snum) {
		this.xu_Snum = xu_Snum;
	}
	public int getXu_APPnum() {
		return xu_APPnum;
	}
	public void setXu_APPnum(int xu_APPnum) {
		this.xu_APPnum = xu_APPnum;
	}
	public int getXu_APnum() {
		return xu_APnum;
	}
	public void setXu_APnum(int xu_APnum) {
		this.xu_APnum = xu_APnum;
	}
	public int getXu_Anum() {
		return xu_Anum;
	}
	public void setXu_Anum(int xu_Anum) {
		this.xu_Anum = xu_Anum;
	}
	public int getXu_xiaoji() {
		return xu_xiaoji;
	}
	public void setXu_xiaoji(int xu_xiaoji) {
		this.xu_xiaoji = xu_xiaoji;
	}
	public int getOne_num() {
		return one_num;
	}
	public void setOne_num(int one_num) {
		this.one_num = one_num;
	}
	public int getThree_num() {
		return three_num;
	}
	public void setThree_num(int three_num) {
		this.three_num = three_num;
	}
	public int getSix_num() {
		return six_num;
	}
	public void setSix_num(int six_num) {
		this.six_num = six_num;
	}
	public int getTwelve_num() {
		return twelve_num;
	}
	public void setTwelve_num(int twelve_num) {
		this.twelve_num = twelve_num;
	}
	public int getOther_num() {
		return other_num;
	}
	public void setOther_num(int other_num) {
		this.other_num = other_num;
	}
	public int getDays_xiaoji() {
		return days_xiaoji;
	}
	public void setDays_xiaoji(int days_xiaoji) {
		this.days_xiaoji = days_xiaoji;
	}
     
}
