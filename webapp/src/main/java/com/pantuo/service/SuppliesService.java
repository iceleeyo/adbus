package com.pantuo.service;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.pantuo.dao.pojo.JpaInvoice;
import com.pantuo.dao.pojo.JpaProduct;
import com.pantuo.mybatis.domain.Industry;
import com.pantuo.mybatis.domain.Invoice;
import com.pantuo.mybatis.domain.Supplies;
import com.pantuo.util.NumberPageUtil;
import com.pantuo.util.Pair;
import com.pantuo.web.view.InvoiceView;
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
    Pair<Object, String> addSupplies(int city, Supplies obj, Principal principal, HttpServletRequest request);
    public Pair<Boolean, String> delSupp(int Suppid,Principal principal);
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
    List<Supplies> queryMyList(int city, NumberPageUtil page, String name, JpaProduct.Type type, Principal principal);
	/**
	 * 
	 * 取素材列表时总记录数统计
	 *
	 * @param name
	 * @param type
	 * @return
	 * @since pantuotech 1.0-SNAPSHOT
	 */

    int countMyList(int city, String name, JpaProduct.Type type, Principal principal);

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

	int updateSupplies(int city, Supplies supplies);

	List<Supplies> querySuppliesByUser(int city, Principal principal);

	Pair<Object, String> addInvoice(int city,JpaInvoice obj, Principal principal,
			HttpServletRequest request);

	Pair<Boolean, String> savequlifi(Principal principal,
			HttpServletRequest request,String description);

	public SuppliesView getQua(int supplies_id, Principal principal);
	public InvoiceView getInvoiceDetail(int orderid, Principal principal);
	public List<Industry> getIndustry();

    //key order by duration desc
    LinkedHashMap<Long/*duration*/, List<Supplies>> queryFillerSupplies(int city);
    
    public List<Supplies> queryAllBlackSupplies(int city) ;
    
    
    public Map<Integer,Supplies> getSuppliesByIds(List<Integer> ids);
}
