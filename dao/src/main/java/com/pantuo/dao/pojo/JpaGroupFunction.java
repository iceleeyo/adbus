package com.pantuo.dao.pojo;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "group_function")
public class JpaGroupFunction extends CityEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String groupId;
	@ManyToOne
	@JoinColumn(name = "funId")
	private JpaFunction jpaFunction;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public JpaFunction getJpaFunction() {
		return jpaFunction;
	}

	public void setJpaFunction(JpaFunction jpaFunction) {
		this.jpaFunction = jpaFunction;
	}

}
