package com.pantuo.web.view;

import com.pantuo.dao.pojo.JpaPayPlan;
import com.pantuo.util.OrderIdSeq;

public class OrderPlanView {

	String payName;

	public OrderPlanView(JpaPayPlan plan) {
		this.plan = plan;

		if (plan != null && plan.getPayType() != null) {
			payName = OrderView.getPayName(plan.getPayType());
		}
	}

	JpaPayPlan plan;

	public JpaPayPlan getPlan() {
		return plan;
	}

	public void setPlan(JpaPayPlan plan) {
		this.plan = plan;
	}

	public long getLongOrderId() {
		if (plan != null && plan.getOrder() != null) {

			return OrderIdSeq.getLongOrderId(plan.getOrder());
		}
		return 0L;
	}

	public String getPayName() {
		return payName;
	}

	public void setPayName(String payName) {
		this.payName = payName;
	}

}
