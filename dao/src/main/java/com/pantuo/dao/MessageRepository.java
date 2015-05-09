package com.pantuo.dao;

import com.pantuo.dao.pojo.JpaOrders;
import com.pantuo.dao.pojo.JpaProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * @author panxh
 */

public interface MessageRepository extends JpaRepository<JpaOrders, Integer>, QueryDslPredicateExecutor<JpaOrders> {
}