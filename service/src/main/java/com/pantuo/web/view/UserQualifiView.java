package com.pantuo.web.view;

public class UserQualifiView {
  private String user_license;      //用户个人资质  10(营业执照复印件副本)
  private String  user_tax;     //用户个人资质  11(税务登记复印件副本)
  private String  user_code;     //用户个人资质14（组织机构代码证书）
  
  
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
  
}
