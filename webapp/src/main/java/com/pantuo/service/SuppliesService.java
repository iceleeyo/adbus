package com.pantuo.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.pantuo.mybatis.domain.Supplies;
import com.pantuo.util.NumberPageUtil;
import com.pantuo.util.Pair;
import com.pantuo.web.view.SuppliesView;

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
	/**
	 * 
	 * 录入素材
	 *
	 * @param obj
	 * @param request
	 * @return
	 * @since pantuotech 1.0-SNAPSHOT
	 */

	public Pair<Boolean, String> addSupplies(Supplies obj, HttpServletRequest request);
	/**
	 * 
	 * 取素材列表
	 *
	 * @param page 
	 * @param name 素材标题查询
	 * @param type 素材类型查询
	 * @param request
	 * @return
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	public List<Supplies> queryMyList(NumberPageUtil page, String name, String type, HttpServletRequest request);
	/**
	 * 
	 * 取素材列表时总记录数统计
	 *
	 * @param name
	 * @param type
	 * @param request
	 * @return
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	
	public int countMyList(String name, String type, HttpServletRequest request);
	/**
	 * 
	 * 删除素材
	 *
	 * @param supplies_id
	 * @param request
	 * @return
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	public Pair<Boolean, String> removeSupplies(int supplies_id, HttpServletRequest request);
	
	/**
	 * 
	 * 查单个素材详细
	 *
	 * @param supplies_id
	 * @param request
	 * @return
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	public SuppliesView getSuppliesDetail(int supplies_id, HttpServletRequest request);
}
