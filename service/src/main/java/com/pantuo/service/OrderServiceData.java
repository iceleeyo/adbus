package com.pantuo.service;
import org.springframework.data.domain.Page;

import com.pantuo.dao.pojo.JpaContract;
import com.pantuo.dao.pojo.JpaOrders;


public interface OrderServiceData {
	Page<JpaOrders> getAllOrders(int city, String productId, int page, int pageSize);

    Page<JpaOrders> getValidOrders(int city, int page, int pageSize);

	
}
