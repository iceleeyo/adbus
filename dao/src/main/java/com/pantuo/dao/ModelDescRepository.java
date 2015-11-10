package com.pantuo.dao;

import com.pantuo.dao.pojo.JpaBusModel;
import com.pantuo.dao.pojo.JpaBusline;
import com.pantuo.dao.pojo.JpaModeldesc;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * @author tliu
 */

public interface ModelDescRepository extends JpaRepository<JpaModeldesc, Integer>, QueryDslPredicateExecutor<JpaModeldesc> {
}