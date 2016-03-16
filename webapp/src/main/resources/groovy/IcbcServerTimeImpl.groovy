package com.pantuo.service.impl;

import org.springframework.stereotype.Service





import com.pantuo.service.IcbcServerTime


/**
 * 
 * <b><code>IcbcServerTimeImpl</code></b>
 * <p>
 * icbc服务器时间 
 * </p>
 * <b>Creation Time:</b> 2016年3月16日 下午1:29:29
 * @author impanxh@gmail.com
 * @since pantuo 1.0-SNAPSHOT
 */

@Service
public class IcbcServerTimeImpl implements IcbcServerTime  {
	public long getTime(){
		return 20160325145528L;
	}
}