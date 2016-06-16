package com.pantuo.util;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
	public static String getMd5(int id) {
		String SOLT ="timeBetween";
		long	k=System.currentTimeMillis();
	    String md5=DigestUtils.md5Hex(SOLT + k + id).toLowerCase();
	    StringBuilder builder=new StringBuilder();
	    builder.append("i=")
	    .append(id)
	    .append("&k=")
	    .append(k)
	    .append("&qs=")
	    .append(md5);
		return builder.toString();
	}
}
