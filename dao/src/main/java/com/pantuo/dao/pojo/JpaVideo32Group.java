package com.pantuo.dao.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Video32Group")
public class JpaVideo32Group {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String name;
	private int sortBy;

	public JpaVideo32Group() {
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

	public int getSortBy() {
		return sortBy;
	}

	public void setSortBy(int sortBy) {
		this.sortBy = sortBy;
	}

}
