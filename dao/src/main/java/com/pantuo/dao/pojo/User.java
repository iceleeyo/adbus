package com.pantuo.dao.pojo;

import javax.persistence.*;

/**
 * @author tliu
 *
 * 用户
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"username"}))
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    public String username;
    public String password;
    public boolean enabled = true;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User() {
        super();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "User [username=" + username + ", password=******, enabled=" + enabled + "]";
    }
}