package com.pantuo.pojo;

import java.util.Map;

import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;

public class HistoricTaskView extends HistoricTaskInstanceEntity{

	 
	 
	 
	 
	public Map<String,Object> vars;

	public Map<String, Object> getVars() {
		return vars;
	}

	public void setVars(Map<String, Object> vars) {
		this.vars = vars;
	}
	
	
	public String comment;
	public boolean result;

	public String getComment() {
		return comment;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	 
}
