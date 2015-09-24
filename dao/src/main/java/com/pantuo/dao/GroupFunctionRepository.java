package com.pantuo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.pantuo.dao.pojo.JpaGroupFunction;


public interface GroupFunctionRepository extends JpaRepository<JpaGroupFunction, Integer>, QueryDslPredicateExecutor<JpaGroupFunction> {
}