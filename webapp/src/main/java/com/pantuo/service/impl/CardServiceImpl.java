package com.pantuo.service.impl;

import java.security.Principal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.pantuo.dao.CardBoxBodyRepository;
import com.pantuo.dao.CardBoxRepository;
import com.pantuo.dao.CardHelperRepository;
import com.pantuo.dao.OrdersRepository;
import com.pantuo.dao.ProductRepository;
import com.pantuo.dao.UserDetailRepository;
import com.pantuo.dao.pojo.JpaBusOrderDetailV2;
import com.pantuo.dao.pojo.JpaBusline;
import com.pantuo.dao.pojo.JpaCardBoxBody;
import com.pantuo.dao.pojo.JpaCardBoxHelper;
import com.pantuo.dao.pojo.JpaCardBoxMedia;
import com.pantuo.dao.pojo.JpaCity;
import com.pantuo.dao.pojo.JpaCity.MediaType;
import com.pantuo.dao.pojo.JpaOrders;
import com.pantuo.dao.pojo.JpaProduct;
import com.pantuo.dao.pojo.QJpaBusOrderDetailV2;
import com.pantuo.dao.pojo.QJpaCardBoxBody;
import com.pantuo.dao.pojo.QJpaCardBoxHelper;
import com.pantuo.dao.pojo.QJpaCardBoxMedia;
import com.pantuo.mybatis.domain.CardboxBody;
import com.pantuo.mybatis.domain.CardboxBodyExample;
import com.pantuo.mybatis.domain.CardboxHelper;
import com.pantuo.mybatis.domain.CardboxMedia;
import com.pantuo.mybatis.domain.CardboxMediaExample;
import com.pantuo.mybatis.domain.CardboxUser;
import com.pantuo.mybatis.domain.CardboxUserExample;
import com.pantuo.mybatis.persistence.CardboxBodyMapper;
import com.pantuo.mybatis.persistence.CardboxHelperMapper;
import com.pantuo.mybatis.persistence.CardboxMediaMapper;
import com.pantuo.mybatis.persistence.CardboxUserMapper;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.ActivitiService;
import com.pantuo.service.CardService;
import com.pantuo.service.CityService;
import com.pantuo.util.CardUtil;
import com.pantuo.util.DateUtil;
import com.pantuo.util.Only1ServieUniqLong;
import com.pantuo.util.Pair;
import com.pantuo.util.Request;
import com.pantuo.web.view.CardBoxHelperView;
import com.pantuo.web.view.CardTotalView;
import com.pantuo.web.view.CardView;
import com.pantuo.web.view.MediaSurvey;

@Service
public class CardServiceImpl implements CardService {
	@Autowired
	CardboxMediaMapper cardMapper;
	@Autowired
	CardboxHelperMapper cardboxHelpMapper;
	@Autowired
	ActivitiService activitiService;
	@Autowired
	CardboxBodyMapper cardBodyMapper;
	@Autowired
	ProductRepository productRepository;
	@Autowired
	BusOrderDetailV2Repository busOrderDetailV2Repository;

	@Autowired
	CardHelperRepository cardHelperRepository;

	@Autowired
	CardboxUserMapper cardboxUserMapper;
	@Autowired
	CardBoxRepository cardBoxRepository;
	@Autowired
	CardBoxBodyRepository cardBoxBodyRepository;
	@Autowired
	OrdersRepository ordersRepository;
	@Autowired
	CityService cityService;
	@Autowired
	 UserDetailRepository userRepo;
	
	
	private static Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
	@Override
	public long getCardBingSeriaNum(Principal principal) {
		long result = 0;
		if (principal != null) {
			String uid = Request.getUserId(principal);
			CardboxUserExample example = new CardboxUserExample();
			example.createCriteria().andUserIdEqualTo(uid);

			List<CardboxUser> r = cardboxUserMapper.selectByExample(example);
			if (r.isEmpty()) {
				long seriaNum = Only1ServieUniqLong.getUniqLongNumber();
				CardboxUser record = new CardboxUser();
				record.setSeriaNum(seriaNum);
				record.setUserId(uid);
				record.setCreated(new Date());
				record.setUpdated(new Date());
				record.setIsPay(0);
				cardboxUserMapper.insert(record);
				result = seriaNum;
			} else {
				result = r.get(0).getSeriaNum();
			}

		}

		return result;
	}

	@Override
	public boolean checkSeriaNumOwner(long seriaNum, Principal principal) {
		if (principal != null) {
			String uid = Request.getUserId(principal);
			CardboxUserExample example = new CardboxUserExample();
			example.createCriteria().andUserIdEqualTo(uid).andSeriaNumEqualTo(seriaNum);
			List<CardboxUser> r = cardboxUserMapper.selectByExample(example);
			return !r.isEmpty();
		}
		return false;
	}

