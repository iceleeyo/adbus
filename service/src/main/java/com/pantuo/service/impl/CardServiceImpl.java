package com.pantuo.service.impl;

import java.security.Principal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
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
import com.pantuo.dao.BodyOrderLogRepository;
import com.pantuo.dao.BusOrderDetailV2Repository;
import com.pantuo.dao.CardBoxBodyRepository;
import com.pantuo.dao.CardBoxRepository;
import com.pantuo.dao.CardHelperRepository;
import com.pantuo.dao.OrdersRepository;
import com.pantuo.dao.ProductRepository;
import com.pantuo.dao.UserDetailRepository;
import com.pantuo.dao.Vedio32OrderDetailRepository;
import com.pantuo.dao.Vedio32OrderRepository;
import com.pantuo.dao.Vedio32OrderStatusRepository;
import com.pantuo.dao.VedioGroupRepository;
import com.pantuo.dao.pojo.Jpa32Order;
import com.pantuo.dao.pojo.JpaBusOrderDetailV2;
import com.pantuo.dao.pojo.JpaBusline;
import com.pantuo.dao.pojo.JpaCardBoxBody;
import com.pantuo.dao.pojo.JpaCardBoxHelper;
import com.pantuo.dao.pojo.JpaCardBoxMedia;
import com.pantuo.dao.pojo.JpaCity;
import com.pantuo.dao.pojo.JpaCity.MediaType;
import com.pantuo.dao.pojo.JpaOrders;
import com.pantuo.dao.pojo.JpaOrders.PayType;
import com.pantuo.dao.pojo.JpaProduct;
import com.pantuo.dao.pojo.JpaVideo32Group;
import com.pantuo.dao.pojo.JpaVideo32OrderDetail;
import com.pantuo.dao.pojo.JpaVideo32OrderStatus;
import com.pantuo.dao.pojo.QJpaBusOrderDetailV2;
import com.pantuo.dao.pojo.QJpaCardBoxBody;
import com.pantuo.dao.pojo.QJpaCardBoxHelper;
import com.pantuo.dao.pojo.QJpaCardBoxMedia;
import com.pantuo.dao.pojo.QJpaOrders;
import com.pantuo.dao.pojo.UserDetail;
import com.pantuo.dao.pojo.JpaBusline.Level;
import com.pantuo.mybatis.domain.BodyOrderLog;
import com.pantuo.mybatis.domain.BodyOrderLogExample;
import com.pantuo.mybatis.domain.CardboxBody;
import com.pantuo.mybatis.domain.CardboxBodyExample;
import com.pantuo.mybatis.domain.CardboxHelper;
import com.pantuo.mybatis.domain.CardboxMedia;
import com.pantuo.mybatis.domain.CardboxMediaExample;
import com.pantuo.mybatis.domain.CardboxUser;
import com.pantuo.mybatis.domain.CardboxUserExample;
import com.pantuo.mybatis.domain.Orders;
import com.pantuo.mybatis.persistence.BodyOrderLogMapper;
import com.pantuo.mybatis.persistence.CardboxBodyMapper;
import com.pantuo.mybatis.persistence.CardboxHelperMapper;
import com.pantuo.mybatis.persistence.CardboxMediaMapper;
import com.pantuo.mybatis.persistence.CardboxUserMapper;
import com.pantuo.mybatis.persistence.OrdersMapper;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.ActivitiService;
import com.pantuo.service.CardService;
import com.pantuo.service.CityService;
import com.pantuo.service.ContractService;
import com.pantuo.service.UserServiceInter;
import com.pantuo.service.security.Request;
import com.pantuo.util.BeanUtils;
import com.pantuo.util.CardUtil;
import com.pantuo.util.DateUtil;
import com.pantuo.util.JsonTools;
import com.pantuo.util.Only1ServieUniqLong;
import com.pantuo.util.OrderIdSeq;
import com.pantuo.util.Pair;
import com.pantuo.web.view.BodyProView;
import com.pantuo.web.view.CardBoxHelperView;
import com.pantuo.web.view.CardTotalView;
import com.pantuo.web.view.CardView;
import com.pantuo.web.view.MediaSurvey;
import com.pantuo.web.view.MessageView;
import com.pantuo.web.view.Offlinecontract;
import com.pantuo.web.view.UserQualifiView;

