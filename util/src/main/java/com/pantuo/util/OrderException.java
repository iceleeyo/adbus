package com.pantuo.util;
/**
 * 
 * <b><code>OrderMissException</code></b>
 * <p>
 * 
 * </p>
 * <b>Creation Time:</b> 2015年7月3日 下午7:57:44
 * @author impanxh@gmail.com
 * @since pantuo 1.0-SNAPSHOT
 */
@SuppressWarnings("serial")
public class OrderException extends RuntimeException {
	public OrderException(String message) {
		super(message);
	}

	public OrderException(String message, Throwable cause) {
		super(message,cause);
	}
}
