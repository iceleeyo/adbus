package com.pantuo.service;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pantuo.mybatis.domain.Order;
import com.pantuo.mybatis.persistence.OrderMapper;
import com.pantuo.util.Pair;
import com.pantuo.util.Request;
@Service
public class OrderService {
	@Autowired
	OrderMapper orderMapper;
//	 public int countMyList(String name,String code, HttpServletRequest request) ;
//	 public List<Contract> queryContractList(NumberPageUtil page, String name, String code, HttpServletRequest request);
	public Pair<Boolean, String> saveOrder(Order order,HttpServletRequest request){
		Pair<Boolean, String> r = null;
		try {
			order.setCreateTime(new Date());
			order.setEditTime(new Date());
//			order.setUserId(Request.getUserId(request));
			int dbId = orderMapper.insert(order);
			if (dbId > 0) {
					r = new Pair<Boolean, String>(true, "下订单成功！");
			}
		} catch (Exception e) {
			r = new Pair<Boolean, String>(false, "下订单失败！");
		}
		return r;
		
	}

}
