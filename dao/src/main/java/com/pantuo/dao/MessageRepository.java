package com.pantuo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.pantuo.dao.pojo.JpaMessage;

/**
 * @author panxh
 */

public interface MessageRepository extends JpaRepository<JpaMessage, Integer>, QueryDslPredicateExecutor<JpaMessage> {

	@Modifying
	@Transactional
	@Query(value = "update JpaMessage o set o.main_type=:mt where o.recID=:uname")
	public int updateMsgMainStatus(@Param("uname") String name, @Param("mt") JpaMessage.Main_type mt);
}