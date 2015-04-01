package com.pantuo.service;

import java.util.List;

import com.pantuo.dao.pojo.UserDetail;
import com.pantuo.mybatis.domain.Order;
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
	public void startProcess(UserDetail u,Order order);
	
	public Pair<Boolean, String> payment(int orderid,String taskid,UserDetail u);
	
	public List<OrderView> findTask(String userid,NumberPageUtil page) ;

}
