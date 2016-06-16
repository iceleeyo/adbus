package com.pantuo.dao;

import com.pantuo.dao.pojo.JpaBusOrderDetailV2;
import com.pantuo.dao.pojo.JpaProduct;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * @author tliu
 */

public interface BusOrderDetailV2Repository extends JpaRepository<JpaBusOrderDetailV2, Integer>, QueryDslPredicateExecutor<JpaBusOrderDetailV2> {
}