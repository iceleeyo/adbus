package com.pantuo.service;

import java.security.Principal;
import java.util.Date;
<<<<<<< .mine
import java.util.List;
import java.util.Map;
=======
>>>>>>> .r167

import javax.servlet.http.HttpServletRequest;

import com.pantuo.dao.OrdersRepository;
import com.pantuo.service.security.ActivitiUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.pantuo.dao.pojo.JpaOrders;
import com.pantuo.mybatis.domain.Contract;
<<<<<<< .mine
import com.pantuo.mybatis.domain.Order;
import com.pantuo.mybatis.domain.OrderExample;
import com.pantuo.mybatis.domain.OrderExample.Criteria;
import com.pantuo.mybatis.persistence.OrderMapper;
import com.pantuo.service.impl.SuppliesServiceImpl;
=======
import com.pantuo.mybatis.domain.Orders;
import com.pantuo.mybatis.persistence.OrdersMapper;
>>>>>>> .r167
import com.pantuo.util.Pair;
import com.pantuo.util.Request;

@Service
public class OrderService {

	private static Logger log = LoggerFactory.getLogger(OrderService.class);

/*	public enum Stats {
		unpay, paid, datetime, report, finish, canel;
		public static Stats getQt(String queryType) {
			try {
				return Stats.valueOf(queryType);
			} catch (Exception e) {
				return Stats.unpay;
			}
		}
	}

	public enum PayWay {
		ht_pay, dire_pay;
		public static PayWay getQt(String queryType) {
			try {
				return PayWay.valueOf(queryType);
			} catch (Exception e) {
				return PayWay.ht_pay;
			}
		}
	}*/

	public Orders selectOrderById(Integer id) {
		return orderMapper.selectByPrimaryKey(id);

	}
	public List<Order> selectOrderByUser(String userid,Integer id) {
		OrderExample example=new OrderExample();
		OrderExample.Criteria c=example.createCriteria();
		if(null!=userid&&userid!=""){
			c.andUserIdEqualTo(userid);
		}
		if(null!=id&&id>0){
			c.andIdEqualTo(id);
		}
		
		return orderMapper.selectByExample(example);

	}

	@Autowired
	ContractService contractService;
	@Autowired
    OrdersMapper orderMapper;
	
	
	@Autowired
    OrdersRepository ordersRepository;

	@Autowired
	ActivitiService activitiService;

	//	 public int countMyList(String name,String code, HttpServletRequest request) ;
	//	 public List<JpaContract> queryContractList(NumberPageUtil page, String name, String code, HttpServletRequest request);
	public Pair<Boolean, String> saveOrder(Orders order, Principal principal) {
		Pair<Boolean, String> r = null;
		try {
			order.setCreated(new Date());
			order.setUpdated(new Date());
			//	
			order.setUserId(Request.getUserId(principal));
			if (order.getPayType() == JpaOrders.PayType.contract.ordinal()) {

				Contract c = contractService.queryContractByCode(order.getContractCode());
				if (c == null) {
					return new Pair<Boolean, String>(false, "系统查不到相应的合同号！");
				} else {
					order.setContractId(c.getId());
					order.setStats(JpaOrders.Status.paid.ordinal());
				}
			}
			int dbId = orderMapper.insert(order);
			if (dbId > 0) {

				activitiService.startProcess(Request.getUser(principal), order);
				r = new Pair<Boolean, String>(true, "下订单成功！");
			}
		} catch (Exception e) {
			log.error("order ", e);
			r = new Pair<Boolean, String>(false, "下订单失败！");
		}
		return r;
	}
	
	
	
	public Pair<Boolean, String> saveOrderJpa(JpaOrders order, Principal principal) {
		Pair<Boolean, String> r = null;
		try {
			order.setCreated(new Date());
			order.setUpdated(new Date());
			//	
			order.setUserId(Request.getUserId(principal));
			if (order.getPayType() == JpaOrders.PayType.contract) {
				
				Contract c = contractService.queryContractByCode(order.getContractCode());
				if (c == null) {
					return new Pair<Boolean, String>(false, "系统查不到相应的合同号！");
				} else {
					order.setContractId(c.getId());
					order.setStats(JpaOrders.Status.paid);
				}
			}
			 ordersRepository.save(order);
			// System.out.println(order.getId());
			//int dbId = orderMapper.insert(order);
			if (order.getId() > 0) {

				activitiService.startProcess2(Request.getUser(principal), order);
				r = new Pair<Boolean, String>(true, "下订单成功！");
			}
		} catch (Exception e) {
			log.error("order ", e);
			r = new Pair<Boolean, String>(false, "下订单失败！");
		}
		return r;
	}

	
}
