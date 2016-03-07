package com.pantuo.web.table;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.support.NgTableMapField;

import com.pantuo.web.push.PushInter;

/**
 * 
 * <b><code>AngularRequest</code></b>
 * <p>
 * angular request
 * </p>
 * <b>Creation Time:</b> 2016年1月13日 下午5:23:32
 * @author impanxh@gmail.com
 * @since pantuo 1.0-SNAPSHOT
 */
public class AngularRequest {
	private int page = 0;
	private int length = 10;
	private @NgTableMapField Map<String/*column*/, String/*sort*/> order;

	private @NgTableMapField Map<String, String> filter;
	private PushInter pushLet = PushInter.DONOTHING;

	public AngularRequest() {
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		if (length < 1)
			length = 1;
		this.length = length;
	}

	public Sort getSort(String ascOrDesc, String defaultSort) {
		if (order == null || order.isEmpty()) {
			return (defaultSort == null ? null : new Sort(Direction.fromString(ascOrDesc), defaultSort));
		}
		Sort sort = null;
		for (Map.Entry<String, String> o : order.entrySet()) {
			String dir = "".equals(o.getValue()) ? "ASC" : o.getValue().toUpperCase();
			Sort s = new Sort(Sort.Direction.valueOf(dir), o.getKey());
			if (sort == null)
				sort = s;
			else
				sort = sort.and(s);
		}
		return sort;
	}

	public Sort getSort(String defaultSort) {
		return getSort("desc", defaultSort);
	}

	public Map<String, String> getOrder() {
		return this.order;
	}

	public void setOrder(Map<String, String> order) {
		this.order = order;
	}

	/*public int getPage() {
		return start / length;
	}*/

	public Map<String, String> getFilter() {
		return filter;
	}

	public void setFilter(Map<String, String> filter) {
		this.filter = filter;
	}

	public String getFilter(String key) {
		return getFilter(key, (String) null);
	}

	public String getFilter(String key, String defaultValue) {
		if (filter == null || !filter.containsKey(key))
			return defaultValue;
		String val = filter.get(key);
		if (StringUtils.isBlank(val)) {
			return defaultValue;
		}
		return val;
	}

	public int getFilterInt(String key, int defaultValue) {
		String str = getFilter(key);
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public <T extends Enum<T>> T getFilter(String key, Class<T> tEnum) {
		return getFilter(key, tEnum, (T) null);
	}

	public <T extends Enum<T>> T getFilter(String key, Class<T> tEnum, T defaultValue) {
		if (filter == null)
			return defaultValue;

		String enumStr = filter.get(key);
		if (enumStr == null)
			return defaultValue;
		try {
			return Enum.valueOf(tEnum, enumStr);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	@Override
	public String toString() {
		return "TableRequest{" + "filter=" + filter + ", order=" + order + ", length=" + length + ", page=" + page
				+ '}';
	}

	public PushInter getPushLet() {
		return pushLet;
	}

	public void setPushLet(PushInter pushLet) {
		this.pushLet = pushLet;
	}

}
