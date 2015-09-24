package com.pantuo.dao.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "bus_function")
public class JpaFunction extends CityEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String name;
	private String fundesc;
	private String funcode;

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

	public String getFundesc() {
		return fundesc;
	}

	public void setFundesc(String fundesc) {
		this.fundesc = fundesc;
	}

	public String getFuncode() {
		return funcode;
	}

	public void setFuncode(String funcode) {
		this.funcode = funcode;
	}

	public JpaFunction(String name, String fundesc, String funcode) {
		super();
		this.name = name;
		this.fundesc = fundesc;
		this.funcode = funcode;
	}

	 

}
