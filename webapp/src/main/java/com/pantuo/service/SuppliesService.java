package com.pantuo.service;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.pantuo.dao.pojo.JpaProduct;
import com.pantuo.mybatis.domain.Supplies;
import com.pantuo.util.NumberPageUtil;
import com.pantuo.util.Pair;
import com.pantuo.web.view.SuppliesView;

import org.springframework.data.domain.*;
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
    Pair<Boolean, String> addSupplies(Supplies obj, Principal principal, HttpServletRequest request);

	/**
	 * 
	 * 取素材列表
	 *
	 * @param page 
	 * @param name 素材标题查询
	 * @param type 素材类型查询
	 * @return
	 * @since pantuotech 1.0-SNAPSHOT
	 */
    List<Supplies> queryMyList(NumberPageUtil page, String name, JpaProduct.Type type, Principal principal);
	/**
	 * 
	 * 取素材列表时总记录数统计
	 *
	 * @param name
	 * @param type
	 * @return
	 * @since pantuotech 1.0-SNAPSHOT
	 */

    int countMyList(String name, JpaProduct.Type type, Principal principal);

	/**
	 *
	 * 删除素材
	 *
	 * @param supplies_id
	 * @param request
	 * @return
	 * @since pantuotech 1.0-SNAPSHOT
	 */
    Pair<Boolean, String> removeSupplies(int supplies_id, Principal principal, HttpServletRequest request);

    /**
	 * 
	 * 查单个素材详细
	 *
	 * @param supplies_id
	 * @return
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	public SuppliesView getSuppliesDetail(int supplies_id, Principal principal);

	Supplies selectSuppliesById(Integer suppliesId);

	int updateSupplies(Supplies supplies);

	List<Supplies> querySuppliesByUser(Principal principal);
}
