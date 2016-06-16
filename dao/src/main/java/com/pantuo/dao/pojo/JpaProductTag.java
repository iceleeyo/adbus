package com.pantuo.dao.pojo;

import javax.persistence.*;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * 
 * 
 * 产品标签 未来扩展
 *
 */
@Entity
@Table(name = "product_tag")
public class JpaProductTag extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@ManyToOne
	@JoinColumn(name = "productId")
	private JpaProduct product;

	@ManyToOne
	@JoinColumn(name = "locationId")
	private JpaProductLocation productLocation;
	
	
	private String operationUser;


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public JpaProduct getProduct() {
		return product;
	}


	public void setProduct(JpaProduct product) {
		this.product = product;
	}


	public JpaProductLocation getProductLocation() {
		return productLocation;
	}


	public void setProductLocation(JpaProductLocation productLocation) {
		this.productLocation = productLocation;
	}


	public String getOperationUser() {
		return operationUser;
	}


	public void setOperationUser(String operationUser) {
		this.operationUser = operationUser;
	}
	
	
	
	

}