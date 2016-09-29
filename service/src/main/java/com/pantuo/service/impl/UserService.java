package com.pantuo.service.impl;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
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
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.google.common.collect.Lists;
import com.mysema.query.types.expr.BooleanExpression;
import com.pantuo.ActivitiConfiguration;
import com.pantuo.dao.CustomerHistoryRepository;
import com.pantuo.dao.InvoiceRepository;
import com.pantuo.dao.UserDetailRepository;
import com.pantuo.dao.pojo.BaseEntity;
import com.pantuo.dao.pojo.JpaCustomerHistory;
import com.pantuo.dao.pojo.JpaInvoice;
import com.pantuo.dao.pojo.JpaVideo32OrderStatus;
import com.pantuo.dao.pojo.QJpaCustomerHistory;
import com.pantuo.dao.pojo.QJpaInvoice;
import com.pantuo.dao.pojo.QUserDetail;
import com.pantuo.dao.pojo.UserDetail;
import com.pantuo.dao.pojo.UserDetail.UStats;
import com.pantuo.dao.pojo.UserDetail.UType;
import com.pantuo.mybatis.domain.Attachment;
import com.pantuo.mybatis.domain.Invoice;
import com.pantuo.mybatis.domain.InvoiceExample;
import com.pantuo.mybatis.persistence.AttachmentMapper;
import com.pantuo.mybatis.persistence.InvoiceMapper;
import com.pantuo.mybatis.persistence.UserAutoCompleteMapper;
import com.pantuo.pojo.TableRequest;
import com.pantuo.service.ActivitiService.SystemRoles;
import com.pantuo.service.AttachmentService;
import com.pantuo.service.GoupManagerService;
import com.pantuo.service.MailService;
import com.pantuo.service.SuppliesService;
import com.pantuo.service.UserServiceInter;
import com.pantuo.service.security.ActivitiUserDetails;
import com.pantuo.service.security.ActivitiUserDetailsService;
import com.pantuo.service.security.Request;
import com.pantuo.util.BeanUtils;
import com.pantuo.util.JsonTools;
import com.pantuo.util.Pair;
import com.pantuo.util.ShortString;
import com.pantuo.web.view.AutoCompleteView;
import com.pantuo.web.view.InvoiceView;
import com.pantuo.web.view.UserQualifiView;

/**
 * @author tliu
 */
@Service

public class UserService implements UserServiceInter {
	private static Logger log = LoggerFactory.getLogger(UserService.class);
	@Autowired
	private UserDetailRepository userRepo;
	@Autowired
	private CustomerHistoryRepository customerHistoryRepository;

	@Autowired
	private IdentityService identityService;
	

	@Autowired
	private GoupManagerService goupManagerService;
	
	@Autowired
	private MailService mailService;
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
	InvoiceRepository invoiceRepository;
	@Autowired
	AttachmentMapper attachmentMapper;

	@Autowired
	private ActivitiUserDetailsService authUserService;
	
	
	static Map<String,String> USERDETAIL_FIELD=new HashMap<String, String>();
	static {
		USERDETAIL_FIELD.put("phone", "联系电话");
		USERDETAIL_FIELD.put("company", "公司名称");
		USERDETAIL_FIELD.put("companyAddr", "公司地址");
		USERDETAIL_FIELD.put("legalman", "法定代表人");
		USERDETAIL_FIELD.put("relateman", "联系人");
		USERDETAIL_FIELD.put("zipCode", "邮编");
	}

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
	public Page<UserDetail> getAllUsers(String utype,String name, int page, int pageSize, Sort order) {
		return getUsers(  utype,name, null, page, pageSize, order,null,null);
	}

