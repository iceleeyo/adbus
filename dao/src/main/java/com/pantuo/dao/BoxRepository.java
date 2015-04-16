package com.pantuo.dao;

import com.pantuo.dao.pojo.JpaBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author tliu
 */

public interface BoxRepository extends JpaRepository<JpaBox, Integer>, QueryDslPredicateExecutor<JpaBox> {
    List<JpaBox> findByDay(Date day);
}