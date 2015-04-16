package com.pantuo.dao;

import com.pantuo.dao.pojo.JpaProduct;
import com.pantuo.dao.pojo.JpaTimeslot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * @author tliu
 */

public interface TimeslotRepository extends JpaRepository<JpaTimeslot, Integer>, QueryDslPredicateExecutor<JpaTimeslot> {
    @Query("select sum(t.duration) from JpaTimeslot t where t.enabled=true")
    long sumDuration();
    @Query("select sum(t.duration) from JpaTimeslot t where t.enabled=true and t.peak=true")
    long sumPeakDuration();

}