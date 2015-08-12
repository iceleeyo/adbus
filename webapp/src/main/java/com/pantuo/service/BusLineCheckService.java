package com.pantuo.service;

import java.text.ParseException;
import java.util.List;

import com.pantuo.dao.pojo.JpaBus;
import com.pantuo.dao.pojo.JpaBusLock;
import com.pantuo.mybatis.domain.BusLock;
import com.pantuo.util.Pair;
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
	public int countByFreeCars(int lineId,Integer  modelId, JpaBus.Category category, String start, String end);

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

	public List<JpaBusLock> getBusLockListBySeriNum(long seriaNum);

	public Pair<Boolean, String> saveBusLock(BusLock buslock, String startD, String endD)throws ParseException ;

}
