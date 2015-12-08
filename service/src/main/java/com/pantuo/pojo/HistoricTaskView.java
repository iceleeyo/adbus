package com.pantuo.pojo;

import java.util.Map;

import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;

public class HistoricTaskView extends HistoricTaskInstanceEntity {

	public Map<String, Object> vars;

	public Map<String, Object> getVars() {
		return vars;
	}

	public void setVars(Map<String, Object> vars) {
		this.vars = vars;
	}

	public String comment;

	public String result;

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result != null ? (((Boolean) result) ? "同意" : "拒绝") : "--";
	}

}
