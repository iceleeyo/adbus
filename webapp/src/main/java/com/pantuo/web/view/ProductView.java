package com.pantuo.web.view;

import com.pantuo.dao.pojo.JpaProduct;

public class ProductView extends JpaProduct {
	private int runningCount;
	private int finishedCount;
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
	
	
	

}
