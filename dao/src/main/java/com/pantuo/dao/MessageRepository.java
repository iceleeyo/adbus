package com.pantuo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.pantuo.dao.pojo.JpaMessage;

/**
 * @author panxh
 */

public interface MessageRepository extends JpaRepository<JpaMessage, Integer>, QueryDslPredicateExecutor<JpaMessage> {
}