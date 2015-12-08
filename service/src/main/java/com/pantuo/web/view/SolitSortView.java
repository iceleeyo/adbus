package com.pantuo.web.view;

import com.pantuo.mybatis.domain.Supplies;

public class SolitSortView {

	public int id;
	public String type;
	public int position;
	public long size;
	public int sort_index;
	public Supplies supplieStr;
	
	
	public int supplieId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public Supplies getSupplieStr() {
		return supplieStr;
	}

	public void setSupplieStr(Supplies supplieStr) {
		this.supplieStr = supplieStr;
	}

	public int getSort_index() {
		return sort_index;
	}

	public void setSort_index(int sort_index) {
		this.sort_index = sort_index;
	}

	public int getSupplieId() {
		return supplieId;
	}

	public void setSupplieId(int supplieId) {
		this.supplieId = supplieId;
	}

}
