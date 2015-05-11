package com.pantuo.dao.pojo;

import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.UserEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tliu
 *
 * 用户
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"username"}))
public class UserDetail extends BaseEntity {
    /**
	 * Comment here.
	 *
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	private static final long serialVersionUID = -1842498354096493867L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Transient
    public User user;
    @Transient
    public List<Group> groups = new ArrayList<Group>();
    public String username;
    public boolean enabled = true;
    
    //工作流表 有user_info表 发现key-vale 但是没有提供一次获取所有info信息
    
    //用户昵称，登录名，真实姓名，密码，邮箱  这里信息放在 ACT_ID_USER
    //电话，权限（勾选），所属公司，所属部门。
    
    
	public String phone;
	public String company;
	public String department;
	
	//以下是页面表单值 
	@Transient
	public String password;
	@Transient
	public String firstName = "";
	@Transient
	public String lastName = "";
	@Transient
	public String email;
	@Transient
	public List<String> roles;

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    private List<JpaCity> cities;

	public void buildMySelf() {
        user = new UserEntity(username);
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        if (roles != null)
            setStringGroups(roles);
	}

    public UserDetail(String username, String password, String firstName, String lastName, String email) {
        this.username = username;
        user = new UserEntity(username);
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
    }

    public UserDetail (User user) {
        this.user = user;
        this.username = user.getId();
    }

    public UserDetail() {
        super();
    }

    public List<Group> getGroups () {
        return groups;
    }

    public void setStringGroups(List<String> groups) {
        for (String group : groups) {
            this.groups.add(new GroupEntity(group));
        }
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<JpaCity> getCities() {
        return cities;
    }

    public void setCities(List<JpaCity> cities) {
        this.cities = cities;
    }

    @Override
    public String toString() {
        return "UserDetail [username=" + username + ", password=******, enabled=" + enabled + "]";
    }

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}




}