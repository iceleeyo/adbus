package com.pantuo.mybatis.persistence;

import org.apache.ibatis.annotations.Param;
/**
 * 
 * <b><code>CpdProductMapper</code></b>
 * <p>
 * 更新竞价商品计数
 * </p>
 * <b>Creation Time:</b> 2015年7月2日 下午5:16:58
 * @author impanxh@gmail.com
 * @since pantuo 1.0-SNAPSHOT
 */
public interface CpdProductMapper {
	/**
	 * 
	 * 设置竞价产品围观数
	 *
	 * @param id
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	int updateCpdPv(@Param("id") int id);

	/**
	 * 
	 * 设置竞价产品被竞价次数
	 *
	 * @param id
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	int updateCpdCompareCount(@Param("id") int id);
	
	
	/**
	 * 
	 * 更新合同使用次数 
	 *
	 * @param id
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	int updateContractId(@Param("dateObj") String dateObj);

}