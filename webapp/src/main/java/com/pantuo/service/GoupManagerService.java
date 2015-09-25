package com.pantuo.service;

import java.security.Principal;
import java.util.List;

import com.pantuo.dao.pojo.JpaFunction;
import com.pantuo.dao.pojo.UserDetail;
import com.pantuo.mybatis.domain.ActIdGroup;
import com.pantuo.mybatis.domain.BusFunction;
import com.pantuo.util.Pair;
import com.pantuo.web.view.RoleView;

public interface GoupManagerService {

	/**
	 * 
	 * 增加需要判断组名是否存在
	 *
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public List<JpaFunction> getAllFunction();
	
	
	/**
	 * 
	 * 查一个用户的功能集合 内部需要先查他的组再关联功能
	 *
	 * @param userId
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public List<BusFunction> getFunction4UserId(UserDetail user);

	/**
	 * 
	 * 查组组按groupid 排序
	 *
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */

	public List<ActIdGroup> getAllDescionGroup(int city);

	/**
	 * 
	 * 增加需要判断组名是否存在
	 *
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public Pair<Boolean, String> addGroup(ActIdGroup ActIdGroup);

	/**
	 * 
	 * 删除组需要判断组内是否有成员
	 *
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public Pair<Boolean, String> deleteGroup(String groupId);

	/**
	 * 
	 *  增加 组和功能的关联
	 *
	 * @param groupId
	 * @param functionIds
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public Pair<Boolean, String> addGroupFunction(String groupId, String functionIds,int cityid);

	/**
	 * 
	 * 修改组和功能的关系
	 *
	 * @param groupId
	 * @param functionIds
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public Pair<Boolean, String> editGroupFunction(String groupId, String functionIds,int cityid);

	/**
	 * 
	 * 指定人与组的关联
	 *
	 * @param userid
	 * @param groupIds
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public Pair<Boolean, String> setPersonGroup(String userid, String groupIds);

	/**
	 * 
	 * 修改人与组的关联
	 *
	 * @param userid
	 * @param groupIds
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public Pair<Boolean, String> editPersonGroup(String userid, String groupIds);


	public Pair<Boolean, String> saveRole(String ids, String rolename, String funcode, String fundesc,
			Principal principal, int city);
	public Pair<Boolean, String> editRole(String groupid,String ids, String rolename, String funcode, String fundesc,
			Principal principal, int city);


	public List<RoleView> findAllBodyRoles(int cityId);


	public ActIdGroup getActIdGroupByID(String groupid);
	public List<Integer> findFuncIdsByGroupId(String id);
}
