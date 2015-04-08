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
    private int productId;
    private int contractId;
    private String contractCode;
    private Date startTime;
    private PayType payType;
    private double price;
    private Status stats;
    private String creator;

    public JpaOrders() {
        //for serialization
    }

    public JpaOrders(String userId, int suppliesId, int productId, int contractId, String contractCode, Date startTime, PayType payType, double price, Status stats, String creator) {
        this.userId = userId;
        this.suppliesId = suppliesId;
        this.productId = productId;
        this.contractId = contractId;
        this.contractCode = contractCode;
        this.startTime = startTime;
        this.payType = payType;
        this.price = price;
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
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
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

    public PayType getPayType() {
        return payType;
    }

    public void setPayType(PayType payType) {
        this.payType = payType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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
                "stats=" + stats +
                ", creator='" + creator + '\'' +
                ", price=" + price +
                ", payType=" + payType +
                ", startTime=" + startTime +
                ", contractCode='" + contractCode + '\'' +
                ", contractId=" + contractId +
                ", productId=" + productId +
                ", suppliesId=" + suppliesId +
                ", userId='" + userId + '\'' +
                '}';
    }
}