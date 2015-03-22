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
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Transient
    public User user;
    @Transient
    public List<Group> groups = new ArrayList<Group>();
    public String username;
    public boolean enabled = true;

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

    @Override
    public String toString() {
        return "UserDetail [username=" + username + ", password=******, enabled=" + enabled + "]";
    }
}