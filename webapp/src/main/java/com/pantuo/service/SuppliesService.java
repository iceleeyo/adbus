package com.pantuo.service;

import javax.servlet.http.HttpServletRequest;

import com.pantuo.mybatis.domain.Supplies;
import com.pantuo.util.Pair;

/**
 * 
 * <b><code>SuppliesService</code></b>
 * <p>
 * 素材接口
 * </p>
 * <b>Creation Time:</b> Mar 30, 2015 10:58:48 AM
 * @author impanxh@gmail.com
 * @since pantuotech 1.0-SNAPSHOT
 */
public interface SuppliesService {

	public Pair<Boolean, String> addSupplies(Supplies obj,HttpServletRequest request);
	
	
}
