package com.pantuo.dao;

import com.pantuo.dao.pojo.JpaProduct;
import com.pantuo.dao.pojo.JpaProductV2;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * @author tliu
 */

public interface ProductV2Repository extends JpaRepository<JpaProductV2, Integer>, QueryDslPredicateExecutor<JpaProductV2> {
}