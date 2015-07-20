package com.pantuo.service.impl;

import java.io.StringWriter;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.mysema.query.types.expr.BooleanExpression;
import com.pantuo.ActivitiConfiguration;
import com.pantuo.dao.UserDetailRepository;
import com.pantuo.dao.pojo.BaseEntity;
import com.pantuo.dao.pojo.QUserDetail;
import com.pantuo.dao.pojo.UserDetail;
import com.pantuo.dao.pojo.UserDetail.UStats;
import com.pantuo.mybatis.domain.Attachment;
import com.pantuo.mybatis.domain.Invoice;
import com.pantuo.mybatis.domain.InvoiceExample;
import com.pantuo.mybatis.persistence.AttachmentMapper;
import com.pantuo.mybatis.persistence.InvoiceMapper;
import com.pantuo.mybatis.persistence.UserAutoCompleteMapper;
import com.pantuo.service.ActivitiService.SystemRoles;
import com.pantuo.service.AttachmentService;
import com.pantuo.service.SuppliesService;
import com.pantuo.service.UserServiceInter;
import com.pantuo.service.security.ActivitiUserDetails;
import com.pantuo.service.security.ActivitiUserDetailsService;
import com.pantuo.util.FreeMarker;
import com.pantuo.util.GlobalMethods;
import com.pantuo.util.Mail;
import com.pantuo.util.Pair;
import com.pantuo.util.Request;
import com.pantuo.web.view.AutoCompleteView;
import com.pantuo.web.view.InvoiceView;

/**
 * @author tliu
 */
@Service
public class UserService implements UserServiceInter {
	private static Logger log = LoggerFactory.getLogger(UserService.class);
	@Autowired
	private UserDetailRepository userRepo;


	@Autowired
	private IdentityService identityService;
	@Autowired
	private AttachmentService attachmentService;
	@Autowired
	private SuppliesService suppliesService;

	@Autowired
	private ManagementService managementService;
	@Autowired
	UserAutoCompleteMapper userAutoCompleteMapper;
	@Autowired
	InvoiceMapper invoiceMapper;
	@Autowired
	AttachmentMapper attachmentMapper;
	
	
	 @Autowired
	 private ActivitiUserDetailsService authUserService;

