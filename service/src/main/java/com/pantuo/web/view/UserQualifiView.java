package com.pantuo.web.view;

public class UserQualifiView {
  private String user_license;      //用户个人资质  10(营业执照复印件副本)
  private String  user_tax;     //用户个人资质  11(税务登记复印件副本)
  private String  user_code;     //用户个人资质14（组织机构代码证书）
  private String  bank_license;     //银行开户许可证复印件
  private String  common_tag;     //一般纳税人资格认证复印件
  private String  other_qualifi;     //其他资质文件
  
  
public UserQualifiView() {
	super();
}

public String getUser_code() {
	return user_code;
}

public void setUser_code(String user_code) {
	this.user_code = user_code;
}

public String getUser_license() {
	return user_license;
}
public void setUser_license(String user_license) {
	this.user_license = user_license;
}
public String getUser_tax() {
	return user_tax;
}
public void setUser_tax(String user_tax) {
	this.user_tax = user_tax;
}

public String getBank_license() {
	return bank_license;
}

public void setBank_license(String bank_license) {
	this.bank_license = bank_license;
}

public String getCommon_tag() {
	return common_tag;
}

public void setCommon_tag(String common_tag) {
	this.common_tag = common_tag;
}

public String getOther_qualifi() {
	return other_qualifi;
}

public void setOther_qualifi(String other_qualifi) {
	this.other_qualifi = other_qualifi;
}
  
}
