package com.pantuo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.pantuo.dao.pojo.JpaCpd;


public interface CpdRepository extends JpaRepository<JpaCpd, Integer>, QueryDslPredicateExecutor<JpaCpd> {
}