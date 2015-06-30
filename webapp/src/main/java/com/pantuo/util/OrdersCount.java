package com.pantuo.util;

public class OrdersCount {
	private int product_id;
	private int runningCount;
	private int finishedCount;

	public int getProduct_id() {
		return product_id;
	}

	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}

	public int getRunningCount() {
		return runningCount;
	}

	public void setRunningCount(int runningCount) {
		this.runningCount = runningCount;
	}

	public int getFinishedCount() {
		return finishedCount;
	}

	public void setFinishedCount(int finishedCount) {
		this.finishedCount = finishedCount;
	}

	@Override
	public String toString() {
		return "OrdersCount [product_id=" + product_id + ", runningCount=" + runningCount + ", finishedCount="
				+ finishedCount + "]";
	}

}
