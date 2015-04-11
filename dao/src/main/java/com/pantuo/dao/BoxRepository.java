package com.pantuo.dao;

import com.pantuo.dao.pojo.Box;
import com.pantuo.dao.pojo.JpaOrders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.Date;
import java.util.List;

/**
 * @author tliu
 */

public interface BoxRepository extends JpaRepository<Box, Integer>, QueryDslPredicateExecutor<Box> {
    List<Box> findByDay(Date day);
}