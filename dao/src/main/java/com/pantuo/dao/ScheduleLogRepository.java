package com.pantuo.dao;

import com.pantuo.dao.pojo.ScheduleLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.Date;
import java.util.List;

/**
 * @author tliu
 */

public interface ScheduleLogRepository extends JpaRepository<ScheduleLog, Integer>, QueryDslPredicateExecutor<ScheduleLog> {
    ScheduleLog findByCityAndDayAndOrderId(int city, Date day, int orderId);

    ScheduleLog findByCityAndDayAndStatus(int city, Date day, ScheduleLog.Status status);

    List<ScheduleLog> findByCityAndOrderId(int city, int orderId);
}