package com.pantuo.web.push;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Principal;

import nl.justobjects.pushlet.core.Dispatcher;
import nl.justobjects.pushlet.core.Event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pantuo.service.security.Request;
import com.pantuo.util.JsonTools;

/**
 * 
 * <b><code>ScheduleProgressListener</code></b>
 * <p>
 * </p>
 * <b>Creation Time:</b> 2015年11月26日 上午10:08:07
 * @author impanxh@gmail.com
 * @since pantuo 1.0-SNAPSHOT
 */
public class PushLet implements PushInter {
	private static Logger log = LoggerFactory.getLogger(PushLet.class);
	Principal principal;
	String url;
	boolean mockBusy = true;

	public PushLet() {
		super();
	}

	public PushLet(String url, Principal principal) {
		this.principal = principal;
		this.url = url;
	}

	public PushLet(String url, Principal principal, boolean mockBusy) {
		this.principal = principal;
		this.url = url;
		this.mockBusy = mockBusy;
	}

	public void pushMsgToClient(String msg) {
		String userid = Request.getUserId(principal);
		Event event = Event.createDataEvent(url + userid);
		try {
			msg = URLEncoder.encode(msg, "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		event.setField("message", msg);
		Dispatcher.getInstance().multicast(event);//消息的推送有单播，组播，广播三种方式，对应不同的api调用
		log.info("#pushMsgToClient:{},msg{}", url + userid, msg);
		if (mockBusy) {
			try {
				int sleepTime = (int) Math.abs((Math.random() * 1000 + 2000));
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
			}
		}
	}

	public void endAndClosePush(Object result) {
		String userid = Request.getUserId(principal);
		Event event = Event.createDataEvent(url + userid);
		String jsonString = JsonTools.getJsonFromObject(result);
		try {
			event.setField("json", URLEncoder.encode(jsonString, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
		}
		event.setField("message", "");
		log.info("#endAndClosePush:{},msg{}", url + userid, jsonString);
		Dispatcher.getInstance().multicast(event);

	}

}