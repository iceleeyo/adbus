package com.pantuo.service.impl;

import com.mysema.query.types.expr.BooleanExpression;
import com.pantuo.dao.pojo.QJpaContract;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.mysema.query.types.Predicate;
import com.pantuo.dao.OrdersRepository;
import com.pantuo.dao.pojo.JpaOrders;
import com.pantuo.dao.pojo.QJpaOrders;
import com.pantuo.mybatis.persistence.OrdersMapper;
import com.pantuo.service.OrderServiceData;

@Service
public class OrderDataServiceImpl implements OrderServiceData {
	@Autowired
	OrdersMapper orderMapper;
	
	@Autowired
	OrdersRepository orderRepo;

	public Page<JpaOrders> getAllOrders(int city, String productId, int page, int pageSize) {
		if (page < 0)
            page = 0;
        if (pageSize < 1)
            pageSize = 1;
        Pageable p = new PageRequest(page, pageSize, new Sort("id"));
        BooleanExpression query = QJpaOrders.jpaOrders.city.eq(city);
        if (productId != null && !StringUtils.isEmpty(productId)) {
            query = query.and(QJpaOrders.jpaOrders.product.id.like("%" + productId + "%"));
        }
        return orderRepo.findAll(query, p);
    }

	public Page<JpaOrders> getValidOrders(int city, int page, int pageSize) {
		if (page < 0)
            page = 0;
        if (pageSize < 1)
            pageSize = 1;
        Pageable p = new PageRequest(page, pageSize, new Sort(Sort.Direction.DESC, "id"));
        Predicate query = QJpaContract.jpaContract.city.eq(city);
        return orderRepo.findAll(query, p);
	}
	
    
}
