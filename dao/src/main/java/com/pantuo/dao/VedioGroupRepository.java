package com.pantuo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.pantuo.dao.pojo.JpaVideo32Group;

public interface VedioGroupRepository extends JpaRepository<JpaVideo32Group, Integer>,
		QueryDslPredicateExecutor<JpaVideo32Group> {
}