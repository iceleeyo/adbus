package com.pantuo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.pantuo.dao.pojo.JpaBusOnline;

/**
 * @author tliu
 */

public interface BusOnlineRepository extends JpaRepository<JpaBusOnline, Integer>, QueryDslPredicateExecutor<JpaBusOnline> {
}