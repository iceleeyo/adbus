package com.pantuo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.pantuo.dao.pojo.JpaCpdLog;


public interface CpdLogRepository extends JpaRepository<JpaCpdLog, Integer>, QueryDslPredicateExecutor<JpaCpdLog> {
}