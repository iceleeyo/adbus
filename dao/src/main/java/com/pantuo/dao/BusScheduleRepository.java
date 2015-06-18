package com.pantuo.dao;

import com.pantuo.dao.pojo.JpaBusSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.awt.print.Pageable;
import java.util.List;

/**
 * @author tliu
 */

public interface BusScheduleRepository extends JpaRepository<JpaBusSchedule, Integer>, QueryDslPredicateExecutor<JpaBusSchedule> {

    int deleteByOrderId(int orderId);
}