	/**
	 * @see com.pantuo.service.UserServiceInter#getValidUsers(int, int, org.springframework.data.domain.Sort)
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	public Page<UserDetail> getValidUsers(String utype,int page, int pageSize, Sort order) {
		return getUsers(  utype,null, true, page, pageSize, order,null,null);
	}

	/**
	 * @see com.pantuo.service.UserServiceInter#getAllGroup()
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	public List<Group> getAllGroup() {
		return identityService.createGroupQuery().list();
	}

	public Page<UserDetail> getUsers(String utype, String name, Boolean isEnabled, int page, int pageSize,
			Sort order,UType loginUserType,String ustats) {
		if (page < 0)
			page = 0;
		if (pageSize < 1)
			pageSize = 1;
		UStats ustatsQuery =null;
		try {
			  ustatsQuery = UStats.valueOf(ustats);
		} catch (Exception e) {
		}

		Page<UserDetail> result = null;
		Pageable p = new PageRequest(page, pageSize, (order == null ? new Sort("id") : order));

		if (StringUtils.isEmpty(name) && isEnabled == null) {
			QUserDetail q = QUserDetail.userDetail;
			if (utype != null) {
				BooleanExpression query = null;
				UserDetail.UType u =   UserDetail.UType.valueOf(utype);
				if(u == UType.body){
					query = (query == null ? q.utype.eq(UType.body) : query.and(q.utype.eq(UType.body)));
				}else if(u==UType.screen){
					query = (query == null ? q.utype.eq(UType.screen) : query.and(q.utype.ne(UType.screen)));
				}else if(u==UType.pub){
					query = (query == null ? q.utype.eq(UType.pub) : query.and(q.utype.ne(UType.pub)));
				}
				if (ustatsQuery != null ) {
					query = (query == null ? q.ustats.eq(ustatsQuery) : query.and(q.ustats.eq(ustatsQuery)));
				}
				query = (query == null ? q.createBySales.isNull()  : query.and(q.createBySales.isNull() ));
				result = userRepo.findAll(query, p);
			} else {
				if(loginUserType!=null){
					BooleanExpression query = null;
					if(loginUserType==UType.body){
						query = (query == null ? q.utype.eq(UType.body) : query.and(q.utype.eq(UType.body)));
						result = userRepo.findAll(query, p);
					}else if(loginUserType==UType.screen){
						query = (query == null ? q.utype.ne(UType.body) : query.and(q.utype.ne(UType.body)));
						query = (query == null ? q.createBySales.isNull()  : query.and(q.createBySales.isNull() ));
						result = userRepo.findAll(query, p);
					}
					if (ustatsQuery != null) {
						query = (query == null ? q.ustats.eq(ustatsQuery) : query.and(q.ustats.eq(ustatsQuery)));
					}
					
				}else {
					result = userRepo.findAll(p);	
				}
				
			}
		} else { 
			QUserDetail q = QUserDetail.userDetail;
			BooleanExpression query = null;
			if (!StringUtils.isEmpty(name)) {
				query = q.username.like("%" + name + "%");
			}
			if (isEnabled != null) {
				query = (query == null ? q.enabled.eq(isEnabled) : query.and(q.enabled.eq(isEnabled)));
			}
			if (utype != null) {
				UserDetail.UType u = utype == null ? UserDetail.UType.pub : UserDetail.UType.valueOf(utype);
				
				
				if(u == UType.body){
					query = (query == null ? q.utype.eq(UType.body) : query.and(q.utype.eq(UType.body)));
				}else if(u==UType.screen){
					query = (query == null ? q.utype.ne(UType.body) : query.and(q.utype.ne(UType.body)));
				}else if(u==UType.pub){
					query = (query == null ? q.utype.eq(UType.pub) : query.and(q.utype.eq(UType.pub)));
				}
				if (ustatsQuery != null) {
					query = (query == null ? q.ustats.eq(ustatsQuery) : query.and(q.ustats.eq(ustatsQuery)));
				}

			}
			query = (query == null ? q.createBySales.isNull()  : query.and(q.createBySales.isNull() ));
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
	
	@Override
	public Page<JpaCustomerHistory> getCustomerHistory(TableRequest req, Principal principal) {
		String userId = req.getFilter("userId");
		int page = req.getPage(), pageSize = req.getLength();
		Sort sort = req.getSort("id");
		
		if (page < 0)
			page = 0;
		if (pageSize < 1)
			pageSize = 1;
		sort = (sort == null ? new Sort("id") : sort);
		Pageable p = new PageRequest(page, pageSize, sort);
		int id=0;
		if(StringUtils.isNotBlank(userId)){
			 id=NumberUtils.toInt(userId);
		}
		BooleanExpression query = QJpaCustomerHistory.jpaCustomerHistory.userDetailId.eq(id);
		return customerHistoryRepository.findAll(query, p);
	}

	public Page<UserDetail> getClientUser(TableRequest req, Principal principal) {
		String company = req.getFilter("company"),relateMan=req.getFilter("relateMan"),salesMan=req.getFilter("salesMan");
		int page = req.getPage(), pageSize = req.getLength();
		Sort sort = req.getSort("id");
		
		if (page < 0)
			page = 0;
		if (pageSize < 1)
			pageSize = 1;
		sort = (sort == null ? new Sort("id") : sort);
		Pageable p = new PageRequest(page, pageSize, sort);
		BooleanExpression query = QUserDetail.userDetail.createBySales.isNotNull();
		if(!Request.hasAuth(principal, ActivitiConfiguration.SALESMANAGER)){
			query = query.and(QUserDetail.userDetail.createBySales.eq(Request.getUserId(principal)));
		}
		if (StringUtils.isNotBlank(company)) {
			query = query.and(QUserDetail.userDetail.company.like("%"+company+"%"));
		}
		if (StringUtils.isNotBlank(relateMan)) {
			query = query.and(QUserDetail.userDetail.relateman.like("%"+relateMan+"%"));
		}
		if (StringUtils.isNotBlank(salesMan)) {
			query = query.and(QUserDetail.userDetail.createBySales.eq(salesMan));
		}
	
		return userRepo.findAll(query, p);
	}
	

	@Override
	public List<String> salesManAutoComplete(int city, String name) {
		if(StringUtils.isBlank(name)){
			name=null;
		}
		List<String> list= userAutoCompleteMapper.getUserIdLike(name, "sales");
		return list;
	}

	@Override
	public Pair<Boolean, String> saveClientUser(UserDetail userDetail, UserQualifiView userQualifiView,
			HttpServletRequest request, Principal principal) {
		try {
			if (null != userQualifiView) {
				userDetail.setQulifijsonstr(JsonTools.getJsonFromObject(userQualifiView));
			}
			
			if (userDetail.getId() < 1) {
				userDetail.setUsername(ShortString.getRandomString(8).toLowerCase());
				userDetail.setCreated(new Date());
				userDetail.setCreateBySales(Request.getUserId(principal));
			}

			BooleanExpression query = QUserDetail.userDetail.id.eq(userDetail.getId());
			UserDetail source = userRepo.findOne(query);
			//add change history by impanxh
			if (source != null) {
				userDetail.setCreateBySales(source.getCreateBySales());
				//----------
				//原copy一份 
				UserDetail sourceCopy = new UserDetail();
				org.apache.commons.beanutils.BeanUtils.copyProperties(sourceCopy, source);
				//得到变化的字段
				List<String> changeField = BeanUtils.copyPropertiesReturnChangeField(userDetail, source);

				for (String filed : changeField) {//变量中文转义
					if (USERDETAIL_FIELD.containsKey(filed)) {
						Date now = new Date();
						JpaCustomerHistory obj = new JpaCustomerHistory();
						obj.setNewValue(org.apache.commons.beanutils.BeanUtils.getProperty(userDetail, filed));
						obj.setOldValue(org.apache.commons.beanutils.BeanUtils.getProperty(sourceCopy, filed));
						obj.setOperationUser(Request.getUserId(principal));
						obj.setUpdated(now);
						obj.setCreated(now);
						obj.setFieldViewName(USERDETAIL_FIELD.get(filed));
						obj.setUserDetailId(userDetail.getId());
						customerHistoryRepository.save(obj);
						log.info(filed + "#" + org.apache.commons.beanutils.BeanUtils.getProperty(sourceCopy, filed)
								+ "#" + org.apache.commons.beanutils.BeanUtils.getProperty(userDetail, filed));

					}
				}
			}
			userRepo.save(userDetail);
		} catch (Exception e) {
			log.error("store-customer-ex", e);
		}
		return new Pair<Boolean, String>(true, "保存成功");
	}

	@Override
	public Pair<Boolean, String> saveClientInvoice(JpaInvoice jpaInvoice, UserQualifiView userQualifiView,
			HttpServletRequest request, Principal principal) {
		try {
			if (null != userQualifiView) {
				jpaInvoice.setQulifijsonstr(JsonTools.getJsonFromObject(userQualifiView));
			}
			jpaInvoice.setCreated(new Date());
			jpaInvoice.setType(JpaInvoice.Type.special);
			invoiceRepository.save(jpaInvoice);
			}
          catch (Exception e) {
		}
		return new Pair<Boolean, String>(true,"保存成功");
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
	
	public List<AutoCompleteView> salesAutoCompleteByName(String name) {
		name = StringUtils.isNoneBlank(name) ? "%" + name.trim() + "%" : null;
		List<String> us = userAutoCompleteMapper.getSalesIdLike(name, "sales");
		List<AutoCompleteView> r = new ArrayList<AutoCompleteView>();
		if (!us.isEmpty()) {
			for (String u : us) {
				r.add(new AutoCompleteView(u, u));
			}
		}
		return r;
	}

	public List<AutoCompleteView> queryMyCustomers(String name, Principal principal) {
		List<AutoCompleteView> r = new ArrayList<AutoCompleteView>();
		Iterable<UserDetail> list = null;
		 if (Request.hasAuth(principal, "salesManager")) {
			list = userRepo.findAll(QUserDetail.userDetail.createBySales.isNotNull());

		}
		 else if (Request.hasAuth(principal, "sales")) {
			list = userRepo.findAll(QUserDetail.userDetail.createBySales.eq(Request.getUserId(principal)));

		} 
		if (list != null) {
			for (UserDetail u : list) {
				r.add(new AutoCompleteView(u.getCompany(), u.getCompany(), String.valueOf(u.getUsername())));
			}
		}
		return r;
	}
	

	public InvoiceView findInvoiceByUser(int invoice_id, Principal principal) {
		InvoiceView v = new InvoiceView();
		InvoiceExample example = new InvoiceExample();
		InvoiceExample.Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(Request.getUserId(principal));
		criteria.andIdEqualTo(invoice_id);
		List<Invoice> invoices = invoiceMapper.selectByExample(example);
		if (invoices.size() > 0) {
			v.setMainView(invoices.get(0));
			List<Attachment> files = attachmentService.queryinvoiceF(principal, invoices.get(0).getId());
			v.setFiles(files);
			return v;
		}
		return null;
	}

	
	@Override
	public JpaInvoice findInvoiceByUserName(String username) {
		BooleanExpression query=QJpaInvoice.jpaInvoice.userId.eq(username);
		List<JpaInvoice> list=(List<JpaInvoice>) invoiceRepository.findAll(query);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}

	/**
	 * @see com.pantuo.service.UserServiceInter#getByUsername(java.lang.String)
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	public UserDetail getByUsername(String username) {
		
		List<UserDetail> users = userRepo.findByUsername(username);
		if (users.isEmpty()){
			UserDetail detail = goupManagerService.checkUserHaveGroup(username);
			if (detail != null) {
				return detail;
			}
			return null;
		}
			

		UserDetail user = users.get(0);
		//fetch and fill info from activiti user table
		User activitiUser = identityService.createUserQuery().userId(username).singleResult();
		/**
		 * 查用户组
		 */
		List<Group> listGroup = identityService.createGroupQuery().groupMember(username).list();
		user.setUser(activitiUser);
		user.setGroups(listGroup);
		
