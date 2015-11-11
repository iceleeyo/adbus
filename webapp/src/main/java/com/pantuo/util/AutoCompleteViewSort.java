package com.pantuo.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

	public static List<AutoCompleteView> sort(List<AutoCompleteView> list) {

		List<AutoCompleteView> r = new ArrayList<AutoCompleteView>();
		List<AutoCompleteView> numberList = new ArrayList<AutoCompleteView>();
		//专开头
		List<AutoCompleteView> zList = new ArrayList<AutoCompleteView>();
		//特开头
		List<AutoCompleteView> sList = new ArrayList<AutoCompleteView>();

		List<AutoCompleteView> chartList = new ArrayList<AutoCompleteView>();
		for (AutoCompleteView autoCompleteView : list) {
			String lineName = StringUtils.trim(autoCompleteView.getValue());
			boolean needSort = false;
			if (Character.isDigit(lineName.charAt(0))) {
				needSort = true;
				numberList.add(autoCompleteView);
			} else if (StringUtils.startsWith(lineName, "专")) {
				needSort = true;
				zList.add(autoCompleteView);
			} else if (StringUtils.startsWith(lineName, "特")) {
				needSort = true;
				sList.add(autoCompleteView);
			} else {
				chartList.add(autoCompleteView);
			}
			if (needSort) {
				String filterChart = lineName.replaceAll("\\D+", "");
				autoCompleteView.setSortKey(NumberUtils.toInt(filterChart));
			}
		}

		Comparator<AutoCompleteView> comparator = new Comparator<AutoCompleteView>() {
			public int compare(AutoCompleteView o1, AutoCompleteView o2) {
				return o1.getSortKey() - o2.getSortKey();
			}
		};
		if (!numberList.isEmpty()) {
			Collections.sort(numberList, comparator);
		}

		if (!zList.isEmpty()) {
			Collections.sort(zList, comparator);
		}
		if (!sList.isEmpty()) {
			Collections.sort(sList, comparator);
		}
		r.addAll(numberList);
		r.addAll(zList);
		r.addAll(sList);
		r.addAll(chartList);
		return r;
	}
}
