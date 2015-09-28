package com.pantuo.dao;

import com.pantuo.dao.pojo.JpaBusOrderV2;
import com.pantuo.dao.pojo.JpaProduct;
import com.pantuo.dao.pojo.JpaProductV2;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * @author tliu
 */

public interface BusOrderV2Repository extends JpaRepository<JpaBusOrderV2, Integer>, QueryDslPredicateExecutor<JpaBusOrderV2> {
}