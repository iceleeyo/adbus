package com.pantuo.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.pantuo.dao.pojo.JpaBus;
import com.pantuo.vo.GroupVo;
import com.pantuo.web.view.AutoCompleteView;

public interface BusLineCheckService {
	/**
	 * 
	 * 查某段时间线路可上刑期的库存 
	 * 
	 * 车辆总数-已上刑的上刑数量
	 *
	 * @param lineId
	 * @param category
	 * @param start
	 * @param end
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public int countByFreeCars(int lineId, JpaBus.Category category, String start, String end);

	/**
	 * 
	 * 搜寻线路
	 *
	 * @param name
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public List<AutoCompleteView> autoCompleteByName(int city, String name,JpaBus.Category category);
	/**
	 * 
	 * 查线路 类型
	 *
	 * @param lineId
	 * @param category
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public List<GroupVo> countCarTypeByLine(int lineId, JpaBus.Category category);

}
