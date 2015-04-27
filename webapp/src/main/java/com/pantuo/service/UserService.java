package com.pantuo.service;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.BooleanOperation;
import com.pantuo.dao.UserDetailRepository;
import com.pantuo.dao.pojo.BaseEntity;
import com.pantuo.dao.pojo.QUserDetail;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.GroupQuery;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;



//import com.pantuo.dao.pojo.QUser;
import com.pantuo.dao.pojo.UserDetail;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
			BeanUtils.copyProperties(user, dbUser);
			dbUser.setId(dbId);
			userRepo.save(dbUser);
			org.activiti.engine.identity.User activitiUser = identityService.createUserQuery()
					.userId(dbUser.getUsername()).singleResult();
			activitiUser.setEmail(user.getEmail());
			activitiUser.setFirstName(user.getFirstName());
			activitiUser.setLastName(user.getLastName());
			identityService.saveUser(activitiUser);
			for (Group g : dbUser.getGroups()) {
				identityService.deleteMembership(dbUser.getUsername(), g.getId());
			}
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
