package com.pantuo.service;

import java.io.StringWriter;
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
import org.springframework.stereotype.Service;

import com.mysema.query.types.expr.BooleanExpression;
import com.pantuo.dao.UserDetailRepository;
import com.pantuo.dao.pojo.BaseEntity;
import com.pantuo.dao.pojo.QUserDetail;
import com.pantuo.dao.pojo.UserDetail;
import com.pantuo.util.Constants;
import com.pantuo.util.FileHelper;
import com.pantuo.util.FreeMarker;
import com.pantuo.util.Mail;
import com.pantuo.util.Pair;

/**
 * @author tliu
 */
@Service
public class UserService {
    private static Logger log = LoggerFactory.getLogger(UserService.class);
	@Autowired
	private UserDetailRepository userRepo;

/*	@Autowired
	private SysConfigMapper sysConfigMapper;

	@Autowired
	private UserRoleRepository roleRepo;

	public UserDetailRepository getUserRepo() {
		return userRepo;
	}

	public UserRoleRepository getRoleRepo() {
		return roleRepo;
	}*/

    @Autowired
    private IdentityService identityService;

    @Autowired
    private ManagementService managementService;

    public long count() {
        return userRepo.count();
    }

    public long countGroups() {
        return identityService.createGroupQuery().count();
    }

	public Page<UserDetail> getAllUsers(String name, int page, int pageSize, Sort order) {
        return getUsers(name, null, page, pageSize, order);
    }

    public Page<UserDetail> getValidUsers(int page, int pageSize, Sort order) {
        return getUsers(null, true, page, pageSize, order);
    }
    public List<Group> getAllGroup(){
    	return  identityService.createGroupQuery().list();
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
                query = (query == null? q.enabled.eq(isEnabled) : query.and(q.enabled.eq(isEnabled)));
            }
            result = userRepo.findAll(query, p);
        }

        //fetch and fill info from activiti user table
        StringBuffer userIds = new StringBuffer ();
        for (UserDetail u : result.getContent()) {
            List<Group> groups = identityService.createGroupQuery().groupMember(u.getUsername()).list();
            u.setGroups(groups);
            userIds.append("'").append(u.getUsername()).append("',");
        }
        if (userIds.length() > 0)
            userIds.setLength( userIds.length() - 1);

        List<User> activitiUsers = identityService.createNativeUserQuery()
                .sql("SELECT * FROM " + managementService.getTableName(UserEntity.class) + " T WHERE T.ID_ in (#{userIds})")
                .parameter("userIds", userIds.toString())
                .list();

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
    public Pair<Boolean, String> addUserMailReset(UserDetail u,HttpServletRequest request) {
//		String md5 = GlobalMethods.md5Encrypted(System.currentTimeMillis().getBytes());
		String md5 ="";
		if (StringUtils.isBlank(u.getUser().getEmail())) {
			return new Pair<Boolean, String>(false, "用户未填写邮箱信息,无法通过邮件找回请联系管理员");
		}

		Mail mail = new Mail();
		mail.setTo(u.getUser().getEmail());
		mail.setFrom("ad_system@163.com");// 你的邮箱  
		mail.setHost("smtp.163.com");
		mail.setUsername("ad_system@163.com");// 用户  
		mail.setPassword("pantuo");// 密码  
		mail.setSubject("[北巴]找回您的账户密码");
		mail.setContent(getMailTemplete(u.getLastName(),
				String.format(StringUtils.trim("http://127.0.0.1:8080/webapp/user/reset_pwd?userId=%s&uuid=%s"), u.getUsername(), md5),request));
		Pair<Boolean, String> resultPair = null;
		String email =u.getUser().getEmail(); 
		String regex = "(\\w{3})(\\w+)(\\w{3})(@\\w+)";
		String mailto = email.replaceAll(regex, "$1..$3$4");
		if (mail.sendMail()) {
			resultPair = new Pair<Boolean, String>(true, "您的申请已提交成功，请查看您的" + mailto + "邮箱。");
		} else {
			resultPair = new Pair<Boolean, String>(false, "往" + mailto + "发邮件操作失败，轻稍后重新尝试！");
		}
		return resultPair;
	}
    
    public String getMailTemplete(String userId, String resetPwd,HttpServletRequest request) {
		StringWriter swriter = new StringWriter();
		try {
//			String xmlTemplete = FileHelper.getAbosluteDirectory("/WEB-INF/ftl/mail_templete");
			String xmlTemplete=request.getSession().getServletContext().getRealPath("/WEB-INF/ftl/mail_templete");
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
    public Pair<Boolean, String> updatePwd(String userId, String psw) throws Exception {
    		 User activitiUser = identityService.createUserQuery().userId(userId).singleResult();
    		 if(activitiUser==null){
    	    		return new Pair<Boolean, String>(false,"用户不存在");
    	    	}else{
    	    		activitiUser.setPassword(psw);
    	    		identityService.saveUser(activitiUser);
    	    		return new Pair<Boolean, String>(true,"修改密码成功");
    	    	}
	}
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

    public UserDetail findByUsername (String username) {
        List<UserDetail> users = userRepo.findByUsername(username);
        if (users.isEmpty()) {
            log.info("Fail to find user with username {}", username);
            return null;
        }
        UserDetail user = users.get(0);
        try {
            org.activiti.engine.identity.User activitiUser = identityService.createUserQuery().userId(username).singleResult();
            if (activitiUser != null)
                user.setUser(activitiUser);
        } catch (Exception e) {
            log.info("Fail to find activiti user with username {}", username, e);
        }
        return user;
    }

    public UserDetail findDetailByUsername (String username) {
        List<UserDetail> users = userRepo.findByUsername(username);
        if (users.isEmpty()) {
            log.info("Fail to find user with username {}", username);
            return null;
        }
        UserDetail user = users.get(0);
        return user;
    }


    public void saveDetail(UserDetail user) {
        userRepo.save(user);
    }

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
    @Transactional
	public boolean updateUserFromPage(UserDetail user) {
		user.buildMySelf();
		UserDetail dbUser = getByUsername(user.getUsername());
		if (dbUser == null) {
			user.setErrorInfo(BaseEntity.ERROR, "用户不存在");
		} else if (user.getGroups() == null || user.getGroups().isEmpty()) {
			user.setErrorInfo(BaseEntity.ERROR, "用户需要设置相应的归属组");
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
			
			return true;
		}
		return false;
	}
       
    

    @Transactional
	public boolean createUserFromPage(UserDetail user) {
		user.buildMySelf();
		UserDetail dbUser = findDetailByUsername(user.getUsername());
		if (dbUser != null) {
			user.setErrorInfo(BaseEntity.ERROR, "登录名已经存在!");
		} else if (user.getUser() != null) {
			com.pantuo.util.BeanUtils.filterXss(user);
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

    @Transactional
    public boolean deleteGroups(List<String> groups) {
        for (String g : groups) {
            identityService.deleteGroup(g);
        }
        return true;
    }

    @Transactional
    public boolean saveGroup(Group group) {
        if (group != null) {
            identityService.saveGroup(group);
            return true;
        }
        return false;
    }
}
