package com.pantuo.web.progress;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpSession;

import nl.justobjects.pushlet.core.Dispatcher;
import nl.justobjects.pushlet.core.Event;

import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.pantuo.service.ScheduleService.SchedUltResult;

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

	Type _key = Type.schInfo;

	public ScheduleProgressListener(HttpSession session) {
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
		}
		Event event = Event.createDataEvent("/ynb/helloworld");
		try {
			msg = URLEncoder.encode(msg,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			//e.printStackTrace();
		} 
		event.setField("message", msg);
		
		Dispatcher.getInstance().multicast(event);//消息的推送有单播，组播，广播三种方式，对应不同的api调用
		System.out.println(event.toString());
	}

	public void updateInfo(String msg) {
		ScheduleInfo pri = (ScheduleInfo) session.getAttribute(_key.name());
		pri.show = msg;

		
		
	}

	public void endResult(SchedUltResult result) {
		ScheduleInfo pri = (ScheduleInfo) session.getAttribute(_key.name());
		pri.setResult(result);
		ObjectMapper t = new ObjectMapper();
		t.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		t.getSerializationConfig().setSerializationInclusion(Inclusion.NON_NULL);
		try {
			String jsonString = t.writeValueAsString(result);
			Event event = Event.createDataEvent("/ynb/helloworld");
			event.setField("abc", URLEncoder.encode(jsonString,"UTF-8"));
			event.setField("message", "dddd");
			Dispatcher.getInstance().multicast(event);//消息的推送有单播，组播，广播三种方式，对应不同的api调用
			System.out.println(event.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(long pBytesRead, long pContentLength, int pItems) {
	}

}