package com.pantuo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.pantuo.dao.pojo.JpaBus;

/**
 * @author tliu
 */

public interface BusRepository extends JpaSpecificationExecutor<JpaBus>,JpaRepository<JpaBus, Integer>, QueryDslPredicateExecutor<JpaBus> {
    long countByCityAndEnabled(int city, boolean enabled);
}