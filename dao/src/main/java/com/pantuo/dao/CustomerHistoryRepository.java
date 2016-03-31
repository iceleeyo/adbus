package com.pantuo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.pantuo.dao.pojo.JpaCpdLog;
import com.pantuo.dao.pojo.JpaCustomerHistory;
import com.pantuo.dao.pojo.JpaProductLocation;

public interface CustomerHistoryRepository extends JpaRepository<JpaCustomerHistory, Integer>,
		QueryDslPredicateExecutor<JpaCustomerHistory> {
}