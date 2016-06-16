package com.pantuo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.pantuo.dao.pojo.JpaCardBoxHelper;
import com.pantuo.dao.pojo.JpaCpdLog;


public interface CardHelperRepository extends JpaRepository<JpaCardBoxHelper, Integer>, QueryDslPredicateExecutor<JpaCardBoxHelper> {
}