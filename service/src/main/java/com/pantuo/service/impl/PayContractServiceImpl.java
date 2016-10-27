package com.pantuo.service.impl;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mysema.query.types.expr.BooleanExpression;
import com.pantuo.dao.OrdersRepository;
import com.pantuo.dao.PayContractRepository;
import com.pantuo.dao.PayPlanRepository;
import com.pantuo.dao.pojo.JpaOrders;
import com.pantuo.dao.pojo.JpaPayContract;
import com.pantuo.dao.pojo.JpaPayPlan;
import com.pantuo.dao.pojo.QJpaContractPayPlan;
import com.pantuo.dao.pojo.QJpaOrders;
import com.pantuo.dao.pojo.QJpaPayContract;
import com.pantuo.dao.pojo.QJpaPayPlan;
import com.pantuo.dao.pojo.UserDetail;
import com.pantuo.mybatis.domain.PayPlan;
import com.pantuo.mybatis.domain.PayPlanExample;
import com.pantuo.mybatis.domain.PaycontractWithBLOBs;
import com.pantuo.mybatis.persistence.OrdersMapper;
import com.pantuo.mybatis.persistence.PayPlanMapper;
import com.pantuo.mybatis.persistence.PaycontractMapper;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.ActivitiService;
import com.pantuo.service.ContractService;
import com.pantuo.service.OrderService;
import com.pantuo.service.PayContractService;
import com.pantuo.service.UserServiceInter;
import com.pantuo.service.security.Request;
import com.pantuo.util.BeanUtils;
import com.pantuo.util.JsonTools;
import com.pantuo.util.OrderIdSeq;
import com.pantuo.util.Pair;
import com.pantuo.web.view.AutoCompleteView;
import com.pantuo.web.view.OrderView;

@Service
public class PayContractServiceImpl implements PayContractService {
	@Autowired
	OrderService orderService;

	@Autowired
	private PayPlanMapper payPlanMapper;
	@Autowired
	OrdersMapper ordersMapper;
	@Autowired
	OrdersRepository ordersRepository;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	UserServiceInter userServiceInter;

	@Autowired
	PaycontractMapper paycontractMapper;

	@Autowired
	PayContractRepository payContractRepository;
	
