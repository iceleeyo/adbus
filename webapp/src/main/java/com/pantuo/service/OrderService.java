package com.pantuo.service;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.StringOperation;
import com.pantuo.dao.OrdersRepository;
import com.pantuo.dao.pojo.JpaOrders;
import com.pantuo.dao.pojo.JpaProduct;
import com.pantuo.dao.pojo.QJpaOrders;
import com.pantuo.dao.pojo.UserDetail;
import com.pantuo.mybatis.domain.Contract;
import com.pantuo.mybatis.domain.Orders;
import com.pantuo.mybatis.domain.OrdersExample;
import com.pantuo.mybatis.persistence.OrdersMapper;
import com.pantuo.pojo.HistoricTaskView;
import com.pantuo.util.DateConverter;
import com.pantuo.util.Pair;
import com.pantuo.util.Request;
import com.pantuo.web.view.OrderView;
import com.pantuo.web.view.SectionView;

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
		return ordersMapper.selectByPrimaryKey(id);

	}

	public List<Orders> selectOrderByUser(int city, String userid, Integer id) {
		OrdersExample example = new OrdersExample();
		OrdersExample.Criteria c = example.createCriteria();
        c.andCityEqualTo(city);
		if (null != userid && userid != "") {
			c.andUserIdEqualTo(userid);
		}
		if (null != id && id > 0) {
			c.andIdEqualTo(id);
		}

		return ordersMapper.selectByExample(example);

	}

	@Autowired
	ContractService contractService;
	@Autowired
	OrdersMapper ordersMapper;
	@Autowired
	ProductService productService;
	@Autowired
	OrdersRepository ordersRepository;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	ActivitiService activitiService;

	//	 public int countMyList(String name,String code, HttpServletRequest request) ;
	//	 public List<JpaContract> queryContractList(NumberPageUtil page, String name, String code, HttpServletRequest request);
	public Pair<Boolean, String> saveOrder(int city, Orders order, Principal principal) {
        order.setCity(city);
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
			int dbId = ordersMapper.insert(order);
			if (dbId > 0) {

				activitiService.startProcess(city, Request.getUser(principal), order);
				r = new Pair<Boolean, String>(true, "下订单成功！");
			}
		} catch (Exception e) {
			log.error("order ", e);
			r = new Pair<Boolean, String>(false, "下订单失败！");
		}
		return r;
	}

	public void saveOrderJpa(int city, JpaOrders order, UserDetail user) {
        order.setCity(city);
		Pair<Boolean, String> r = null;
		try {
			order.setCreated(new Date());
			order.setUpdated(new Date());
			order.setUserId(user.getUsername());
			ordersRepository.save(order);
			if (order.getId() > 0) {
				//if(order.getSuppliesId()>2){
				activitiService.startProcess2(city, user, order);
				//}
				r = new Pair<Boolean, String>(true, "下订单成功！");
			}
		} catch (Exception e) {
			log.error("order ", e);
			r = new Pair<Boolean, String>(false, "下订单失败！");
		}
	}

	public JpaOrders getJpaOrder(int orderId) {
		return ordersRepository.findOne(orderId);
	}

	public Iterable<JpaOrders> getOrdersForSchedule(int city, Date day, JpaProduct.Type type) {
		Predicate query = QJpaOrders.jpaOrders.city.eq(city)
                .and(QJpaOrders.jpaOrders.startTime.stringValue()
				.loe(StringOperation.create(Ops.STRING_CAST, ConstantImpl.create(day))))
				.and(QJpaOrders.jpaOrders.endTime.after(day)).and(QJpaOrders.jpaOrders.type.eq(type))
				.and(QJpaOrders.jpaOrders.stats.eq(JpaOrders.Status.paid));

		return ordersRepository.findAll(query);
	}

	public Page<OrderView> getOrderList(int city, int page, int pageSize, Sort sort, Principal principal) {
		return activitiService.findTask(city, Request.getUserId(principal), page, pageSize, sort);
	}

	public Orders queryOrderDetail(int orderid, Principal principal) {
		return ordersMapper.selectByPrimaryKey(orderid);
	}

	public Map<String, SectionView> getTaskSection(List<HistoricTaskView> views) {
		Map<String, SectionView> v = new HashMap<String, SectionView>();
		if (!views.isEmpty()) {
			for (HistoricTaskView historicTaskView : views) {
				if (historicTaskView.getEndTime() != null) {
					v.put(historicTaskView.getTaskDefinitionKey(),
							new SectionView(DateConverter.doConvertToString(historicTaskView.getEndTime(),
									DateConverter.DATE_PATTERN), DateConverter.doConvertToString(
									historicTaskView.getEndTime(), DateConverter.TIME_PATTERN)));
				}
			}
		}
		return v;

	}

}
