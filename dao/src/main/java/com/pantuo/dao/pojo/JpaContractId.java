package com.pantuo.dao.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "contract_Id")
public class JpaContractId implements Serializable {

	/**
	 * Comment here.
	 *
	 * @since pantuo 1.0-SNAPSHOT
	 */
	private static final long serialVersionUID = -6524682993181311983L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private Date dateObj;
	private int count;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDateObj() {
		return dateObj;
	}

	public void setDateObj(Date dateObj) {
		this.dateObj = dateObj;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public JpaContractId() {
		super();
	}

}
