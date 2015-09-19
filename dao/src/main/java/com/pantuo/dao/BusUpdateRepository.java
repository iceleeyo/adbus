package com.pantuo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.pantuo.dao.pojo.JpaBusOnline;
import com.pantuo.dao.pojo.JpaBusUpLog;

/**
 * @author tliu
 */

public interface BusUpdateRepository extends JpaRepository<JpaBusUpLog, Integer>, QueryDslPredicateExecutor<JpaBusUpLog> {
}