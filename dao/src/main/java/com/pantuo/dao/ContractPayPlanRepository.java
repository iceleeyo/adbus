package com.pantuo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.pantuo.dao.pojo.JpaContractPayPlan;

public interface ContractPayPlanRepository extends JpaRepository<JpaContractPayPlan, Integer>,
		QueryDslPredicateExecutor<JpaContractPayPlan> {
}