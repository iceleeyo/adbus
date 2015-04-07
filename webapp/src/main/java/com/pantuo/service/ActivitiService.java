package com.pantuo.service;

import java.util.List;
import java.util.Map;

import com.pantuo.dao.pojo.UserDetail;
import com.pantuo.mybatis.domain.Orders;
import com.pantuo.util.NumberPageUtil;
import com.pantuo.util.Pair;
import com.pantuo.web.view.OrderView;

public interface ActivitiService {
	/**
	 * 
	 * 启动工作流
	 *
	 * @param u
	 * @param order
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	public void startProcess(UserDetail u, Orders order);

	public Pair<Boolean, String> payment(int orderid, String taskid, UserDetail u);

	public List<OrderView> findTask(String userid, NumberPageUtil page);

	public Pair<Boolean, String> handle(String orderid, String taskid, String comment, String isok, UserDetail user);
	/**
	 * 
	 * 办理任务
	 *
	 * @param taskId
	 * @param variables
	 * @param u
	 * @return
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	public Pair<Boolean, String> complete(String taskId, Map<String, Object> variables, UserDetail u);

	/*
	 * 扫描工作流 去掉订单不存在的工作流
	 */
	public String reset(String p);

	public OrderView findOrderViewByTaskId(String taskid);
}
