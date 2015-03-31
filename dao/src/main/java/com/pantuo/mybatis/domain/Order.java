package com.pantuo.mybatis.domain;

import java.io.Serializable;
import java.util.Date;

public class Order implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order.id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order.user_id
     *
     * @mbggenerated
     */
    private String userId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order.supplies_id
     *
     * @mbggenerated
     */
    private Integer suppliesId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order.product_id
     *
     * @mbggenerated
     */
    private Integer productId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order.contract_id
     *
     * @mbggenerated
     */
    private Integer contractId;
    public String contract_code;

    public String getContract_code() {
		return contract_code;
	}

	public void setContract_code(String contract_code) {
		this.contract_code = contract_code;
	}

	/**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order.start_time
     *
     * @mbggenerated
     */
    private Date startTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order.pay_type
     *
     * @mbggenerated
     */
    private String payType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order.price
     *
     * @mbggenerated
     */
    private Double price;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order.stats
     *
     * @mbggenerated
     */
    private String stats;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order.create_time
     *
     * @mbggenerated
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order.edit_time
     *
     * @mbggenerated
     */
    private Date editTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table order
     *
     * @mbggenerated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order.id
     *
     * @return the value of order.id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order.id
     *
     * @param id the value for order.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order.user_id
     *
     * @return the value of order.user_id
     *
     * @mbggenerated
     */
    public String getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order.user_id
     *
     * @param userId the value for order.user_id
     *
     * @mbggenerated
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order.supplies_id
     *
     * @return the value of order.supplies_id
     *
     * @mbggenerated
     */
    public Integer getSuppliesId() {
        return suppliesId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order.supplies_id
     *
     * @param suppliesId the value for order.supplies_id
     *
     * @mbggenerated
     */
    public void setSuppliesId(Integer suppliesId) {
        this.suppliesId = suppliesId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order.product_id
     *
     * @return the value of order.product_id
     *
     * @mbggenerated
     */
    public Integer getProductId() {
        return productId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order.product_id
     *
     * @param productId the value for order.product_id
     *
     * @mbggenerated
     */
    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order.contract_id
     *
     * @return the value of order.contract_id
     *
     * @mbggenerated
     */
    public Integer getContractId() {
        return contractId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order.contract_id
     *
     * @param contractId the value for order.contract_id
     *
     * @mbggenerated
     */
    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order.start_time
     *
     * @return the value of order.start_time
     *
     * @mbggenerated
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order.start_time
     *
     * @param startTime the value for order.start_time
     *
     * @mbggenerated
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order.pay_type
     *
     * @return the value of order.pay_type
     *
     * @mbggenerated
     */
    public String getPayType() {
        return payType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order.pay_type
     *
     * @param payType the value for order.pay_type
     *
     * @mbggenerated
     */
    public void setPayType(String payType) {
        this.payType = payType == null ? null : payType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order.price
     *
     * @return the value of order.price
     *
     * @mbggenerated
     */
    public Double getPrice() {
        return price;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order.price
     *
     * @param price the value for order.price
     *
     * @mbggenerated
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order.stats
     *
     * @return the value of order.stats
     *
     * @mbggenerated
     */
    public String getStats() {
        return stats;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order.stats
     *
     * @param stats the value for order.stats
     *
     * @mbggenerated
     */
    public void setStats(String stats) {
        this.stats = stats == null ? null : stats.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order.create_time
     *
     * @return the value of order.create_time
     *
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order.create_time
     *
     * @param createTime the value for order.create_time
     *
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order.edit_time
     *
     * @return the value of order.edit_time
     *
     * @mbggenerated
     */
    public Date getEditTime() {
        return editTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order.edit_time
     *
     * @param editTime the value for order.edit_time
     *
     * @mbggenerated
     */
    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }
}