	public CardView getMediaList(Principal principal, int isComfirm, String meids, String boids) {
		List<Integer> meidLists = new ArrayList<Integer>();
		List<Integer> boidLists = new ArrayList<Integer>();
		if (StringUtils.isNotBlank(meids)) {
			String idsa[] = meids.split(",");
			for (int i = 0; i < idsa.length; i++) {
				if (!idsa[i].trim().equals("")) {
					meidLists.add(NumberUtils.toInt(idsa[i]));
				}
			}
		}
		if (StringUtils.isNotBlank(boids)) {
			String idsa[] = boids.split(",");
			for (int i = 0; i < idsa.length; i++) {
				if (!idsa[i].trim().equals("")) {
					boidLists.add(NumberUtils.toInt(idsa[i]));
				}
			}
		}
		long seriaNum = getCardBingSeriaNum(principal);
		BooleanExpression query = QJpaCardBoxMedia.jpaCardBoxMedia.isConfirm.eq(isComfirm);
//		query = query.and(QJpaCardBoxMedia.jpaCardBoxMedia.isConfirm.eq(isComfirm));
		BooleanExpression query2 = QJpaCardBoxBody.jpaCardBoxBody.isConfirm.eq(isComfirm);
//		query2 = query2.and(QJpaCardBoxBody.jpaCardBoxBody.isConfirm.eq(isComfirm));
		List<JpaCardBoxMedia> page = null;
		List<JpaCardBoxBody> page2 = null;
		if (!meidLists.isEmpty() || !boidLists.isEmpty()) {
			if (!meidLists.isEmpty()) {
				query = query.and(QJpaCardBoxMedia.jpaCardBoxMedia.id.in(meidLists));
				page = cardBoxRepository.findAll(query, new PageRequest(0, 1024, new Sort("id"))).getContent();
			} else {
				page = null;
			}
			if (!boidLists.isEmpty()) {
				query2 = query2.and(QJpaCardBoxBody.jpaCardBoxBody.id.in(boidLists));
				page2 = cardBoxBodyRepository.findAll(query2, new PageRequest(0, 1024, new Sort("id"))).getContent();
			} else {
				page2 = null;
			}
		} else {
			page = cardBoxRepository.findAll(query, new PageRequest(0, 1024, new Sort("id"))).getContent();
			page2 = cardBoxBodyRepository.findAll(query2, new PageRequest(0, 1024, new Sort("id"))).getContent();
		}
		CardTotalView totalView = getBoxPrice(seriaNum, isComfirm, meidLists, boidLists);
		CardView w = new CardView(page, page2, totalView.getPrice(), getBoxTotalnum(seriaNum, isComfirm, meidLists,
				boidLists));
		return w;
	}

	@Override
	public CardTotalView saveCard(int proid, int needCount, Principal principal, int city, String type,
			int IsDesign) {
		double totalPrice = 0;
		int totalnum = 0;
		if (StringUtils.equals(type, "media")) {
			long seriaNum = getCardBingSeriaNum(principal);
			CardboxMediaExample example = new CardboxMediaExample();
			example.createCriteria().andSeriaNumEqualTo(seriaNum).andProductIdEqualTo(proid)
					.andUserIdEqualTo(Request.getUserId(principal)).andIsConfirmEqualTo(0);
			List<CardboxMedia> c = cardMapper.selectByExample(example);
			JpaProduct product = productRepository.findOne(proid);
			if (c.isEmpty()) {//无记录时增加
				CardboxMedia media = new CardboxMedia();
				media.setCity(city);
				media.setUserId(Request.getUserId(principal));
				media.setCreated(new Date());
				media.setNeedCount(needCount);
				media.setPrice(product.getPrice());
				media.setTotalprice(product.getPrice() * needCount);
				media.setSeriaNum(seriaNum);
				media.setProductId(proid);
				media.setIsConfirm(0);
				media.setType(product.getType().ordinal());
				cardMapper.insert(media);
			} else {
				CardboxMedia existMedia = c.get(0);
				if (needCount == 0) {//如果是0时删除
					cardMapper.deleteByExample(example);
				} else {
					existMedia.setNeedCount(needCount);
					existMedia.setTotalprice(product.getPrice() * needCount);
					cardMapper.updateByPrimaryKey(existMedia);
				}
			}

			//			totalPrice = getBoxPrice(seriaNum, 0, null);
			//			totalnum = getBoxTotalnum(seriaNum, 0, null);
			return getBoxPrice(seriaNum, 0, null, null);
			//return new Pair<Double, Integer>(totalPrice, totalnum);
		} else {
			long seriaNum = getCardBingSeriaNum(principal);
			CardboxBodyExample example = new CardboxBodyExample();
			example.createCriteria().andSeriaNumEqualTo(seriaNum).andProductIdEqualTo(proid)
					.andUserIdEqualTo(Request.getUserId(principal)).andIsConfirmEqualTo(0).andIsDesignEqualTo(IsDesign);
			List<CardboxBody> c = cardBodyMapper.selectByExample(example);
			JpaBusOrderDetailV2 product = busOrderDetailV2Repository.findOne(proid);
			if (c.isEmpty()) {//无记录时增加
				CardboxBody media = new CardboxBody();
				media.setCity(city);
				media.setUserId(Request.getUserId(principal));
				media.setCreated(new Date());
				media.setNeedCount(needCount);
				media.setPrice(product.getPrice());
				media.setTotalprice(needCount * product.getPrice());
				media.setSeriaNum(seriaNum);
				media.setProductId(proid);
				media.setIsConfirm(0);
				media.setDays(product.getDays());
				media.setIsDesign(IsDesign);
				cardBodyMapper.insert(media);
			} else {
				CardboxBody existMedia = c.get(0);
				if (needCount == 0) {//如果是0时删除
					cardBodyMapper.deleteByExample(example);
				} else {
					existMedia.setNeedCount(needCount);
					existMedia.setTotalprice(existMedia.getDays() / product.getDays() * needCount * product.getPrice());
					cardBodyMapper.updateByPrimaryKey(existMedia);
				}
			}
			  	return getBoxPrice(seriaNum, 0, null, null);
			//return new Pair<Double, Integer>(totalPrice, totalnum);
		}
	}

