package com.pantuo.dao;

import com.pantuo.dao.pojo.JpaCity;
import com.pantuo.dao.pojo.JpaIndustry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * @author tliu
 */

public interface CityRepository extends JpaRepository<JpaCity, Integer>,
        QueryDslPredicateExecutor<JpaCity> {
}