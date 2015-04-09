package com.pantuo.dao.pojo;

import javax.persistence.*;
import java.util.Date;

/**
 * @author tliu
 *
 * 订单
 */
@Entity
@Table(name="orders")
public class JpaOrders extends BaseEntity {
    public static enum PayType {
        online, contract
    }
    public static enum Status {
        unpaid, paid, scheduled, started, completed, cancelled
    }


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String userId;
    private int suppliesId;
    @ManyToOne
    @JoinColumn(name = "productId")
    private JpaProduct product;
    private int contractId;
    private String contractCode;
    private Date startTime;
    private Date endTime;
    private JpaProduct.Type type;
    private PayType payType;
    private Status stats;
    private String creator;

    public JpaOrders() {
        //for serialization
    }

    public JpaOrders(String userId, int suppliesId, int productId, int contractId,
                     String contractCode, Date startTime, Date endTime, JpaProduct.Type type,
                     PayType payType,
                     Status stats, String creator) {
        this.userId = userId;
        this.suppliesId = suppliesId;
        this.product = new JpaProduct();
        this.product.setId(productId);
        this.type = type;
        this.contractId = contractId;
        this.contractCode = contractCode;
        this.startTime = startTime;
        this.endTime = endTime;
        this.payType = payType;
        this.stats = stats;
        this.creator = creator;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getSuppliesId() {
        return suppliesId;
    }

    public void setSuppliesId(int suppliesId) {
        this.suppliesId = suppliesId;
    }

    public int getProductId() {
        return product.getId();
    }

    public void setProductId(int productId) {
        this.product.setId(productId);
    }

    public JpaProduct getProduct() {
        return product;
    }

    public void setProduct(JpaProduct product) {
        this.product = product;
    }

    public int getContractId() {
        return contractId;
    }

    public void setContractId(int contractId) {
        this.contractId = contractId;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public JpaProduct.Type getType() {
        return type;
    }

    public void setType(JpaProduct.Type type) {
        this.type = type;
    }

    public PayType getPayType() {
        return payType;
    }

    public void setPayType(PayType payType) {
        this.payType = payType;
    }

    public Status getStats() {
        return stats;
    }

    public void setStats(Status stats) {
        this.stats = stats;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Override
    public String toString() {
        return "JpaOrders{" +
                "userId='" + userId + '\'' +
                ", suppliesId=" + suppliesId +
                ", productId=" + product.getId() +
                ", contractId=" + contractId +
                ", contractCode='" + contractCode + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", type=" + type +
                ", payType=" + payType +
                ", stats=" + stats +
                ", creator='" + creator + '\'' +
                '}';
    }
}