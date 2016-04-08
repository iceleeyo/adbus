package com.pantuo.service;

import java.security.Principal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.StringOperation;
import com.pantuo.ActivitiConfiguration;
import com.pantuo.dao.OrdersRepository;
import com.pantuo.dao.PayPlanRepository;
import com.pantuo.dao.pojo.JpaCpd;
import com.pantuo.dao.pojo.JpaOrders;
import com.pantuo.dao.pojo.JpaOrders.PayType;
import com.pantuo.dao.pojo.JpaPayPlan;
import com.pantuo.dao.pojo.JpaProduct;
import com.pantuo.dao.pojo.QJpaOrders;
import com.pantuo.dao.pojo.QJpaPayPlan;
import com.pantuo.dao.pojo.UserDetail;
import com.pantuo.mybatis.domain.Contract;
import com.pantuo.mybatis.domain.Orders;
import com.pantuo.mybatis.domain.OrdersExample;
import com.pantuo.mybatis.domain.PayPlan;
import com.pantuo.mybatis.domain.PayPlanExample;
import com.pantuo.mybatis.domain.Product;
import com.pantuo.mybatis.domain.RoleCpd;
import com.pantuo.mybatis.domain.Supplies;
import com.pantuo.mybatis.persistence.OrdersMapper;
import com.pantuo.mybatis.persistence.PayPlanMapper;
import com.pantuo.mybatis.persistence.ProductMapper;
import com.pantuo.mybatis.persistence.RoleCpdMapper;
import com.pantuo.pojo.DataTablePage;
import com.pantuo.pojo.HistoricTaskView;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.ActivitiService.TaskQueryType;
import com.pantuo.service.impl.UserService;
import com.pantuo.service.security.Request;
import com.pantuo.util.Constants;
import com.pantuo.util.DateConverter;
import com.pantuo.util.DateUtil;
import com.pantuo.util.NumberPageUtil;
import com.pantuo.util.OrderIdSeq;
import com.pantuo.util.Pair;
import com.pantuo.web.view.OrderPlanView;
import com.pantuo.web.view.OrderView;
import com.pantuo.web.view.SectionView;

@Service
public class OrderService {
	@Autowired
	private RoleCpdMapper roleCpdMapper;

	@Autowired
	private PayPlanMapper payPlanMapper;
	
	
	@Autowired
	private SuppliesService suppliesService;
	private static Logger log = LoggerFactory.getLogger(OrderService.class);

	public Orders selectOrderById(Integer id) {
		return ordersMapper.selectByPrimaryKey(id);

	}
	
	
	public void updatePayPrice(Integer id, double payPrice) {
		Orders order = selectOrderById(id);
		if (order != null) {
			Orders record = new Orders();
			record.setId(id);
			record.setPayPrice(payPrice + order.getPayPrice());
			ordersMapper.updateByPrimaryKeySelective(record);
		}
	}
 

	
	public Pair<Object, String> updatePlanState(HttpServletRequest request,String payWay, int orderid, String payNextLocation,
			JpaPayPlan.PayState state,String uid) {
		Pair<Object, String> r = null;
		String[] split = StringUtils.split(payNextLocation, "_");
		if (split.length > 0) {
			double payPrice = 0;
			List<PayPlan> planList = new ArrayList<PayPlan>();
			for (String string : split) {//先判断能否更新
				int id = NumberUtils.toInt(string, -1);
				if (id > 0) {
					PayPlan plan = payPlanMapper.selectByPrimaryKey(id);
					if (plan != null) {
						payPrice += plan.getPrice();
						plan.setPayState(state.ordinal());
						plan.setPayUser(uid);
						plan.setUpdated(new Date());
						try {
							plan.setPayType(PayType.valueOf(request.getParameter("payType")).ordinal());
						} catch (Exception e) {
						}
						planList.add(plan);
						if (plan.getOrderId().intValue() != orderid) {
							return new Pair<Object, String>(false, "非法操作");
						}
					}
				}
			}//再更新
			if (!planList.isEmpty()) {
				for (PayPlan payPlan : planList) {
					payPlanMapper.updateByPrimaryKey(payPlan);
				}

			}
			Orders order = ordersMapper.selectByPrimaryKey(orderid);
			double basePrice = 0;
			if (order != null) {
				basePrice = order.getPayPrice();
				if(StringUtils.equals(payWay, "payAll")){
					basePrice+=(order.getPrice()- order.getPayPrice());
				}else if(StringUtils.equals(payWay, "payNext")){
					basePrice+=payPrice;
				} 
			}
			

			if (basePrice > 0) {

				Orders record = new Orders();
				record.setId(orderid);
				record.setPayPrice(basePrice);
			//	ordersMapper.updateByPrimaryKeySelective(record);
			}

		}
		return  new Pair<Object, String>(true, "操作成功");

	}

