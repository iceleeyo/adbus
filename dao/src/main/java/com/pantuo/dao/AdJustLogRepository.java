package com.pantuo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.pantuo.dao.pojo.JpaBusAdjustLog;

public interface AdJustLogRepository extends JpaRepository<JpaBusAdjustLog, Integer>,
		QueryDslPredicateExecutor<JpaBusAdjustLog> {
}