		user.setFunctions(goupManagerService.getFunction4UserId(user));
		
		return user;
	}

	public UserDetail getByUsernameSafe(String username) {
		UserDetail u = getByUsername(username);
		if (u != null && u.getUser() != null) {
			u.getUser().setPassword(StringUtils.EMPTY);
		}
		return u;
	}
    

	@Override
	public Pair<Boolean, String> deleteClinent(String username) {
		UserDetail userDetail=findByUsername(username);
		if(userDetail==null){
			return new Pair<Boolean, String>(false,"信息丢失");
		}
		userRepo.delete(userDetail);
		InvoiceExample example=new InvoiceExample();
		example.createCriteria().andUserIdEqualTo(username);
		invoiceMapper.deleteByExample(example);
		return new Pair<Boolean, String>(true,"删除成功");
	}

	@Override
	public List<Integer> gettypeListByAttach(List<Attachment> attachments) {
		List<Integer> list=new ArrayList<Integer>();
       for (Attachment attachment : attachments) {
    	   list.add(attachment.getType());
      }
		return list;
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
	public  List<JpaVideo32OrderStatus.Status>  queryOrderStatus(UserDetail u){
		List<JpaVideo32OrderStatus.Status> sList=Lists.newArrayList();
		List<String> userGoups=getUserGroupList(u);
		for (String one : userGoups) {
			if(JpaVideo32OrderStatus.statusMap.containsKey(one)){
				sList.add(JpaVideo32OrderStatus.statusMap.get(one));
			}
		}
		return sList;
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
	public Pair<Boolean, String> updateUserFromPage(boolean isForceUpdate,UserDetail user, Principal principal, HttpServletRequest request) {
		user.buildMySelf();
		UserDetail dbUser = getByUsername(user.getUsername());
		if (dbUser == null) {
			return new Pair<Boolean, String>(false, "用户不存在");
		}
		if (!Request.hasOnlyAuth(principal, ActivitiConfiguration.ADVERTISER)
				&& !StringUtils.equals(user.getUsername(), Request.getUserId(principal))) {

			if (isForceUpdate) {
				//---管理员修改广告主信息
				updateUser(user, dbUser);
				return new Pair<Boolean, String>(true, "保存成功");
			} else {
				if (user.getGroups() == null || user.getGroups().isEmpty()) {
					return new Pair<Boolean, String>(false, "请至少选择一个分组");
				} else if (user.getUser() != null) {
					List<Group> existGroup = dbUser.getGroups();
					com.pantuo.util.BeanUtils.copyPropertiesFilterZero(user, dbUser);
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

					return new Pair<Boolean, String>(true, "保存成功");
				}
			}
			return new Pair<Boolean, String>(false, "保存失败");
		} else {
			if (!StringUtils.equals(user.getUsername(), Request.getUserId(principal))) {
				return new Pair<Boolean, String>(false, "操作非法");
			} else if (user.getUser() != null) {
				updateUser(user, dbUser);
				//suppliesService.savequlifi(principal, request, null);
				return new Pair<Boolean, String>(true, "保存成功");
			}
			return new Pair<Boolean, String>(false, "保存失败");
		}
	}

	private void updateUser(UserDetail user, UserDetail dbUser) {
		UType u =dbUser.getUtype();//用户类型不能修改
		com.pantuo.util.BeanUtils.copyPropertiesFilterZero(user, dbUser);
		dbUser.setUtype(u);
		userRepo.save(dbUser);//先更新user_detail 信息
		org.activiti.engine.identity.User activitiUser = identityService.createUserQuery()
				.userId(dbUser.getUsername()).singleResult();
		activitiUser.setEmail(user.getEmail());
		activitiUser.setFirstName(user.getFirstName());
		activitiUser.setLastName(user.getLastName());
		identityService.saveUser(activitiUser);//更新工作流中的user表
	}

	@Override
	public Pair<Boolean, String> resetPassW(String userName, Principal principal) {
		org.activiti.engine.identity.User activitiUser = identityService.createUserQuery()
				.userId(userName).singleResult();
		if(activitiUser==null){
			return new Pair<Boolean, String>(false,"用户信息丢失");
		}
		activitiUser.setPassword("123456");
		identityService.saveUser(activitiUser);//更新工作流中的user表
	    return new Pair<Boolean, String>(true,"用户"+userName+"重置密码成功");
	}

	/**
	 * @see com.pantuo.service.UserServiceInter#createUserFromPage(com.pantuo.dao.pojo.UserDetail)
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	@Transactional
	public boolean createUserFromPage(UserDetail user,HttpServletRequest request,Principal principal) {
		user.buildMySelf();
		UserDetail dbUser = findDetailByUsername(user.getUsername());
		if (dbUser != null) {
			user.setErrorInfo(BaseEntity.ERROR, "登录名已经存在!");
		} else if (user.getUser() != null) {
			com.pantuo.util.BeanUtils.filterXss(user);
			user.setUstats(UStats.init);
			if(principal!=null && (Request.hasAuth(principal,"UserManager") ||Request.hasAuth(principal,"body_roleManager"))){
				user.setIsActivate(1);
				userRepo.save(user);
				identityService.saveUser(user.getUser());
			}else{
				user.setIsActivate(0);
				userRepo.save(user);
				identityService.saveUser(user.getUser());
			}
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
		InvoiceExample example = new InvoiceExample();
		InvoiceExample.Criteria c = example.createCriteria();
		c.andIdEqualTo(invoice_id);
		c.andUserIdEqualTo(Request.getUserId(principal));
		int a = invoiceMapper.deleteByExample(example);
		if (a > 0) {
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
			return new Pair<Boolean, String>(true, "删除发票成功！");
		}
		return new Pair<Boolean, String>(true, "删除发票失败！");
	}

	@Override
	public List<Invoice> queryInvoiceByUser(int cityId, Principal principal) {
		InvoiceExample example = new InvoiceExample();
		InvoiceExample.Criteria criteria = example.createCriteria();
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
					request.getSession().setAttribute("_utype", String.valueOf(udetail.getUtype()));
					Authentication auth = new UsernamePasswordAuthenticationToken(newUser, newUser.getPassword(),
							newUser.getAuthorities());
					SecurityContextHolder.getContext().setAuthentication(auth);
				}
			}
		}
		return p;
	}
	/**
	 * 
	 * 取的用户唯一加密md5 中间混淆部分值
	 *
	 * @param uname
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public String getUserUniqCode(String uname) {
		String r = StringUtils.EMPTY;
		if (StringUtils.isNoneBlank(uname + "/md5/" + uname.hashCode())) {
			r = DigestUtils.md5Hex(uname);
		}
		return r;
	}

	@Override
	public Pair<Boolean, String> editPwd(String userId, String oldpassword, String psw) {
		User activitiUser = identityService.createUserQuery().userId(userId).singleResult();
		if (activitiUser == null) {
			return new Pair<Boolean, String>(false, "用户不存在");
		} else {
			if(!StringUtils.equals(oldpassword, activitiUser.getPassword())){
				return new Pair<Boolean, String>(false, "原密码不正确");
			}
			activitiUser.setPassword(psw);
			identityService.saveUser(activitiUser);
			return new Pair<Boolean, String>(true, "修改密码成功");
		}
	}
}
