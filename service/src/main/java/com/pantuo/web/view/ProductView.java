package com.pantuo.web.view;

import com.pantuo.dao.pojo.JpaProduct;
/**
 * 
 * <b><code>ProductView</code></b>
 * <p>
 * Comment here.
 * </p>
 * <b>Creation Time:</b> 2015年6月30日 下午7:10:29
 * @author impanxh@gmail.com
 * @since pantuo 1.0-SNAPSHOT
 */
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
