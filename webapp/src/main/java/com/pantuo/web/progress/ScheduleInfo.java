package com.pantuo.web.progress;

import com.pantuo.service.ScheduleService.SchedUltResult;

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
public class ScheduleInfo {
	 
	public String show = "";
	public String sessionId ; 
	public boolean isOver=false;
	
	//-------------------
	private SchedUltResult result;

	public String getShow() {
		return show;
	}

	public void setShow(String show) {
		this.show = show;
	}

	public boolean isOver() {
		return isOver;
	}

	public void setOver(boolean isOver) {
		this.isOver = isOver;
	}

	public SchedUltResult getResult() {
		return result;
	}

	public void setResult(SchedUltResult result) {
		isOver=true;
		this.result = result;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
 

}
