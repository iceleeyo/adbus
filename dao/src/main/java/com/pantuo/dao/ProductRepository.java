package com.pantuo.dao;

import com.pantuo.dao.pojo.JpaProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * @author tliu
 */

public interface ProductRepository extends JpaRepository<JpaProduct, Integer>, QueryDslPredicateExecutor<JpaProduct> {
}