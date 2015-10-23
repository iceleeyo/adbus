package com.pantuo.service.impl;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
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
import com.pantuo.dao.ProductRepository;
import com.pantuo.dao.pojo.JpaBusOrderDetailV2;
import com.pantuo.dao.pojo.JpaBusline;
import com.pantuo.dao.pojo.JpaCardBoxBody;
import com.pantuo.dao.pojo.JpaCardBoxHelper;
import com.pantuo.dao.pojo.JpaCardBoxMedia;
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
import com.pantuo.service.CardService;
import com.pantuo.util.CardUtil;
import com.pantuo.util.Only1ServieUniqLong;
import com.pantuo.util.Pair;
import com.pantuo.util.Request;
import com.pantuo.web.view.CardView;

@Service
public class CardServiceImpl implements CardService {
	@Autowired
	CardboxMediaMapper cardMapper;
	@Autowired
	CardboxHelperMapper cardboxHelpMapper;

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

	@Override
	public long getCardBingSeriaNum(Principal principal) {
		long result = 0;
		if (principal != null) {
			String uid = Request.getUserId(principal);
			CardboxUserExample example = new CardboxUserExample();
			example.createCriteria().andUserIdEqualTo(uid).andIsPayEqualTo(0);

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
		BooleanExpression query = QJpaCardBoxMedia.jpaCardBoxMedia.seriaNum.eq(seriaNum);
		query = query.and(QJpaCardBoxMedia.jpaCardBoxMedia.isConfirm.eq(isComfirm));

		query = query.and(QJpaCardBoxMedia.jpaCardBoxMedia.isConfirm.eq(isComfirm));
		BooleanExpression query2 = QJpaCardBoxBody.jpaCardBoxBody.seriaNum.eq(seriaNum);
		query2 = query2.and(QJpaCardBoxBody.jpaCardBoxBody.isConfirm.eq(isComfirm));
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

		CardView w = new CardView(page, page2, getBoxPrice(seriaNum, isComfirm, meidLists, boidLists), getBoxTotalnum(
				seriaNum, isComfirm, meidLists, boidLists));
		return w;
	}

	@Override
	public Pair<Double, Integer> saveCard(int proid, int needCount, Principal principal, int city, String type,
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
			return new Pair<Double, Integer>(totalPrice, totalnum);
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

			return new Pair<Double, Integer>(totalPrice, totalnum);
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
				int a=cardMapper.insert(media);
				if(a>0){
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

	public double getBoxPrice(Principal principal) {
		long seriaNum = getCardBingSeriaNum(principal);
		return seriaNum == 0 ? 0 : getBoxPrice(seriaNum, 0, null, null);
	}

	@Override
	public double getBoxPrice(long seriaNum, int iscomfirm, List<Integer> meLists, List<Integer> boLists) {
		double r = 0;
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
			}
		}
		if (bodyList != null && !bodyList.isEmpty()) {
			for (CardboxBody obj : bodyList) {
				r += obj.getTotalprice();
			}
		}
		return r;
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

	@Override
	public Pair<Boolean, String> payment(String paytype, String divid, long seriaNum, Principal principal, int city,
			String meids, String boids) {
		CardboxHelper helper = new CardboxHelper();
		helper.setCity(city);
		helper.setCreated(new Date());
		helper.setFengqi(divid);
		helper.setPayType(JpaOrders.PayType.valueOf(paytype).ordinal());
		helper.setSeriaNum(seriaNum);
		helper.setUserid(Request.getUserId(principal));

		List<Integer> medisIds = CardUtil.parseIdsFromString(meids);
		List<Integer> carid = CardUtil.parseIdsFromString(boids);
		double totalPrice = getBoxPrice(seriaNum, 0, medisIds, carid);
		helper.setTotalMoney(totalPrice);
		Set<Integer> set = new HashSet<Integer>();
		if (medisIds != null) {
			set.addAll(medisIds);
		}
		if (carid != null) {
			set.addAll(carid);
		}
		helper.setIsPay(1);
		helper.setProductCount(set.size());
		if (cardboxHelpMapper.insert(helper) > 0) {
			return new Pair<Boolean, String>(true, "支付成功");
		}
		return new Pair<Boolean, String>(false, "支付失败");
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

	public Page<JpaCardBoxHelper> myCards(int city, Principal principal, TableRequest req) {
		Sort sort = req.getSort("id");
		String orderid = req.getFilter("orderid");
		int page = req.getPage(), pageSize = req.getLength();
		if (page < 0)
			page = 0;
		if (pageSize < 1)
			pageSize = 1;
		BooleanExpression query = QJpaCardBoxHelper.jpaCardBoxHelper.city.eq(city);
		Pageable p = new PageRequest(page, pageSize, sort);
		String u = StringUtils.EMPTY;
		boolean isAdmin = false;
		if (principal != null) {
			u = Request.getUserId(principal);
			if (Request.hasAuth(principal, ActivitiConfiguration.ORDER)) {
				isAdmin = true;
			}
			if (!isAdmin) {
				query = query.and(QJpaCardBoxHelper.jpaCardBoxHelper.userid.eq(u));
			}
		} else {
			query = query.and(QJpaCardBoxHelper.jpaCardBoxHelper.userid.eq(u));
		}
		if (StringUtils.isNoneBlank(orderid)) {
			long seriaNum = NumberUtils.toLong(StringUtils.replace(orderid, "W", StringUtils.EMPTY));
			query = query.and(QJpaCardBoxHelper.jpaCardBoxHelper.seriaNum.eq(seriaNum));
		}

		Page<JpaCardBoxHelper> list = cardHelperRepository.findAll(query, p);
		return list;
	}

	public Page<JpaBusOrderDetailV2> searchProducts(int city, Principal principal, TableRequest req) {

		Map<String, List<String>> map = getQuestObj(req.getFilter("sh"));
		BooleanExpression query = QJpaBusOrderDetailV2.jpaBusOrderDetailV2.city.eq(city);
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

	
}
