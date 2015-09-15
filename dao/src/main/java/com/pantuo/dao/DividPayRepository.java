package com.pantuo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.pantuo.dao.pojo.JapDividPay;
import com.pantuo.dao.pojo.JpaBusLock;
import com.pantuo.dao.pojo.JpaBusline;
import com.pantuo.dao.pojo.JpaPublishLine;

/**
 * @author tliu
 */

public interface DividPayRepository extends JpaRepository<JapDividPay, Integer>, QueryDslPredicateExecutor<JapDividPay> {
}