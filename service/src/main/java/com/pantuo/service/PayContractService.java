package com.pantuo.service;

import java.security.Principal;
import java.util.List;

import com.pantuo.dao.pojo.JpaOrders;

public interface PayContractService {
	/**
	 * 
	 * 查我的未支付订单
	 *
	 * @param principal
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public List<String> queryMyUnPayOrders(Principal principal);
	/**
	 * 
	 * 根据订单号查
	 *
	 * @param orderId
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public List<JpaOrders> queryOrders(List<String> orderId);
	
	
	
	/**
	 * 
	 * 查是不是都属于同一个
	 *
	 * @param orderId
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public boolean checkSameCustom(List<String> orderId);

}
