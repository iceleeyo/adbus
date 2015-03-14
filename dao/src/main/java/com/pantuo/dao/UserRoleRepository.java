package com.pantuo.dao;

import com.pantuo.dao.pojo.UserRole;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author tliu
 */

public interface UserRoleRepository extends CrudRepository<UserRole, Long> {
    List<UserRole> findByUsername(String username);
}