	@Override
	public Pair<Boolean, String> putIncar(int proid, int needCount, int days, Principal principal, int city, String type) {
		if (StringUtils.equals(type, "media")) {
			long seriaNum = getCardBingSeriaNum(principal);
			CardboxMediaExample example = new CardboxMediaExample();
			example.createCriteria().andSeriaNumEqualTo(seriaNum).andProductIdEqualTo(proid)
					.andUserIdEqualTo(Request.getUserId(principal)).andIsConfirmEqualTo(0);
			List<CardboxMedia> c = cardMapper.selectByExample(example);
			JpaProduct product = productRepository.findOne(proid);
			if (c.isEmpty()) {//无记录时增加
				CardboxMedia media = new CardboxMedia();
				media.setCity(city);
				media.setUserId(Request.getUserId(principal));
				media.setCreated(new Date());
				media.setNeedCount(needCount);
				media.setPrice(product.getPrice());
				media.setTotalprice(product.getPrice() * needCount);
				media.setSeriaNum(seriaNum);
				media.setProductId(proid);
				media.setIsConfirm(0);
				media.setType(product.getType().ordinal());
				cardMapper.insert(media);
			} else {
				CardboxMedia existMedia = c.get(0);
				if (needCount == 0) {//如果是0时删除
					cardMapper.deleteByExample(example);
				} else {
					existMedia.setNeedCount(needCount);
					existMedia.setTotalprice(product.getPrice() * needCount);
					cardMapper.updateByPrimaryKey(existMedia);
				}
			}

			return new Pair<Boolean, String>(true, "加入购物车成功");
		} else {
			long seriaNum = getCardBingSeriaNum(principal);
			CardboxBodyExample example = new CardboxBodyExample();
			example.createCriteria().andSeriaNumEqualTo(seriaNum).andProductIdEqualTo(proid)
					.andUserIdEqualTo(Request.getUserId(principal)).andIsConfirmEqualTo(0).andIsDesignEqualTo(1);
			;
			List<CardboxBody> c = cardBodyMapper.selectByExample(example);
			JpaBusOrderDetailV2 product = busOrderDetailV2Repository.findOne(proid);
			if (c.isEmpty()) {//无记录时增加
				CardboxBody media = new CardboxBody();
				media.setCity(city);
				media.setUserId(Request.getUserId(principal));
				media.setCreated(new Date());
				media.setNeedCount(needCount);
				media.setPrice(product.getPrice());
				media.setTotalprice(days / product.getDays() * needCount * product.getPrice());
				media.setSeriaNum(seriaNum);
				media.setProductId(proid);
				media.setIsConfirm(0);
				media.setDays(days);
				media.setIsDesign(1);
				cardBodyMapper.insert(media);
			} else {
				CardboxBody existMedia = c.get(0);
				if (needCount == 0) {//如果是0时删除
					cardBodyMapper.deleteByExample(example);
				} else {
					existMedia.setNeedCount(needCount);
					existMedia.setDays(days);
					existMedia.setTotalprice(days / product.getDays() * needCount * product.getPrice());
					cardBodyMapper.updateByPrimaryKey(existMedia);
				}
			}

			return new Pair<Boolean, String>(true, "加入购物车成功");
		}

	}

	@Override
	public Pair<Boolean, String> buy(int proid, int needCount, int days, Principal principal, int city, String type) {
		if (StringUtils.equals(type, "media")) {
			long seriaNum = getCardBingSeriaNum(principal);
			CardboxMediaExample example = new CardboxMediaExample();
			example.createCriteria().andSeriaNumEqualTo(seriaNum).andProductIdEqualTo(proid)
					.andUserIdEqualTo(Request.getUserId(principal));
			List<CardboxMedia> c = cardMapper.selectByExample(example);
			JpaProduct product = productRepository.findOne(proid);
			if (c.isEmpty()) {//无记录时增加
				CardboxMedia media = new CardboxMedia();
				media.setCity(city);
				media.setUserId(Request.getUserId(principal));
				media.setCreated(new Date());
				media.setNeedCount(needCount);
				media.setPrice(product.getPrice());
				media.setTotalprice(product.getPrice() * needCount);
				media.setSeriaNum(seriaNum);
				media.setProductId(proid);
				media.setIsConfirm(0);
				media.setType(product.getType().ordinal());
				int a = cardMapper.insert(media);
				if (a > 0) {
					return new Pair<Boolean, String>(true, String.valueOf(media.getId()));
				}
				return new Pair<Boolean, String>(false, "");
			} else {
				CardboxMedia existMedia = c.get(0);
				if (needCount == 0) {//如果是0时删除
					cardMapper.deleteByExample(example);
					return new Pair<Boolean, String>(false, "");
				} else {
					existMedia.setNeedCount(needCount);
					existMedia.setTotalprice(product.getPrice() * needCount);
					cardMapper.updateByPrimaryKey(existMedia);
					return new Pair<Boolean, String>(true, String.valueOf(existMedia.getId()));
				}
			}

		} else {
			long seriaNum = getCardBingSeriaNum(principal);
			CardboxBody media = new CardboxBody();
			JpaBusOrderDetailV2 product = busOrderDetailV2Repository.findOne(proid);
			media.setCity(city);
			media.setUserId(Request.getUserId(principal));
			media.setCreated(new Date());
			media.setNeedCount(needCount);
			media.setPrice(product.getPrice());
			media.setTotalprice(days / product.getDays() * needCount * product.getPrice());
			media.setSeriaNum(seriaNum);
			media.setProductId(proid);
			media.setIsConfirm(0);
			media.setDays(days);
			media.setIsDesign(1);
			int a = cardBodyMapper.insert(media);
			if (a > 0) {
				return new Pair<Boolean, String>(true, String.valueOf(media.getId()));
			}
			return new Pair<Boolean, String>(false, "");
		}

	}

