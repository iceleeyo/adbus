package com.pantuo.dao.pojo;

import javax.persistence.*;

import org.apache.commons.lang3.StringUtils;

import com.pantuo.dao.pojo.JpaBusline.Level;

import java.util.*;

/**
 * 产品标签
 *
 */
@Entity
@Table(name = "product_tag")
public class JpaProductTag extends TopEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@Column(length = 64)
	String tagName;

	public JpaProductTag(String tagName, int productId) {
		this.tagName = tagName;
		this.productId = productId;
	}

	private int productId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

}