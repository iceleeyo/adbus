package com.pantuo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.pantuo.dao.pojo.JpaCardBoxBody;

public interface CardBoxBodyRepository extends JpaRepository<JpaCardBoxBody, Integer>,
		QueryDslPredicateExecutor<JpaCardBoxBody> {
}