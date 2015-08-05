package com.pantuo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.pantuo.dao.pojo.JpaBlackAd;

/**
 * @author tliu
 */

public interface BlackAdRepository extends JpaRepository<JpaBlackAd, Integer>, QueryDslPredicateExecutor<JpaBlackAd> {
}