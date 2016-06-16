package com.pantuo.dao;

import com.pantuo.dao.pojo.JpaContract;
import com.pantuo.dao.pojo.JpaProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * @author tliu
 */

public interface ContractRepository extends JpaRepository<JpaContract, Integer>, QueryDslPredicateExecutor<JpaContract> {
}