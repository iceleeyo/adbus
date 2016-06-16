package com.pantuo.dao.pojo;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * @author tliu
 *
 * base entity
 */
@MappedSuperclass
public abstract class BaseEntity implements Serializable {
    public static final int OK = 1;
    public static final int ERROR = 0;

    public Date created;
    public Date updated;
    /*error code*/
    @Transient
    public Integer error = OK;
    /*error message*/
    @Transient
    public String errorMessage;

    public BaseEntity() {
        created = new Date();
        updated = created;
    }

    public BaseEntity(int error, String errorMessage) {
        this();
        this.error = error;
        this.errorMessage = errorMessage;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setErrorInfo(int error, String errorMessage) {
        setError(error);
        setErrorMessage(errorMessage);
    }
}
