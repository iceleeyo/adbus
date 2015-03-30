package com.pantuo.mybatis.domain;

import java.io.Serializable;
import java.util.Date;

public class Attachment implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column attachment.id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column attachment.type
     *
     * @mbggenerated
     */
    private String type;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column attachment.url
     *
     * @mbggenerated
     */
    private String url;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column attachment.name
     *
     * @mbggenerated
     */
    private String name;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column attachment.main_id
     *
     * @mbggenerated
     */
    private Integer mainId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column attachment.create_time
     *
     * @mbggenerated
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column attachment.edit_time
     *
     * @mbggenerated
     */
    private Date editTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column attachment.user_id
     *
     * @mbggenerated
     */
    private String userId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table attachment
     *
     * @mbggenerated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column attachment.id
     *
     * @return the value of attachment.id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column attachment.id
     *
     * @param id the value for attachment.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column attachment.type
     *
     * @return the value of attachment.type
     *
     * @mbggenerated
     */
    public String getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column attachment.type
     *
     * @param type the value for attachment.type
     *
     * @mbggenerated
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column attachment.url
     *
     * @return the value of attachment.url
     *
     * @mbggenerated
     */
    public String getUrl() {
        return url;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column attachment.url
     *
     * @param url the value for attachment.url
     *
     * @mbggenerated
     */
    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column attachment.name
     *
     * @return the value of attachment.name
     *
     * @mbggenerated
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column attachment.name
     *
     * @param name the value for attachment.name
     *
     * @mbggenerated
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column attachment.main_id
     *
     * @return the value of attachment.main_id
     *
     * @mbggenerated
     */
    public Integer getMainId() {
        return mainId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column attachment.main_id
     *
     * @param mainId the value for attachment.main_id
     *
     * @mbggenerated
     */
    public void setMainId(Integer mainId) {
        this.mainId = mainId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column attachment.create_time
     *
     * @return the value of attachment.create_time
     *
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column attachment.create_time
     *
     * @param createTime the value for attachment.create_time
     *
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column attachment.edit_time
     *
     * @return the value of attachment.edit_time
     *
     * @mbggenerated
     */
    public Date getEditTime() {
        return editTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column attachment.edit_time
     *
     * @param editTime the value for attachment.edit_time
     *
     * @mbggenerated
     */
    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column attachment.user_id
     *
     * @return the value of attachment.user_id
     *
     * @mbggenerated
     */
    public String getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column attachment.user_id
     *
     * @param userId the value for attachment.user_id
     *
     * @mbggenerated
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }
}