package com.pantuo.dao;

import com.pantuo.dao.pojo.JpaGoods;
import com.pantuo.dao.pojo.JpaGoodsBlack;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * @author tliu
 */

public interface GoodsBlackRepository extends JpaRepository<JpaGoodsBlack, Integer>, QueryDslPredicateExecutor<JpaGoodsBlack> {
}