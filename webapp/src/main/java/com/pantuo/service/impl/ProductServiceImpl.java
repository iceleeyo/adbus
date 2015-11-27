package com.pantuo.service.impl;

import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.omg.PortableInterceptor.INACTIVE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.mysema.query.types.expr.BooleanExpression;
import com.pantuo.ActivitiConfiguration;
import com.pantuo.dao.BusOrderDetailV2Repository;
import com.pantuo.dao.BusOrderV2Repository;
import com.pantuo.dao.CpdRepository;
import com.pantuo.dao.ProductRepository;
import com.pantuo.dao.ProductTagRepository;
import com.pantuo.dao.ProductV2Repository;
import com.pantuo.dao.pojo.JpaBusOrderDetailV2;
import com.pantuo.dao.pojo.JpaBusOrderV2;
import com.pantuo.dao.pojo.JpaBusline;
import com.pantuo.dao.pojo.JpaCpd;
import com.pantuo.dao.pojo.JpaProduct;
import com.pantuo.dao.pojo.JpaProduct.FrontShow;
import com.pantuo.dao.pojo.JpaProductV2;
import com.pantuo.dao.pojo.QJpaBusOrderDetailV2;
import com.pantuo.dao.pojo.QJpaBusOrderV2;
import com.pantuo.dao.pojo.QJpaProduct;
import com.pantuo.dao.pojo.QJpaProductV2;
import com.pantuo.mybatis.domain.BusOrderDetailV2;
import com.pantuo.mybatis.domain.BusOrderDetailV2Example;
import com.pantuo.mybatis.domain.BusOrderV2;
import com.pantuo.mybatis.domain.OrdersExample;
import com.pantuo.mybatis.domain.Product;
import com.pantuo.mybatis.domain.ProductExample;
import com.pantuo.mybatis.domain.ProductV2;
import com.pantuo.mybatis.persistence.BusOrderDetailV2Mapper;
import com.pantuo.mybatis.persistence.BusOrderV2Mapper;
import com.pantuo.mybatis.persistence.OrdersMapper;
import com.pantuo.mybatis.persistence.ProductMapper;
import com.pantuo.mybatis.persistence.ProductV2Mapper;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.AttachmentService;
import com.pantuo.service.BusService;
import com.pantuo.service.ProductService;
import com.pantuo.simulate.ProductProcessCount;
import com.pantuo.util.BusinessException;
import com.pantuo.util.NumberPageUtil;
import com.pantuo.util.Pair;
import com.pantuo.util.ProductOrderCount;
import com.pantuo.util.Request;
import com.pantuo.web.view.MediaSurvey;
import com.pantuo.web.view.ProductView;

