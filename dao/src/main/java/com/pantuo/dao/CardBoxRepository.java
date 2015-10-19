package com.pantuo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.pantuo.dao.pojo.JpaCardBoxMedia;

public interface CardBoxRepository extends JpaRepository<JpaCardBoxMedia, Integer>,
		QueryDslPredicateExecutor<JpaCardBoxMedia> {
}