package com.pantuo.dao.pojo;

import javax.persistence.*;

/**
 * @author tliu
 *
 * 角色
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class Role extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    public String name;
    public String description;
    public boolean enabled = true;

    public Role(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Role() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "Role [name=" + name + ", description=" + description + ", enabled=" + enabled + "]";
    }
}