	private int getBoxTotalnum(long seriaNum, int isconfirm, List<Integer> meidLists, List<Integer> boidLists) {
		int r = 0;
		CardboxMediaExample example = new CardboxMediaExample();
		example.createCriteria().andSeriaNumEqualTo(seriaNum).andIsConfirmEqualTo(isconfirm);
		if (meidLists != null && !meidLists.isEmpty()) {
			example.createCriteria().andIdIn(meidLists);
		}
		List<CardboxMedia> list = cardMapper.selectByExample(example);
		for (CardboxMedia cardboxMedia : list) {
			r += cardboxMedia.getNeedCount();
		}
		CardboxBodyExample bodyExample = new CardboxBodyExample();
		bodyExample.createCriteria().andSeriaNumEqualTo(seriaNum).andIsConfirmEqualTo(isconfirm);
		if (boidLists != null && !boidLists.isEmpty()) {
			bodyExample.createCriteria().andIdIn(boidLists);
		}
		List<CardboxBody> bodyList = cardBodyMapper.selectByExample(bodyExample);
		for (CardboxBody obj : bodyList) {
			r += obj.getNeedCount();
		}
		return r;
	}

	@Override
	public Pair<Boolean, String> updateMedia(CardboxMedia media, boolean isadd, Principal principal, long seriaNum) {

		Pair<Boolean, String> r = new Pair<Boolean, String>(false, StringUtils.EMPTY);

		if (principal != null) {
			String uid = Request.getUserId(principal);
			if (checkSeriaNumOwner(seriaNum, principal)) {
				CardboxMediaExample example = new CardboxMediaExample();
				example.createCriteria().andSeriaNumEqualTo(seriaNum).andProductIdEqualTo(media.getProductId())
						.andUserIdEqualTo(uid);
				List<CardboxMedia> c = cardMapper.selectByExample(example);
				if (c.isEmpty()) {//无记录时增加
					cardMapper.insert(media);
					r.setLeft(true);
					r.setRight("Addsuccess");
				} else {

					if (media.getNeedCount() == 0 || !isadd) {//如果是0时删除
						cardMapper.deleteByExample(example);
					} else {
						CardboxMedia existMedia = c.get(0);
						existMedia.setNeedCount(existMedia.getNeedCount());
						cardMapper.updateByPrimaryKey(existMedia);
					}
					r.setLeft(true);
					r.setRight("Updatesuccess");
				}
			}
		}
		return r;
	}

	@Override
	public Pair<Boolean, String> updateBody(CardboxBody media, boolean isadd, Principal principal, long seriaNum) {
		Pair<Boolean, String> r = new Pair<Boolean, String>(false, StringUtils.EMPTY);

		if (principal != null) {
			String uid = Request.getUserId(principal);
			if (checkSeriaNumOwner(seriaNum, principal)) {
				CardboxBodyExample example = new CardboxBodyExample();
				example.createCriteria().andSeriaNumEqualTo(seriaNum).andProductIdEqualTo(media.getProductId())
						.andUserIdEqualTo(uid);
				List<CardboxBody> c = cardBodyMapper.selectByExample(example);
				if (c.isEmpty()) {//无记录时增加
					cardBodyMapper.insert(media);
					r.setLeft(true);
					r.setRight("Addsuccess");
				} else {
					if (media.getNeedCount() == 0 || !isadd) {//如果是0时删除
						cardBodyMapper.deleteByExample(example);
					} else {
						CardboxBody existBody = c.get(0);
						existBody.setNeedCount(existBody.getNeedCount());
						cardBodyMapper.updateByPrimaryKey(existBody);
					}
					r.setLeft(true);
					r.setRight("Updatesuccess");
				}
			}
		}
		return r;
	}

	@Override
	public void add(CardboxHelper helper, Principal principal) {

	}
	public CardTotalView getCarSumInfo( Principal principal) {
		long seriaNum = getCardBingSeriaNum(principal);
		return getBoxPrice(seriaNum, 0, null, null);
	}
	public double getBoxPrice(Principal principal) {
		long seriaNum = getCardBingSeriaNum(principal);
		return seriaNum == 0 ? 0 : getBoxPrice(seriaNum, 0, null, null).price;
	}

