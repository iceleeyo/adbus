package com.pantuo.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;

public class CardUtil {
	public static List<Integer> parseIdsFromString(String ids) {
		List<Integer> r = null;
		String[] t = StringUtils.split(ids, ",");
		if (t.length > 0) {
			r = new ArrayList<Integer>();
			for (String id : t) {
				r.add(NumberUtils.toInt(id));
			}
		}
		return r;  
	}
}
