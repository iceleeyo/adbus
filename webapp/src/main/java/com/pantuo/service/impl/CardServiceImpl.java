package com.pantuo.service.impl;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pantuo.mybatis.domain.CardboxBody;
import com.pantuo.mybatis.domain.CardboxBodyExample;
import com.pantuo.mybatis.domain.CardboxHelper;
import com.pantuo.mybatis.domain.CardboxMedia;
import com.pantuo.mybatis.domain.CardboxMediaExample;
import com.pantuo.mybatis.domain.CardboxUser;
import com.pantuo.mybatis.domain.CardboxUserExample;
import com.pantuo.mybatis.persistence.CardboxBodyMapper;
import com.pantuo.mybatis.persistence.CardboxMediaMapper;
import com.pantuo.mybatis.persistence.CardboxUserMapper;
import com.pantuo.service.CardService;
import com.pantuo.util.Only1ServieUniqLong;
import com.pantuo.util.Pair;
import com.pantuo.util.Request;

@Service
public class CardServiceImpl implements CardService {
	@Autowired
	CardboxMediaMapper cardMapper;

	@Autowired
	CardboxBodyMapper cardBodyMapper;

	@Autowired
	CardboxUserMapper cardboxUserMapper;

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
	public boolean checkSeriaNumOwner(long seriaNum，Principal principal) {
		return true;
	}

	@Override
	public Pair<Boolean, String> updateMedia(CardboxMedia media, boolean isadd, Principal principal, long seriaNum) {

		Pair<Boolean, String> r = new Pair<Boolean, String>(false, StringUtils.EMPTY);

		if (principal != null) {
			String uid = Request.getUserId(principal);
			if (checkSeriaNumOwner(seriaNum)) {
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
			if (checkSeriaNumOwner(seriaNum)) {
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

	@Override
	public double getBoxPrice(long seriaNum) {
		double r = 0;
		CardboxMediaExample example = new CardboxMediaExample();
		example.createCriteria().andSeriaNumEqualTo(seriaNum);
		List<CardboxMedia> list = cardMapper.selectByExample(example);
		for (CardboxMedia cardboxMedia : list) {
			r += cardboxMedia.getPrice();
		}
		CardboxBodyExample bodyExample = new CardboxBodyExample();
		bodyExample.createCriteria().andSeriaNumEqualTo(seriaNum);
		List<CardboxBody> bodyList = cardBodyMapper.selectByExample(bodyExample);
		for (CardboxBody obj : bodyList) {
			r += obj.getPrice();
		}
		return r;
	}
}
