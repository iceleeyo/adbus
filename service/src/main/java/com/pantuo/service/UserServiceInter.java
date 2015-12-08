package com.pantuo.service;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.activiti.engine.identity.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import scala.collection.generic.BitOperations.Int;

import com.pantuo.dao.pojo.UserDetail;
import com.pantuo.dao.pojo.UserDetail.UType;
import com.pantuo.mybatis.domain.Attachment;
import com.pantuo.mybatis.domain.Invoice;
import com.pantuo.pojo.TableRequest;
import com.pantuo.util.Pair;
import com.pantuo.web.view.AutoCompleteView;
import com.pantuo.web.view.InvoiceView;

public interface UserServiceInter {

	public abstract long count();
	public abstract long countModeldesc();

	public abstract long countGroups();

	public abstract Page<UserDetail> getAllUsers(String utype,String name, int page, int pageSize, Sort order);
public	Page<UserDetail> getUsers(String utype, String name, Boolean isEnabled, int page, int pageSize,
			Sort order,UType loginUserType);

	public abstract Page<UserDetail> getValidUsers(String utype,int page, int pageSize, Sort order);

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
	
	public abstract UserDetail getByUsernameSafe(String username);



	public abstract Pair<Boolean, String> updatePwd(String userId, String psw) throws Exception;

	public abstract List<String> getUserGroupList(UserDetail u);

	public abstract UserDetail findByUsername(String username);

	public abstract UserDetail findDetailByUsername(String username);

	public abstract void saveDetail(UserDetail user);

	public abstract boolean createUser(UserDetail user);


	public abstract boolean createUserFromPage(UserDetail user,HttpServletRequest request,Principal principal);

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

	public  InvoiceView findInvoiceByUser(int invoice_id,Principal principal);

	public  Pair<Boolean, String> delInvoice(int invoice_id, Principal principal);

	public  List<Invoice> queryInvoiceByUser(int cityId, Principal principal);

	public  Pair<Boolean, String> updateUserFromPage(UserDetail detail, Principal principal,
			HttpServletRequest request);
	 /**
	  * 
	  * ajax 登录 
	  *
	  * @param request
	  * @param name
	  * @param pwd
	  * @return
	  * @since pantuo 1.0-SNAPSHOT
	  */
	public Pair<Boolean,String> loginForLayer(HttpServletRequest request,String name,String pwd);
	
	public String getUserUniqCode(String uname);

	public abstract Pair<Boolean, String> editPwd(String userId, String oldpassword, String psw);
	public abstract List<Integer> gettypeListByAttach(List<Attachment> attachments);

}