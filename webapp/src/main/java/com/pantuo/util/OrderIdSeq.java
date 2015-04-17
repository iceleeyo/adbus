package com.pantuo.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.math.NumberUtils;

import com.pantuo.dao.pojo.JpaOrders;

/**
 * 
 * <b><code>OrderIdSeq</code></b>
 * <p>
 * 订单号规则 
 * </p>
 * <b>Creation Time:</b> 2015年4月17日 下午7:55:34
 * @author impanxh@gmail.com
 * @since pantuotech 1.0-SNAPSHOT
 */
public class OrderIdSeq {

	static long maxId = 100000L, split = maxId * 10L;
	
	public static long getLongOrderId(JpaOrders order) {
		SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
		return NumberUtils.toLong(sd.format(order.getCreated())) * split + maxId + order.getId();
	}

	public static long getIdFromDate(int id, Date date) {
		SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
		return NumberUtils.toLong(sd.format(date)) * split + maxId + id;
	}

	public int longOrderId2DbId(long longOrderId) {
		return (int) (longOrderId - (longOrderId / split) * split - maxId);
	}

}
