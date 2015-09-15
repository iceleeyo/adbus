package com.pantuo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.pantuo.dao.pojo.JpaBusLock;
import com.pantuo.dao.pojo.JpaBusline;
import com.pantuo.dao.pojo.JpaPublishLine;

/**
 * @author tliu
 */

public interface PublishLineRepository extends JpaRepository<JpaPublishLine, Integer>, QueryDslPredicateExecutor<JpaPublishLine> {
}