@Service
public class CardServiceImpl implements CardService {
	@Autowired
	CardboxMediaMapper cardMapper;
	@Autowired
	CardboxHelperMapper cardboxHelpMapper;
	@Autowired
	BodyOrderLogMapper bodyOrderLogMapper;
	@Autowired
	ActivitiService activitiService;
	@Autowired
	ContractService contractService;
	@Autowired
	CardboxBodyMapper cardBodyMapper;
	@Autowired
	ProductRepository productRepository;
	@Autowired
	BusOrderDetailV2Repository busOrderDetailV2Repository;

	@Autowired
	CardHelperRepository cardHelperRepository;
	@Autowired
	BodyOrderLogRepository bodyOrderLogRepository;

	@Autowired
	CardboxUserMapper cardboxUserMapper;
	@Autowired
	CardBoxRepository cardBoxRepository;
	@Autowired
	CardBoxBodyRepository cardBoxBodyRepository;
	@Autowired
	OrdersRepository ordersRepository;
	@Autowired
	Vedio32OrderRepository vedio32OrderRepository;
	@Autowired
	Vedio32OrderStatusRepository statusRepository;
	@Autowired
	Vedio32OrderDetailRepository detailRepository;
	@Autowired
	CityService cityService;
	@Autowired
	UserDetailRepository userRepo;
	@Autowired
	UserServiceInter userServiceInter;
	@Autowired
	VedioGroupRepository  groupRep;
	@Autowired
	OrdersMapper orderMapper;

	private static Logger log = LoggerFactory.getLogger(CardServiceImpl.class);

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
		query = query.and(QJpaCardBoxMedia.jpaCardBoxMedia.userId.eq(Request.getUserId(principal)));
		BooleanExpression query2 = QJpaCardBoxBody.jpaCardBoxBody.isConfirm.eq(isComfirm);
		query2 = query2.and(QJpaCardBoxBody.jpaCardBoxBody.userId.eq(Request.getUserId(principal)));
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
	public CardTotalView saveCard(int proid, int needCount, Principal principal, int city, String type, int IsDesign) {
		double totalPrice = 0;
		int totalnum = 0;
		if (StringUtils.equals(type, "media")) {
			long seriaNum = getCardBingSeriaNum(principal);
			CardboxMediaExample example = new CardboxMediaExample();
			example.createCriteria().andSeriaNumEqualTo(seriaNum).andProductIdEqualTo(proid)
					.andUserIdEqualTo(Request.getUserId(principal)).andIsConfirmEqualTo(0);
			List<CardboxMedia> c = cardMapper.selectByExample(example);
			JpaProduct product = productRepository.findOne(proid);
			if (c.isEmpty()) {// 无记录时增加
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
				if (needCount == 0) {// 如果是0时删除
					cardMapper.deleteByExample(example);
				} else {
					existMedia.setNeedCount(needCount);
					existMedia.setTotalprice(product.getPrice() * needCount);
					cardMapper.updateByPrimaryKey(existMedia);
				}
			}

			// totalPrice = getBoxPrice(seriaNum, 0, null);
			// totalnum = getBoxTotalnum(seriaNum, 0, null);
			return getBoxPrice(seriaNum, 0, null, null);
			// return new Pair<Double, Integer>(totalPrice, totalnum);
		} else {
			long seriaNum = getCardBingSeriaNum(principal);
			CardboxBodyExample example = new CardboxBodyExample();
			example.createCriteria().andSeriaNumEqualTo(seriaNum).andProductIdEqualTo(proid)
					.andUserIdEqualTo(Request.getUserId(principal)).andIsConfirmEqualTo(0).andIsDesignEqualTo(IsDesign);
			List<CardboxBody> c = cardBodyMapper.selectByExample(example);
			JpaBusOrderDetailV2 product = busOrderDetailV2Repository.findOne(proid);
			if (c.isEmpty()) {// 无记录时增加
				CardboxBody media = new CardboxBody();
				media.setCity(2);
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
				if (needCount == 0) {// 如果是0时删除
					cardBodyMapper.deleteByExample(example);
				} else {
					existMedia.setNeedCount(needCount);
					existMedia.setTotalprice(existMedia.getDays() / product.getDays() * needCount * product.getPrice());
					cardBodyMapper.updateByPrimaryKey(existMedia);
				}
			}
			return getBoxPrice(seriaNum, 0, null, null);
			// return new Pair<Double, Integer>(totalPrice, totalnum);
		}
	}
	
