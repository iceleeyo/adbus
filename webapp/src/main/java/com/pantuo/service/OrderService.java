package com.pantuo.service;

import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.identity.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.StringOperation;
import com.pantuo.ActivitiConfiguration;
import com.pantuo.dao.OrderBusesRepository;
import com.pantuo.dao.OrdersRepository;
import com.pantuo.dao.pojo.JpaCpd;
import com.pantuo.dao.pojo.JpaOrderBuses;
import com.pantuo.dao.pojo.JpaOrders;
import com.pantuo.dao.pojo.JpaProduct;
import com.pantuo.dao.pojo.QJpaOrders;
import com.pantuo.dao.pojo.UserDetail;
import com.pantuo.mybatis.domain.Contract;
import com.pantuo.mybatis.domain.Orders;
import com.pantuo.mybatis.domain.OrdersExample;
import com.pantuo.mybatis.domain.Product;
import com.pantuo.mybatis.domain.RoleCpd;
import com.pantuo.mybatis.persistence.OrderBusesMapper;
import com.pantuo.mybatis.persistence.OrdersMapper;
import com.pantuo.mybatis.persistence.ProductMapper;
import com.pantuo.mybatis.persistence.RoleCpdMapper;
import com.pantuo.pojo.HistoricTaskView;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.ActivitiService.TaskQueryType;
import com.pantuo.service.impl.UserService;
import com.pantuo.util.Constants;
import com.pantuo.util.DateConverter;
import com.pantuo.util.DateUtil;
import com.pantuo.util.Pair;
import com.pantuo.util.Request;
import com.pantuo.web.view.OrderView;
import com.pantuo.web.view.SectionView;

@Service
public class OrderService {
	@Autowired
	private RoleCpdMapper roleCpdMapper;
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
/*		OrdersExample example = new OrdersExample();
		OrdersExample.Criteria c = example.createCriteria();
        c.andCityEqualTo(city);
		if (null != userid && userid != "") {
			c.andUserIdEqualTo(userid);
		}
		if (null != id && id > 0) {
			c.andIdEqualTo(id);
		}

		return ordersMapper.selectByExample(example);*/
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
	ProductService productService;
	@Autowired
	OrdersRepository ordersRepository;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	ActivitiService activitiService;
	@Autowired
	UserService userService;
    @Autowired
    private OrderBusesRepository orderBusesRepo;
    @Autowired
    private OrderBusesMapper orderBusesMapper;
    @Autowired
    private ProductMapper productMapper;
    
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
			//设置订单的价格
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
				if (cpdid > 0) {//如果是竞价商品 判断用户对不对
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
				} else {//如果不带cpdid参数 需要验证一次是否是竞价商品 如果是 肯定是伪造的请求
					if (prod == null) {
						isRightRole = false;
					} else if (prod.getIscompare() == 1) {
						isRightRole = false;
					}
				}

				//

				if (isRightRole) {
					activitiService.startProcess2(city, user, order);
				} else {
					throw new AccessDeniedException(Constants.BUSSINESS_ERROR);
				}

			}

			/*if (order.getId() > -1) {
				//if(order.getSuppliesId()>2){
				activitiService.startProcess2(city, user, order);
				//}
				r = new Pair<Boolean, String>(true, "下订单成功！");
				if (cpdid > 0) {
					RoleCpd cpd = roleCpdMapper.selectByPrimaryKey(cpdid);
					if (cpd != null) {
						if (StringUtils.equals(cpd.getUserId(), user.getUsername())) {
							cpd.setCheckOrder(JpaCpd.CheckOrder.Y.ordinal());
							roleCpdMapper.updateByPrimaryKeySelective(cpd);
						} else {
							log.warn("{},cpdid:{},非法操作",user.getUsername(),cpdid);
						}
					} else {

					}
				}

			}*/
		} catch (Exception e) {
			log.error("order ", e);
			//r = new Pair<Boolean, String>(false, "下订单失败！");
			if (e instanceof AccessDeniedException) {
				throw e;
			}
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

	public Page<OrderView> getOrderList(int city,TableRequest req,Principal principal) {
		return activitiService.findTask(city, principal,req,TaskQueryType.task);
	}

	public JpaOrders queryOrderDetail(int orderid, Principal principal) {
		//		return ordersMapper.selectByPrimaryKey(orderid);
		JpaOrders r = selectJpaOrdersById(orderid);
		//判断单纯是广告主身份的
		if (r != null && principal!=null && Request.hasOnlyAuth(principal, ActivitiConfiguration.ADVERTISER)) {
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
							new SectionView(DateConverter.doConvertToString(historicTaskView.getEndTime(),
									DateConverter.DATE_PATTERN), DateConverter.doConvertToString(
									historicTaskView.getEndTime(), DateConverter.TIME_PATTERN)));
				}
			}
		}
		return v;

	}

    public JpaOrders selectJpaOrdersById(int orderId) {
        return ordersRepository.findOne(orderId);
    }

    public boolean updateStatus(int orderId,Principal principal, JpaOrders.Status status) {
        JpaOrders o = queryOrderDetail(orderId,principal);
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

    public void saveOrderBuses(JpaOrderBuses orderBuses) {
        orderBusesRepo.save(orderBuses);
    }

    public void updateWithBuses(JpaOrders order) {
        ordersRepository.save(order);
    }
	public Pair<Object, String> geteleContract(int orderid) {
		Orders orders=ordersMapper.selectByPrimaryKey(orderid);
		String proname="";
		if(orders!=null){
			UserDetail userDetail = userService.findByUsername(orders.getUserId());
			Product product =productMapper.selectByPrimaryKey(orders.getProductId());
			if(product!=null){
				proname=product.getName();
			}
			return new Pair<Object, String>(userDetail,proname);
		}
		return new Pair<Object, String>(null,proname);
	}
	public List<Orders> queryLogByProId(int id) {
		OrdersExample example=new OrdersExample();
		OrdersExample.Criteria criteria=example.createCriteria();
		criteria.andProductIdEqualTo(id);
		example.setOrderByClause("id desc");
		example.setLimitStart(0);
		example.setLimitEnd(10);
		return ordersMapper.selectByExample(example);
	}
	public int queryLogCountByProId(int id) {
		OrdersExample example=new OrdersExample();
		OrdersExample.Criteria criteria=example.createCriteria();
		criteria.andProductIdEqualTo(id);
		return ordersMapper.countByExample(example);
	}

}
