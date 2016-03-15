package com.pantuo.util;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 
 * <b><code>Only1ServieUniqLong</code></b>
 * <p>
 * 单系统唯一数值
 * </p>
 * <b>Creation Time:</b> 2015年8月11日 下午5:51:25
 * @author impanxh@gmail.com
 * @since pantuo 1.0-SNAPSHOT
 */
public class Only1ServieUniqLong {
	private static java.util.concurrent.atomic.AtomicLong autolong = new AtomicLong(System.currentTimeMillis());

	public static long getUniqLongNumber() {
		return autolong.incrementAndGet();
	}

	public static String getUniqByDbId(int id) {
		return DateUtil.longDf3.get().format(System.currentTimeMillis()) + String.format("%5d", id).replace(" ", "0");
	}
}
