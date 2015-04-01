package com.pantuo.service;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pantuo.mybatis.domain.Contract;
import com.pantuo.mybatis.domain.Order;
import com.pantuo.mybatis.persistence.OrderMapper;
import com.pantuo.service.impl.SuppliesServiceImpl;
import com.pantuo.util.Pair;
import com.pantuo.util.Request;

@Service
public class OrderService {

	private static Logger log = LoggerFactory.getLogger(OrderService.class);

	public enum Stats {
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
	}

	public Order selectOrderById(Integer id) {
		return orderMapper.selectByPrimaryKey(id);

	}

	@Autowired
	ContractService contractService;
	@Autowired
	OrderMapper orderMapper;

	@Autowired
	ActivitiService activitiService;

	//	 public int countMyList(String name,String code, HttpServletRequest request) ;
	//	 public List<Contract> queryContractList(NumberPageUtil page, String name, String code, HttpServletRequest request);
	public Pair<Boolean, String> saveOrder(Order order, HttpServletRequest request) {
		Pair<Boolean, String> r = null;
		try {
			order.setCreateTime(new Date());
			order.setEditTime(new Date());
			//	
			order.setUserId(Request.getUserId(request));
			if (StringUtils.equals(order.getPayType(), PayWay.ht_pay.name())) {

				Contract c = contractService.queryContractByCode(order.getContract_code());
				if (c == null) {
					return new Pair<Boolean, String>(false, "系统查不到相应的合同号！");
				} else {
					order.setContractId(c.getId());
					order.setStats(Stats.paid.name());
				}
			}
			int dbId = orderMapper.insert(order);
			if (dbId > 0) {

				activitiService.startProcess(Request.getUser(request), order);
				r = new Pair<Boolean, String>(true, "下订单成功！");
			}
		} catch (Exception e) {
			log.error("order ", e);
			r = new Pair<Boolean, String>(false, "下订单失败！");
		}
		return r;
	}
}
