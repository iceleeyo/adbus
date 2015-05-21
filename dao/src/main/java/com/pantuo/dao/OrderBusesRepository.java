package com.pantuo.dao;

import com.pantuo.dao.pojo.JpaBus;
import com.pantuo.dao.pojo.JpaOrderBuses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * @author tliu
 */

public interface OrderBusesRepository extends JpaRepository<JpaOrderBuses, Integer>, QueryDslPredicateExecutor<JpaOrderBuses> {
}