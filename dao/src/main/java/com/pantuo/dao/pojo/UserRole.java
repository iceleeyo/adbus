package com.pantuo.dao.pojo;

import javax.persistence.*;

/**
 * @author tliu
 *
 * 用户角色
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"username", "role"}))
public class UserRole extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    public String username;
    public String role;
    public boolean enabled = true;

    public UserRole(String username, String role) {
        this.username = username;
        this.role = role;
    }

    public UserRole() {
        super();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "UserRole [username=" + username + ", role=" + role + ", enabled = " + enabled + "]";
    }
}