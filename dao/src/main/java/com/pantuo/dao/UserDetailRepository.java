package com.pantuo.dao;

import java.util.List;

import com.pantuo.dao.pojo.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * @author tliu
 */

public interface UserDetailRepository extends JpaRepository<UserDetail, Long>, QueryDslPredicateExecutor<UserDetail> {
	public List<UserDetail> findByUsername(String username);

	//@Query("SELECT  u.username FROM  UserDetail u INNER JOIN UserRole a ON a.username = u.username WHERE  u.id = :idArea")
	//List<UserDetail> findByIdarea(@Param("idArea") int idArea);

}