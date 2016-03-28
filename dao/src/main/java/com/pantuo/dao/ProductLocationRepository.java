package com.pantuo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.pantuo.dao.pojo.JpaCpdLog;
import com.pantuo.dao.pojo.JpaProductLocation;


public interface ProductLocationRepository extends JpaRepository<JpaProductLocation, Integer>, QueryDslPredicateExecutor<JpaProductLocation> {
}