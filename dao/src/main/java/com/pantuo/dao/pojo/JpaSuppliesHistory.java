package com.pantuo.dao.pojo;

import javax.persistence.*;
import java.util.Date;

/**
 * @author tliu
 *
 * 物料历史记录
 */
@Entity
@Table(name="supplies_history")
public class JpaSuppliesHistory extends CityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int suppliesId;
    private String stats;   //操作类型
    private String operUser;
    private String operComment;

    public JpaSuppliesHistory() {
        //for serialization
    }

    public JpaSuppliesHistory(int city, int suppliesId, String stats, String operUser, String operComment) {
        super(city);
        this.suppliesId = suppliesId;
        this.stats = stats;
        this.operUser = operUser;
        this.operComment = operComment;
    }

    public int getSuppliesId() {
        return suppliesId;
    }

    public void setSuppliesId(int suppliesId) {
        this.suppliesId = suppliesId;
    }

    public String getStats() {
        return stats;
    }

    public void setStats(String stats) {
        this.stats = stats;
    }

    public String getOperUser() {
        return operUser;
    }

    public void setOperUser(String operUser) {
        this.operUser = operUser;
    }

    public String getOperComment() {
        return operComment;
    }

    public void setOperComment(String operComment) {
        this.operComment = operComment;
    }

    @Override
    public String toString() {
        return "JpaSuppliesHistory{" +
                "operComment='" + operComment + '\'' +
                ", operUser='" + operUser + '\'' +
                ", stats='" + stats + '\'' +
                ", suppliesId=" + suppliesId +
                '}';
    }
}