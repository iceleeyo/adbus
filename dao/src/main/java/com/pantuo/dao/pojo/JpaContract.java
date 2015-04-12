package com.pantuo.dao.pojo;

import javax.persistence.*;
import java.util.Date;

/**
 * @author tliu
 *
 * 合同
 */
@Entity
@Table(name="contract")
public class JpaContract extends BaseEntity {
    public static enum Status {
        not_started, starting, ended
    }


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String contractCode;
    private String contractName;
    private String userId;
    private Date startDate;
    private Date endDate;
    private Status stats;
    private boolean isUpload;
    private String remark;
    private String creator;

    public JpaContract() {
        //for serialization
    }

    public JpaContract(String contractCode, String contractName, String userId, Date startDate, Date endDate, Status stats, boolean isUpload, String remark, String creator) {
        super();
        this.contractCode = contractCode;
        this.contractName = contractName;
        this.userId = userId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.stats = stats;
        this.isUpload = isUpload;
        this.remark = remark;
        this.creator = creator;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Status getStats() {
        return stats;
    }

    public void setStats(Status stats) {
        this.stats = stats;
    }

    public boolean isUpload() {
        return isUpload;
    }

    public void setUpload(boolean isUpload) {
        this.isUpload = isUpload;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Override
    public String toString() {
        return "JpaContract{" +
                "creator='" + creator + '\'' +
                ", remark='" + remark + '\'' +
                ", isUpload=" + isUpload +
                ", stats=" + stats +
                ", endDate=" + endDate +
                ", startDate=" + startDate +
                ", userId=" + userId +
                ", contractName='" + contractName + '\'' +
                ", contractCode='" + contractCode + '\'' +
                '}';
    }
}