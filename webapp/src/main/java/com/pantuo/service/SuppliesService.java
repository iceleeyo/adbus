package com.pantuo.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.pantuo.mybatis.domain.Supplies;
import com.pantuo.util.NumberPageUtil;
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

	public Pair<Boolean, String> addSupplies(Supplies obj, HttpServletRequest request);

	public List<Supplies> queryMyList(NumberPageUtil page, String name, String type, HttpServletRequest request);

	public int countMyList(String name, String type, HttpServletRequest request);

	public Pair<Boolean, String> removeSupplies(int supplies_id, HttpServletRequest request);
}
