package com.pantuo.pojo;

import org.springframework.data.domain.*;

import java.util.List;

/**
 * Page object with jquery DataTable members
 */
public class DataTablePage<T> extends PageImpl<T> {
	private int draw;

	public Object tableInfo;

	public DataTablePage(List content, Pageable pageable, long total) {
		super(content, pageable, total);
	}

	public DataTablePage(List content) {
		super(content);
	}

	public DataTablePage(final Page<T> page, int draw) {

		super(page.getContent(), new PageRequest(page.getNumber(), page.getSize(), page.getSort()), page
				.getTotalElements());
		this.draw = draw;
		tableInfo = new A();
	}

	class A {
		public String getAbs() {
			return this.getClass().getName().concat("nihao");
		}
	}

	public int getDraw() {
		return draw;
	}

	public void setDraw(int draw) {
		this.draw = draw;
	}

	public long getRecordsTotal() {
		return this.getTotalElements();
	}

	public void setRecordsTotal(long recordsTotal) {
	}

	public long getRecordsFiltered() {
		return this.getTotalElements();
	}

	public void setRecordsFiltered(long recordsFiltered) {
	}

	public Object getTableInfo() {
		return tableInfo;
	}

	public void setTableInfo(Object tableInfo) {
		this.tableInfo = tableInfo;
	}

}
