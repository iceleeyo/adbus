package com.pantuo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.pantuo.dao.pojo.JpaInfoImgSchedule;

/**
 * @author tliu
 */

public interface InfoImgScheduleRepository extends JpaRepository<JpaInfoImgSchedule, Integer>, QueryDslPredicateExecutor<JpaInfoImgSchedule> {
}