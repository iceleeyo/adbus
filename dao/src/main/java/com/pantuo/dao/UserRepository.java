package com.pantuo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pantuo.dao.pojo.User;

/**
 * @author tliu
 */

public interface UserRepository extends JpaRepository<User, Long> {
	public List<User> findByUsername(String username);

	//@Query("SELECT  u.username FROM  User u INNER JOIN UserRole a ON a.username = u.username WHERE  u.id = :idArea")
	//List<User> findByIdarea(@Param("idArea") int idArea);

}