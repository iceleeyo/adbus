package com.pantuo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.pantuo.dao.pojo.JpaProductTag;





public interface ProductTagRepository extends JpaRepository<JpaProductTag, Integer>,
		QueryDslPredicateExecutor<JpaProductTag> {
}