	@Override
	public CardTotalView getBoxPrice(long seriaNum, int iscomfirm, List<Integer> meLists, List<Integer> boLists) {
		double r = 0;
		int needCount = 0, cardCount = 0;
		CardboxMediaExample example = new CardboxMediaExample();
		CardboxMediaExample.Criteria criteria1 = example.createCriteria();
		criteria1.andSeriaNumEqualTo(seriaNum).andIsConfirmEqualTo(iscomfirm);
		CardboxBodyExample bodyExample = new CardboxBodyExample();
		CardboxBodyExample.Criteria criteria2 = bodyExample.createCriteria();
		criteria2.andSeriaNumEqualTo(seriaNum).andIsConfirmEqualTo(iscomfirm);
		List<CardboxMedia> list = null;
		List<CardboxBody> bodyList = null;
		if ((meLists != null && !meLists.isEmpty()) || (boLists != null && !boLists.isEmpty())) {
			if (meLists != null && !meLists.isEmpty()) {
				criteria1.andIdIn(meLists);
				list = cardMapper.selectByExample(example);
			} else {
				list = null;
			}
			if (boLists != null && !boLists.isEmpty()) {
				criteria2.andIdIn(boLists);
				bodyList = cardBodyMapper.selectByExample(bodyExample);
			} else {
				bodyList = null;
			}
		} else {
			list = cardMapper.selectByExample(example);
			bodyList = cardBodyMapper.selectByExample(bodyExample);
		}
		if (list != null && !list.isEmpty()) {
			for (CardboxMedia cardboxMedia : list) {
				r += cardboxMedia.getPrice() * cardboxMedia.getNeedCount();
				needCount += cardboxMedia.getNeedCount();
				cardCount++;
			}
		}
		if (bodyList != null && !bodyList.isEmpty()) {
			for (CardboxBody obj : bodyList) {
				r += obj.getTotalprice();
				needCount += obj.getNeedCount();
				cardCount++;
			}
		}
		return new CardTotalView(seriaNum, r, cardCount, needCount);
	}

	@Override
	public Pair<Boolean, String> delOneCarBox(String type, int id) {
		if (StringUtils.equals(type, "media")) {
			if (cardMapper.deleteByPrimaryKey(id) > 0) {
				return new Pair<Boolean, String>(true, "删除成功");
			}
		} else {
			if (cardBodyMapper.deleteByPrimaryKey(id) > 0) {
				return new Pair<Boolean, String>(true, "删除成功");
			}
		}
		return new Pair<Boolean, String>(false, "操作失败");
	}

	@Override
	public void confirmByids(Principal principal, String meids, String boids) {
		String idsa[] = meids.split(",");
		String idsa2[] = boids.split(",");
		for (int i = 0; i < idsa.length; i++) {
			if (!idsa[i].trim().equals("")) {
				CardboxMedia cardboxMedia = cardMapper.selectByPrimaryKey(NumberUtils.toInt(idsa[i]));
				if (cardboxMedia != null) {
					if (cardboxMedia.getNeedCount() > 0) {
						cardboxMedia.setIsConfirm(1);
						cardMapper.updateByPrimaryKey(cardboxMedia);
					}
				}
			}
		}
		for (int i = 0; i < idsa2.length; i++) {
			if (!idsa2[i].trim().equals("")) {
				CardboxBody cardboxMedia = cardBodyMapper.selectByPrimaryKey(NumberUtils.toInt(idsa2[i]));
				if (cardboxMedia != null) {
					if (cardboxMedia.getNeedCount() > 0) {
						cardboxMedia.setIsConfirm(1);
						cardBodyMapper.updateByPrimaryKey(cardboxMedia);
					}
				}
			}
		}

	}

	static class TypeCount {
		int city;
		int productCount;
		double price;

		public int getProductCount() {
			return productCount;
		}

		public void setProductCount(int productCount) {
			this.productCount = productCount;
		}

		public double getPrice() {
			return price;
		}

		public void setPrice(double price) {
			this.price = price;
		}

		public int getCity() {
			return city;
		}

		public void setCity(int city) {
			this.city = city;
		}

		public TypeCount(int city, int productCount, double price) {
			super();
			this.city = city;
			this.productCount = productCount;
			this.price = price;
		}

	}

	public Collection<TypeCount> countCardByCity(long seriaNum, int iscomfirm, List<Integer> meLists,
			List<Integer> boLists) {

		Map<Integer, TypeCount> map = new HashMap<Integer, CardServiceImpl.TypeCount>();
		CardboxMediaExample example = new CardboxMediaExample();
		CardboxMediaExample.Criteria criteria1 = example.createCriteria();
		criteria1.andSeriaNumEqualTo(seriaNum).andIsConfirmEqualTo(iscomfirm);
		CardboxBodyExample bodyExample = new CardboxBodyExample();
		CardboxBodyExample.Criteria criteria2 = bodyExample.createCriteria();
		criteria2.andSeriaNumEqualTo(seriaNum).andIsConfirmEqualTo(iscomfirm);
		List<CardboxMedia> list = null;
		List<CardboxBody> bodyList = null;
		if ((meLists != null && !meLists.isEmpty()) || (boLists != null && !boLists.isEmpty())) {
			if (meLists != null && !meLists.isEmpty()) {
				criteria1.andIdIn(meLists);
				list = cardMapper.selectByExample(example);
			} else {
				list = null;
			}
			if (boLists != null && !boLists.isEmpty()) {
				criteria2.andIdIn(boLists);
				bodyList = cardBodyMapper.selectByExample(bodyExample);
			} else {
				bodyList = null;
			}
		} else {
			list = cardMapper.selectByExample(example);
			bodyList = cardBodyMapper.selectByExample(bodyExample);
		}
		if (list != null && !list.isEmpty()) {
			int m = list.size();
			for (CardboxMedia cardboxMedia : list) {
				double w = cardboxMedia.getPrice() * cardboxMedia.getNeedCount();
				if (!map.containsKey(cardboxMedia.getCity())) {
					map.put(cardboxMedia.getCity(), new TypeCount(cardboxMedia.getCity(), m, w));
				} else {
					TypeCount v = map.get(cardboxMedia.getCity());
					v.setPrice(v.getPrice() + w);
				}

			}
		}
		if (bodyList != null && !bodyList.isEmpty()) {
			int l = bodyList.size();
			for (CardboxBody obj : bodyList) {
				double w = obj.getTotalprice();
				if (!map.containsKey(obj.getCity())) {
					map.put(obj.getCity(), new TypeCount(obj.getCity(), l, w));
				} else {
					TypeCount v = map.get(obj.getCity());
					v.setPrice(v.getPrice() + w);
				}
			}
		}
		return map.values();
	}

