package com.pantuo.vo;

public class SortRequest {

	public int sort_index;
	public int id;

	public int getSort_index() {
		return sort_index;
	}

	public void setSort_index(int sort_index) {
		this.sort_index = sort_index;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public SortRequest(int sort_index, int id) {
		super();
		this.sort_index = sort_index;
		this.id = id;
	}

}
