package com.pantuo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.pantuo.dao.pojo.JpaFunction;


public interface FunctionRepository extends JpaRepository<JpaFunction, Integer>, QueryDslPredicateExecutor<JpaFunction> {
}