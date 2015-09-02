package com.pantuo.dao.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
* good free
*/
@Entity
@Table(name = "goods_black", uniqueConstraints = @UniqueConstraint(columnNames = {"inboxPosition", "slotId", "day" }))
public class JpaGoodsBlack extends CityEntity implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private long size;
	private long inboxPosition = -1; //箱子中的位置

	private Date day;
	private int slotId;
	private int sort_index;
	private int suppliesId;

	public int getId() {

		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public long getInboxPosition() {
		return inboxPosition;
	}

	public void setInboxPosition(long inboxPosition) {
		this.inboxPosition = inboxPosition;
	}

	public Date getDay() {
		return day;
	}

	public void setDay(Date day) {
		this.day = day;
	}

	public int getSlotId() {
		return slotId;
	}

	public void setSlotId(int slotId) {
		this.slotId = slotId;
	}

	public int getSort_index() {
		return sort_index;
	}

	public void setSort_index(int sort_index) {
		this.sort_index = sort_index;
	}

	public int getSuppliesId() {
		return suppliesId;
	}

	public void setSuppliesId(int suppliesId) {
		this.suppliesId = suppliesId;
	}

}
