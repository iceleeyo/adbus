package com.pantuo.dao;

import com.pantuo.dao.pojo.Role;
import com.pantuo.dao.pojo.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author tliu
 */

public interface RoleRepository extends CrudRepository<Role, Long> {
    public List<Role> findByName(String name);
}