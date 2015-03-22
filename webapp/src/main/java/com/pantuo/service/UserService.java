package com.pantuo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.pantuo.dao.UserRepository;
import com.pantuo.dao.UserRoleRepository;
//import com.pantuo.dao.pojo.QUser;
import com.pantuo.dao.pojo.User;
import com.pantuo.mybatis.domain.SysConfig;
import com.pantuo.mybatis.persistence.SysConfigMapper;

/**
 * @author tliu
 */
@Service
public class UserService {
	@Autowired
	private UserRepository userRepo;

	@Autowired
	private SysConfigMapper sysConfigMapper;

	@Autowired
	private UserRoleRepository roleRepo;

	public UserRepository getUserRepo() {
		return userRepo;
	}

	public UserRoleRepository getRoleRepo() {
		return roleRepo;
	}

	public Page<User> getAllUsers(int page, int pageSize) {
		if (page < 0)
			page = 0;
		if (pageSize < 1)
			pageSize = 1;
		//test();
		Pageable p = new PageRequest(page, pageSize, new Sort("id"));
		return userRepo.findAll(p);
	}

	public void test() {
		SysConfig g = sysConfigMapper.selectByPrimaryKey(1);
		if (g != null) {
			System.out.println(g.getKeyCode());
		}
		//  List<User> u=  userRepo.findByIdarea(1);
		//  System.out.println(u);
	}
}