	@Override
	public Pair<Boolean, String> payment(String startdate1,String paytype,int isdiv, String divid, long seriaNum, Principal principal, int city,
			String meids, String boids) {
		List<Integer> medisIds = CardUtil.parseIdsFromString(meids);
		List<Integer> carid = CardUtil.parseIdsFromString(boids);

		Collection<TypeCount> list = countCardByCity(seriaNum, 0, medisIds, carid);

		for (TypeCount typeCount : list) {
			CardboxHelper helper = new CardboxHelper();
			helper.setCity(typeCount.getCity());
			helper.setCreated(new Date());
			helper.setFengqi(divid);
			helper.setPayType(JpaOrders.PayType.valueOf(paytype).ordinal());
			helper.setSeriaNum(seriaNum);
			helper.setIsdivid(isdiv);
			helper.setUserid(Request.getUserId(principal));
			helper.setTotalMoney(typeCount.getPrice());
			helper.setProductCount(typeCount.getProductCount());
			helper.setIsPay(1);
			helper.setStats(JpaCardBoxHelper.Stats.init.ordinal());
			JpaCity _city = cityService.fromId(typeCount.getCity());
			if (_city != null) {
				helper.setMediaType(_city.getMediaType().ordinal());
			}

			int a=cardboxHelpMapper.insert(helper);
			if(a>0 && helper.getMediaType()==0){
				change2Order(startdate1,medisIds,helper,principal);
			}
		}
		return new Pair<Boolean, String>(true, "支付成功");
	}

