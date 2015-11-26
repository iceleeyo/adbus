package com.pantuo.web.progress;

import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.ProgressListener;

import com.pantuo.service.ScheduleService.SchedUltResult;
import com.pantuo.web.upload.ProcessInfo;

/**
 * 
 * <b><code>ScheduleProgressListener</code></b>
 * <p>
 * </p>
 * <b>Creation Time:</b> 2015年11月26日 上午10:08:07
 * @author impanxh@gmail.com
 * @since pantuo 1.0-SNAPSHOT
 */
public class ScheduleProgressListener implements ProgressListener {

	public ScheduleProgressListener(HttpSession session) {
		setSession(session);
	}

	private HttpSession session;

	public void setSession(HttpSession session) {
		this.session = session;
		ScheduleInfo status = new ScheduleInfo();
		status.setSessionId(session.getId());
		session.setAttribute("schInfo", status);
	}

	/*
	 * pBytesRead 到目前为止读取文件的比特数 pContentLength 文件总大小 pItems 目前正在读取第几个文件
	 */
	public void update(String msg) {
		ScheduleInfo pri = (ScheduleInfo) session.getAttribute("schInfo");
		pri.show = msg;
	}

	public void endResult(SchedUltResult result) {
		ScheduleInfo pri = (ScheduleInfo) session.getAttribute("schInfo");
		pri.setResult(result);
	}

	@Override
	public void update(long pBytesRead, long pContentLength, int pItems) {
	}

}