	/**
	 * 
	 * 分析参数存在
	 *
	 * @param groups
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public List<Integer> getGroupids(String groups) {
		List<Integer> r = getLineIdsBySh(groups);
		if (!r.isEmpty()) {

			List<JpaVideo32Group> page = groupRep.findAll(r);
			r.clear();
			for (JpaVideo32Group jpaVideo32Group : page) {
				r.add(jpaVideo32Group.getId());
			}

		}

		return r;
	}

	public List<Integer> getLineIdsBySh(String sh){
		List<Integer> lineIds = new ArrayList<Integer>();
		if(StringUtils.isNotBlank(sh)){
			String[] shSplit = StringUtils.split(sh, ",");
			if (shSplit != null) {
				for (String string : shSplit) {// p1
					String[] one = StringUtils.split(string, "_");
					lineIds.add(NumberUtils.toInt(one[1]));
				}
			}
		}
		return lineIds;
	}
	@Override
	public Pair<Boolean, String> putIncar(int proid, int needCount, int days, Principal principal, int city,
			String startdate1, String type,HttpServletRequest request) {
		String sh=request.getParameter("sh");
		String isChangeOrder=request.getParameter("isChangeOrder");
		if (StringUtils.equals(type, "media")) {
			long seriaNum = getCardBingSeriaNum(principal);
			List<Integer> groupIds=  getGroupids(sh) ;
			if(groupIds.isEmpty()){
				groupIds.add(-1);
			}
			
			for (Integer groupId : groupIds) {

				CardboxMediaExample example = new CardboxMediaExample();
				CardboxMediaExample.Criteria ca = example.createCriteria();
				ca.andSeriaNumEqualTo(seriaNum).andProductIdEqualTo(proid).andUserIdEqualTo(Request.getUserId(principal)).andIsConfirmEqualTo(0);
				if (groupId > 0) {
					ca.andGroupIdEqualTo(groupId);//add 分组
				}
				List<CardboxMedia> c = cardMapper.selectByExample(example);
				JpaProduct product = productRepository.findOne(proid);
				if (c.isEmpty()) {// 无记录时增加
					CardboxMedia media = new CardboxMedia();
					try {
						if (StringUtils.isNotBlank(startdate1)) {
							Date st = DateUtil.longDf.get().parse(startdate1);
							media.setStartTime(st);
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
					
					if (groupId > 0) {
						media.setGroupId(groupId);
					}
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
					if(StringUtils.isNoneBlank(isChangeOrder))
					media.setIsChangeOrder(BooleanUtils.toBoolean(isChangeOrder));
					cardMapper.insert(media);
				} else {
					CardboxMedia existMedia = c.get(0);
					try {
						if (StringUtils.isNotBlank(startdate1)) {
							Date st = DateUtil.longDf.get().parse(startdate1);
							existMedia.setStartTime(st);
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
					
					if(StringUtils.isNoneBlank(isChangeOrder))
						existMedia.setIsChangeOrder(BooleanUtils.toBoolean(isChangeOrder));
					
					
					if (needCount == 0) {// 如果是0时删除
						cardMapper.deleteByExample(example);
					} else {
						existMedia.setNeedCount(needCount);
						existMedia.setTotalprice(product.getPrice() * needCount);
						cardMapper.updateByPrimaryKey(existMedia);
					}
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
			if (c.isEmpty()) {// 无记录时增加
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
				if (needCount == 0) {// 如果是0时删除
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
	public Pair<Boolean, String> buy(int proid, int needCount, int days, Principal principal, int city,
			String startdate1, String type,HttpServletRequest request) {
		String sh=request.getParameter("sh");
		List<Integer> lineIds=getLineIdsBySh(sh);
		if (StringUtils.equals(type, "media")) {
			long seriaNum = getCardBingSeriaNum(principal);
			CardboxMediaExample example = new CardboxMediaExample();
			example.createCriteria().andSeriaNumEqualTo(seriaNum).andProductIdEqualTo(proid).andIsConfirmEqualTo(0)
					.andUserIdEqualTo(Request.getUserId(principal));
			List<CardboxMedia> c = cardMapper.selectByExample(example);
			JpaProduct product = productRepository.findOne(proid);
			if (c.isEmpty()) {// 无记录时增加
				CardboxMedia media = new CardboxMedia();
				try {
					if (StringUtils.isNotBlank(startdate1)) {
						Date st = DateUtil.longDf.get().parse(startdate1);
						media.setStartTime(st);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
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
				try {
					if (StringUtils.isNotBlank(startdate1)) {
						Date st = DateUtil.longDf.get().parse(startdate1);
						existMedia.setStartTime(st);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
				if (needCount == 0) {// 如果是0时删除
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
				if (c.isEmpty()) {// 无记录时增加
					cardMapper.insert(media);
					r.setLeft(true);
					r.setRight("Addsuccess");
				} else {

					if (media.getNeedCount() == 0 || !isadd) {// 如果是0时删除
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
				if (c.isEmpty()) {// 无记录时增加
					cardBodyMapper.insert(media);
					r.setLeft(true);
					r.setRight("Addsuccess");
				} else {
					if (media.getNeedCount() == 0 || !isadd) {// 如果是0时删除
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

	public CardTotalView getCarSumInfo(Principal principal) {
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
		JpaCity.MediaType mediaType;

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

		public JpaCity.MediaType getMediaType() {
			return mediaType;
		}

		public void setMediaType(JpaCity.MediaType mediaType) {
			this.mediaType = mediaType;
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
				}
				TypeCount v = map.get(cardboxMedia.getCity());
				v.setPrice(v.getPrice() + w);

				v.setMediaType(JpaCity.MediaType.screen);

			}
		}
		if (bodyList != null && !bodyList.isEmpty()) {
			double p = 0.0;
			int l = bodyList.size();
			for (CardboxBody obj : bodyList) {
				double w = obj.getTotalprice();
				p += w;
				if (!map.containsKey(obj.getCity())) {
					map.put(obj.getCity(), new TypeCount(obj.getCity(), l, w));
				}
				TypeCount v = map.get(obj.getCity());
				v.setPrice(p);
				v.setMediaType(JpaCity.MediaType.body);
			}
		}
		return map.values();
	}

	@Override
	public Pair<Boolean, Object> payment(HttpServletRequest request,String startdate1, String paytype, int isdiv, String divid, long seriaNum,
			Principal principal, int city, String meids, String boids, long runningNum) {
		List<Integer> medisIds = CardUtil.parseIdsFromString(meids);
		List<Integer> carid = CardUtil.parseIdsFromString(boids);

		Collection<TypeCount> list = countCardByCity(seriaNum, 0, medisIds, carid);
		double totalMoney = 0.0;
		for (TypeCount typeCount : list) {
			log.info("typeCount:{},{},{},{}",typeCount.getMediaType(),medisIds,boids,carid);
		}
		for (TypeCount typeCount : list) {
			totalMoney += typeCount.getPrice();
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
			helper.setMediaType(typeCount.mediaType.ordinal());
			helper.setCardBodyIds(boids);
			UserDetail user = Request.getUser(principal);
			if (user != null) {
				// 过滤掉不需要存的字段 风险大
				UserDetail u = new UserDetail();
				BeanUtils.copyProperties(user, u);
				u.setGroups(null);
				u.setFunctions(null);
				u.setPassword(StringUtils.EMPTY);
				u.setRoles(null);
				u.setUser(null);
				u.setQulifijsonstr(StringUtils.EMPTY);
				helper.setUserJson(JsonTools.getJsonFromObject(u));
			}
			//	helper.setNewBodySeriaNum(Only1ServieUniqLong.getUniqLongNumber());
			/*
			 * JpaCity _city = cityService.fromId(typeCount.getCity()); if
			 * (_city != null) {
			 * helper.setMediaType(_city.getMediaType().ordinal()); }
			 */
			helper.setNewBodySeriaNum(0L);
			int a = cardboxHelpMapper.insert(helper);
			if (a > 0) {
				int id = helper.getId();
				CardboxHelper r = new CardboxHelper();
				r.setId(id);
				r.setNewBodySeriaNum(Long.parseLong(Only1ServieUniqLong.getUniqByDbId(id)));
				cardboxHelpMapper.updateByPrimaryKeySelective(r);
			}

