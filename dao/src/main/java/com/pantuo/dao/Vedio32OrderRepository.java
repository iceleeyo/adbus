package com.pantuo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.pantuo.dao.pojo.Jpa32Order;

public interface Vedio32OrderRepository extends JpaRepository<Jpa32Order, Integer>,
		QueryDslPredicateExecutor<Jpa32Order> {
}