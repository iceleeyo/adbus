package com.pantuo.service.impl;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.mysema.query.types.Predicate;
import com.pantuo.dao.MessageRepository;
import com.pantuo.dao.OrdersRepository;
import com.pantuo.dao.pojo.JpaMessage;
import com.pantuo.dao.pojo.JpaOrders;
import com.pantuo.dao.pojo.QJpaMessage;
import com.pantuo.mybatis.domain.Message;
import com.pantuo.mybatis.persistence.MessageMapper;
import com.pantuo.service.MessageService;
import com.pantuo.service.security.Request;
import com.pantuo.web.view.MessageView;

@Service
public class MessageServiceImpl implements MessageService {

	@Autowired
	MessageMapper messageMapper;

	@Autowired
	MessageRepository messageRepository;

	@Autowired
	OrdersRepository orderRepository;

	public MessageServiceImpl() {
	}

	public void addMsg(Message messages) {
		messages.setCreated(new Date());
		messages.setUpdated(messages.getCreated());
		messageMapper.insert(messages);
	}

	public Page<MessageView> getValidMessage(int page, int pageSize, Principal principal) {
		if (page < 0)
			page = 0;
		if (pageSize < 1)
			pageSize = 1;
		Sort sort = new Sort(Sort.Direction.DESC, "id");
		Pageable p = new PageRequest(page, pageSize, sort);
		Predicate predicate = QJpaMessage.jpaMessage.recID.eq(Request.getUserId(principal));
		Page<JpaMessage> list = messageRepository.findAll(predicate, p);
		List<MessageView> r = new ArrayList<MessageView>();
		if (list != null && !list.getContent().isEmpty()) {
			List<Integer> ids = new ArrayList<Integer>();
			for (JpaMessage jpaMessage : list.getContent()) {
				ids.add(jpaMessage.getOrderid());
			}
			List<JpaOrders> orderList = orderRepository.findAll(ids);
			Map<Integer, JpaOrders> map = new HashMap<Integer, JpaOrders>();
			for (JpaOrders jpaOrders : orderList) {
				map.put(jpaOrders.getId(), jpaOrders);
			}
			for (JpaMessage jpaMessage : list.getContent()) {
				MessageView w = new MessageView(jpaMessage, jpaMessage.getOrderid(), map.get(jpaMessage.getOrderid()));
				r.add(w);
			}
			messageRepository.updateMsgMainStatus(Request.getUserId(principal), JpaMessage.Main_type.read);
		}

		return new org.springframework.data.domain.PageImpl<MessageView>(r, p, list.getTotalElements());
	}

	public long getUnReadCount(String uname) {
		com.mysema.query.types.Predicate predicate = QJpaMessage.jpaMessage.main_type.eq(JpaMessage.Main_type.unread)
				.and(QJpaMessage.jpaMessage.recID.eq(uname));
		return messageRepository.count(predicate);
	}
}
