package com.pantuo.dao;

import com.pantuo.dao.pojo.JpaCalendar;
import com.pantuo.dao.pojo.JpaCity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * @author tliu
 */

public interface CalendarRepository extends JpaRepository<JpaCalendar, Integer>,
        QueryDslPredicateExecutor<JpaCalendar> {
}