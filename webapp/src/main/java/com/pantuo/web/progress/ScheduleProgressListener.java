package com.pantuo.web.progress;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Principal;
import java.util.Arrays;

import javax.servlet.http.HttpSession;

import nl.justobjects.pushlet.core.Dispatcher;
import nl.justobjects.pushlet.core.Event;
import nl.justobjects.pushlet.util.Log;

import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.lang3.StringUtils;

import com.pantuo.service.ScheduleService.SchedUltResult;
import com.pantuo.util.JsonTools;
import com.pantuo.util.Request;

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

	
	
	public static enum Type {
		schInfo, _checkFeature;
	}
	public static Type[] CHECK_TYPE =  new Type[]{
			Type._checkFeature,
	};
	public static Type[] SCH_TYPE =  new Type[]{
			Type.schInfo,
	};

	Type _key = Type.schInfo;
	Principal principal;

	public ScheduleProgressListener(HttpSession session, Principal principal) {
		this.principal = principal;
		setSession(session);
	}
	public ScheduleProgressListener(HttpSession session,Principal principal, Type key) {
		this.principal = principal;
		this._key = key;
		setSession(session);
		
	}
	public ScheduleProgressListener(HttpSession session, Type key) {
		this._key = key;
		setSession(session);
	}

	private HttpSession session;

	public void setSession(HttpSession session) {
		this.session = session;
		ScheduleInfo status = new ScheduleInfo();
		status.setSessionId(session.getId());
		session.setAttribute(_key.name(), status);
	}

	/*
	 * pBytesRead 到目前为止读取文件的比特数 pContentLength 文件总大小 pItems 目前正在读取第几个文件
	 */
	public void update(String msg) {
		if (StringUtils.equals(_key.name(), Type.schInfo.name())) {
			ScheduleInfo pri = (ScheduleInfo) session.getAttribute(_key.name());
			pri.show = msg;
			pushMsgToClient(msg);
		}
	}
	
	
	
	public void update(String msg, Type[] types) {
		if (Arrays.asList(types).contains(_key)) {
			ScheduleInfo pri = (ScheduleInfo) session.getAttribute(_key.name());
			pri.show = msg;
			pushMsgToClient(msg);
		}
	}
	
	

	private void pushMsgToClient(String msg) {
		String userid = Request.getUserId(principal);
		Event event = Event.createDataEvent("/schedulePush/" + userid);
		try {
			msg = URLEncoder.encode(msg, "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		event.setField("message", msg);
		event.setField("from", _key.name());
		Dispatcher.getInstance().multicast(event);//消息的推送有单播，组播，广播三种方式，对应不同的api调用
	}

	public void updateInfo(String msg) {
		ScheduleInfo pri = (ScheduleInfo) session.getAttribute(_key.name());
		pri.show = msg;
		pushMsgToClient(msg);

	}

	public void endResult(SchedUltResult result) {

		ScheduleInfo pri = (ScheduleInfo) session.getAttribute(_key.name());
		pri.setResult(result);
		if (result != null) {
			result.setReqType(_key.name());
		}
		Log.info(result.toString());
		String userid = Request.getUserId(principal);
		Event event = Event.createDataEvent("/schedulePush/" + userid);

		String jsonString = JsonTools.getJsonFromObject(result);
		try {
			event.setField("json", URLEncoder.encode(jsonString, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
		}
		event.setField("message", "");
		Dispatcher.getInstance().multicast(event);

	}

	@Override
	public void update(long pBytesRead, long pContentLength, int pItems) {
	}

}