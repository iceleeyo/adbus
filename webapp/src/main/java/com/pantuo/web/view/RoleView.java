package com.pantuo.web.view;

import com.pantuo.mybatis.domain.ActIdGroup;

public class RoleView {
   ActIdGroup actIdGroup;
   String functions;
public ActIdGroup getActIdGroup() {
	return actIdGroup;
}
public void setActIdGroup(ActIdGroup actIdGroup) {
	this.actIdGroup = actIdGroup;
}
public String getFunctions() {
	return functions;
}
public void setFunctions(String functions) {
	this.functions = functions;
}
   
}
