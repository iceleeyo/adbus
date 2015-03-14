package com.pantuo.dao;

import com.pantuo.dao.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author tliu
 */

public interface UserRepository extends JpaRepository<User, Long> {
    public List<User> findByUsername(String username);
}