package com.pantuo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pantuo.dao.FunctionRepository;
import com.pantuo.dao.GroupFunctionRepository;
import com.pantuo.dao.pojo.JpaFunction;
import com.pantuo.mybatis.domain.ActIdGroup;
import com.pantuo.mybatis.persistence.ActIdGroupMapper;
import com.pantuo.service.GoupManagerService;
import com.pantuo.util.Pair;

@Service
public class GoupManagerServiceImpl implements GoupManagerService {

	@Autowired
	ActIdGroupMapper actIdGroupMapper;

	@Autowired
	FunctionRepository functionRepository;

	@Autowired
	GroupFunctionRepository groupFunctionRepository;

	@Override
	public List<JpaFunction> getAllFunction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ActIdGroup> getAllDescionGroup() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pair<Boolean, String> addGroup(ActIdGroup ActIdGroup) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pair<Boolean, String> deleteGroup(String groupId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pair<Boolean, String> addGroupFunction(String groupId, String functionIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pair<Boolean, String> editGroupFunction(String groupId, String functionIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pair<Boolean, String> setPersonGroup(String userid, List<String> groupIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pair<Boolean, String> editPersonGroup(String userid, List<String> groupIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<JpaFunction> getFunction4UserId(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

}
