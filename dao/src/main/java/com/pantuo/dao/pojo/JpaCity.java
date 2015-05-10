package com.pantuo.dao.pojo;

import javax.persistence.*;
import java.util.Date;

/**
 * @author tliu
 *
 * 城市
 */
@Entity
@Table(name="city")
public class JpaCity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;

    public JpaCity() {
        //for serialization
    }

    public JpaCity(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "JpaCity{" +
                "name='" + name + '\'' +
                '}';
    }
}