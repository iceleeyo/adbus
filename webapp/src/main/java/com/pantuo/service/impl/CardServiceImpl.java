package com.pantuo.service.impl;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.mysema.query.types.expr.BooleanExpression;
import com.pantuo.dao.CardBoxBodyRepository;
import com.pantuo.dao.CardBoxRepository;
import com.pantuo.dao.ProductRepository;
import com.pantuo.dao.pojo.JpaCardBoxBody;
import com.pantuo.dao.pojo.JpaCardBoxMedia;
import com.pantuo.dao.pojo.JpaOrders;
import com.pantuo.dao.pojo.JpaProduct;
import com.pantuo.dao.pojo.QJpaCardBoxBody;
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
import com.pantuo.service.CardService;
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

	public CardView getMediaList(Principal principal, int isComfirm, String ids) {
		List<Integer> idLists = new ArrayList<Integer>();
		if (StringUtils.isNotBlank(ids)) {
			String idsa[] = ids.split(",");
			for (int i = 0; i < idsa.length; i++) {
				if (!idsa[i].trim().equals("")) {
					idLists.add(NumberUtils.toInt(idsa[i]));
				}
			}
		}
		long seriaNum = getCardBingSeriaNum(principal);
		BooleanExpression query = QJpaCardBoxMedia.jpaCardBoxMedia.seriaNum.eq(seriaNum);
		query = query.and(QJpaCardBoxMedia.jpaCardBoxMedia.isConfirm.eq(isComfirm));
		if (!idLists.isEmpty()) {
			query = query.and(QJpaCardBoxMedia.jpaCardBoxMedia.id.in(idLists));
		}
		List<JpaCardBoxMedia> page = cardBoxRepository.findAll(query, new PageRequest(0, 1024, new Sort("id")))
				.getContent();
		BooleanExpression query2 = QJpaCardBoxBody.jpaCardBoxBody.seriaNum.eq(seriaNum);
		query2 = query2.and(QJpaCardBoxBody.jpaCardBoxBody.isConfirm.eq(isComfirm));
		if (!idLists.isEmpty()) {
			query2 = query2.and(QJpaCardBoxBody.jpaCardBoxBody.id.in(idLists));
		}
		List<JpaCardBoxBody> page2 = cardBoxBodyRepository.findAll(query2, new PageRequest(0, 1024, new Sort("id")))
				.getContent();
		CardView w = new CardView(page, page2, getBoxPrice(seriaNum, isComfirm, idLists), getBoxTotalnum(seriaNum,
				isComfirm, idLists));
		return w;
	}

	@Override
	public Pair<Double, Integer> saveCard(int proid, double uprice, int needCount, Principal principal, int city,
			String type) {
		double totalPrice = 0;
		int totalnum = 0;
		if (StringUtils.equals(type, "media")) {
			long seriaNum = getCardBingSeriaNum(principal);
			CardboxMediaExample example = new CardboxMediaExample();
			example.createCriteria().andSeriaNumEqualTo(seriaNum).andProductIdEqualTo(proid)
					.andUserIdEqualTo(Request.getUserId(principal)).andIsConfirmEqualTo(0);
			List<CardboxMedia> c = cardMapper.selectByExample(example);
			if (c.isEmpty()) {//无记录时增加
				CardboxMedia media = new CardboxMedia();
				JpaProduct product = productRepository.findOne(proid);
				media.setCity(city);
				media.setUserId(Request.getUserId(principal));
				media.setCreated(new Date());
				media.setNeedCount(needCount);
				media.setPrice(uprice * needCount);
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
					cardMapper.updateByPrimaryKey(existMedia);
				}
			}
			totalPrice = getBoxPrice(seriaNum, 0, null);
			totalnum = getBoxTotalnum(seriaNum, 0, null);
			return new Pair<Double, Integer>(totalPrice, totalnum);
		}
		return new Pair<Double, Integer>(totalPrice, totalnum);
	}

	private int getBoxTotalnum(long seriaNum, int isconfirm, List<Integer> idLists) {
		int r = 0;
		CardboxMediaExample example = new CardboxMediaExample();
		example.createCriteria().andSeriaNumEqualTo(seriaNum).andIsConfirmEqualTo(isconfirm);
		if (idLists != null && !idLists.isEmpty()) {
			example.createCriteria().andIdIn(idLists);
		}
		List<CardboxMedia> list = cardMapper.selectByExample(example);
		for (CardboxMedia cardboxMedia : list) {
			r += cardboxMedia.getNeedCount();
		}
		CardboxBodyExample bodyExample = new CardboxBodyExample();
		bodyExample.createCriteria().andSeriaNumEqualTo(seriaNum).andIsConfirmEqualTo(isconfirm);
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
		return seriaNum == 0 ? 0 : getBoxPrice(seriaNum, 0, null);
	}

	@Override
	public double getBoxPrice(long seriaNum, int iscomfirm, List<Integer> idLists) {
		double r = 0;
		CardboxMediaExample example = new CardboxMediaExample();
		example.createCriteria().andSeriaNumEqualTo(seriaNum).andIsConfirmEqualTo(iscomfirm);
		if (idLists != null && !idLists.isEmpty()) {
			example.createCriteria().andIdIn(idLists);
		}
		List<CardboxMedia> list = cardMapper.selectByExample(example);
		for (CardboxMedia cardboxMedia : list) {
			r += cardboxMedia.getPrice() * cardboxMedia.getNeedCount();
		}
		CardboxBodyExample bodyExample = new CardboxBodyExample();
		bodyExample.createCriteria().andSeriaNumEqualTo(seriaNum).andIsConfirmEqualTo(iscomfirm);
		List<CardboxBody> bodyList = cardBodyMapper.selectByExample(bodyExample);
		for (CardboxBody obj : bodyList) {
			r += obj.getPrice() * obj.getNeedCount();
		}
		return r;
	}

	@Override
	public Pair<Boolean, String> delOneCarBox(int id) {
		if (cardMapper.deleteByPrimaryKey(id) > 0) {
			return new Pair<Boolean, String>(true, "删除成功");
		}
		return new Pair<Boolean, String>(false, "操作失败");
	}

	@Override
	public void confirmByids(Principal principal, String ids) {
		String idsa[] = ids.split(",");
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

	}

	@Override
	public Pair<Boolean, String> payment(String paytype, String divid, long seriaNum, Principal principal, int city) {
		CardboxHelper helper = new CardboxHelper();
		helper.setCity(city);
		helper.setCreated(new Date());
		helper.setFengqi(divid);
		helper.setPayType(JpaOrders.PayType.valueOf(paytype).ordinal());
		helper.setSeriaNum(seriaNum);
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

}
