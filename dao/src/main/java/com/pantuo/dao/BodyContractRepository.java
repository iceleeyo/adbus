package com.pantuo.dao;

import com.pantuo.dao.pojo.JpaBodyContract;
import com.pantuo.dao.pojo.JpaBus;
import com.pantuo.dao.pojo.JpaTimeslot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;


public interface BodyContractRepository extends JpaRepository<JpaBodyContract, Integer>,
		QueryDslPredicateExecutor<JpaBodyContract> {
}