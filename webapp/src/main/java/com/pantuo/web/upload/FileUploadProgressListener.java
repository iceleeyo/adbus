package com.pantuo.web.upload;

import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.ProgressListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * <b><code>FileUploadProgressListener</code></b>
 * <p>
 * 进度监听
 * </p>
 * <b>Creation Time:</b> 2015年5月12日 下午7:27:53
 * @author impanxh@gmail.com
 * @since pantuotech 1.0-SNAPSHOT
 */
public class FileUploadProgressListener implements ProgressListener {

	public FileUploadProgressListener(HttpSession session) {
		setSession(session);
	}

	private HttpSession session;
	private static Logger log = LoggerFactory.getLogger(FileUploadProgressListener.class);

	public void setSession(HttpSession session) {
		//log.info("upload sessionId: " + session.getId());
		this.session = session;
		ProcessInfo status = new ProcessInfo();
		status.setSessionId(session.getId());
		session.setAttribute("proInfo", status);
	}

	/*
	 * pBytesRead 到目前为止读取文件的比特数 pContentLength 文件总大小 pItems 目前正在读取第几个文件
	 */
	public void update(long pBytesRead, long pContentLength, int pItems) {
		ProcessInfo pri = (ProcessInfo) session.getAttribute("proInfo");
		pri.itemNum = pItems;
		pri.readSize = pBytesRead;
		pri.totalSize = pContentLength;
		pri.show = pBytesRead + "/" + pContentLength + " byte";
		pri.rate = Math.round(new Float(pBytesRead) / new Float(pContentLength) * 100);
	}

}