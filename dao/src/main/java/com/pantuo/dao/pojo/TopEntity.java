package com.pantuo.dao.pojo;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * base entity
 */
@MappedSuperclass
public abstract class TopEntity implements Serializable {
	public static final int OK = 1;
	public static final int ERROR = 0;

	/*error code*/
	@Transient
	public Integer error = OK;
	/*error message*/
	@Transient
	public String errorMessage;

	public TopEntity() {
	}

	public TopEntity(int error, String errorMessage) {
		this();
		this.error = error;
		this.errorMessage = errorMessage;
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