			if (a > 0 && helper.getMediaType() == 0 && medisIds != null && !medisIds.isEmpty()) {
				change2Order(request,paytype, startdate1, medisIds, helper, runningNum, principal);
			}
			
		}
		MessageView v = new MessageView(totalMoney, runningNum, "创建订单成功", paytype);
		return new Pair<Boolean, Object>(true, v);
		//return new Pair<Boolean, Object>(false, new MessageView(0, 0, "操作异常", paytype));
	}

	public void change2Order(HttpServletRequest request,String paytype,String startdate1, List<Integer> medisIds, CardboxHelper helper, long runningNum, Principal principal) {
		BooleanExpression query = QJpaCardBoxMedia.jpaCardBoxMedia.city.eq(helper.getCity());
		query = query.and(QJpaCardBoxMedia.jpaCardBoxMedia.seriaNum.eq(helper.getSeriaNum()));
		query = query.and(QJpaCardBoxMedia.jpaCardBoxMedia.id.in(medisIds));
		List<JpaCardBoxMedia> mList = (List<JpaCardBoxMedia>) cardBoxRepository.findAll(query);
		
		UserDetail copy = new UserDetail();
		UserDetail source = Request.getUser(principal);
		BeanUtils.copyProperties(source, copy);
		if (source.getUser() != null) {
			copy.setFirstName(source.getUser().getFirstName());
			copy.setEmail(source.getUser().getEmail());
			copy.setUser(null);
			copy.setGroups(null);
			copy.setFunctions(null);
		}
		String customerId = request.getParameter("customerId");
		UserDetail customerObject  =null;
		if(StringUtils.isNoneBlank(customerId)){
			customerObject = userServiceInter.findDetailByUsername(customerId);
		}
		List<JpaCardBoxMedia> c32Media= new ArrayList<JpaCardBoxMedia>();
		double totalPrice=0;
		for (JpaCardBoxMedia jpaCardBoxMedia : mList) {
			
			if(jpaCardBoxMedia.getProduct().getType().equals(JpaProduct.Type.inchof32)){
				c32Media.add(jpaCardBoxMedia);	
				totalPrice+=jpaCardBoxMedia.getPrice();
				continue;
			}
			JpaOrders order = new JpaOrders();
			order.setCity(jpaCardBoxMedia.getCity());
			order.setPrice(jpaCardBoxMedia.getTotalprice());
			order.setCreated(new Date());
			order.setUpdated(new Date());
			order.setUserId(jpaCardBoxMedia.getUserId());
			order.setCreator(jpaCardBoxMedia.getUserId());
			order.setProduct(jpaCardBoxMedia.getProduct());
			if(JpaOrders.PayType.valueOf(paytype)!=JpaOrders.PayType.payContract){
				String code = contractService.getContractId();
				order.setContractCode(code);
			}
			order.setIsChangeOrder(jpaCardBoxMedia.getIsChangeOrder());
			order.setSuppliesId(1);
			order.setPayType(JpaOrders.PayType.valueOf(paytype));
			order.setStats(JpaOrders.Status.unpaid);
			
			//线上runningNum 线下orderid 20160317101313
			order.setRunningNum(runningNum);
			
			if (jpaCardBoxMedia.getStartTime() != null) {
				Date eDate = DateUtil.dateAdd(jpaCardBoxMedia.getStartTime(), jpaCardBoxMedia.getProduct().getDays());
				order.setStartTime(jpaCardBoxMedia.getStartTime());
				order.setEndTime(eDate);
			}
			if (StringUtils.isNotBlank(startdate1)) {
				try {
					Date sDate = DateUtil.longDf.get().parse(startdate1);
					Date eDate = DateUtil.dateAdd(sDate, jpaCardBoxMedia.getProduct().getDays());
					order.setStartTime(sDate);
					order.setEndTime(eDate);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			/*if (helper.getPayType() == JpaOrders.PayType.valueOf("offline").ordinal()) {
				order.setStats(JpaOrders.Status.unpaid);
			} else {
				order.setStats(JpaOrders.Status.paid);
			}*/
			order.setType(jpaCardBoxMedia.getProduct().getType());
			
			order.setOrderUserJson(JsonTools.getJsonFromObject(copy));
			if (customerObject != null) {
				order.setCustomerJson(JsonTools.getJsonFromObject(customerObject));
			}
			ordersRepository.save(order);
			if (order.getId() > 0) {
				if(!PayType.online.name().equals(paytype)){
					Orders orderUpdate =new Orders();
					orderUpdate.setId(order.getId());
					orderUpdate.setRunningNum(OrderIdSeq.getLongOrderId(order));
					orderMapper.updateByPrimaryKeySelective(orderUpdate);
				}
			
				
				activitiService.startProcess2(jpaCardBoxMedia.getCity(), Request.getUser(principal), order,customerObject);
			}
		}
		if(c32Media.size()>0){
			Jpa32Order jpa32Order=new Jpa32Order();
			jpa32Order.setCreated(new Date());
			jpa32Order.setUpdated(new Date());
			jpa32Order.setOrderUserJson(JsonTools.getJsonFromObject(copy));
			if (customerObject != null) {
				jpa32Order.setCustomerJson(JsonTools.getJsonFromObject(customerObject));
			}
			jpa32Order.setPayType(Jpa32Order.PayType.valueOf(paytype));
			jpa32Order.setStats(Jpa32Order.Status.ings);
			jpa32Order.setCreator(Request.getUserId(principal));
			jpa32Order.setRunningNum(runningNum);
			jpa32Order.setPrice(totalPrice);
			Jpa32Order jpa32Order2=vedio32OrderRepository.save(jpa32Order);
			if(jpa32Order2!=null){
				 for (JpaVideo32OrderStatus.Status s : JpaVideo32OrderStatus.Status.values()) {
					 JpaVideo32OrderStatus st=new JpaVideo32OrderStatus();
					 st.setCreated(new Date());
					 st.setUpdated(new Date());
					 st.setOrder(jpa32Order2);
					 st.setStatus(s);
					 if(s.equals(JpaVideo32OrderStatus.Status.paid)){
						 st.setR(JpaVideo32OrderStatus.Result.N);
						 st.setCreater(Request.getUserId(principal));
					 }else{
						 st.setR(JpaVideo32OrderStatus.Result.Z);
					 }
					 statusRepository.save(st);
		            }
				for (JpaCardBoxMedia jpaCardBoxMedia2 : c32Media) {
					JpaVideo32OrderDetail detail=new JpaVideo32OrderDetail();
					detail.setSuppliesId(1);
					detail.setGroup(jpaCardBoxMedia2.getGroup());
					detail.setOrder(jpa32Order2);
					detail.setRunningNum(runningNum);
					if (jpaCardBoxMedia2.getStartTime() != null) {
						Date eDate = DateUtil.dateAdd(jpaCardBoxMedia2.getStartTime(), jpaCardBoxMedia2.getProduct().getDays());
						detail.setStartTime(jpaCardBoxMedia2.getStartTime());
						detail.setEndTime(eDate);
					}
					detail.setStats(JpaVideo32OrderDetail.Status.upload);
					detail.setUserId(Request.getUserId(principal));
					detailRepository.save(detail);
				}
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
			for (String string : shSplit) {// p1
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

	@Override
	public List<BodyOrderLog> getBodyOrderLog(Principal principal, TableRequest req) {
		String helpId = req.getFilter("helpId");
		List<BodyOrderLog> list = new ArrayList<BodyOrderLog>();
		if (StringUtils.isNotBlank(helpId)) {
			int hid = NumberUtils.toInt(helpId);
			BodyOrderLogExample example = new BodyOrderLogExample();
			example.createCriteria().andHelperIdEqualTo(hid);
			example.setOrderByClause("id desc");
			list = bodyOrderLogMapper.selectByExample(example);
		}
		return list;
	}

	public Page<CardBoxHelperView> myCards(int city, Principal principal, TableRequest req) {
		Sort sort = new Sort(Direction.fromString("desc"), "id");// req.getSort("id");
		String orderid = req.getFilter("orderid"), media_type = req.getFilter("media_type");
		String stats = req.getFilter("stats");
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
			if (StringUtils.isNoneBlank(u)) {
				query = QJpaCardBoxHelper.jpaCardBoxHelper.userid.eq(u);
			}
		}
		if (StringUtils.isNoneBlank(orderid)) {
			long seriaNum = NumberUtils.toLong(StringUtils.replace(orderid, "W", StringUtils.EMPTY));
			query = query == null ? QJpaCardBoxHelper.jpaCardBoxHelper.newBodySeriaNum.eq(seriaNum) : query
					.and(QJpaCardBoxHelper.jpaCardBoxHelper.newBodySeriaNum.eq(seriaNum));
		}

		if (StringUtils.isNoneBlank(stats)) {
			query = query == null ? QJpaCardBoxHelper.jpaCardBoxHelper.stats.eq(JpaCardBoxHelper.Stats.valueOf(stats))
					: query.and(QJpaCardBoxHelper.jpaCardBoxHelper.stats.eq(JpaCardBoxHelper.Stats.valueOf(stats)));
		}
		// if (StringUtils.isNoneBlank(media_type) &&
		// !StringUtils.equals("defaultAll", media_type)) {
		// query = query == null ?
		// QJpaCardBoxHelper.jpaCardBoxHelper.mediaType.eq(MediaType.valueOf(media_type))
		// :
		// query.and(QJpaCardBoxHelper.jpaCardBoxHelper.mediaType.eq(MediaType.valueOf(media_type)));
		// }
		if (principal != null && !Request.hasOnlyAuth(principal, ActivitiConfiguration.ADVERTISER)) {
			query = query == null ? QJpaCardBoxHelper.jpaCardBoxHelper.city.eq(city) : query
					.and(QJpaCardBoxHelper.jpaCardBoxHelper.city.eq(city));
		}

		Page<JpaCardBoxHelper> list = cardHelperRepository.findAll(query, p);
		List<CardBoxHelperView> views = new ArrayList<CardBoxHelperView>();
		for (JpaCardBoxHelper jpaCardBoxHelper : list.getContent()) {
			CardBoxHelperView obj = new CardBoxHelperView(jpaCardBoxHelper);
			obj.setMedia_type(jpaCardBoxHelper.getMediaType().ordinal());
			/*
			 * JpaCity _city = cityService.fromId(jpaCardBoxHelper.getCity());
			 * if (_city != null) {
			 * obj.setMedia_type(_city.getMediaType().ordinal()); }
			 */

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
			sort = new Sort(Direction.fromString("desc"), "price");// new
																	// Sort("id");
		Pageable p = new PageRequest(page, length, sort);
		String helpid = req.getFilter("helpid");
		JpaCardBoxHelper help = cardHelperRepository.findOne(NumberUtils.toInt(helpid));

		List<Integer> ids = CardUtil.parseIdsFromString(help.getCardBodyIds());
		BooleanExpression query = QJpaCardBoxBody.jpaCardBoxBody.city.eq(help.getCity());
		if (ids != null && !ids.isEmpty()) {
			// query =
			// query.and(QJpaCardBoxBody.jpaCardBoxBody.seriaNum.eq(help.getSeriaNum()));
			query = query.and(QJpaCardBoxBody.jpaCardBoxBody.isConfirm.eq(1));
			query = query.and(QJpaCardBoxBody.jpaCardBoxBody.id.in(ids));
		} else {
			query = query.and(QJpaCardBoxBody.jpaCardBoxBody.id.eq(-1));
		}
		return query == null ? cardBoxBodyRepository.findAll(p) : cardBoxBodyRepository.findAll(query, p);
	}

	@Override
	public Page<JpaCardBoxMedia> queryCarBoxMedia(int cityId, TableRequest req, int page, int length, Sort sort) {
		if (page < 0)
			page = 0;
		if (length < 1)
			length = 1;
		if (sort == null)
			sort = new Sort(Direction.fromString("desc"), "price");// new
																	// Sort("id");
		Pageable p = new PageRequest(page, length, sort);
		String helpid = req.getFilter("helpid");
		JpaCardBoxHelper help = cardHelperRepository.findOne(NumberUtils.toInt(helpid));
		BooleanExpression query = QJpaCardBoxMedia.jpaCardBoxMedia.city.eq(help.getCity());
		query = query.and(QJpaCardBoxMedia.jpaCardBoxMedia.seriaNum.eq(help.getSeriaNum()));
		query = query.and(QJpaCardBoxMedia.jpaCardBoxMedia.isConfirm.eq(1));
		return query == null ? cardBoxRepository.findAll(p) : cardBoxRepository.findAll(query, p);
	}

	@Override
	public JpaCardBoxHelper queryCarHelperyByid(int id) {
		return cardHelperRepository.findOne(id);
	}

	@Override
	public Pair<Boolean, String> editCarHelper(CardboxHelper helper, String stas, String userId, String remarks) {
		CardboxHelper cardboxHelper = cardboxHelpMapper.selectByPrimaryKey(helper.getId());
		if (cardboxHelper == null) {
			return new Pair<Boolean, String>(false, "信息丢失");
		}
		cardboxHelper.setStats(JpaCardBoxHelper.Stats.valueOf(stas).ordinal());
		cardboxHelper.setRemarks(helper.getRemarks());
		int a = cardboxHelpMapper.updateByPrimaryKey(cardboxHelper);
		if (a > 0) {
			BodyOrderLog log = new BodyOrderLog();
			log.setCreated(new Date());
			log.setHelperId(cardboxHelper.getId());
			log.setUserid(userId);
			log.setStats(JpaCardBoxHelper.Stats.valueOf(stas).getNameStr());
			log.setRemarks(remarks);
			int b = bodyOrderLogMapper.insert(log);
			if (b > 0) {
				return new Pair<Boolean, String>(true, "操作成功");
			} else {
				return new Pair<Boolean, String>(false, "操作记录保存失败");
			}
		} else {
			return new Pair<Boolean, String>(false, "操作失败");
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
	@Override
	public UserQualifiView getUserQualifiViewfromJsonStr(String jsonString) {
		
		UserQualifiView s = null;
		if (StringUtils.isBlank(jsonString)) {
			return s;
		}
		try {
			ObjectMapper t = new ObjectMapper();
			t.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			t.getSerializationConfig().setSerializationInclusion(Inclusion.NON_NULL);
			s = t.readValue(jsonString, UserQualifiView.class);
		} catch (Exception e) {
			log.error("getJsonfromJsonStr,{}", e);
		}
		return s;
	}

	@Override
	public BodyProView getBodyProViewfromJsonStr(String jsonString) {

		BodyProView s = null;
		if (StringUtils.isBlank(jsonString)) {
			return s;
		}
		try {
			ObjectMapper t = new ObjectMapper();
			t.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			t.getSerializationConfig().setSerializationInclusion(Inclusion.NON_NULL);
			s = t.readValue(jsonString, BodyProView.class);
		} catch (Exception e) {
			log.error("getJsonfromJsonStr,{}", e);
		}
		return s;
	}

	@Override
	public Offlinecontract getContractfromJsonStr(String jsonStr) {
		Offlinecontract s = null;
		if (StringUtils.isBlank(jsonStr)) {
			return s;
		}
		try {
			ObjectMapper t = new ObjectMapper();
			t.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			t.getSerializationConfig().setSerializationInclusion(Inclusion.NON_NULL);
			s = t.readValue(jsonStr, Offlinecontract.class);
		} catch (Exception e) {
			log.error("getJsonfromJsonStr,{}", e);
		}
		return s;
	}

	@Override
	public UserQualifiView getUserQualifiView(String qulifijsonstr) {
		UserQualifiView s = null;
		if (StringUtils.isBlank(qulifijsonstr)) {
			return s;
		}
		try {
			ObjectMapper t = new ObjectMapper();
			t.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			t.getSerializationConfig().setSerializationInclusion(Inclusion.NON_NULL);
			s = t.readValue(qulifijsonstr, UserQualifiView.class);
		} catch (Exception e) {
			log.error("getJsonfromJsonStr,{}", e);
		}
		return s;
	}

	@Override
	public boolean updateCardMeida(String start, int mediaId) {
		CardboxMedia cardboxMedia = cardMapper.selectByPrimaryKey(mediaId);
		if (!StringUtils.isNotBlank(start) || cardboxMedia == null) {
			return false;
		}
		try {
			Date sDate = DateUtil.longDf.get().parse(start);
			cardboxMedia.setStartTime(sDate);
			int a = cardMapper.updateByPrimaryKey(cardboxMedia);
			if (a > 0) {
				return true;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean checkPayed(long runningNum, int orderId) {
		boolean r = false;
		if( orderId>0){
			BooleanExpression query = QJpaOrders.jpaOrders.id.eq(orderId);
			JpaOrders orders=ordersRepository.findOne(query);
			if (orders.getStats().equals(JpaOrders.Status.paid)) {
				r = true;
			}
		}else{
		BooleanExpression query = QJpaOrders.jpaOrders.runningNum.eq(runningNum);
		List<JpaOrders> list = (List<JpaOrders>) ordersRepository.findAll(query);
		for (JpaOrders jpaOrders : list) {
			if (jpaOrders.getStats().equals(JpaOrders.Status.paid)) {
				r = true;
				break;
			}
		}
		
		}
		return r;
	}

}
