package com.pantuo.dao;

import com.pantuo.dao.pojo.JpaIndustry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * @author tliu
 */

public interface IndustryRepository extends JpaRepository<JpaIndustry, Integer>,
        QueryDslPredicateExecutor<JpaIndustry> {
}