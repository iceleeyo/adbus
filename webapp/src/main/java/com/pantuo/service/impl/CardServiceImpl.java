package com.pantuo.service.impl;

import java.security.Principal;

import org.springframework.stereotype.Service;

import com.pantuo.mybatis.domain.CardboxBody;
import com.pantuo.mybatis.domain.CardboxHelper;
import com.pantuo.mybatis.domain.CardboxMedia;
import com.pantuo.service.CardService;
import com.pantuo.util.Pair;

@Service
public class CardServiceImpl implements CardService{

	@Override
	public long getCardBingSeriaNum(Principal principal) {
		return 0;
	}

	@Override
	public boolean checkSeriaNumOwner(long seriaNum) {
		return false;
	}

	@Override
	public Pair<Boolean, String> updateMedia(CardboxMedia media, Principal principal, long seriaNum) {
		return null;
	}

	@Override
	public Pair<Boolean, String> updateBody(CardboxBody media, Principal principal, long seriaNum) {
		return null;
	}

	@Override
	public void add(CardboxHelper helper, Principal principal) {
		
	}

}
