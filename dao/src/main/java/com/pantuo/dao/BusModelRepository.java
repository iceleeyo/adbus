package com.pantuo.dao;

import com.pantuo.dao.pojo.JpaBusModel;
import com.pantuo.dao.pojo.JpaBusline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * @author tliu
 */

public interface BusModelRepository extends JpaRepository<JpaBusModel, Integer>, QueryDslPredicateExecutor<JpaBusModel> {
    public JpaBusModel findByName(String name);
}