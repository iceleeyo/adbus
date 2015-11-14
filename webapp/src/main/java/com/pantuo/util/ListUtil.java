package com.pantuo.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class ListUtil {

	public static String[] trimList(String[] v) {
		List<String> r = new ArrayList<String>();
		for (String string : v) {
			if (StringUtils.isNoneBlank(string)) {
				r.add(StringUtils.trim(string));
			}
		}
		String[] wr = new String[r.size()];
		return r.toArray(wr);
	}

}
