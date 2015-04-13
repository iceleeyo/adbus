package com.pantuo.service.impl;

import com.mysema.query.types.expr.BooleanExpression;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.mysema.query.types.Predicate;
import com.pantuo.dao.ContractRepository;
import com.pantuo.dao.ProductRepository;
import com.pantuo.dao.pojo.JpaContract;
import com.pantuo.dao.pojo.QJpaContract;
import com.pantuo.dao.pojo.QJpaProduct;
import com.pantuo.mybatis.persistence.ContractMapper;
import com.pantuo.service.ContractServiceData;

@Service
public class ContractServiceImpl implements ContractServiceData {
	@Autowired
	ContractMapper contractMapper;
	
	@Autowired
    ContractRepository contractRepo;
	public Page<JpaContract> getAllContracts(String name, String code,int page, int pageSize, Sort sort) {
		if (page < 0)
            page = 0;
        if (pageSize < 1)
            pageSize = 1;
        sort = (sort == null? new Sort("id") : sort);
        Pageable p = new PageRequest(page, pageSize, sort);
        if (StringUtils.isBlank(name) && StringUtils.isBlank(code)) {
            return  contractRepo.findAll(p);
        } else {
            BooleanExpression query = null;
            if (StringUtils.isNotBlank(name)) {
                query = QJpaContract.jpaContract.contractName.like("%" + name + "%");
            }
            if (StringUtils.isNoneBlank(code)) {
                BooleanExpression q = QJpaContract.jpaContract.contractCode.like("%" + code + "%");
                if (query == null)
                    query = q;
                else
                    query = query.and(q);
            }
            return contractRepo.findAll(query, p);
        }
	}

	public Page<JpaContract> getValidContracts(int page, int pageSize, Sort sort) {
		if (page < 0)
            page = 0;
        if (pageSize < 1)
            pageSize = 1;
        sort = (sort == null? new Sort(Sort.Direction.DESC, "id") : sort);
        Pageable p = new PageRequest(page, pageSize, sort);
  //      Predicate query = QJpaContract.jpaContract.enabled.isTrue();
        return contractRepo.findAll( p);  
	}
	
    
}
