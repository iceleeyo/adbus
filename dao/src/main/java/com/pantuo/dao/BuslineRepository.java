package com.pantuo.dao;

import com.pantuo.dao.pojo.JpaBusline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * @author tliu
 */

public interface BuslineRepository extends JpaRepository<JpaBusline, Integer>, QueryDslPredicateExecutor<JpaBusline> {
    public JpaBusline findByName(String name);
}