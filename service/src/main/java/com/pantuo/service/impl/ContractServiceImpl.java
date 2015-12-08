package com.pantuo.service.impl;

import java.security.Principal;

import com.mysema.query.types.expr.BooleanExpression;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.mysema.query.types.Predicate;
import com.pantuo.ActivitiConfiguration;
import com.pantuo.dao.BlackAdRepository;
import com.pantuo.dao.ContractRepository;
import com.pantuo.dao.ProductRepository;
import com.pantuo.dao.pojo.JpaBlackAd;
import com.pantuo.dao.pojo.JpaContract;
import com.pantuo.dao.pojo.QJpaBlackAd;
import com.pantuo.dao.pojo.QJpaContract;
import com.pantuo.dao.pojo.QJpaProduct;
import com.pantuo.dao.pojo.QJpaSupplies;
import com.pantuo.mybatis.persistence.ContractMapper;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.ContractServiceData;
import com.pantuo.util.Request;

@Service
public class ContractServiceImpl implements ContractServiceData {
	@Autowired
	ContractMapper contractMapper;

	@Autowired
	ContractRepository contractRepo;
	@Autowired
	BlackAdRepository  blackAdRepository;

	public Page<JpaContract> getAllContracts(int city, TableRequest req, Principal principal) {

		String name = req.getFilter("contractName"), code = req.getFilter("contractCode");
		int page = req.getPage(), pageSize = req.getLength();
		Sort sort = req.getSort("id");

		if (page < 0)
			page = 0;
		if (pageSize < 1)
			pageSize = 1;
		sort = (sort == null ? new Sort("id") : sort);
		Pageable p = new PageRequest(page, pageSize, sort);
		BooleanExpression query = QJpaContract.jpaContract.city.eq(city);
		if (!(StringUtils.isBlank(name) && StringUtils.isBlank(code))) {
			if (StringUtils.isNotBlank(name)) {
				query = query.and(QJpaContract.jpaContract.contractName.like("%" + name + "%"));
			}
			if (StringUtils.isNoneBlank(code)) {
				BooleanExpression q = QJpaContract.jpaContract.contractCode.like("%" + code + "%");
				if (query == null)
					query = q;
				else
					query = query.and(q);
			}
		}
		if (Request.hasAuth(principal, ActivitiConfiguration.ORDER)) {
		} else if (Request.hasAuth(principal, ActivitiConfiguration.ADVERTISER)) {
			query = query.and(QJpaContract.jpaContract.userId.eq(Request.getUserId(principal)));
			query = query.or(QJpaContract.jpaContract.creator.eq(Request.getUserId(principal)));
		} else {
			query = query.and(QJpaContract.jpaContract.creator.eq(Request.getUserId(principal)));
		}
		return contractRepo.findAll(query, p);
	}

	public Page<JpaContract> getValidContracts(int city, int page, int pageSize, Sort sort) {
		if (page < 0)
			page = 0;
		if (pageSize < 1)
			pageSize = 1;
		sort = (sort == null ? new Sort(Sort.Direction.DESC, "id") : sort);
		Pageable p = new PageRequest(page, pageSize, sort);
		Predicate query = QJpaContract.jpaContract.city.eq(city);
		return contractRepo.findAll(query, p);
	}

	@Override
	public Page<JpaBlackAd> getAllblackAd(int city, TableRequest req, Principal principal) {
		String name = req.getFilter("adName"), code = req.getFilter("seqNumber");
		int page = req.getPage(), pageSize = req.getLength();
		Sort sort = req.getSort("id");
		if (page < 0)
			page = 0;
		if (pageSize < 1)
			pageSize = 1;
		sort = (sort == null ? new Sort("id") : sort);
		Pageable p = new PageRequest(page, pageSize, sort);
		BooleanExpression query = QJpaBlackAd.jpaBlackAd.main_type.eq(JpaBlackAd.Main_type.online);
		if (!(StringUtils.isBlank(name) && StringUtils.isBlank(code))) {
			if (StringUtils.isNotBlank(name)) {
				query = query.and(QJpaBlackAd.jpaBlackAd.adName.like("%" + name + "%"));
			}
			if (StringUtils.isNoneBlank(code)) {
				query = query.and(QJpaBlackAd.jpaBlackAd.seqNumber.like("%" + code + "%"));
			}
		}
		return blackAdRepository.findAll(query, p);
	}

	@Override
	public Page<JpaBlackAd> getValidblackAd(int city, int page, int pageSize, Sort sort) {
		if (page < 0)
			page = 0;
		if (pageSize < 1)
			pageSize = 1;
		sort = (sort == null ? new Sort(Sort.Direction.DESC, "id") : sort);
		Pageable p = new PageRequest(page, pageSize, sort);
		Predicate query = QJpaBlackAd.jpaBlackAd.main_type.eq(JpaBlackAd.Main_type.online);
		return blackAdRepository.findAll(query, p);
	}

}
