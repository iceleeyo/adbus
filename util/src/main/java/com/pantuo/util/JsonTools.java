package com.pantuo.util;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pantuo.dao.pojo.UserDetail;
/**
 * 
 * <b><code>JsonTools</code></b>
 * <p>
 * 
 * </p>
 * <b>Creation Time:</b> 2015年11月27日 下午8:43:06
 * @author impanxh@gmail.com
 * @since pantuo 1.0-SNAPSHOT
 */
public class JsonTools {

	private static Logger log = LoggerFactory.getLogger(JsonTools.class);

	public static String getJsonFromObject(Object Object) {
		String jsonString = StringUtils.EMPTY;
		ObjectMapper t = new ObjectMapper();
		t.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		t.getSerializationConfig().setSerializationInclusion(Inclusion.NON_NULL);
		try {
			jsonString = t.writeValueAsString(Object);
		} catch (Exception e) {
			log.error("getJsonFromObject", e);
		}
		return jsonString;
	}
	
	
	public static Object readValue(String Object, Class<?> valueType) {
		Object r = null;
		ObjectMapper t = new ObjectMapper();
		try {
			r = t.readValue(Object, valueType);
		} catch (Exception e) {
			log.error("getJsonFromObject", e);
		}
		return r;
	}
}