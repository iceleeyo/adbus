package com.pantuo.dao;

import com.pantuo.dao.pojo.JpaContract;
import com.pantuo.dao.pojo.JpaSupplies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * @author tliu
 */

public interface SuppliesRepository extends JpaRepository<JpaSupplies, Integer>, QueryDslPredicateExecutor<JpaSupplies> {
}