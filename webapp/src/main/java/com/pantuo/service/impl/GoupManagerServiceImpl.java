package com.pantuo.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.eclipse.jetty.util.HttpCookieStore.Empty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import scala.actors.threadpool.Arrays;

import com.pantuo.dao.FunctionRepository;
import com.pantuo.dao.GroupFunctionRepository;
import com.pantuo.dao.UserDetailRepository;
import com.pantuo.dao.pojo.JpaFunction;
import com.pantuo.dao.pojo.UserDetail;
import com.pantuo.mybatis.domain.ActIdGroup;
import com.pantuo.mybatis.domain.ActIdGroupExample;
import com.pantuo.mybatis.domain.BusFunction;
import com.pantuo.mybatis.domain.BusFunctionExample;
import com.pantuo.mybatis.domain.GroupFunction;
import com.pantuo.mybatis.domain.GroupFunctionExample;
import com.pantuo.mybatis.persistence.ActIdGroupMapper;
import com.pantuo.mybatis.persistence.BusFunctionMapper;
import com.pantuo.mybatis.persistence.GroupFunctionMapper;
import com.pantuo.mybatis.persistence.UserAutoCompleteMapper;
import com.pantuo.service.GoupManagerService;
import com.pantuo.util.Pair;

@Service
public class GoupManagerServiceImpl implements GoupManagerService {

	@Autowired
	ActIdGroupMapper actIdGroupMapper;

	@Autowired
	FunctionRepository functionRepository;
	@Autowired
	UserDetailRepository userRepo;

	@Autowired
	GroupFunctionRepository groupFunctionRepository;
	@Autowired
	GroupFunctionMapper groupFunctionMapper;
	@Autowired
	UserAutoCompleteMapper userAutoCompleteMapper;
	@Autowired
	BusFunctionMapper busFunctionMapper;

	public final static String BODY_TAG="bd_%d";
	@Override
	public List<JpaFunction> getAllFunction() {
		return functionRepository.findAll();
	}

	@Override
	public List<ActIdGroup> getAllDescionGroup(int city) {
		ActIdGroupExample example = new ActIdGroupExample();
		example.createCriteria().andIdLike(String.format(BODY_TAG, city).concat("%"));
		example.setOrderByClause("id asc ");
		return actIdGroupMapper.selectByExample(example);
	}

	@Override
	public Pair<Boolean, String> addGroup(ActIdGroup ActIdGroup) {
		ActIdGroupExample example = new ActIdGroupExample();
		if (ActIdGroup != null && StringUtils.isNotBlank(ActIdGroup.getId())) {
			if (actIdGroupMapper.selectByPrimaryKey(ActIdGroup.getId()) != null) {
				return new Pair<Boolean, String>(false, "角色英文名已经存在");
			}
			if (actIdGroupMapper.insert(ActIdGroup) > 0) {
				return new Pair<Boolean, String>(true, "角色创建成功");
			}
		}
		return new Pair<Boolean, String>(false, "角色创建失败");
	}

	@Override
	public Pair<Boolean, String> deleteGroup(String groupId) {
		if (StringUtils.isNotBlank(groupId)) {
			List<UserDetail> users = userRepo.findAll();
			for (UserDetail userDetail : users) {
				if (userDetail.getGroupIdList().contains(groupId)) {
					return new Pair<Boolean, String>(false, "该角色已有用户占用,删除失败");
				}
			}
			if (actIdGroupMapper.deleteByPrimaryKey(groupId) > 0) {
				return new Pair<Boolean, String>(true, "删除成功");
			} else {
				return new Pair<Boolean, String>(false, "操作失败");
			}
		}
		return new Pair<Boolean, String>(false, "操作失败");
	}

	@Override
	public Pair<Boolean, String> addGroupFunction(String groupId, String functionIds, int cityid) {
		if (StringUtils.isNotBlank(functionIds)) {
			String[] arr = functionIds.split(",");
			List<String> list = Arrays.asList(arr);
			for (String string : list) {
				GroupFunction groupFunction = new GroupFunction();
				groupFunction.setCity(cityid);
				groupFunction.setGroupId(groupId);
				groupFunction.setFunId(NumberUtils.toInt(string));
				groupFunctionMapper.insert(groupFunction);
			}
			return new Pair<Boolean, String>(true, "操作成功");
		}
		return new Pair<Boolean, String>(false, "操作失败");
	}

	@Override
	public Pair<Boolean, String> editGroupFunction(String groupId, String functionIds, int cityid) {
		GroupFunctionExample example = new GroupFunctionExample();
		example.createCriteria().andGroupIdEqualTo(groupId).andCityEqualTo(cityid);
		groupFunctionMapper.deleteByExample(example);
		if (StringUtils.isNotBlank(functionIds)) {
			String[] arr = functionIds.split(",");
			List<String> list = Arrays.asList(arr);
			for (String string : list) {
				GroupFunction groupFunction = new GroupFunction();
				groupFunction.setCity(cityid);
				groupFunction.setGroupId(groupId);
				groupFunction.setFunId(NumberUtils.toInt(string));
				groupFunctionMapper.insert(groupFunction);
			}
			return new Pair<Boolean, String>(true, "操作成功");
		}
		return new Pair<Boolean, String>(false, "操作失败");
	}

	@Override
	public Pair<Boolean, String> setPersonGroup(String userid, String groupIds) {
		List<UserDetail> users = userRepo.findByUsername(userid);
		if (users.isEmpty())
			return new Pair<Boolean, String>(false, "用户不存在");
		UserDetail user = users.get(0);
		user.setGroupIdList(groupIds);
		if (userRepo.save(user) != null) {
			return new Pair<Boolean, String>(true, "操作成功");
		}
		return new Pair<Boolean, String>(false, "操作失败");
	}

	@Override
	public Pair<Boolean, String> editPersonGroup(String userid, String groupIds) {
		//		List<UserDetail> users = userRepo.findByUsername(userid);
		//		if (users.isEmpty())
		//			return new Pair<Boolean, String>(false,"用户不存在");
		//		UserDetail user = users.get(0);
		//		user.setGroupIdList(groupIds);
		//		if(userRepo.save(user)!=null){
		//			return new Pair<Boolean, String>(true,"操作成功");
		//		}
		//		return new Pair<Boolean, String>(false,"操作失败");
		return setPersonGroup(userid, groupIds);
	}

	@Override
	public List<BusFunction> getFunction4UserId(String userId) {
		List<UserDetail> users = userRepo.findByUsername(userId);
		if (!users.isEmpty()) {
			UserDetail user = users.get(0);
			String groupIds = user.getGroupIdList();
			if (StringUtils.isNoneBlank(groupIds)) {
				String[] arr = groupIds.split(",");

				List<String> gidlist = new ArrayList<String>();
				for (String string : arr) {
					if (StringUtils.isNoneBlank(string))
						gidlist.add(string);
				}
				if (gidlist.size() > 0) {
					return userAutoCompleteMapper.selectFunidsByPid(gidlist);
				}
			}
		}
		return new ArrayList<BusFunction>(0);
	}

}
