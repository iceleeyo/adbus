package com.pantuo.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.activiti.engine.identity.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import com.pantuo.dao.pojo.UserDetail;
import com.pantuo.util.Pair;
import com.pantuo.web.view.AutoCompleteView;

public interface UserServiceInter {

	public abstract long count();

	public abstract long countGroups();

	public abstract Page<UserDetail> getAllUsers(String name, int page, int pageSize, Sort order);

	public abstract Page<UserDetail> getValidUsers(int page, int pageSize, Sort order);

	public abstract List<Group> getAllGroup();

	public abstract List<UserDetail> queryUserByname(String name);

	/**
	 * 
	 * 广告主 自动补全
	 *
	 * @param name
	 * @return
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	public abstract List<AutoCompleteView> autoCompleteByName(String name);

	public abstract UserDetail getByUsername(String username);

	public abstract Pair<Boolean, String> addUserMailReset(UserDetail u, HttpServletRequest request);

	public abstract String getMailTemplete(String userId, String resetPwd, HttpServletRequest request);

	public abstract Pair<Boolean, String> updatePwd(String userId, String psw) throws Exception;

	public abstract List<String> getUserGroupList(UserDetail u);

	public abstract UserDetail findByUsername(String username);

	public abstract UserDetail findDetailByUsername(String username);

	public abstract void saveDetail(UserDetail user);

	public abstract boolean createUser(UserDetail user);

	public abstract boolean updateUserFromPage(UserDetail user);

	public abstract boolean createUserFromPage(UserDetail user);

	public abstract boolean deleteUser(String username);

	public abstract boolean deleteGroups(List<String> groups);

	public abstract boolean saveGroup(Group group);

	/**
	 * 
	 * 判断用户是否属于某个组
	 *
	 * @param uname
	 * @param group
	 * @return
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	public abstract boolean isUserHaveGroup(String uname, String group);

}