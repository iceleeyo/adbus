package com.pantuo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.pantuo.dao.pojo.JpaBodyOrderLog;
import com.pantuo.dao.pojo.JpaCardBoxHelper;
import com.pantuo.dao.pojo.JpaCpdLog;


public interface BodyOrderLogRepository extends JpaRepository<JpaBodyOrderLog, Integer>, QueryDslPredicateExecutor<JpaBodyOrderLog> {
}