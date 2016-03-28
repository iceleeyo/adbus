package com.pantuo.dao.pojo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "product_location")
public class JpaProductLocation implements Serializable {
	/**
	 * Comment here.
	 *
	 * @since pantuo 1.0-SNAPSHOT
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	public static enum BelongTag {
		HOT, VIDEO, IMAGE_TEXT
	}

	private String locationName;
	private String locationTag;

	private BelongTag belongTag;//位置属于哪类商品

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getLocationTag() {
		return locationTag;
	}

	public void setLocationTag(String locationTag) {
		this.locationTag = locationTag;
	}

	public BelongTag getBelongTag() {
		return belongTag;
	}

	public void setBelongTag(BelongTag belongTag) {
		this.belongTag = belongTag;
	}

}
