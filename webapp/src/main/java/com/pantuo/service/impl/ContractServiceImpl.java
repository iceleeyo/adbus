package com.pantuo.service.impl;

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
	public Page<JpaContract> getAllContracts(String name, int page, int pageSize) {
		if (page < 0)
            page = 0;
        if (pageSize < 1)
            pageSize = 1;
        Pageable p = new PageRequest(page, pageSize, new Sort("id"));
        if (name == null || StringUtils.isEmpty(name)) {
            return  contractRepo.findAll(p);
        } else {
            Predicate query = QJpaProduct.jpaProduct.name.like("%" + name + "%");
            return contractRepo.findAll(query, p);
        }
	}

	public Page<JpaContract> getValidContracts(int page, int pageSize) {
		if (page < 0)
            page = 0;
        if (pageSize < 1)
            pageSize = 1;
        Pageable p = new PageRequest(page, pageSize, new Sort(Sort.Direction.DESC, "id"));
  //      Predicate query = QJpaContract.jpaContract.enabled.isTrue();
        return contractRepo.findAll( p);  
	}
	
    
}
