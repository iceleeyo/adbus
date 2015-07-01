package com.pantuo.service;

import java.security.Principal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import com.pantuo.dao.pojo.JpaCpd;
import com.pantuo.dao.pojo.JpaCpdLog;
import com.pantuo.dao.pojo.JpaProduct;
import com.pantuo.util.Pair;
/**
 * 
 * 
 * <b><code>CpdService</code></b>
 * <p>
 * 创建商品信息 走原来的save方法
 * </p>
 * <b>Creation Time:</b> 2015年7月1日 下午12:56:21
 * @author impanxh@gmail.com
 * @since pantuo 1.0-SNAPSHOT
 */
public interface CpdService {
	/**
	 * 
	 * 测试cpd 表关联查询
	 *
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public void test();

	/**
	 * 
	 * 根据商品类型(视频,图片,文字) city 查竞价商品
	 *
	 * @param productType
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public Page<JpaCpd> queryCpd(int city, int page, int pageSize, Sort sort, JpaProduct.Type productType);
	
	
	/**
	 * 
	 * 
	 * 对竞价商品进行出价 
	 * 1:update set price =price where price<{price} 
	 * 2:如果之前有出价过,之前一次出价作废,做相应的金额处理 (帐户先加前一次的出价金额 再减这次的出价)
	 * 3:保存到cpd log表
	 * 4:截止时间过了的 最高出价不在这里处理 加一个定时处理处理 
	 * @param cpdid
	 * @param principal
	 * @param myPrice
	 * @return 出价最高返回true 不然返回false
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public Pair<Boolean,String> setMyPrice(int cpdid, Principal principal,double myPrice);

	/**
	 * 
	 * 获取单个竞价商品的出价列表,可以只查前20个
	 *
	 * @param cpdid
	 * @param sort
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public Page<JpaCpdLog> queryCpd(  int cpdid, Sort sort);
	/**
	 * 
	 * 查单个竞价cpd信息 列表点进去显示单个竞价商品
	 *
	 * @param cpdid
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public JpaCpd queryOneCpdDetail(int cpdid);
	/**
	 * 
	 * 更新或保存 竞价信息
	 * 1：jpa 原则如果有id就更新 没有设置id就保存
	 *
	 * @param cpd
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public Pair<Boolean,JpaCpd> saveOrUpdateCpd(JpaCpd cpd);
}
