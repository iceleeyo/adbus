package com.pantuo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.pantuo.dao.pojo.JpaScheduleChangeLog;


public interface ScheduleChangeLogRepository extends JpaRepository<JpaScheduleChangeLog, Integer>, QueryDslPredicateExecutor<JpaScheduleChangeLog> {
}