	@Autowired
	PayPlanRepository payPlanRepository;
	@Autowired
	ContractService contractService;
	/**
	 * 
	 * 查我的未支付订单
	 *
	 * @param principal
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public Map<String,String> queryMyUnPayOrders(Principal principal, String customName) {
		Map<String,String> idList = new LinkedHashMap<String,String>();
		if (StringUtils.isBlank(customName)) {
			return idList;
		}
		ProcessInstanceQuery queryTools = runtimeService.createProcessInstanceQuery().includeProcessVariables().processDefinitionKey(ActivitiService.MAIN_PROCESS)
				.involvedUser(Request.getUserId(principal));
		if (!StringUtils.isBlank(customName)) {
			queryTools = queryTools.variableValueLike(ActivitiService.COMPANY, "%" + customName + "%");
		}
		List<ProcessInstance> processInstances = queryTools.orderByProcessInstanceId().desc().list();
		for (ProcessInstance processInstance : processInstances) {
			Integer orderid = (Integer) processInstance.getProcessVariables().get(ActivitiService.ORDER_ID);
			if (orderid > 0) {
				JpaOrders order = ordersRepository.findOne(orderid);
				if (order != null) {
					long longOrderId = OrderIdSeq.getIdFromDate(orderid, order.getCreated());
					String id=String.valueOf(longOrderId);
					idList.put(id,id.concat(",") +order.getProduct().getName().concat(",")+ order.getProduct().getType().getTypeName()+","+order.getPrice()+","+(order.getIsChangeOrder() ?"换版订单":""));
				}
			}

		}

		/*
		 * OrdersExample example=new OrdersExample();
		 * example.createCriteria().andUserIdEqualTo(Request.getUserId(principal
		 * )).andStatsEqualTo(JpaOrders.Status.unpaid.ordinal()); List<Orders>
		 * list=ordersMapper.selectByExample(example); for (Orders orders :
		 * list) { long longOrderId = OrderIdSeq.getIdFromDate(orders.getId(),
		 * orders.getCreated()); idList.add(String.valueOf(longOrderId)); }
		 */
		return idList;

	}

	@Override
	public List<AutoCompleteView> OrderIdComplete(Principal principal, String name, HttpServletRequest request) {
		List<AutoCompleteView> r = new ArrayList<AutoCompleteView>();
		String customName = request.getParameter("customerName");
		Map<String,String> idList = queryMyUnPayOrders(principal, customName);
		for (Map.Entry<String,String> entry : idList.entrySet()) {
			r.add(new AutoCompleteView(entry.getKey()	, entry.getValue()));
		}
		return r;
	}

	/**
	 * 
	 * 根据订单号查
	 *
	 * @param orderId
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public List<JpaOrders> queryOrders(List<String> orderId) {
		List<Integer> intids = Lists.newArrayList();
		for (String one : orderId) {
			long longid = NumberUtils.toLong(one.trim());
			int intid = OrderIdSeq.longOrderId2DbId(longid);
			intids.add(intid);
		}
		BooleanExpression q = QJpaOrders.jpaOrders.id.in(intids);
		List<JpaOrders> list = (List<JpaOrders>) ordersRepository.findAll(q);
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
	public boolean checkSameCustom(List<String> orderId) {
		Set<String> set = Sets.newHashSet();
		List<JpaOrders> list = queryOrders(orderId);
		for (JpaOrders jpaOrders : list) {
			UserDetail userDetail = (UserDetail) JsonTools.readValue(jpaOrders.getCustomerJson(), UserDetail.class);
			set.add(userDetail.getUsername());
		}
		if (set.size() == 1) {
			return true;
		}
		return false;
	}

	@Override
	public List<OrderView> showOrderDetail(String orderIds) {
		List<OrderView> list = Lists.newArrayList();
		if (StringUtils.isNotBlank(orderIds)) {
			String[] arr = StringUtils.split(orderIds, ",");
			List<String> idStrings = Arrays.asList(arr);
			List<JpaOrders> jpaOrders = queryOrders(idStrings);
			for (JpaOrders one : jpaOrders) {
				OrderView oView = new OrderView();
				oView.setOrder(one);
				list.add(oView);
			}
		}
		return list;
	}

	@Override
	public Pair<Boolean, String> savePayContract(Principal principal, PaycontractWithBLOBs contract, HttpServletRequest request) {
		String orderIds = request.getParameter("orderid");
		if (StringUtils.isNotBlank(orderIds)) {
			String[] arr = StringUtils.split(orderIds, ",");
			List<String> idStrings = Arrays.asList(arr);
			contract.setOrderJson(JsonTools.getJsonFromObject(idStrings));
			if (null != contract.getId() && contract.getId() > 0) {
				PaycontractWithBLOBs paycontractWithBLOBs = paycontractMapper.selectByPrimaryKey(contract.getId());
				if (paycontractWithBLOBs != null) {
					paycontractWithBLOBs.setUpdated(new Date());
					BeanUtils.copyProperties(contract, paycontractWithBLOBs);
					int r = paycontractMapper.updateByPrimaryKeyWithBLOBs(paycontractWithBLOBs);
					if (r > 0) {
						List<JpaOrders> orders=queryOrders(idStrings);
						if(orders.size()>0){
							relateContract2Order(orders,contract.getId());
						}
						updateContractPlan(paycontractWithBLOBs);
						return new Pair<Boolean, String>(true, "合同修改成功！");
					}
				}
				return new Pair<Boolean, String>(false, "合同信息丢失！");

			}
			UserDetail copy = new UserDetail();
			UserDetail source = Request.getUser(principal);
			BeanUtils.copyProperties(source, copy);
			String customerId = request.getParameter("customerId");
			UserDetail customerObject = null;
			if (StringUtils.isNoneBlank(customerId)) {
				customerObject = userServiceInter.findDetailByUsername(customerId);
			}
			if (source.getUser() != null) {
				copy.setFirstName(source.getUser().getFirstName());
				copy.setEmail(source.getUser().getEmail());
				copy.setUser(null);
				copy.setGroups(null);
				copy.setFunctions(null);
			}
			if (customerObject != null) {
				contract.setCustomerJson(JsonTools.getJsonFromObject(customerObject));
			}
			contract.setOrderUserJson(JsonTools.getJsonFromObject(copy));
			contract.setCreated(new Date());
			contract.setUserId(Request.getUserId(principal));
			contract.setOrderJson(JsonTools.getJsonFromObject(idStrings));
			contract.setDelFlag(false);
			contract.setPayPrice(0.0);
			contract.setContractCode(contractService.getContractId());
			contract.setCloseFlag(false);
			int r = paycontractMapper.insert(contract);
			if (r > 0) {
				updateContractPlan(contract);
				List<JpaOrders> orders=queryOrders(idStrings);
				if(orders.size()>0){
					relateContract2Order(orders,contract.getId());
				}
				return new Pair<Boolean, String>(true, "合同创建成功！").put("contractInfo", contract);
			}
		}
		return new Pair<Boolean, String>(false, "操作失败！");
	}

	
	@Autowired
	ActivitiService activitiService;
	public void relateContract2Order(List<JpaOrders> orders, Integer payContratcId) {
		JpaPayContract contract=payContractRepository.findOne(payContratcId);
		for (JpaOrders jpaOrders : orders) {
			jpaOrders.setJpaPayContract(contract);
			jpaOrders.setContractCode(contract.getContractCode());
			ordersRepository.save(jpaOrders);
			if(contract.getPayPrice()>0){
				activitiService.closeOrderPay(jpaOrders.getId());
			}
		}
		
	}

	public void updateContractPlan(PaycontractWithBLOBs contract) {

		PayPlan plan = new PayPlan();
		plan.setContractId(contract.getId());
		PayPlanExample example = new PayPlanExample();
		example.createCriteria().andSeriaNumEqualTo(contract.getSeriaNum());
		payPlanMapper.updateByExampleSelective(plan, example);

	}

	@Override
	public Page<JpaPayContract> getAllContracts(TableRequest req, Principal principal) {
		String code = req.getFilter("contractCode"),stateKey=req.getFilter("stateKey"),
				salesName= req.getFilter("salesName");
		int page = req.getPage(), pageSize = req.getLength();
		Sort sort = req.getSort("id");
		if (page < 0)
			page = 0;
		if (pageSize < 1)
			pageSize = 1;
		sort = (sort == null ? new Sort("id") : sort);
		Pageable p = new PageRequest(page, pageSize, sort);
		BooleanExpression query = QJpaPayContract.jpaPayContract.id.gt(0);
		if (StringUtils.isNotBlank(code)) {
			query = query.and(QJpaPayContract.jpaPayContract.contractCode.like("%" + code + "%"));
		}
		if (StringUtils.isNotBlank(stateKey)) {
			if(StringUtils.equals("ing", stateKey)){
				query = query.and(QJpaPayContract.jpaPayContract.delFlag.eq(false).and(QJpaPayContract.jpaPayContract.price.gt(QJpaPayContract.jpaPayContract.payPrice)));
			}else if(StringUtils.equals("completed", stateKey)){
				query = query.and(QJpaPayContract.jpaPayContract.delFlag.eq(false).and(QJpaPayContract.jpaPayContract.payPrice.goe(QJpaPayContract.jpaPayContract.price)));
			}else if(StringUtils.equals("closed", stateKey)){
				query = query.and(QJpaPayContract.jpaPayContract.closeFlag.eq(true));
			}else if(StringUtils.equals("del", stateKey)){
				query = query.and(QJpaPayContract.jpaPayContract.delFlag.eq(true));
			}
		}else{
			query = query.and(QJpaPayContract.jpaPayContract.delFlag.eq(false).and( QJpaPayContract.jpaPayContract.closeFlag.eq(false)));
			query = query.and(QJpaPayContract.jpaPayContract.price.gt(QJpaPayContract.jpaPayContract.payPrice));
		}
		if (StringUtils.isNotBlank(salesName)) {
			query = query.and(QJpaPayContract.jpaPayContract.salesName.like("%" + salesName + "%"));
		}
		return payContractRepository.findAll(query, p);
	}
	public long getAllNotPayContractCount(TableRequest req, Principal principal) {
		BooleanExpression query = getQueryExpress(req);
		return payContractRepository.count(query);
	}
	@Override
	public Page<JpaPayContract> getAllNotPayContracts(TableRequest req, Principal principal) {
		BooleanExpression query = getQueryExpress(req);
		int page = req.getPage(), pageSize = req.getLength();
		Sort sort = req.getSort("id");
		if (page < 0)
			page = 0;
		if (pageSize < 1)
			pageSize = 1;
		sort = (sort == null ? new Sort("id") : sort);
		Pageable p = new PageRequest(page, pageSize, sort);
		return payContractRepository.findAll(query, p);
	}

	private BooleanExpression getQueryExpress(TableRequest req) {
		String code = req.getFilter("contractCode"),stateKey = req.getFilter("stateKey");;
		
		
		BooleanExpression query = QJpaPayContract.jpaPayContract.delFlag.isFalse().and(QJpaPayContract.jpaPayContract.closeFlag.isFalse());
		if (StringUtils.isNotBlank(code)) {
			query = query.and(QJpaPayContract.jpaPayContract.contractCode.like("%" + code + "%"));
		}
		if (StringUtils.isNoneBlank(stateKey)) {
			if (StringUtils.equals(stateKey, "complete")) {
				query=query.and(QJpaPayContract.jpaPayContract.price.loe(QJpaPayContract.jpaPayContract.payPrice));
			} else if (StringUtils.equals(stateKey, "notcomplete")) {
				query=query.and(QJpaPayContract.jpaPayContract.price.gt(QJpaPayContract.jpaPayContract.payPrice));
			}
		} else {
			query=query.and(QJpaPayContract.jpaPayContract.price.gt(QJpaPayContract.jpaPayContract.payPrice));
		}
		return query;
	}

	@Override
	public String toRestPayContract(int contarctId, Model model, Principal principal) {
		JpaPayContract contract=getPayContractById(contarctId);
		double payAll = contract.getPrice() - contract.getPayPrice();
		model.addAttribute("payAll", payAll);
		orderService.getPayNextMoney(0,contarctId, model);
		model.addAttribute("jpaPayContract", contract);
		return "payContract/restPayContract";
	}

	@Override
	public Pair<Boolean, String> saveMark(int id, HttpServletRequest request, Principal principal) {
		JpaPayContract contract= getPayContractById(id);
		String jsonStr=request.getParameter("jsonStr");
		if(contract==null){
			return new Pair<Boolean, String>(false, "合同不存在");
		}
		if(StringUtils.isNotBlank(jsonStr)){
			contract.setAgreement(jsonStr);
			if(payContractRepository.save(contract)!=null){
				return new Pair<Boolean, String>(true, "保存成功");
			}
		}
		return new Pair<Boolean, String>(false, "操作失败");
	}

	@Override
	public JpaPayContract getPayContractById(int id) {
		return payContractRepository.findOne(id);
	}

	@Override
	public Pair<Boolean, String> delPayContract(Principal principal, int contractId) {
		JpaPayContract contract = payContractRepository.findOne(contractId);
		if (contract != null) {
			contract.setDelFlag(true);
			if (payContractRepository.save(contract) != null) {
				return new Pair<Boolean, String>(true, "删除成功");
			}
		}
		return new Pair<Boolean, String>(false, "操作失败");
	}

	@Override
	public Pair<Boolean, String> colseContract(int contractId, Principal principal) {
		JpaPayContract contract = payContractRepository.findOne(contractId);
		if(contract==null){
			return new Pair<Boolean, String>(false, "合同不存在");
		}
		int count = queryContractUse(contractId);
		if(count>0){
			return new Pair<Boolean, String>(false, "当前合同下有待审核的款项,不能关闭");
		}
		if(contract.isCloseFlag()){
			return new Pair<Boolean, String>(false, "合同已经关闭");
		}
		if(contract.getPrice()==contract.getPayPrice() || contract.getPayPrice()>contract.getPrice()){
			return new Pair<Boolean, String>(false, "合同已完成");
		}
		contract.setCloseFlag(true);
		contract.setUpdated(new Date());
		contract.setUpdator(Request.getUserId(principal));
		if(payContractRepository.save(contract)!=null){
			return new Pair<Boolean, String>(true, "操作成功");
		}
		return new Pair<Boolean, String>(true, "操作成功");
	}

	public int queryContractUse(int contractId) {
		BooleanExpression q=QJpaPayPlan.jpaPayPlan.contract.id.eq(contractId)
				.and(QJpaPayPlan.jpaPayPlan.payState.eq(JpaPayPlan.PayState.check))
				.and(QJpaPayPlan.jpaPayPlan.tableType.eq(JpaPayPlan.TableType.reced));
		int count=(int) payPlanRepository.count(q);
		return count;
	}

	@Override
	public Map<String, String> getAgreemet(JpaPayContract contract) {
		Map<String, String> map=new HashMap<>();
		if(StringUtils.isNotBlank(contract.getAgreement())){
			map=(Map<String, String>) JsonTools.readValue(contract.getAgreement(), Map.class);
		}
		if(StringUtils.isNotBlank(contract.getCustomerJson())){
			UserDetail userDetail = (UserDetail) JsonTools.readValue(contract.getCustomerJson(), UserDetail.class);
			map.put("company", userDetail.getCompany());
		}
		return map;
	}

	public JpaPayPlan queryByPlanId(int planId){
		return payPlanRepository.findOne(planId);
	}

	@Override
	public JpaPayContract queryContractByPlanId(int planId) {
		JpaPayPlan plan = queryByPlanId(planId);
		return plan != null ? plan.getContract() : null;
	}
	 
}
