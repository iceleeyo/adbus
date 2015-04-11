package com.pantuo.dao;

import com.pantuo.dao.pojo.JpaGoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * @author tliu
 */

public interface GoodsRepository extends JpaRepository<JpaGoods, Integer>, QueryDslPredicateExecutor<JpaGoods> {
}