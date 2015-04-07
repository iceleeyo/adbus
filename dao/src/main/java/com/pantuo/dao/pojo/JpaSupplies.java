package com.pantuo.dao.pojo;

import javax.persistence.*;

/**
 * @author tliu
 *
 * 物料
 */
@Entity
@Table(name="supplies")
public class JpaSupplies extends BaseEntity {
    public static enum Status {
        unloaded, firstApproved, secondApproved, disapproved
    }


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;
    private JpaProduct.Type suppliesType;
    private String userId;
    private String filePath;
    private String infoContext;
    private Status stats;
    private String operFristuser;
    private String operFristcomment;
    private String operFinaluser;
    private String operFinalcomment;
    private String seqNumber;
    private String carNumber;
    private String responseCid;

    public JpaSupplies() {
        //for serialization
    }

    public JpaSupplies(String name, JpaProduct.Type suppliesType, String userId, String filePath, String infoContext, Status stats, String operFristuser, String operFristcomment, String operFinaluser, String operFinalcomment, String seqNumber, String carNumber, String responseCid) {
        this.name = name;
        this.suppliesType = suppliesType;
        this.userId = userId;
        this.filePath = filePath;
        this.infoContext = infoContext;
        this.stats = stats;
        this.operFristuser = operFristuser;
        this.operFristcomment = operFristcomment;
        this.operFinaluser = operFinaluser;
        this.operFinalcomment = operFinalcomment;
        this.seqNumber = seqNumber;
        this.carNumber = carNumber;
        this.responseCid = responseCid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JpaProduct.Type getSuppliesType() {
        return suppliesType;
    }

    public void setSuppliesType(JpaProduct.Type suppliesType) {
        this.suppliesType = suppliesType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getInfoContext() {
        return infoContext;
    }

    public void setInfoContext(String infoContext) {
        this.infoContext = infoContext;
    }

    public Status getStats() {
        return stats;
    }

    public void setStats(Status stats) {
        this.stats = stats;
    }

    public String getOperFristuser() {
        return operFristuser;
    }

    public void setOperFristuser(String operFristuser) {
        this.operFristuser = operFristuser;
    }

    public String getOperFristcomment() {
        return operFristcomment;
    }

    public void setOperFristcomment(String operFristcomment) {
        this.operFristcomment = operFristcomment;
    }

    public String getOperFinaluser() {
        return operFinaluser;
    }

    public void setOperFinaluser(String operFinaluser) {
        this.operFinaluser = operFinaluser;
    }

    public String getOperFinalcomment() {
        return operFinalcomment;
    }

    public void setOperFinalcomment(String operFinalcomment) {
        this.operFinalcomment = operFinalcomment;
    }

    public String getSeqNumber() {
        return seqNumber;
    }

    public void setSeqNumber(String seqNumber) {
        this.seqNumber = seqNumber;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getResponseCid() {
        return responseCid;
    }

    public void setResponseCid(String responseCid) {
        this.responseCid = responseCid;
    }

    @Override
    public String toString() {
        return "JpaSupplies{" +
                "carNumber='" + carNumber + '\'' +
                ", responseCid='" + responseCid + '\'' +
                ", seqNumber='" + seqNumber + '\'' +
                ", operFinalcomment='" + operFinalcomment + '\'' +
                ", operFinaluser='" + operFinaluser + '\'' +
                ", operFristcomment='" + operFristcomment + '\'' +
                ", operFristuser='" + operFristuser + '\'' +
                ", stats=" + stats +
                ", infoContext='" + infoContext + '\'' +
                ", filePath='" + filePath + '\'' +
                ", userId='" + userId + '\'' +
                ", suppliesType=" + suppliesType +
                ", name='" + name + '\'' +
                '}';
    }
}