package com.pantuo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.pantuo.dao.pojo.JpaVideo32OrderStatus;

public interface Vedio32OrderStatusRepository extends JpaRepository<JpaVideo32OrderStatus, Integer>,
		QueryDslPredicateExecutor<JpaVideo32OrderStatus> {
}