@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	ProductMapper productMapper;
	@Autowired
	BusOrderDetailV2Mapper busOrderDetailV2Mapper;
	@Autowired
	ProductV2Mapper productV2Mapper;
	@Autowired
	BusOrderV2Mapper busOrderV2Mapper;
	@Autowired
	ProductRepository productRepo;
	
	
	
	@Autowired
	ProductTagRepository productTagRepository;
	@Autowired
	CpdRepository cpdRepository;
	@Autowired
	ProductV2Repository productV2Repository;
	@Autowired
	BusOrderV2Repository busOrderV2Repository;
	@Autowired
	BusOrderDetailV2Repository busOrderDetailV2Repository;
	@Autowired
	BusService busService;
	@Autowired
	AttachmentService attachmentService;

	@Autowired
	BusOrderDetailV2Mapper v2Mapper;
	@Autowired
	OrdersMapper ordersMapper;
	@Autowired
	BusOrderV2Mapper v2OMapper;

	@Autowired
	BusService busservice;

	private static Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

	public Page<JpaProduct> getAllProducts(int city, boolean includeExclusive, String exclusiveUser, TableRequest req) {
		String name = req.getFilter("name"),stats=req.getFilter("stats");
		int page = req.getPage(), pageSize = req.getLength();
		Sort sort = req.getSort("id");

		if (page < 0)
			page = 0;
		if (pageSize < 1)
			pageSize = 1;
		sort = (sort == null ? new Sort("id") : sort);
		Pageable p = new PageRequest(page, pageSize, sort);
		BooleanExpression query = city >= 0 ? QJpaProduct.jpaProduct.city.eq(city) : QJpaProduct.jpaProduct.city.goe(0);
		if (!includeExclusive) {
			query = query.and(QJpaProduct.jpaProduct.exclusive.eq(false));
		} else if (exclusiveUser != null) {
			query = query.and(QJpaProduct.jpaProduct.exclusive.eq(false).or(
					QJpaProduct.jpaProduct.exclusiveUser.eq(exclusiveUser)));
		}
		if (StringUtils.isNotBlank(name)) {
			query = query.and(QJpaProduct.jpaProduct.name.like("%" + name + "%"));
		}
		if (StringUtils.isNotBlank(stats) && !StringUtils.endsWith(stats, "defaultAll")) {
			
			query = query.and(QJpaProduct.jpaProduct.enabled.eq(BooleanUtils.toBooleanObject(stats)));
		}
		query = query.and(QJpaProduct.jpaProduct.iscompare.eq(0));
		return productRepo.findAll(query, p);
	}

	BooleanExpression getQueryFromPage(String name, String sh) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		BooleanExpression query = null;
		String[] shSplit = StringUtils.split(sh, ",");
		if (shSplit != null) {
			for (String string : shSplit) {//p1
				String[] one = StringUtils.split(string, "_");
				String field = one[0];
				String v = one[1];
				if (!map.containsKey(field)) {
					map.put(field, new ArrayList<String>());
				}
				if (!StringUtils.equals("all", v)) {
					map.get(field).add(v);
				}
			}
		}
		for (Map.Entry<String, List<String>> entry : map.entrySet()) {
			List<String> vIntegers = entry.getValue();
			if (StringUtils.equals(entry.getKey(), "t") && vIntegers.size() > 0) {
				List<JpaProduct.Type> right = new ArrayList<JpaProduct.Type>();
				for (String type : vIntegers) {
					right.add(JpaProduct.Type.valueOf(type));
				}
				query = query == null ? QJpaProduct.jpaProduct.type.in(right) : query.and(QJpaProduct.jpaProduct.type
						.in(right));
			} else if (StringUtils.equals(entry.getKey(), "p") && vIntegers.size() > 0) {
				BooleanExpression subQuery = null;
				for (String playNumber : vIntegers) {
					if (StringUtils.equals("2", playNumber)) {
						subQuery = subQuery == null ? QJpaProduct.jpaProduct.iscompare.eq(1) : subQuery
								.or(QJpaProduct.jpaProduct.iscompare.eq(1));
					} else if (StringUtils.equals("3", playNumber)) {
						subQuery = subQuery == null ? QJpaProduct.jpaProduct.iscompare.eq(0) : subQuery
								.or(QJpaProduct.jpaProduct.iscompare.eq(0));
					}
				}
				query = query == null ? subQuery : query.and(subQuery);
			} else if (StringUtils.equals(entry.getKey(), "s") && vIntegers.size() > 0) {
				BooleanExpression subQuery = null;
				for (String playNumber : vIntegers) {
					 String[] qc=StringUtils.split(playNumber, "-");
	                    int f= NumberUtils.toInt(qc[0]);
	                    int e= NumberUtils.toInt(qc[1]);
	                    subQuery = subQuery == null ? QJpaProduct.jpaProduct.playNumber.between(f, e) : subQuery
								.or(QJpaProduct.jpaProduct.playNumber.between(f, e));
				}
				query = query == null ? subQuery : query.and(subQuery);
			} else if (StringUtils.equals(entry.getKey(), "d") && vIntegers.size() > 0) {
				BooleanExpression subQuery = null;
				for (String playNumber : vIntegers) {
                     String[] qc=StringUtils.split(playNumber, "-");
                    int f= NumberUtils.toInt(qc[0]);
                    int e= NumberUtils.toInt(qc[1]);
					subQuery = subQuery == null ? QJpaProduct.jpaProduct.days.between(f, e) : subQuery
								.or(QJpaProduct.jpaProduct.days.between(f, e));
					
				}
				query = query == null ? subQuery : query.and(subQuery);
			} else if (StringUtils.equals(entry.getKey(), "lev") && vIntegers.size() > 0) {
				BooleanExpression subQuery = null;
				List<JpaBusline.Level> right = new ArrayList<JpaBusline.Level>();
				for (String type : vIntegers) {
					right.add(JpaBusline.Level.valueOf(type));
				}
				subQuery = subQuery == null ? QJpaProduct.jpaProduct.lineLevel.in(right) : subQuery
						.and(QJpaProduct.jpaProduct.lineLevel.in(right));
				query = query == null ? subQuery : query.and(subQuery);
			}

		}

		return query;

	}

	public Page<JpaProduct> searchProducts(int city, Principal principal, TableRequest req) {
		String name = req.getFilter("name");
		String sh = req.getFilter("sh"),price1=req.getFilter("price1"),price2=req.getFilter("price2"),searchText=req.getFilter("searchText");
		BooleanExpression commonEx = getQueryFromPage(name, sh);
		int page = req.getPage(), pageSize = req.getLength();
		Sort sort = req.getSort("id");

		if (page < 0)
			page = 0;
		if (pageSize < 1)
			pageSize = 1;
		sort = (sort == null ? new Sort("id") : sort);
		if (StringUtils.equals(req.getFilter("p"), "1")) {
			sort = new Sort(Direction.fromString("desc"), "price");
		} else {
			sort = new Sort(Direction.fromString("asc"), "price"); 
		}
		Pageable p = new PageRequest(page, pageSize, sort);
		BooleanExpression query = city >= 0 ? QJpaProduct.jpaProduct.city.eq(city) : QJpaProduct.jpaProduct.city.goe(0);
		query = query.and(QJpaProduct.jpaProduct.enabled.eq(true));
		query = query.and(QJpaProduct.jpaProduct.iscompare.eq(0));
		if (principal == null || Request.hasOnlyAuth(principal, ActivitiConfiguration.ADVERTISER)) {
			query = query.and(QJpaProduct.jpaProduct.exclusive.eq(false).or(
					QJpaProduct.jpaProduct.exclusiveUser.eq(Request.getUserId(principal))));
		}
		if (commonEx != null) {
			query = query.and(commonEx);
		}
		if (name != null && !StringUtils.isEmpty(name)) {
			query = query.and(QJpaProduct.jpaProduct.name.like("%" + name + "%"));
		}
		if (StringUtils.isNotEmpty(searchText)) {
			searchText =StringUtils.replace(searchText, "%", "");
			searchText =StringUtils.replace(searchText, "'", "");
			searchText =StringUtils.replace(searchText, "\r\n", "");
			if(StringUtils.isNotEmpty(searchText)){
				query = query.and(QJpaProduct.jpaProduct.tags.like("%" + searchText + "%")
						.or(QJpaProduct.jpaProduct.name.like("%" + searchText + "%")));
			
			}else {//保证不查到记录
				query = query.and(QJpaProduct.jpaProduct.tags.like("%QJpaProduct.jpaProduct.%"));	
			}
		}
		if (StringUtils.isNotBlank(price1)) {
			double p1=NumberUtils.toDouble(price1);
			query = query.and(QJpaProduct.jpaProduct.price.goe(p1));
		}
		if (StringUtils.isNotBlank(price2)) {
			double p2=NumberUtils.toDouble(price2);
			query = query.and(QJpaProduct.jpaProduct.price.loe(p2));
		}

		return productRepo.findAll(query, p);
	}

	//  @Override
	public Page<JpaProduct> getValidProducts(int city, JpaProduct.Type type, boolean includeExclusive,
			String exclusiveUser, TableRequest req, FrontShow... fs) {
		int page = req.getPage(), pageSize = req.getLength();
		Sort sort = req.getSort("id");
		if (page < 0)
			page = 0;
		if (pageSize < 1)
			pageSize = 1;
		sort = (sort == null ? new Sort(Sort.Direction.DESC, "id") : sort);
		Pageable p = new PageRequest(page, pageSize, sort);
		BooleanExpression query = city >= 0 ? QJpaProduct.jpaProduct.city.eq(city) : QJpaProduct.jpaProduct.city.goe(0);
		if (!includeExclusive) {
			query = query.and(QJpaProduct.jpaProduct.exclusive.eq(false));
		} else if (exclusiveUser != null) {
			query = query.and(QJpaProduct.jpaProduct.exclusive.eq(false).or(
					QJpaProduct.jpaProduct.exclusiveUser.eq(exclusiveUser)));
		}
		if (type != null) {
			query = query.and(QJpaProduct.jpaProduct.type.eq(type));
		}
		if (fs != null && fs.length > 0) {
			query = query.and(QJpaProduct.jpaProduct.frontShow.eq(fs[0]));
		}
		query = query.and(QJpaProduct.jpaProduct.iscompare.eq(0));
		query = query.and(QJpaProduct.jpaProduct.enabled.isTrue());
		return productRepo.findAll(query, p);
	}

	//  @Override
	public JpaProduct findById(int productId) {
		return productRepo.findOne(productId);
	}

	@Override
	public JpaCpd findCpdById(int id) {
		return cpdRepository.findOne(id);
	}

	//  @Override
	public void saveProduct(int city, JpaProduct product,MediaSurvey survey,HttpServletRequest request) {
		try {
			if(null!=survey){
				ObjectMapper t = new ObjectMapper();
				t.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				t.getSerializationConfig().setSerializationInclusion(Inclusion.NON_NULL);
				try {
					String jsonString = t.writeValueAsString(survey);
					product.setJsonString(jsonString);
				} catch (JsonGenerationException e) {
					e.printStackTrace();
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			product.setCity(city);
			com.pantuo.util.BeanUtils.filterXss(product);
			product.setExclusiveUser(product.getExclusiveUser());
			if(product.getId()>0){
				String a=attachmentService.saveAttachmentSimple(request);
				if(a.length()>1){
					product.setImgurl(a);
				}
			}else{
				if(request!=null){
					product.setImgurl(attachmentService.saveAttachmentSimple(request));
				}
			}
			productRepo.save(product);
		} catch (BusinessException e) {
		}
	}

	public int countMyList(int city, String name, String code, HttpServletRequest request) {
		return productMapper.countByExample(getExample(city, name, code));
	}

	public ProductExample getExample(int city, String name, String code) {
		ProductExample example = new ProductExample();
		ProductExample.Criteria ca = example.createCriteria();
		ca.andCityEqualTo(city);
		if (StringUtils.isNoneBlank(name)) {
			//	ca.andContractNameLike("%" + name + "%");
		}
		if (StringUtils.isNoneBlank(code) && Long.parseLong(code) > 0) {
			//ca.andContractNumEqualTo(Long.parseLong(code));
		}
		return example;
	}

	public List<Product> queryContractList(int city, NumberPageUtil page, String name, String code,
			HttpServletRequest request) {
		ProductExample ex = getExample(city, name, code);
		ex.setOrderByClause("id desc");
		ex.setLimitStart(page.getLimitStart());
		ex.setLimitEnd(page.getPagesize());
		return productMapper.selectByExample(ex);
	}

	public Product selectProById(Integer productId) {
		return productMapper.selectByPrimaryKey(productId);
	}

	@Override
	public Page<ProductView> getProductView(Page<JpaProduct> list) {

		List<ProductView> plist = new ArrayList<ProductView>();
		if (list != null) {
			for (JpaProduct jpaProduct : list) {
				ProductView w = new ProductView();
				BeanUtils.copyProperties(jpaProduct, w);
				plist.add(w);
				ProductOrderCount c = ProductProcessCount.map.get(jpaProduct.getId());
				if (c != null) {
					w.setRunningCount(c.getRunningCount());
					w.setFinishedCount(c.getFinishedCount());
				}
			}
		}
		Pageable p = new PageRequest(list.getNumber(), list.getSize(), list.getSort());
		org.springframework.data.domain.PageImpl<ProductView> r = new org.springframework.data.domain.PageImpl<ProductView>(
				plist, p, list.getTotalElements());
		return r;
	}

	@Override
	public Pair<Boolean, Long> saveBusOrderDetail(JpaBusOrderDetailV2 prod) {
		//com.pantuo.util.BeanUtils.filterXss(prod);
		 long price=busService.getMoneyFromBusModel(prod.getLeval(), prod.isDoubleDecker());
		 price=price*prod.getBusNumber()*prod.getDays()/30;
		 prod.setPrice(price);
		long sumprice=0;
		if(busOrderDetailV2Repository.save(prod)!=null){
		    sumprice=getSumPriceBySerinum(prod.getSeriaNum());
			return new Pair<Boolean, Long>(true,sumprice);
		}
		return new Pair<Boolean, Long>(false,sumprice);
	}

	private long getSumPriceBySerinum(long seriaNum) {
		BusOrderDetailV2Example example=new BusOrderDetailV2Example();
		example.createCriteria().andSeriaNumEqualTo(seriaNum);
		List<BusOrderDetailV2> list=busOrderDetailV2Mapper.selectByExample(example);
		long sum=0;
		if(list.size()>0){
			for (BusOrderDetailV2 busOrderDetailV2 : list) {
				sum+=busOrderDetailV2.getPrice();
			}
		}
		return sum;
	}

	@Override
	public Page<JpaBusOrderDetailV2> searchBusOrderDetailV2(int orderid,int pid,long seriaNum,int city, Principal principal, TableRequest req) {
		int page = req.getPage(), pageSize = req.getLength();
		Sort sort = req.getSort("id");
		if (page < 0)
			page = 0;
		if (pageSize < 1)
			pageSize = 1;
		sort = (sort == null ? new Sort("id") : sort);
		Pageable p = new PageRequest(page, pageSize, sort);
		BooleanExpression query = QJpaBusOrderDetailV2.jpaBusOrderDetailV2.city.eq(city);
		if(seriaNum>0){
			query = query.and(QJpaBusOrderDetailV2.jpaBusOrderDetailV2.seriaNum.eq(seriaNum));
		}
		if( pid>0){
			query = query.and(QJpaBusOrderDetailV2.jpaBusOrderDetailV2.JpaProductV2.id.eq(pid));
		}
		if( orderid>0){
			query = query.and(QJpaBusOrderDetailV2.jpaBusOrderDetailV2.jpaBusOrderV2.id.eq(orderid));
		}
		return busOrderDetailV2Repository.findAll(query, p);
	}

	@Override
	public Pair<Boolean, String> saveProductV2(ProductV2 productV2,MediaSurvey survey, long seriaNum, String userId) {
		BusOrderDetailV2Example example = new BusOrderDetailV2Example();
		 example.createCriteria().andSeriaNumEqualTo(seriaNum);
		List<BusOrderDetailV2> list = busOrderDetailV2Mapper.selectByExample(example);
		if (list.size() == 0) {
			return new Pair<Boolean, String>(false, "请添加套餐方案");
		}
		if(null!=survey){
			ObjectMapper t = new ObjectMapper();
			t.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			t.getSerializationConfig().setSerializationInclusion(Inclusion.NON_NULL);
			try {
				String jsonString = t.writeValueAsString(survey);
				productV2.setJsonString(jsonString);
			} catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		productV2.setCreated(new Date());
		productV2.setUpdated(new Date());
		productV2.setCreater(userId);
		productV2.setStats(0);
		int a = productV2Mapper.insert(productV2);
		if (a > 0) {
			for (BusOrderDetailV2 v : list) {
				if (v != null) {
				  v.setProductId(productV2.getId());
				  busOrderDetailV2Mapper.updateByPrimaryKey(v);
				}
			}
			return new Pair<Boolean, String>(true, "添加套餐成功");
		} 
			return new Pair<Boolean, String>(false, "添加套餐失败");
		
	}

	@Override
	public Page<JpaProductV2> searchProductV2s(int city, Principal principal, TableRequest req) {
		String name = req.getFilter("name");
		int page = req.getPage(), pageSize = req.getLength();
		Sort sort = req.getSort("id");

		if (page < 0)
			page = 0;
		if (pageSize < 1)
			pageSize = 1;
		sort = (sort == null ? new Sort("id") : sort);
		Pageable p = new PageRequest(page, pageSize, sort);
		BooleanExpression query = city >= 0 ? QJpaProductV2.jpaProductV2.city.eq(city) : QJpaProduct.jpaProduct.city.goe(0);
		if (StringUtils.isNotBlank(name)) {
			query = query.and(QJpaProductV2.jpaProductV2.name.like("%" + name + "%"));
		}
		return productV2Repository.findAll(query, p);
	}

	@Override
	public Pair<Boolean, String> buyBodyPro(int pid, int city, String userId) {
		JpaProductV2 productV2=productV2Repository.findOne(pid);
		if(productV2!=null){
			BusOrderV2 v2=new BusOrderV2();
			v2.setCity(city);
			v2.setCreated(new Date());
			v2.setCreater(userId);
			v2.setIspay(false);
			v2.setOrderPrice(productV2.getPrice());
			v2.setProductPrice(productV2.getPrice());
			v2.setOrderStatus(JpaBusOrderV2.BusOrderStatus.begin.ordinal());
			v2.setProductId(pid);
			v2.setSeriaNum((long)0);
			if(busOrderV2Mapper.insert(v2)>0){
				return new Pair<Boolean, String>(true, "下单成功");
			}else{
				return new Pair<Boolean, String>(false, "下单失败");
			}
		}else{
			return new Pair<Boolean, String>(false, "信息丢失");
		}
	}

	@Override
	public Page<JpaBusOrderV2> searchBusOrderV2(int city, Principal principal, TableRequest req, String type) {
		String name = req.getFilter("name");
		int page = req.getPage(), pageSize = req.getLength();
		Sort sort = req.getSort("id");

		if (page < 0)
			page = 0;
		if (pageSize < 1)
			pageSize = 1;
		sort = (sort == null ? new Sort("id") : sort);
		Pageable p = new PageRequest(page, pageSize, sort);
		BooleanExpression query = city >= 0 ? QJpaBusOrderV2.jpaBusOrderV2.city.eq(city) : QJpaProduct.jpaProduct.city.goe(0);
		if (StringUtils.isNotBlank(name)) {
			query = query.and(QJpaBusOrderV2.jpaBusOrderV2.JpaProductV2.name.like("%" + name + "%"));
		}
		if(StringUtils.equals(type, "my")){
			query = query.and(QJpaBusOrderV2.jpaBusOrderV2.creater.eq(Request.getUserId(principal)));
		}
		return busOrderV2Repository.findAll(query, p);
	}

	@Override
	public Pair<Boolean, String> removeBusOrderDetail(Principal principal, int city, int id) {
		BusOrderDetailV2 v=busOrderDetailV2Mapper.selectByPrimaryKey(id);
		if(v!=null){
			if(busOrderDetailV2Mapper.deleteByPrimaryKey(id)>0){
				return new Pair<Boolean, String>(true, "删除成功");
			}else{
				return new Pair<Boolean, String>(false, "操作失败");
			}
		}
		return new Pair<Boolean, String>(false, "信息丢失");
	}

	public static class PlanRequest {
		String level = null;
		Boolean doubleChecker = false;
		int days = 0;
		String msg;

		public PlanRequest(String level, Boolean doubleChecker, int days) {
			this.level = level;
			this.doubleChecker = doubleChecker;
			this.days = days;
		}

		public PlanRequest(String msg) {
			super();
			this.msg = msg;
		}

		public PlanRequest() {
			super();
		}

		public String getLevel() {
			return level;
		}

		public void setLevel(String level) {
			this.level = level;
		}

		public Boolean getDoubleChecker() {
			return doubleChecker;
		}

		public void setDoubleChecker(Boolean doubleChecker) {
			this.doubleChecker = doubleChecker;
		}

		public int getDays() {
			return days;
		}

		public void setDays(int days) {
			this.days = days;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

	}

	public Pair<Boolean, PlanRequest> checkPlan(int city, String select) {
		PlanRequest request = new PlanRequest();
		Pair<Boolean, PlanRequest> r = new Pair<Boolean, PlanRequest>(false, request);
		String[] split = StringUtils.split(select, ",");
		if (split.length < 3) {
			request.msg = ("组合筛选条件不够,请再选择!");
			return r;
		}
		String level = null;
		Boolean doubleChecker = false;
		int days = 0;
		//lev_APP,dc_N,d_90,
		for (String string : split) {
			String[] sp2 = StringUtils.split(string, "_");
			if (StringUtils.startsWith(string, "lev_")) {
				level = sp2.length > 1 ? sp2[1] : null;
				request.setLevel(level);
			} else if (StringUtils.startsWith(string, "dc_")) {
				doubleChecker = sp2.length > 1 ? BooleanUtils.toBoolean(sp2[1]) : null;
				request.setDoubleChecker(doubleChecker);
			} else if (StringUtils.startsWith(string, "d_")) {
				days = sp2.length > 1 ? NumberUtils.toInt(sp2[1]) : 0;
				request.setDays(days);
			}
		}
		if (StringUtils.isBlank(level)) {
			request.msg = ("请选择相应的车辆级别!");
			return r;
		} else if (doubleChecker == null) {
			request.msg = ("请选择相应的车辆类型!");
			return r;
		} else if (days == 0) {
			request.msg = ("请选择相应的展示周期!");
			return r;
		}
		r.setLeft(true);
		return r;
	}

	public Pair<Boolean, PlanRequest> addPlan(int city, long seriaNum, String select, int number, String startDate1,
			Principal principal) {
		Pair<Boolean, PlanRequest> checkResult = checkPlan(city, select);
		try {
			if (!checkResult.getLeft())
				return checkResult;
			BusOrderDetailV2 v2 = new BusOrderDetailV2();
			v2.setBusNumber(number);
			v2.setDoubleDecker(checkResult.getRight().doubleChecker);
			v2.setCreated(new Date());
			v2.setCity(city);
			v2.setUpdated(new Date());
			v2.setLeval(JpaBusline.Level.valueOf(checkResult.getRight().level).ordinal());
			v2.setStartTime((Date) new SimpleDateFormat("yyyy-MM-dd").parseObject(startDate1));
			v2.setSeriaNum(seriaNum);
			double basePrice = busservice.getMoneyFromBusModel(JpaBusline.Level.valueOf(checkResult.getRight().level),
					checkResult.getRight().doubleChecker) * 1d;
			v2.setPrice(basePrice * checkResult.getRight().days / 30 * number);
			v2.setDays(checkResult.getRight().days);
			v2.setCreater(Request.getUserId(principal));
			v2Mapper.insert(v2);
		} catch (ParseException e) {
			checkResult.setLeft(false);
			checkResult.getRight().setMsg("保存投放计划失败!");
			return checkResult;
		}
		return checkResult;
	}

	public List<BusOrderDetailV2> getOrderDetailV2BySeriNum(long seriaNum, Principal principal) {
		BusOrderDetailV2Example example = new BusOrderDetailV2Example();
		example.createCriteria().andSeriaNumEqualTo(seriaNum).andCreaterEqualTo(Request.getUserId(principal));
		//需要加用户判断 
		return v2Mapper.selectByExample(example);
	}

	public Pair<Boolean, String> buildPlan(int city, long seriaNum, Principal principal) {
		Pair<Boolean, String> r = new Pair<Boolean, String>(false, StringUtils.EMPTY);
		BusOrderDetailV2Example example = new BusOrderDetailV2Example();
		example.createCriteria().andSeriaNumEqualTo(seriaNum).andCreaterEqualTo(Request.getUserId(principal));
		int totalPlan = v2Mapper.countByExample(example);
		
		double totalMoney = 0d;
		if (totalPlan < 1) {
			r.setRight("未找着投放计划");
			return r;
		}else {
		List<BusOrderDetailV2>	s = v2Mapper.selectByExample(example);
			for (BusOrderDetailV2 busOrderDetailV2 : s) {
				totalMoney+=busOrderDetailV2.getPrice();
			}
		}
		BusOrderV2 record = new BusOrderV2();
		record.setCity(city);
		record.setCreated(new Date());
		record.setUpdated(new Date());
		record.setIspay(false);
		record.setSeriaNum(seriaNum);
		record.setCreater(Request.getUserId(principal));
		record.setProductPrice(totalMoney);
		record.setOrderPrice(totalMoney);
		record.setOrderStatus(JpaBusOrderV2.BusOrderStatus.begin.ordinal());
		v2OMapper.insert(record);
		BusOrderDetailV2 v2 =new BusOrderDetailV2();
		BusOrderDetailV2Example example2 = new BusOrderDetailV2Example();
		example2.createCriteria().andSeriaNumEqualTo(seriaNum).andCreaterEqualTo(Request.getUserId(principal));
		v2.setOrderid(record.getId());
		v2Mapper.updateByExampleSelective(v2, example2);
		r.setLeft(true);
		return r;

	}

	public Pair<Boolean, String> delPlan(int id, Principal principal) {
		Pair<Boolean, String> r = new Pair<Boolean, String>(false, StringUtils.EMPTY);
		BusOrderDetailV2 v2 = v2Mapper.selectByPrimaryKey(id);
		if (v2 != null) {

			if (!StringUtils.equals(Request.getUserId(principal), v2.getCreater())) {
				r.setRight("未删除成功,记录属主不对!");

			} else {
				v2Mapper.deleteByPrimaryKey(id);
				r.setLeft(true);
			}
		} else {

			r.setRight("记录不存在!");
		}
		return r;

	}

	public Double querySelectPrice(int city, String select) {

		Pair<Boolean, PlanRequest> checkResult = checkPlan(city, select);
		if (!checkResult.getLeft())
			return 0d;
		double basePrice = busservice.getMoneyFromBusModel(JpaBusline.Level.valueOf(checkResult.getRight().level),
				checkResult.getRight().doubleChecker) * 1d;
		basePrice *= checkResult.getRight().days / 30;
		return basePrice;

	}

	@Override
	public Pair<Boolean, String> changeProV2Stats(int proId, String enable) {
		ProductV2 v=productV2Mapper.selectByPrimaryKey(proId);
		if(v!=null){
			v.setStats(JpaProductV2.Status.valueOf(enable).ordinal());
			if(productV2Mapper.updateByPrimaryKey(v)>0){
				return new Pair<Boolean, String>(true,"操作成功");
			}
			return new Pair<Boolean, String>(false,"操作失败");
		}
		return new Pair<Boolean, String>(false,"信息丢失");
	}
	
	public  JpaProductV2 findV2ById(int productId){
		  	return productV2Repository.findOne(productId);
	}

	@Override
	public Pair<Boolean, String> changeProStats(int proId, int enable) {
		Product product=productMapper.selectByPrimaryKey(proId);
		if(product==null){
			return new Pair<Boolean, String>(false,"产品不存在");
		}
		if(enable==1){
			product.setEnabled(true);
		}else{
			product.setEnabled(false);
		}
		int a=productMapper.updateByPrimaryKey(product);
		if(a>0){
			return new Pair<Boolean, String>(true,"操作成功");
		}
		return new Pair<Boolean, String>(false,"操作失败");
	}

	@Override
	public Pair<Boolean, String> checkProHadBought(int productId) {
		OrdersExample example=new OrdersExample();
		example.createCriteria().andProductIdEqualTo(productId);
		if(ordersMapper.selectByExample(example).size()>0){
			return new Pair<Boolean, String>(false,"该产品已有用户下单，不能编辑");
		}
		return new Pair<Boolean, String>(true,"操作成功");
	}
	
	
}
