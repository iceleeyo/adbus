package com.pantuo.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.pantuo.web.view.AutoCompleteView;

/**
 * 
 * <b><code>AutoCompleteViewSort</code></b>
 * <p>
 * 线路名称 排序
 * </p>
 * <b>Creation Time:</b> 2015年11月11日 下午2:17:17
 * @author impanxh@gmail.com
 * @since pantuo 1.0-SNAPSHOT
 */
public class AutoCompleteViewSort {

	static Map<String, Integer> SPECIALLINE = new HashMap<String, Integer>();

	static {
		SPECIALLINE.put("四", 4);
		SPECIALLINE.put("六", 6);
		SPECIALLINE.put("八", 8);
		SPECIALLINE.put("五", 5);
		SPECIALLINE.put("二", 2);
		SPECIALLINE.put("九", 9);
		SPECIALLINE.put("三", 3);
		SPECIALLINE.put("七", 7);
		SPECIALLINE.put("一", 1);
	}

	public static List<AutoCompleteView> sort(List<AutoCompleteView> list) {

		List<AutoCompleteView> r = new ArrayList<AutoCompleteView>();
		//0开头的排序
		List<AutoCompleteView> aezoList = new ArrayList<AutoCompleteView>();
		List<AutoCompleteView> numberList = new ArrayList<AutoCompleteView>();
		//专开头排序集合
		List<AutoCompleteView> zList = new ArrayList<AutoCompleteView>();
		//特开头集合
		List<AutoCompleteView> sList = new ArrayList<AutoCompleteView>();

		//一到九
		List<AutoCompleteView> specilList = new ArrayList<AutoCompleteView>();

		List<AutoCompleteView> chartList = new ArrayList<AutoCompleteView>();

		for (AutoCompleteView autoCompleteView : list) {
			String lineName = StringUtils.trim(autoCompleteView.getValue());
			boolean needSort = false,isMapSort = false;
			if (StringUtils.startsWith(lineName, "0")) {
				needSort = true;
				aezoList.add(autoCompleteView);
			} else if (Character.isDigit(lineName.charAt(0))) {
				needSort = true;
				numberList.add(autoCompleteView);
			} else if (StringUtils.startsWith(lineName, "专")) {
				needSort = true;
				zList.add(autoCompleteView);
			} else if (StringUtils.startsWith(lineName, "特")) {
				needSort = true;
				sList.add(autoCompleteView);
			} else if (SPECIALLINE.containsKey(lineName)) {
				needSort = true;
				specilList.add(autoCompleteView);
				isMapSort = true;
			} else {
				chartList.add(autoCompleteView);
			}
			if (needSort) {
				String filterChart = lineName.replaceAll("\\D+", "");
				autoCompleteView.setSortKey(NumberUtils.toInt(filterChart));
			}
			if (isMapSort) {
				autoCompleteView.setSortKey(SPECIALLINE.get(lineName));
			}
		}

		Comparator<AutoCompleteView> comparator = new Comparator<AutoCompleteView>() {
			public int compare(AutoCompleteView o1, AutoCompleteView o2) {
				return o1.getSortKey() - o2.getSortKey();
			}
		};

		if (!aezoList.isEmpty()) {
			Collections.sort(aezoList, comparator);
		}
		if (!numberList.isEmpty()) {
			Collections.sort(numberList, comparator);
		}

		if (!zList.isEmpty()) {
			Collections.sort(zList, comparator);
		}
		if (!sList.isEmpty()) {
			Collections.sort(sList, comparator);
		}
		if (!specilList.isEmpty()) {
			Collections.sort(specilList, comparator);
		}
		r.addAll(aezoList);
		r.addAll(numberList);
		r.addAll(zList);
		r.addAll(sList);
		r.addAll(chartList);
		r.addAll(specilList);
		return r;
	}
}