	public void getPayNextMoney(int orderid, Model model) {
		double r = 0;
		StringBuilder ids = new StringBuilder();
		StringBuilder allIds = new StringBuilder();
		PayPlanExample example = new PayPlanExample();
		example.createCriteria().andOrderIdEqualTo(orderid);
		example.setOrderByClause("day asc");
		List<PayPlan> plan = payPlanMapper.selectByExample(example);
		for (PayPlan obj : plan) {
			allIds.append(obj.getId());
			allIds.append("_");
		}
		Date now = new Date();
		for (PayPlan obj : plan) {

			if (obj.getDay().before(now)) {
				if (obj.getPayState().intValue() == (JpaPayPlan.PayState.init.ordinal())) {
					r += obj.getPrice();
					ids.append(obj.getId());
					ids.append("_");
				}
			} else {
				if (obj.getPayState().intValue() == (JpaPayPlan.PayState.init.ordinal())) {
					r += obj.getPrice();
					ids.append(obj.getId());
					ids.append("_");
					break;
				}

			}
		}

		model.addAttribute("payNext",r);
		model.addAttribute("payNextLocation", ids.toString());
		model.addAttribute("allLocation", allIds.toString());

	}

	public double payed(int orderid) {
		double r = 0;
		PayPlanExample example = new PayPlanExample();
		example.createCriteria().andOrderIdEqualTo(orderid).andPayStateEqualTo(JpaPayPlan.PayState.check.ordinal());
		example.setOrderByClause("day asc");
		List<PayPlan> plan = payPlanMapper.selectByExample(example);
		for (PayPlan payPlan : plan) {
			r += payPlan.getPrice();
		}
		return r;
	}
	
	public void fullFristPayInfo(Model model, String activityId, OrderView view) {
		if (StringUtils.equals("financialCheck", activityId)) {
			if (view != null) {
				JpaOrders order = view.getOrder();
				double payed = payed(order.getId());
				model.addAttribute("payed", payed);
				model.addAttribute("needPay", order.getPrice() - payed < 0 ? 0 : order.getPrice() - payed);
			}
		}
	}

	public void fullPayPlanInfo(Model model, String activityId, OrderView view) {
		if (StringUtils.equals("userFristPay", activityId)) {
			if (view != null) {
				JpaOrders order = view.getOrder();
				double payPrice = order.getPrice() - order.getPayPrice();
				model.addAttribute("payAll", payPrice);
				getPayNextMoney(order.getId(), model);
			}
		}
	}

