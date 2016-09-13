package com.pantuo.service.impl;

import java.security.Principal;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mysema.query.types.expr.BooleanExpression;
import com.pantuo.dao.OrdersRepository;
import com.pantuo.dao.pojo.JpaOrders;
import com.pantuo.dao.pojo.QJpaOrders;
import com.pantuo.dao.pojo.UserDetail;
import com.pantuo.mybatis.domain.Orders;
import com.pantuo.mybatis.domain.OrdersExample;
import com.pantuo.mybatis.persistence.OrdersMapper;
import com.pantuo.service.PayContractService;
import com.pantuo.service.security.Request;
import com.pantuo.util.JsonTools;
import com.pantuo.util.OrderIdSeq;

public class PayContractServiceImpl  implements PayContractService{
	@Autowired
	OrdersMapper ordersMapper;
	@Autowired
	OrdersRepository ordersRepository;
	/**
	 * 
	 * 查我的未支付订单
	 *
	 * @param principal
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public List<String> queryMyUnPayOrders(Principal principal){
		List<String> idList=Lists.newArrayList();
		OrdersExample example=new OrdersExample();
		example.createCriteria().andUserIdEqualTo(Request.getUserId(principal)).andStatsEqualTo(JpaOrders.Status.unpaid.ordinal());
		List<Orders> list=ordersMapper.selectByExample(example);
		for (Orders orders : list) {
			long longOrderId = OrderIdSeq.getIdFromDate(orders.getId(), orders.getCreated());
			idList.add(String.valueOf(longOrderId));
		}
		return idList;
		
	}
	/**
	 * 
	 * 根据订单号查
	 *
	 * @param orderId
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public List<JpaOrders> queryOrders(List<String> orderId){
		List<Integer> intids=Lists.newArrayList();
		for (String one : orderId) {
			long longid=NumberUtils.toLong(one);
			int intid=OrderIdSeq.longOrderId2DbId(longid);
			intids.add(intid);
		}
		BooleanExpression q=QJpaOrders.jpaOrders.id.in(intids);
		List<JpaOrders> list=(List<JpaOrders>) ordersRepository.findAll(q);
		return list;
	}
	
	
	
	/**
	 * 
	 * 查是不是都属于同一个客户
	 *
	 * @param orderId
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public boolean checkSameCustom(List<String> orderId){
		Set<String> set=Sets.newHashSet();
		List<JpaOrders> list=queryOrders(orderId);
		for (JpaOrders jpaOrders : list) {
			UserDetail userDetail=(UserDetail) JsonTools.readValue(jpaOrders.getCustomerJson(),UserDetail.class);
			set.add(userDetail.getUsername());
		}
		if(set.size()==1){
			return true;
		}
		return false;
	}

}
