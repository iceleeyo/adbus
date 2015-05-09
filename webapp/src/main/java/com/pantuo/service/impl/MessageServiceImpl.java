package com.pantuo.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pantuo.mybatis.domain.Message;
import com.pantuo.mybatis.persistence.MessageMapper;
import com.pantuo.service.MessageService;

@Service
public class MessageServiceImpl implements MessageService {

	@Autowired
	MessageMapper messageMapper;

	public MessageServiceImpl() {
	}

	public void addMsg(Message messages) {
		messages.setCreated(new Date());
		messages.setUpdated(messages.getCreated());
		messageMapper.insert(messages);
	}

}
