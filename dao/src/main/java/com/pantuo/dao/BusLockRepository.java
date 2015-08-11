package com.pantuo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.pantuo.dao.pojo.JpaBusLock;
import com.pantuo.dao.pojo.JpaBusline;

/**
 * @author tliu
 */

public interface BusLockRepository extends JpaRepository<JpaBusLock, Integer>, QueryDslPredicateExecutor<JpaBusLock> {
}