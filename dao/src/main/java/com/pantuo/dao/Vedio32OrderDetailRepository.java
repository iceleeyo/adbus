package com.pantuo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.pantuo.dao.pojo.JpaVideo32OrderDetail;

public interface Vedio32OrderDetailRepository extends JpaRepository<JpaVideo32OrderDetail, Integer>, QueryDslPredicateExecutor<JpaVideo32OrderDetail> {
}