	/**
	 * @see com.pantuo.service.UserServiceInter#count()
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	public long count() {
		return userRepo.count();
	}

	/**
	 * @see com.pantuo.service.UserServiceInter#countGroups()
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	public long countGroups() {
		return identityService.createGroupQuery().count();
	}

	/**
	 * @see com.pantuo.service.UserServiceInter#getAllUsers(java.lang.String, int, int, org.springframework.data.domain.Sort)
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	public Page<UserDetail> getAllUsers(String name, int page, int pageSize, Sort order) {
		return getUsers(name, null, page, pageSize, order);
	}

	/**
	 * @see com.pantuo.service.UserServiceInter#getValidUsers(int, int, org.springframework.data.domain.Sort)
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	public Page<UserDetail> getValidUsers(int page, int pageSize, Sort order) {
		return getUsers(null, true, page, pageSize, order);
	}

	/**
	 * @see com.pantuo.service.UserServiceInter#getAllGroup()
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	public List<Group> getAllGroup() {
		return identityService.createGroupQuery().list();
	}

	private Page<UserDetail> getUsers(String name, Boolean isEnabled, int page, int pageSize, Sort order) {
		if (page < 0)
			page = 0;
		if (pageSize < 1)
			pageSize = 1;
		//test();
		Page<UserDetail> result = null;
		Pageable p = new PageRequest(page, pageSize, (order == null ? new Sort("id") : order));
		if (StringUtils.isEmpty(name) && isEnabled == null) {
			result = userRepo.findAll(p);
		} else {
			QUserDetail q = QUserDetail.userDetail;
			BooleanExpression query = null;
			if (!StringUtils.isEmpty(name)) {
				query = q.username.like("%" + name + "%");
			}
			if (isEnabled != null) {
				query = (query == null ? q.enabled.eq(isEnabled) : query.and(q.enabled.eq(isEnabled)));
			}
			result = userRepo.findAll(query, p);
		}

		//fetch and fill info from activiti user table
		StringBuffer userIds = new StringBuffer();
		for (UserDetail u : result.getContent()) {
			List<Group> groups = identityService.createGroupQuery().groupMember(u.getUsername()).list();
			u.setGroups(groups);
			userIds.append("'").append(u.getUsername()).append("',");
		}
		if (userIds.length() > 0)
			userIds.setLength(userIds.length() - 1);

		List<User> activitiUsers = identityService
				.createNativeUserQuery()
				.sql("SELECT * FROM " + managementService.getTableName(UserEntity.class)
						+ " T WHERE T.ID_ in (#{userIds})").parameter("userIds", userIds.toString()).list();

		Map<String, User> userMap = new HashMap<String, User>();
		for (User u : activitiUsers) {
			userMap.put(u.getId(), u);
		}

		for (UserDetail u : result.getContent()) {
			User au = userMap.get(u.getUsername());
			if (au != null)
				u.setUser(au);
		}

		return result;
	}

	/**
	 * @see com.pantuo.service.UserServiceInter#queryUserByname(java.lang.String)
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	public List<UserDetail> queryUserByname(String name) {
		QUserDetail q = QUserDetail.userDetail;
		BooleanExpression query = null;
		if (!StringUtils.isEmpty(name)) {
			query = q.username.like("%" + name + "%");
		}
		List<UserDetail> users = (List<UserDetail>) userRepo.findAll(query);
		return users;
	}

	/**
	 * @see com.pantuo.service.UserServiceInter#autoCompleteByName(java.lang.String)
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	public List<AutoCompleteView> autoCompleteByName(String name) {
		//发现工作流中有按用户=查找的 没有模糊查找
		//identityService.createUserQuery().memberOfGroup(SystemRoles.advertiser.name()).
		name = StringUtils.isNoneBlank(name) ? "%" + name.trim() + "%" : null;
		List<String> us = userAutoCompleteMapper.getUserIdLike(name, SystemRoles.advertiser.name());
		//List<UserDetail> list = queryUserByname(name);
		List<AutoCompleteView> r = new ArrayList<AutoCompleteView>();
		if (!us.isEmpty()) {
			for (String u : us) {
				r.add(new AutoCompleteView(u, u));
			}
		}
		return r;
	}
	public  InvoiceView findInvoiceByUser(int invoice_id,Principal principal){
		InvoiceView v = new InvoiceView();
		InvoiceExample example=new InvoiceExample();
		InvoiceExample.Criteria criteria=example.createCriteria();
		criteria.andUserIdEqualTo(Request.getUserId(principal));
		criteria.andIdEqualTo(invoice_id);
		List<Invoice> invoices=invoiceMapper.selectByExample(example);
		if(invoices.size()>0){
			v.setMainView(invoices.get(0));
			List<Attachment> files = attachmentService.queryinvoiceF(principal, invoices.get(0).getId());
			v.setFiles(files);
			return v;
		}
		return null;
	}
	/**
	 * @see com.pantuo.service.UserServiceInter#getByUsername(java.lang.String)
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	public UserDetail getByUsername(String username) {
		List<UserDetail> users = userRepo.findByUsername(username);
		if (users.isEmpty())
			return null;

		UserDetail user = users.get(0);
		//fetch and fill info from activiti user table
		User activitiUser = identityService.createUserQuery().userId(username).singleResult();
		/**
		 * 查用户组
		 */
		List<Group> listGroup = identityService.createGroupQuery().groupMember(username).list();
		user.setUser(activitiUser);
		user.setGroups(listGroup);

