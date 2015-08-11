package com.pantuo.mybatis.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface BusSelectMapper {
	
	/**
	 * 
	 * 查线路总数
	 *
	 * @param lineId
	 * @param category
	 * @param enabled
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	int countBusCar(@Param("lineId") int lineId, @Param("category") int category, @Param("enabled") int enabled);
	
	
	
	/**
	 * 
	 * 查线路某个时间段内在线的车辆列表
	 *
	 * @param lineId
	 * @param category
	 * @param start
	 * @param end
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	List<Integer> getOnlineCarList(@Param("lineId") int lineId, @Param("category") int category,
			@Param("start") String start, @Param("end") String end);
	
	
	/**
	 * 
	 * 查线路某个时间段内在线的车辆列表总数
	 *
	 * @param lineId
	 * @param category
	 * @param start
	 * @param end
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	int   countOnlineCarList(@Param("lineId") int lineId, @Param("category") int category,
			@Param("start") String start, @Param("end") String end);

}