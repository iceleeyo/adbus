package com.pantuo.dao.pojo;

import javax.persistence.*;

/**
 * @author tliu
 *
 * 系统配置
 */
@Entity
@Table(name="sys_config")
public class JpaSysConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int bl;
    private String keyDesc;
    private String keyCode;

    public JpaSysConfig(int bl, String keyDesc, String keyCode) {
        this.bl = bl;
        this.keyDesc = keyDesc;
        this.keyCode = keyCode;
    }

    public int getBl() {
        return bl;
    }

    public void setBl(int bl) {
        this.bl = bl;
    }

    public String getKeyDesc() {
        return keyDesc;
    }

    public void setKeyDesc(String keyDesc) {
        this.keyDesc = keyDesc;
    }

    public String getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(String keyCode) {
        this.keyCode = keyCode;
    }
}