		return user;
	}

	public UserDetail getByUsernameSafe(String username) {
		UserDetail u = getByUsername(username);
		if (u != null && u.getUser() != null) {
			u.getUser().setPassword(StringUtils.EMPTY);
		}
		return u;
	}
	/**
	 * @see com.pantuo.service.UserServiceInter#addUserMailReset(com.pantuo.dao.pojo.UserDetail, javax.servlet.http.HttpServletRequest)
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	public Pair<Boolean, String> addUserMailReset(UserDetail u, HttpServletRequest request) {
		String md5 = GlobalMethods.md5Encrypted(u.getUser().getId().getBytes());
		if (StringUtils.isBlank(u.getUser().getEmail())) {
			return new Pair<Boolean, String>(false, "用户未填写邮箱信息,无法通过邮件找回请联系管理员");
		}

		Mail mail = new Mail();
		mail.setTo(u.getUser().getEmail());
		mail.setFrom("ad_system@163.com");// 你的邮箱  
		mail.setHost("smtp.163.com");
		mail.setUsername("ad_system@163.com");// 用户  
		mail.setPassword("pantuo");// 密码  
		mail.setSubject("[北巴广告交易系统]找回您的账户密码");
		mail.setContent(getMailTemplete(
				u.getUser().getLastName(),
				String.format(StringUtils.trim("http://127.0.0.1:8080/webapp/user/reset_pwd?userId=%s&uuid=%s"),
						u.getUsername(), md5), request));
		Pair<Boolean, String> resultPair = null;
		String email = u.getUser().getEmail();
		String regex = "(\\w{3})(\\w+)(\\w{3})(@\\w+)";
		String mailto = email.replaceAll(regex, "$1..$3$4");
		if (mail.sendMail()) {
			resultPair = new Pair<Boolean, String>(true, "您的申请已提交成功，请查看您的" + mailto + "邮箱。");
		} else {
			resultPair = new Pair<Boolean, String>(false, "往" + mailto + "发邮件操作失败，轻稍后重新尝试！");
		}
		return resultPair;
	}

	/**
	 * @see com.pantuo.service.UserServiceInter#getMailTemplete(java.lang.String, java.lang.String, javax.servlet.http.HttpServletRequest)
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	public String getMailTemplete(String userId, String resetPwd, HttpServletRequest request) {
		StringWriter swriter = new StringWriter();
		try {
			//			String xmlTemplete = FileHelper.getAbosluteDirectory("/WEB-INF/ftl/mail_templete");
			String xmlTemplete = request.getSession().getServletContext().getRealPath("/WEB-INF/ftl/mail_templete");
			FreeMarker hf = new FreeMarker();
			hf.init(xmlTemplete);
			Map<String, String> map = new HashMap<String, String>();
			map.put("userName", userId);
			map.put("resetUrl", resetPwd);
			hf.process(map, "mail_templete.ftl", swriter);
		} catch (Exception e) {
			log.error(e.toString());
		}
		return swriter.toString();
	}

	/**
	 * @see com.pantuo.service.UserServiceInter#updatePwd(java.lang.String, java.lang.String)
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	public Pair<Boolean, String> updatePwd(String userId, String psw) throws Exception {
		User activitiUser = identityService.createUserQuery().userId(userId).singleResult();
		if (activitiUser == null) {
			return new Pair<Boolean, String>(false, "用户不存在");
		} else {
			activitiUser.setPassword(psw);
			identityService.saveUser(activitiUser);
			return new Pair<Boolean, String>(true, "修改密码成功");
		}
	}

	/**
	 * @see com.pantuo.service.UserServiceInter#getUserGroupList(com.pantuo.dao.pojo.UserDetail)
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	public List<String> getUserGroupList(UserDetail u) {
		List<String> list = new ArrayList<String>();
		if (u != null) {
			for (Group group : u.getGroups()) {
				list.add(group.getId());
			}
		}
		return list;
	}

	/*	public void test() {
			JpaSysConfig g = sysConfigMapper.selectByPrimaryKey(1);
			if (g != null) {
				System.out.println(g.getKeyCode());
			}
			//  List<UserDetail> u=  userRepo.findByIdarea(1);
			//  System.out.println(u);
		}*/

	/**
	 * @see com.pantuo.service.UserServiceInter#findByUsername(java.lang.String)
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	public UserDetail findByUsername(String username) {
		List<UserDetail> users = userRepo.findByUsername(username);
		if (users.isEmpty()) {
			log.info("Fail to find user with username {}", username);
			return null;
		}
		UserDetail user = users.get(0);
		try {
			org.activiti.engine.identity.User activitiUser = identityService.createUserQuery().userId(username)
					.singleResult();
			if (activitiUser != null)
				user.setUser(activitiUser);
		} catch (Exception e) {
			log.info("Fail to find activiti user with username {}", username, e);
		}
		return user;
	}

	/**
	 * @see com.pantuo.service.UserServiceInter#findDetailByUsername(java.lang.String)
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	public UserDetail findDetailByUsername(String username) {
		List<UserDetail> users = userRepo.findByUsername(username);
		if (users.isEmpty()) {
			log.info("Fail to find user with username {}", username);
			return null;
		}
		UserDetail user = users.get(0);
		return user;
	}

	/**
	 * @see com.pantuo.service.UserServiceInter#saveDetail(com.pantuo.dao.pojo.UserDetail)
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	public void saveDetail(UserDetail user) {
		userRepo.save(user);
	}

	/**
	 * @see com.pantuo.service.UserServiceInter#createUser(com.pantuo.dao.pojo.UserDetail)
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	@Transactional
	public boolean createUser(UserDetail user) {
		if (user.getUser() != null) {
			userRepo.save(user);
			identityService.saveUser(user.getUser());
			if (user.getGroups() != null) {
				for (Group g : user.getGroups()) {
					identityService.createMembership(user.getUser().getId(), g.getId());
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * @see com.pantuo.service.UserServiceInter#updateUserFromPage(com.pantuo.dao.pojo.UserDetail)
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	@Transactional
	public  Pair<Boolean, String> updateUserFromPage(UserDetail user,Principal principal,
			HttpServletRequest request) {
		user.buildMySelf();
		UserDetail dbUser = getByUsername(user.getUsername());
		if (dbUser == null) {
			return new  Pair<Boolean, String>(false,"用户不存在");
		}
		if(!Request.hasOnlyAuth(principal, ActivitiConfiguration.ADVERTISER) &&
				!StringUtils.equals(user.getUsername(), Request.getUserId(principal))){
		      if (user.getGroups() == null || user.getGroups().isEmpty()) {
				return new  Pair<Boolean, String>(false,"请至少选择一个分组");
			  } else if (user.getUser() != null) {
				int dbId = dbUser.getId();
				List<Group> existGroup = dbUser.getGroups();
				BeanUtils.copyProperties(user, dbUser);
				dbUser.setId(dbId);
				userRepo.save(dbUser);//先更新user_detail 信息
				org.activiti.engine.identity.User activitiUser = identityService.createUserQuery()
						.userId(dbUser.getUsername()).singleResult();
				activitiUser.setEmail(user.getEmail());
				activitiUser.setFirstName(user.getFirstName());
				activitiUser.setLastName(user.getLastName());
				identityService.saveUser(activitiUser);//更新工作流中的user表
				//先删除原工作流中的用户权限 只增加不能减
				for (Group g : existGroup) {
					identityService.deleteMembership(dbUser.getUsername(), g.getId());
				}
				//再重建用户的权限 
				for (Group g : user.getGroups()) {
					identityService.createMembership(dbUser.getUsername(), g.getId());
				}

				 return new  Pair<Boolean, String>(true,"保存成功");
			}
			return new  Pair<Boolean, String>(false,"保存失败");
		}
		else{
		  if(!StringUtils.equals(user.getUsername(), Request.getUserId(principal))){
			return new  Pair<Boolean, String>(false,"操作非法");
		  }
		  else if (user.getUser() != null) {
			int dbId = dbUser.getId();
			BeanUtils.copyProperties(user, dbUser);
			dbUser.setId(dbId);
			userRepo.save(dbUser);//先更新user_detail 信息
			org.activiti.engine.identity.User activitiUser = identityService.createUserQuery()
					.userId(dbUser.getUsername()).singleResult();
			activitiUser.setEmail(user.getEmail());
			activitiUser.setFirstName(user.getFirstName());
			activitiUser.setLastName(user.getLastName());
			identityService.saveUser(activitiUser);//更新工作流中的user表
			suppliesService.savequlifi(principal, request, null);
		    return new  Pair<Boolean, String>(true,"保存成功");
		}
		return new  Pair<Boolean, String>(false,"保存失败");
		}
	}

	/**
	 * @see com.pantuo.service.UserServiceInter#createUserFromPage(com.pantuo.dao.pojo.UserDetail)
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	@Transactional
	public boolean createUserFromPage(UserDetail user) {
		user.buildMySelf();
		UserDetail dbUser = findDetailByUsername(user.getUsername());
		if (dbUser != null) {
			user.setErrorInfo(BaseEntity.ERROR, "登录名已经存在!");
		} else if (user.getUser() != null) {
			com.pantuo.util.BeanUtils.filterXss(user);
			user.setUstats(UStats.init);
			userRepo.save(user);
			identityService.saveUser(user.getUser());
			if (user.getGroups() != null) {
				for (Group g : user.getGroups()) {
					identityService.createMembership(user.getUser().getId(), g.getId());
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * @see com.pantuo.service.UserServiceInter#deleteUser(java.lang.String)
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	@Transactional
	public boolean deleteUser(String username) {
		List<UserDetail> users = userRepo.findByUsername(username);
		if (users.isEmpty())
			return false;

		identityService.deleteUser(username);

		for (UserDetail u : users) {
			userRepo.delete(u);
		}
		return true;
	}

	/**
	 * @see com.pantuo.service.UserServiceInter#deleteGroups(java.util.List)
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	@Transactional
	public boolean deleteGroups(List<String> groups) {
		for (String g : groups) {
			identityService.deleteGroup(g);
		}
		return true;
	}

	/**
	 * @see com.pantuo.service.UserServiceInter#saveGroup(org.activiti.engine.identity.Group)
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	@Transactional
	public boolean saveGroup(Group group) {
		if (group != null) {
			identityService.saveGroup(group);
			return true;
		}
		return false;
	}

	public boolean isUserHaveGroup(String username, String group) {
		List<Group> listGroup = identityService.createGroupQuery().groupMember(username).list();
		boolean r = false;
		if (!listGroup.isEmpty()) {
			for (Group g : listGroup) {
				if (StringUtils.equals(group, g.getId())) {
					r = true;
					break;
				}
			}
		}
		return r;
	}

	@Override
	public Pair<Boolean, String> delInvoice(int invoice_id, Principal principal) {
		  InvoiceExample example=new InvoiceExample();
		  InvoiceExample.Criteria c=example.createCriteria();
		  c.andIdEqualTo(invoice_id);
		  c.andUserIdEqualTo(Request.getUserId(principal));
			int a=invoiceMapper.deleteByExample(example);
			if(a>0){
//				AttachmentExample example2=new AttachmentExample();
//				AttachmentExample.Criteria criteria2=example2.createCriteria();
//				AttachmentExample.Criteria criteria3=example2.createCriteria();
//				AttachmentExample.Criteria criteria4=example2.createCriteria();
//			    criteria2.andMainIdEqualTo(invoice_id);
//			    criteria2.andTypeEqualTo(6);
//			    criteria3.andMainIdEqualTo(invoice_id);
//			    criteria3.andTypeEqualTo(7);
//			    criteria4.andMainIdEqualTo(invoice_id);
//			    criteria4.andTypeEqualTo(8);
//			    example2.or(criteria3);
//			    example2.or(criteria4);
//			    List<Attachment> attas=attachmentMapper.selectByExample(example2);
//			    for (Attachment attachment : attas) {
//			    	if(attachment!=null){
//			    		attachmentMapper.deleteByPrimaryKey(attachment.getId());
//			    	}
//				}
		    	return	new Pair<Boolean, String>(true, "删除发票成功！");
			}
			return	new Pair<Boolean, String>(true, "删除发票失败！");
	}

	
	@Override
	public List<Invoice> queryInvoiceByUser(int cityId, Principal principal) {
		InvoiceExample example=new InvoiceExample();
		InvoiceExample.Criteria criteria=example.createCriteria();
		criteria.andCityEqualTo(cityId);
		criteria.andUserIdEqualTo(Request.getUserId(principal));
    	return invoiceMapper.selectByExample(example);
	}
	@Override
	public Pair<Boolean, String> loginForLayer(HttpServletRequest request, String name, String pwd) {
		Pair<Boolean, String> p = new Pair<>(false, request.getHeader("referer"));
		if (StringUtils.isBlank(name)) {
			p.setRight("请填写登录名!");
		} else if (StringUtils.isBlank(pwd)) {
			p.setRight("请输入密码!");
		} else {

			UserDetail udetail = getByUsername(name);
			if (udetail == null || udetail.getUser() == null) {
				p.setRight(name + " 用户不存在!");
			} else {
				if (!StringUtils.equals(udetail.getUser().getPassword(), pwd)) {
					p.setRight("密码不对,请重新登录!");
				} else {
					p.setLeft(true);
					UserDetails newUser = new ActivitiUserDetails(udetail);
					Authentication auth = new UsernamePasswordAuthenticationToken(newUser, newUser.getPassword(),
							newUser.getAuthorities());
					SecurityContextHolder.getContext().setAuthentication(auth);
				}
			}
		}
		return p;
	}
}
