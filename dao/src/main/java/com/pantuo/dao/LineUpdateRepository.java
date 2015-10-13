package com.pantuo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.pantuo.dao.pojo.JpaLineUpLog;

/**
 * @author tliu
 */

public interface LineUpdateRepository extends JpaRepository<JpaLineUpLog, Integer>, QueryDslPredicateExecutor<JpaLineUpLog> {
}