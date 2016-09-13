package com.pantuo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.pantuo.dao.pojo.JpaPayContract;

public interface PayContractRepository extends JpaRepository<JpaPayContract, Integer>,
		QueryDslPredicateExecutor<JpaPayContract> {
}