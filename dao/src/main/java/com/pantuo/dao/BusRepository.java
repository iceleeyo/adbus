package com.pantuo.dao;

import com.pantuo.dao.pojo.JpaBus;
import com.pantuo.dao.pojo.JpaTimeslot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;

/**
 * @author tliu
 */

public interface BusRepository extends JpaRepository<JpaBus, Integer>, QueryDslPredicateExecutor<JpaBus> {
    long countByCityAndEnabled(int city, boolean enabled);
}