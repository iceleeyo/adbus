package com.pantuo.dao.pojo;

import javax.persistence.*;

/**
 * @author tliu
 *
 * 物料
 */
@Entity
@Table(name="supplies")
public class JpaSupplies extends CityEntity {
    public static enum Status {
        unloaded, firstApproved, secondApproved, disapproved,online,offline
    }


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;
    private JpaProduct.Type suppliesType;

    @ManyToOne
    @JoinColumn(name = "industryId")
    private JpaIndustry industry;
    private String userId;
    private long duration;	//物料时长(S)
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

    public JpaSupplies(int city, String name, JpaProduct.Type suppliesType, int industryId,
                       String userId, long duration, String filePath, String infoContext,
                       Status stats, String operFristuser, String operFristcomment,

                       String operFinaluser, String operFinalcomment, String seqNumber,
                       String carNumber, String responseCid) {
        super(city);
        this.name = name;
        this.suppliesType = suppliesType;
        this.industry = new JpaIndustry();
        this.industry.setId(industryId);
        this.userId = userId;
        this.duration = duration;
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

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
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

    public JpaIndustry getIndustry() {
        return industry;
    }

    public void setIndustry(JpaIndustry industry) {
        this.industry = industry;
    }

    public void setIndustryId(int industryId) {
        this.industry = new JpaIndustry();
        this.industry.setId(industryId);
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
                ", duration='" + duration + '\'' +
                ", userId='" + userId + '\'' +
                ", suppliesType=" + suppliesType +
                ", name='" + name + '\'' +
                '}';
    }
}