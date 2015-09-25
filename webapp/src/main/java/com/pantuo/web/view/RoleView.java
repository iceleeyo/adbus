package com.pantuo.web.view;

import java.util.List;

import com.pantuo.mybatis.domain.ActIdGroup;
import com.pantuo.mybatis.domain.BusFunction;

public class RoleView {
	ActIdGroup actIdGroup;
	String functions;
	List<BusFunction> funs;
	String groupId;
	
	

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

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

	public List<BusFunction> getFuns() {
		return funs;
	}

	public void setFuns(List<BusFunction> funs) {
		this.funs = funs;
	}

}
