package com.pantuo.web.upload;

/**
 * 
 * <b><code>ProcessInfo</code></b>
 * <p>
 * 文件进度表
 * </p>
 * <b>Creation Time:</b> 2015年5月12日 下午7:27:40
 * @author impanxh@gmail.com
 * @since pantuotech 1.0-SNAPSHOT
 */
public class ProcessInfo {
	public long totalSize = 1;
	public long readSize = 0;
	public String show = "";
	public int itemNum = 0;
	public int rate = 0;
	public String sessionId ; 

	public long getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(long totalSize) {
		this.totalSize = totalSize;
	}

	public long getReadSize() {
		return readSize;
	}

	public void setReadSize(long readSize) {
		this.readSize = readSize;
	}

	public String getShow() {
		return show;
	}

	public void setShow(String show) {
		this.show = show;
	}

	public int getItemNum() {
		return itemNum;
	}

	public void setItemNum(int itemNum) {
		this.itemNum = itemNum;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	@Override
	public String toString() {
		return "ProcessInfo [show=" + show + ", sessionId=" + sessionId + "]";
	}

}
