package com.pantuo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.pantuo.dao.pojo.JpaOrders;

/**
 * @author panxh
 */

public interface OrdersRepository extends JpaRepository<JpaOrders, Integer>, QueryDslPredicateExecutor<JpaOrders> {
}