	/**
	 * 
	 * 修改单价
	 *
	 * @param id
	 * @param price
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public Pair<Boolean, String> updateOrderPrice(Integer id, double price) {
		Orders orders = selectOrderById(id);
		Pair<Boolean, String> r = null;
		if (orders != null) {
			orders.setPrice(price);
			ordersMapper.updateByPrimaryKey(orders);
			r = new Pair<Boolean, String>(true, "修改成功！");
		} else {
			r = new Pair<Boolean, String>(true, "订单未找到！");
		}
		return r;
	}

	public Iterable<JpaOrders> selectOrderByUser(int city, String userid, Integer id) {
		/*
		 * OrdersExample example = new OrdersExample(); OrdersExample.Criteria c
		 * = example.createCriteria(); c.andCityEqualTo(city); if (null !=
		 * userid && userid != "") { c.andUserIdEqualTo(userid); } if (null !=
		 * id && id > 0) { c.andIdEqualTo(id); }
		 * 
		 * return ordersMapper.selectByExample(example);
		 */
		BooleanExpression q = QJpaOrders.jpaOrders.city.eq(city);
		if (StringUtils.isNotBlank(userid))
			q = q.and(QJpaOrders.jpaOrders.userId.eq(userid));
		if (null != id && id > 0)
			q = q.and(QJpaOrders.jpaOrders.id.eq(id));
		return ordersRepository.findAll(q);
	}

	@Autowired
	ContractService contractService;
	@Autowired
	OrdersMapper ordersMapper;
	@Autowired
	private IdentityService identityService;
	@Autowired
	@Lazy
	ProductService productService;
	@Autowired
	OrdersRepository ordersRepository;
	@Autowired
	PayPlanRepository payPlanRepository;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	ActivitiService activitiService;
	@Autowired
	UserService userService;
	@Autowired
	private ProductMapper productMapper;


	// public int countMyList(String name,String code, HttpServletRequest
	// request) ;
	// public List<JpaContract> queryContractList(NumberPageUtil page, String
	// name, String code, HttpServletRequest request);
	@Autowired
	CpdService cpdService;

	//	 public int countMyList(String name,String code, HttpServletRequest request) ;
	//	 public List<JpaContract> queryContractList(NumberPageUtil page, String name, String code, HttpServletRequest request);
	public Pair<Boolean, String> saveOrder(int city, Orders order, Principal principal) {
		order.setCity(city);
		Pair<Boolean, String> r = null;
		try {
			com.pantuo.util.BeanUtils.filterXss(order);
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

	public void saveOrderJpa(int city, JpaOrders order, UserDetail user, int cpdid, JpaProduct prod) {
		order.setCity(city);
		try {
			// 设置订单的价格
			if (prod.getIscompare() == 1) {
				JpaCpd cpd = cpdService.queryOneCpdByPid(prod.getId());
				if (cpd == null) {
					throw new AccessDeniedException("竞价信息丢失!");
				} else {
					order.setPrice(cpd.getComparePrice());
				}
			}

			order.setCreated(new Date());
			order.setUpdated(new Date());
			order.setUserId(user.getUsername());
			ordersRepository.save(order);

			if (order.getId() > -1) {
				boolean isRightRole = true;
				if (cpdid > 0) {// 如果是竞价商品 判断用户对不对
					RoleCpd cpd = roleCpdMapper.selectByPrimaryKey(cpdid);
					if (cpd != null) {
						if (StringUtils.equals(cpd.getUserId(), user.getUsername())) {
							cpd.setCheckOrder(JpaCpd.CheckOrder.Y.ordinal());
							cpd.setOrderid(order.getId());
							roleCpdMapper.updateByPrimaryKeySelective(cpd);
						} else {
							isRightRole = false;
							log.warn("{},cpdid:{},非法操作", user.getUsername(), cpdid);
						}
					} else {
						isRightRole = false;
					}
				} else {// 如果不带cpdid参数 需要验证一次是否是竞价商品 如果是 肯定是伪造的请求
					if (prod == null) {
						isRightRole = false;
					} else if (prod.getIscompare() == 1) {
						isRightRole = false;
					}
				}

				//

				if (isRightRole) {
					activitiService.startProcess2(city, user, order, null);
				} else {
					throw new AccessDeniedException(Constants.BUSSINESS_ERROR);
				}

			}

			/*
			 * if (order.getId() > -1) { //if(order.getSuppliesId()>2){
			 * activitiService.startProcess2(city, user, order); //} r = new
			 * Pair<Boolean, String>(true, "下订单成功！"); if (cpdid > 0) { RoleCpd
			 * cpd = roleCpdMapper.selectByPrimaryKey(cpdid); if (cpd != null) {
			 * if (StringUtils.equals(cpd.getUserId(), user.getUsername())) {
			 * cpd.setCheckOrder(JpaCpd.CheckOrder.Y.ordinal());
			 * roleCpdMapper.updateByPrimaryKeySelective(cpd); } else {
			 * log.warn("{},cpdid:{},非法操作",user.getUsername(),cpdid); } } else {
			 * 
			 * } }
			 * 
			 * }
			 */
		} catch (Exception e) {
			log.error("order ", e);
			// r = new Pair<Boolean, String>(false, "下订单失败！");
			if (e instanceof AccessDeniedException) {
				throw e;
			}
		}
	}

	public JpaOrders getJpaOrder(int orderId) {
		return ordersRepository.findOne(orderId);
	}

	public List<JpaOrders> getJpaOrders(List<Integer> orderId) {
		return ordersRepository.findAll(orderId);
	}

	public Iterable<JpaOrders> getOrdersForSchedule(int city, Date day, JpaProduct.Type type) {
		Predicate query = QJpaOrders.jpaOrders.city
				.eq(city)
				.and(QJpaOrders.jpaOrders.startTime.stringValue().loe(
						StringOperation.create(Ops.STRING_CAST, ConstantImpl.create(day))))
				.and(QJpaOrders.jpaOrders.endTime.after(day)).and(QJpaOrders.jpaOrders.type.eq(type))
				.and(QJpaOrders.jpaOrders.stats.eq(JpaOrders.Status.paid));

		return ordersRepository.findAll(query);
	}

	public Page<OrderView> getOrderList(int city, TableRequest req, Principal principal) {
		return activitiService.findTask(city, principal, req, TaskQueryType.task);
	}

	public JpaOrders queryOrderDetail(int orderid, Principal principal) {
		// return ordersMapper.selectByPrimaryKey(orderid);
		JpaOrders r = selectJpaOrdersById(orderid);
		//判断单纯是广告主身份的
		if (r != null && principal != null && Request.hasOnlyAuth(principal, ActivitiConfiguration.ADVERTISER)) {
			if (!(StringUtils.isNoneBlank(r.getUserId()) && StringUtils.equals(r.getUserId(),
					Request.getUserId(principal)))) {
				throw new AccessDeniedException(Constants.BUSSINESS_ERROR);
			}
		}
		return r;

	}

	public Map<String, SectionView> getTaskSection(List<HistoricTaskView> views) {
		Map<String, SectionView> v = new HashMap<String, SectionView>();
		if (!views.isEmpty()) {
			for (HistoricTaskView historicTaskView : views) {
				if (historicTaskView.getEndTime() != null) {
					v.put(historicTaskView.getTaskDefinitionKey(),
							new SectionView(
									DateConverter.doConvertToString(historicTaskView.getEndTime(),
											DateConverter.DATE_PATTERN),
							DateConverter.doConvertToString(historicTaskView.getEndTime(),
									DateConverter.TIME_PATTERN)));
				}
			}
		}
		return v;

	}

	public JpaOrders selectJpaOrdersById(int orderId) {
		return ordersRepository.findOne(orderId);
	}

	public boolean updateStatus(int orderId, Principal principal, JpaOrders.Status status) {
		JpaOrders o = queryOrderDetail(orderId, principal);
		if (o != null) {
			o.setStats(status);
			Date now = DateUtil.trimDate(new Date());
			if (status == JpaOrders.Status.paid) {
				o.setFinancialCheckDay(now);
			} else if (status == JpaOrders.Status.scheduled) {
				o.setScheduleDay(now);
			} else if (status == JpaOrders.Status.started) {
				o.setShangboDay(now);
			} else if (status == JpaOrders.Status.completed) {
				o.setShangboDay(now);
			} else if (status == JpaOrders.Status.cancelled) {
				o.setCancelDay(now);
			}
			ordersRepository.save(o);
			return true;
		}
		return false;
	}

	public void updateWithBuses(JpaOrders order) {
		ordersRepository.save(order);
	}

	public Pair<Object, String> geteleContract(int orderid) {
		Orders orders = ordersMapper.selectByPrimaryKey(orderid);
		String proname = "";
		if (orders != null) {
			UserDetail userDetail = userService.findByUsername(orders.getUserId());
			Product product = productMapper.selectByPrimaryKey(orders.getProductId());
			if (product != null) {
				proname = product.getName();
			}
			return new Pair<Object, String>(userDetail, proname);
		}
		return new Pair<Object, String>(null, proname);
	}

	public List<Orders> queryLogByProId(int id) {
		OrdersExample example = new OrdersExample();
		OrdersExample.Criteria criteria = example.createCriteria();
		criteria.andProductIdEqualTo(id);
		example.setOrderByClause("id desc");
		example.setLimitStart(0);
		example.setLimitEnd(10);
		return ordersMapper.selectByExample(example);
	}

	public int queryLogCountByProId(int id) {
		OrdersExample example = new OrdersExample();
		OrdersExample.Criteria criteria = example.createCriteria();
		criteria.andProductIdEqualTo(id);
		return ordersMapper.countByExample(example);
	}

	public void startTest() {
		activitiService.startTest();
	}

	public Pair<Boolean, String> editOrderStartTime(int orderid, int supid, String startD, String ordRemark, int city,
			Principal principal) {
		JpaOrders orders = ordersRepository.findOne(orderid);
		if (orders == null) {
			return new Pair<Boolean, String>(false, "信息丢失");
		}
		if (!StringUtils.equals(orders.getUserId(), Request.getUserId(principal))) {
			return new Pair<Boolean, String>(false, "订单属主不匹配,不能修改");
		}
		try {
			Date st = DateUtil.longDf.get().parse(startD);
			Date end = DateUtil.dateAdd(st, orders.getProduct().getDays());
			orders.setStartTime(st);
			orders.setEndTime(end);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		orders.setOrdRemark(ordRemark);
		orders.setSuppliesId(supid);
		if (ordersRepository.save(orders) != null) {
			return new Pair<Boolean, String>(true, "订单修改成功");
		}
		return new Pair<Boolean, String>(false, "操作异常");
	}

	public Pair<Object, Object> findOrderAndSup(int city, Principal principal, int orderid) {
		JpaOrders orders = ordersRepository.findOne(orderid);
		List<Supplies> supplieslist = suppliesService.querySuppliesByUser(city, principal,
				orders.getProduct().getType().ordinal());
		return new Pair<Object, Object>(orders, supplieslist);
	}

	public List<JpaOrders> findordersList(String code) {
		BooleanExpression query = QJpaOrders.jpaOrders.contractCode.eq(code);
		return (List<JpaOrders>) ordersRepository.findAll(query);
	}

	public List<PayPlan> getPayPlan(int orderId) {
		PayPlanExample example=new PayPlanExample();
		example.createCriteria().andOrderIdEqualTo(orderId);
		example.setOrderByClause("day asc");
		List<PayPlan> list=payPlanMapper.selectByExample(example);
		return list;
	}

	public Pair<Boolean, String> deletePayPlan(int id) {
		PayPlan payPlan=payPlanMapper.selectByPrimaryKey(id);
		if(payPlan==null){
			return new Pair<Boolean, String>(false,"信息丢失");
		}
		if(!StringUtils.isBlank(payPlan.getPayUser())){
			return new Pair<Boolean, String>(false,"已有用户付款，删除失败");
		}
		int a=payPlanMapper.deleteByPrimaryKey(id);
		if(a>0){
			return new Pair<Boolean, String>(true,"删除成功");
		}
		return new Pair<Boolean, String>(false,"操作失败");

	}

	public JpaPayPlan queryPayPlanByid(int id) {
		return payPlanRepository.findOne(id);
	}

	public Pair<Boolean, String> savePayPlan(PayPlan payPlan, int orderId,String payDate, String userId) {
		Date date=new Date();
		if (StringUtils.isNotBlank(payDate)) {
			try {
				date=DateUtil.longDf.get().parse(payDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		PayPlanExample example2=new PayPlanExample();
		example2.createCriteria().andOrderIdEqualTo(orderId);
		example2.setOrderByClause("day desc");
		List<PayPlan> list=payPlanMapper.selectByExample(example2);
		if(list.size()>0){
			if(list.get(0).getDay().after(date)){
				return new Pair<Boolean, String>(false, "付款日期不得小于已添加分期的付款日期，保存失败");
			}
		}
		payPlan.setDay(date);
		payPlan.setSetPlanUser(userId);
		
		if (null != payPlan.getId() && payPlan.getId() > 0) {
			PayPlan dividpay2 = payPlanMapper.selectByPrimaryKey(payPlan.getId());
			if(dividpay2.getPayState()!=JpaPayPlan.PayState.init.ordinal()){
				return new Pair<Boolean, String>(false, "已有用户付款禁止修改");
			}
			com.pantuo.util.BeanUtils.copyProperties(payPlan, dividpay2);
			if (payPlanMapper.updateByPrimaryKey(dividpay2) > 0) {
				return new Pair<Boolean, String>(true, "修改成功");
			}
			return new Pair<Boolean, String>(false, "操作失败");
		}
		PayPlanExample example=new PayPlanExample();
		example.createCriteria().andOrderIdEqualTo(orderId).andPeriodNumEqualTo(payPlan.getPeriodNum());
		if(payPlanMapper.countByExample(example)>0){
			return new Pair<Boolean, String>(false, "期数重复，保存失败");
		}
		payPlan.setPayState(JpaPayPlan.PayState.init.ordinal());
		payPlan.setOrderId(orderId);
		
		if (payPlanMapper.insert(payPlan) > 0) {
			return new Pair<Boolean, String>(true, "保存成功");
		}
		return new Pair<Boolean, String>(false, "保存失败");
	}

	public DataTablePage<OrderPlanView> queryAllPlan(TableRequest req) {
		int page = req.getPage(), length = req.getLength();
		if (page < 0)
			page = 0;
		if (length < 1)
			length = 1;
		Pageable p = new PageRequest(page, length, req.getSort("payState"));
		String orderId = req.getFilter("orderId"), payState = req.getFilter("payState"), aduser = req
				.getFilter("userId"), salesMan = req.getFilter("salesMan");

		BooleanExpression query = QJpaPayPlan.jpaPayPlan.id.goe(0);

		if (StringUtils.isNoneBlank(payState)) {
			if (!StringUtils.equals("defaultAll", payState)) {
				query = query.and(QJpaPayPlan.jpaPayPlan.payState.eq(JpaPayPlan.PayState.valueOf(payState)));
			}
		} else {
			query = query.and(QJpaPayPlan.jpaPayPlan.payState.eq(JpaPayPlan.PayState.check));
		}

		if (StringUtils.isNotBlank(aduser)) {
			query = query.and(QJpaPayPlan.jpaPayPlan.order.creator.eq(aduser));
		}
		if (StringUtils.isNotBlank(salesMan)) {
			query = query.and(QJpaPayPlan.jpaPayPlan.order.creator.eq(salesMan));
		}
		if (StringUtils.isNotBlank(orderId)) {
			query = query.and(QJpaPayPlan.jpaPayPlan.order.id.eq(OrderIdSeq.longOrderId2DbId(NumberUtils
					.toLong(orderId))));
		}

		Page<JpaPayPlan> result = payPlanRepository.findAll(query, p);
		List<OrderPlanView> plans = new ArrayList<OrderPlanView>();
		for (JpaPayPlan plan : result.getContent()) {
			plans.add(new OrderPlanView(plan));
		}
		org.springframework.data.domain.PageImpl<OrderPlanView> r = new org.springframework.data.domain.PageImpl<OrderPlanView>(
				plans, p, result.getTotalElements());
		return new DataTablePage<OrderPlanView>(r, req.getDraw());
	}
	
	public Page<JpaPayPlan> queryPayPlanDetail(TableRequest req, int page, int length) {
		
		if (page < 0)
			page = 0;
		if (length < 1)
			length = 1;
		Pageable p = new PageRequest(page, length,  new Sort("id"));
		int id=0;
		String orderId=req.getFilter("orderId");
		if (StringUtils.isNotBlank(orderId)) {
			id=NumberUtils.toInt(orderId);
		}
		BooleanExpression query = QJpaPayPlan.jpaPayPlan.order.id.eq(id);
		return  payPlanRepository.findAll(query, p);
	}

	public Pair<Boolean, String> checkOrderPrice(int id) {
		double p=0;
		Orders orders=ordersMapper.selectByPrimaryKey(id);
		if(orders==null){
			return new Pair<Boolean, String>(false,"信息丢失");
		}
		PayPlanExample example=new PayPlanExample();
		example.createCriteria().andOrderIdEqualTo(id);
		List<PayPlan> list=payPlanMapper.selectByExample(example);
		for (PayPlan payPlan : list) {
			p+=payPlan.getPrice();
		}
		if(p!=orders.getPrice()){
			return new Pair<Boolean, String>(false,String.valueOf(p));
		}
		return new Pair<Boolean, String>(true,"操作成功");
	}


	public Page<OrderView> getPayPlanOrders(TableRequest req, int page, int length,String userId) {
		List<OrderView> views = new ArrayList<OrderView>();
		if (page < 0)
			page = 0;
		if (length < 1)
			length = 1;
		Pageable p = new PageRequest(page, length,  new Sort("id"));
		BooleanExpression query = QJpaOrders.jpaOrders.userId.eq(userId);
		query=query.and(QJpaOrders.jpaOrders.price.gt(QJpaOrders.jpaOrders.payPrice));
		query=query.and(QJpaOrders.jpaOrders.payPrice.gt(0));
		query=query.and(QJpaOrders.jpaOrders.payType.eq(JpaOrders.PayType.dividpay));
		List<JpaOrders> list= (List<JpaOrders>) ordersRepository.findAll(query);
		for (JpaOrders jpaOrders : list) {
			OrderView v = new OrderView();
			v.setOrder(jpaOrders);
			v.setLongOrderId(OrderIdSeq.getLongOrderId(jpaOrders));
			views.add(v);
		}
		NumberPageUtil pageUtil = new NumberPageUtil(views.size(), page, length);
		return new org.springframework.data.domain.PageImpl<OrderView>(
				views, p,pageUtil.getTotal());
	}

}
