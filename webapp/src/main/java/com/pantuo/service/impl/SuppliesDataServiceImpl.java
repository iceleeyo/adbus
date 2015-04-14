package com.pantuo.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.mysema.query.types.Predicate;
import com.pantuo.dao.SuppliesRepository;
import com.pantuo.dao.pojo.JpaSupplies;
import com.pantuo.dao.pojo.QJpaSupplies;
import com.pantuo.mybatis.persistence.SuppliesMapper;
import com.pantuo.service.SuppliesServiceData;
           
@Service
public class SuppliesDataServiceImpl implements SuppliesServiceData {
	@Autowired
	SuppliesMapper suppliesMapper;
	
	@Autowired
	SuppliesRepository suppliesRepo;

	public Page<JpaSupplies> getAllSupplies(String name, int page, int pageSize, Sort sort) {
		if (page < 0)
            page = 0;
        if (pageSize < 1)
            pageSize = 1;
        sort = (sort == null? new Sort("id") : sort);
        Pageable p = new PageRequest(page, pageSize, sort);
        if (name == null || StringUtils.isEmpty(name)) {
            return  suppliesRepo.findAll(p);
        } else {
            Predicate query = QJpaSupplies.jpaSupplies.name.like("%" + name + "%");
            return suppliesRepo.findAll(query, p);
        }
	}

	public Page<JpaSupplies> getValidSupplies(int page, int pageSize, Sort sort) {
		if (page < 0)
            page = 0;
        if (pageSize < 1)
            pageSize = 1;
        sort = (sort == null? new Sort(Sort.Direction.DESC, "id") : sort);
        Pageable p = new PageRequest(page, pageSize, sort);
        /*Predicate query = QJpaSupplies.jpaSupplies.enabled.isTrue();*/
        return suppliesRepo.findAll( p);
	}
	
    
}
