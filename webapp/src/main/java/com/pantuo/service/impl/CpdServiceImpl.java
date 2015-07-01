package com.pantuo.service.impl;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.mysema.query.types.expr.BooleanExpression;
import com.pantuo.dao.CpdLogRepository;
import com.pantuo.dao.CpdRepository;
import com.pantuo.dao.pojo.JpaCpd;
import com.pantuo.dao.pojo.JpaCpdLog;
import com.pantuo.dao.pojo.JpaProduct.Type;
import com.pantuo.dao.pojo.QJpaCpd;
import com.pantuo.mybatis.persistence.OrdersMapper;
import com.pantuo.service.CpdService;
import com.pantuo.util.Pair;

@Service 
public class CpdServiceImpl implements CpdService {

	@Autowired
	CpdRepository cpdRepository;
	@Autowired
	CpdLogRepository cpdLogRepository;
	OrdersMapper d;

	public void test() {
		
		Pageable p = new PageRequest(0, 20, new Sort("id"));
		BooleanExpression query = QJpaCpd.jpaCpd.id.eq(1);
		Page<JpaCpd> list=	cpdRepository.findAll(query, p);
		System.out.println(list.getContent().size());
		
		  query = QJpaCpd.jpaCpd.product.id.eq(1);
		 list=	cpdRepository.findAll(query, p);
		System.out.println(list.getContent().size());
		
		List<JpaCpd>  tt = cpdRepository.findAll();
		System.out.println(tt);

	}

	@Override
	public Page<JpaCpd> queryCpd(int city, int page, int pageSize, Sort sort, Type productType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pair<Boolean, String> setMyPrice(int cpdid, Principal principal, double myPrice) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<JpaCpdLog> queryCpd(int cpdid, Sort sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JpaCpd queryOneCpdDetail(int cpdid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pair<Boolean, JpaCpd> saveOrUpdateCpd(JpaCpd cpd) {
		// TODO Auto-generated method stub
		return null;
	}
}
