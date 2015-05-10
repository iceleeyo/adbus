package com.pantuo.service;

import java.security.Principal;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.pantuo.mybatis.domain.Message;
import com.pantuo.web.view.MessageView;
/**
 * 
 * <b><code>MessageService</code></b>
 * <p>
 * 消息接口
 * </p>
 * <b>Creation Time:</b> 2015年5月10日 上午11:10:13
 * @author impanxh@gmail.com
 * @since pantuotech 1.0-SNAPSHOT
 */
@Service
public interface MessageService {
	/**
	 * 
	 * 
	 * 增加消息
	 *
	 * @param messages
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	public void addMsg(Message messages);
	/**
	 * 
	 * 按用户查消息
	 *
	 * @param page
	 * @param pageSize
	 * @param principal
	 * @return
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	Page<MessageView> getValidMessage(int page, int pageSize,  Principal principal );
	/**
	 * 
	 * 
	 * 查未读消息条数 
	 *
	 * @param uname
	 * @return
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	long getUnReadCount(String uname);

}