	public void change2Order(String startdate1,List<Integer> medisIds,CardboxHelper helper,Principal principal) {
		BooleanExpression query = QJpaCardBoxMedia.jpaCardBoxMedia.city.eq(helper.getCity());
		query=query.and(QJpaCardBoxMedia.jpaCardBoxMedia.seriaNum.eq(helper.getSeriaNum()));
		query=query.and(QJpaCardBoxMedia.jpaCardBoxMedia.id.in(medisIds));
		List<JpaCardBoxMedia> mList=(List<JpaCardBoxMedia>) cardBoxRepository.findAll(query);
		for (JpaCardBoxMedia jpaCardBoxMedia : mList) {
			JpaOrders order=new JpaOrders();
			order.setCity(jpaCardBoxMedia.getCity());
			order.setPrice(jpaCardBoxMedia.getTotalprice());
			order.setCreated(new Date());
			order.setUpdated(new Date());
			order.setUserId(jpaCardBoxMedia.getUserId());
			order.setCreator(jpaCardBoxMedia.getUserId());
			order.setProduct(jpaCardBoxMedia.getProduct());
			order.setSuppliesId(1);
			if(StringUtils.isNotBlank(startdate1)){
				try {
					Date sDate=DateUtil.longDf.get().parse(startdate1);
					Date eDate=DateUtil.dateAdd(sDate, jpaCardBoxMedia.getProduct().getDays());
					order.setStartTime(sDate);
					order.setEndTime(eDate);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			if(helper.getPayType()==JpaOrders.PayType.valueOf("offline").ordinal()){
				order.setStats(JpaOrders.Status.unpaid);
			}else{
				order.setStats(JpaOrders.Status.paid);
			}
			order.setType(jpaCardBoxMedia.getProduct().getType());
			ordersRepository.save(order);
			if(order.getId()>0){
				activitiService.startProcess2(jpaCardBoxMedia.getCity(),  Request.getUser(principal), order);
			}
		}
		
	}

	@Override
	public void updateCardboxUser(long seriaNum, Principal principal) {
		CardboxUserExample example = new CardboxUserExample();
		example.createCriteria().andUserIdEqualTo(Request.getUserId(principal)).andSeriaNumEqualTo(seriaNum)
				.andIsPayEqualTo(0);
		List<CardboxUser> list = cardboxUserMapper.selectByExample(example);
		if (list.size() > 0) {
			CardboxUser user = list.get(0);
			user.setIsPay(1);
			cardboxUserMapper.updateByPrimaryKey(user);
		}

	}

	public Map<String, List<String>> getQuestObj(String requestString) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		String[] shSplit = StringUtils.split(requestString, ",");
		if (shSplit != null) {
			for (String string : shSplit) {//p1
				String[] one = StringUtils.split(string, "_", 2);
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
		return map;
	}

	public Page<CardBoxHelperView> myCards(int city, Principal principal, TableRequest req) {
		Sort sort = new Sort(Direction.fromString("desc"), "id");//req.getSort("id");
		String orderid = req.getFilter("orderid"), media_type = req.getFilter("media_type");
		;
		int page = req.getPage(), pageSize = req.getLength();
		if (page < 0)
			page = 0;
		if (pageSize < 1)
			pageSize = 1;
		BooleanExpression query = QJpaCardBoxHelper.jpaCardBoxHelper.mediaType.eq(MediaType.body);
		Pageable p = new PageRequest(page, pageSize, sort);
		String u = StringUtils.EMPTY;
		boolean isAdmin = false;
		if (principal != null) {
			u = Request.getUserId(principal);
			if (!Request.hasOnlyAuth(principal, ActivitiConfiguration.ADVERTISER)) {
				isAdmin = true;
			}
			if (!isAdmin) {
				query = query == null ? QJpaCardBoxHelper.jpaCardBoxHelper.userid.eq(u) : query
						.and(QJpaCardBoxHelper.jpaCardBoxHelper.userid.eq(u));
			}
		} else {
			query = QJpaCardBoxHelper.jpaCardBoxHelper.userid.eq(u);
		}
		if (StringUtils.isNoneBlank(orderid)) {
			long seriaNum = NumberUtils.toLong(StringUtils.replace(orderid, "W", StringUtils.EMPTY));
			query = query == null ? QJpaCardBoxHelper.jpaCardBoxHelper.seriaNum.eq(seriaNum) : query
					.and(QJpaCardBoxHelper.jpaCardBoxHelper.seriaNum.eq(seriaNum));
		}

//		if (StringUtils.isNoneBlank(media_type) && !StringUtils.equals("defaultAll", media_type)) {
//			query = query == null ? QJpaCardBoxHelper.jpaCardBoxHelper.mediaType.eq(MediaType.valueOf(media_type))
//					: query.and(QJpaCardBoxHelper.jpaCardBoxHelper.mediaType.eq(MediaType.valueOf(media_type)));
//		}
		if (principal != null && !Request.hasOnlyAuth(principal, ActivitiConfiguration.ADVERTISER)) {
			query = query == null ? QJpaCardBoxHelper.jpaCardBoxHelper.city.eq(city) : query
					.and(QJpaCardBoxHelper.jpaCardBoxHelper.city.eq(city));
		}

		Page<JpaCardBoxHelper> list = cardHelperRepository.findAll(query, p);
		List<CardBoxHelperView> views = new ArrayList<CardBoxHelperView>();
		for (JpaCardBoxHelper jpaCardBoxHelper : list.getContent()) {
			CardBoxHelperView obj = new CardBoxHelperView(jpaCardBoxHelper);

			JpaCity _city = cityService.fromId(jpaCardBoxHelper.getCity());
			if (_city != null) {
				obj.setMedia_type(_city.getMediaType().ordinal());
			}

			views.add(obj);
		}
		org.springframework.data.domain.PageImpl<CardBoxHelperView> result = new org.springframework.data.domain.PageImpl<CardBoxHelperView>(
				views, p, list.getTotalElements());
		return result;
	}

	public Page<JpaBusOrderDetailV2> searchProducts(int city, Principal principal, TableRequest req) {

		Map<String, List<String>> map = getQuestObj(req.getFilter("sh"));
		BooleanExpression query = QJpaBusOrderDetailV2.jpaBusOrderDetailV2.JpaProductV2.id.isNotNull();
		for (Map.Entry<String, List<String>> entry : map.entrySet()) {
			List<String> values = entry.getValue();
			if (StringUtils.equals(entry.getKey(), "B") && values.size() > 0) {
				BooleanExpression subQuery = null;
				for (String address : values) {
					if (StringUtils.isNoneBlank(address)) {
						BooleanExpression addressEx = QJpaBusOrderDetailV2.jpaBusOrderDetailV2.JpaProductV2.addressList
								.like("%" + StringUtils.trim(address) + "%");
						subQuery = subQuery == null ? addressEx : subQuery.or(addressEx);
					}
				}
				query = query.and(subQuery);
			} else if (StringUtils.equals(entry.getKey(), "C") && values.size() > 0) {
				BooleanExpression subQuery = null;
				for (String address : values) {
					if (StringUtils.isNoneBlank(address)) {
						BooleanExpression smallAdressEx = QJpaBusOrderDetailV2.jpaBusOrderDetailV2.JpaProductV2.smallAdressList
								.like("%" + StringUtils.trim(address) + "%");
						subQuery = subQuery == null ? smallAdressEx : subQuery.or(smallAdressEx);
					}
				}
				query = query.and(subQuery);
			} else if (StringUtils.equals(entry.getKey(), "D") && values.size() > 0) {
				BooleanExpression subQuery = null;
				for (String catType : values) {
					if (StringUtils.isNoneBlank(catType)) {
						String[] s = StringUtils.split(catType, "_ ");
						if (s.length == 2) {
							boolean isDoubleChecker = StringUtils.equals(s[0], "1");
							BooleanExpression smallAdressEx = QJpaBusOrderDetailV2.jpaBusOrderDetailV2.doubleDecker
									.eq(isDoubleChecker);
							if (!StringUtils.equals(s[1], "0")) {
								JpaBusline.Level level = JpaBusline.Level.valueOf(s[1]);
								smallAdressEx = smallAdressEx.and(QJpaBusOrderDetailV2.jpaBusOrderDetailV2.leval
										.eq(level));
							}
							subQuery = subQuery == null ? smallAdressEx : subQuery.or(smallAdressEx);
						}
					}
				}
				query = query.and(subQuery);
			}

		}
		Sort sort = new Sort("id");
		if (StringUtils.equals(req.getFilter("p"), "1")) {
			sort = new Sort(Direction.fromString("desc"), "price");
		} else {
			sort = new Sort(Direction.fromString("asc"), "price");
		}
		Double t1 = NumberUtils.toDouble(req.getFilter("price1"));
		if (t1 > 0) {
			query = query.and(QJpaBusOrderDetailV2.jpaBusOrderDetailV2.JpaProductV2.price.goe(t1));
		}
		double t2 = NumberUtils.toDouble(req.getFilter("price2"));
		if (t2 > 0) {
			query = query.and(QJpaBusOrderDetailV2.jpaBusOrderDetailV2.JpaProductV2.price.loe(t2));
		}

		int page = req.getPage(), pageSize = req.getLength();

		if (page < 0)
			page = 0;
		if (pageSize < 1)
			pageSize = 1;
		sort = (sort == null ? new Sort("id") : sort);
		Pageable p = new PageRequest(page, pageSize, sort);
		Page<JpaBusOrderDetailV2> list = busOrderDetailV2Repository.findAll(query, p);
		return list;

	}

	public void test() {
		Sort sort = new Sort("id");
		Pageable p = new PageRequest(0, 30, sort);
		BooleanExpression query = QJpaBusOrderDetailV2.jpaBusOrderDetailV2.city.eq(2);
		BooleanExpression subQuery2 = null;
		BooleanExpression subQuery1 = null;
		subQuery2 = QJpaBusOrderDetailV2.jpaBusOrderDetailV2.doubleDecker.eq(true);

		subQuery1 = QJpaBusOrderDetailV2.jpaBusOrderDetailV2.JpaProductV2.addressList.like("%111%");
		subQuery1 = subQuery1.or(QJpaBusOrderDetailV2.jpaBusOrderDetailV2.JpaProductV2.addressList.like("%222%"));
		BooleanExpression t = subQuery2.and(subQuery1);
		BooleanExpression commonEx = query.and(t);
		Page<JpaBusOrderDetailV2> list = busOrderDetailV2Repository.findAll(commonEx, p);
		System.out.println(list.getContent().size());

	}

	@Override
	public JpaBusOrderDetailV2 getJpaBusOrderDetailV2Byid(int id) {
		return busOrderDetailV2Repository.findOne(id);
	}

	@Override
	public JpaProduct getJpaProductByid(int id) {
		return productRepository.findOne(id);
	}

	@Override
	public Page<JpaCardBoxBody> queryCarBoxBody(int cityId, TableRequest req, int page, int length, Sort sort) {
		if (page < 0)
			page = 0;
		if (length < 1)
			length = 1;
		if (sort == null)
			sort = new Sort(Direction.fromString("desc"), "price");//new Sort("id");
		Pageable p = new PageRequest(page, length, sort);
		String helpid = req.getFilter("helpid");
		JpaCardBoxHelper help = cardHelperRepository.findOne(NumberUtils.toInt(helpid));
		BooleanExpression query = QJpaCardBoxBody.jpaCardBoxBody.city.eq(help.getCity());
		query = query.and(QJpaCardBoxBody.jpaCardBoxBody.seriaNum.eq(help.getSeriaNum()));
		query=query.and(QJpaCardBoxBody.jpaCardBoxBody.isConfirm.eq(1));
		return query == null ? cardBoxBodyRepository.findAll(p) : cardBoxBodyRepository.findAll(query, p);
	}

	@Override
	public Page<JpaCardBoxMedia> queryCarBoxMedia(int cityId, TableRequest req, int page, int length, Sort sort) {
		if (page < 0)
			page = 0;
		if (length < 1)
			length = 1;
		if (sort == null)
			sort = new Sort(Direction.fromString("desc"), "price");//new Sort("id");
		Pageable p = new PageRequest(page, length, sort);
		String helpid = req.getFilter("helpid");
		JpaCardBoxHelper help=cardHelperRepository.findOne(NumberUtils.toInt(helpid));
		BooleanExpression query = QJpaCardBoxMedia.jpaCardBoxMedia.city.eq(help.getCity());
		query=query.and(QJpaCardBoxMedia.jpaCardBoxMedia.seriaNum.eq(help.getSeriaNum()));
		query=query.and(QJpaCardBoxMedia.jpaCardBoxMedia.isConfirm.eq(1));
		return query == null ? cardBoxRepository.findAll(p) : cardBoxRepository.findAll(query, p);
	}

	@Override
	public JpaCardBoxHelper queryCarHelperyByid(int id) {
		return cardHelperRepository.findOne(id);
	}

	@Override
	public Pair<Boolean, String> editCarHelper(CardboxHelper helper,String stas) {
		CardboxHelper cardboxHelper=cardboxHelpMapper.selectByPrimaryKey(helper.getId());
		if(cardboxHelper==null){
			return new Pair<Boolean, String>(false,"信息丢失");
		}
		cardboxHelper.setStats(JpaCardBoxHelper.Stats.valueOf(stas).ordinal());
		cardboxHelper.setRemarks(helper.getRemarks());
		int a=cardboxHelpMapper.updateByPrimaryKey(cardboxHelper);
		if(a>0){
			return new Pair<Boolean, String>(true,"操作成功");
		}else{
			return new Pair<Boolean, String>(false,"操作失败");
		}
	}

	@Override
	public MediaSurvey getJsonfromJsonStr(String jsonString) {
	
		MediaSurvey s = null;
		if (StringUtils.isBlank(jsonString)) {
			return s;
		}
		try {
			ObjectMapper t = new ObjectMapper();
			t.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			t.getSerializationConfig().setSerializationInclusion(Inclusion.NON_NULL);
			s = t.readValue(jsonString, MediaSurvey.class);
		} catch (Exception e) {
			log.error("getJsonfromJsonStr,{}", e);
		}
		return s;
	}
	
}
