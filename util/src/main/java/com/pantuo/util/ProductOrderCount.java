package com.pantuo.util;
/**
 * 
 * 
 * <b><code>ProductOrderCount</code></b>
 * <p>
 * 产品相应的订单个数统计
 * </p>
 * <b>Creation Time:</b> 2015年6月30日 下午7:05:41
 * @author impanxh@gmail.com
 * @since pantuo 1.0-SNAPSHOT
 */
public class ProductOrderCount {
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
