package com.pantuo.dao.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="bus_uplog")
public class JpaBusUpLog extends CityEntity{
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
	@ManyToOne
	@JoinColumn(name = "busid")
	private JpaBus jpabus;
	private String jsonString;
	private String updator;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public JpaBus getJpabus() {
		return jpabus;
	}
	public void setJpabus(JpaBus jpabus) {
		this.jpabus = jpabus;
	}
	public String getJsonString() {
		return jsonString;
	}
	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}
	public String getUpdator() {
		return updator;
	}
	public void setUpdator(String updator) {
		this.updator = updator;
	}
	
}
