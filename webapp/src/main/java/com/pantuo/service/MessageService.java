package com.pantuo.service;

import java.security.Principal;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.pantuo.mybatis.domain.Message;
import com.pantuo.web.view.MessageView;

@Service
public interface MessageService {

	public void addMsg(Message messages);

	Page<MessageView> getValidMessage(int page, int pageSize,  Principal principal );

	long getUnReadCount(String uname);

}
