package com.pantuo.mybatis.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.pantuo.mybatis.domain.Bus;
import com.pantuo.mybatis.domain.BusContract;
import com.pantuo.mybatis.domain.BusExample;
import com.pantuo.mybatis.domain.BusModel;
import com.pantuo.vo.GroupVo;
import com.pantuo.vo.LineBatchUpdateView;

public interface BusSelectMapper {

	List<com.pantuo.mybatis.domain.Bus> getBusList(@Param("lineId") int lineId, @Param("modelId") int modelId,
			@Param("category") int category);

	public List<BusContract> getBusContract(@Param("lineId") int lineId, @Param("modelId") int modelId,
			@Param("category") int category);

	/**
	 * @deprecated
	 * 暂时没用到
	 * 按线路 进行车辆类型统计
	 * @param lineId
	 * @param category
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public List<BusModel> getBusModel(@Param("lineId") int lineId, @Param("category") int category);

	/**
	 * 
	 * 按线路统计车辆个数
	 *
	 * @param idList
	 * @param category
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	List<GroupVo> countCarTypeByLine(@Param("lineId") int lineId, @Param("category") int category);
	
	/**
	 * 
	 * 按线路 查营销中心
	 *
	 * @param idList
	 * @param category
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	List<Integer> queryLineCompanyByModelid(@Param("lineId") int lineId, @Param("modelId") int modelid);
	
	
	List<Integer> queryUplog(@Param("serial_number") String serial_number);
	/**
	 * 
	 * 按线路查车型描述
	 *
	 * @param idList
	 * @param category
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	List<String> queryLineDesByModelid(@Param("lineId") int lineId, @Param("modelId") int modelid);

	/**
	 * 
	 * 按线路统计车辆个数
	 *
	 * @param idList
	 * @param category
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	List<GroupVo> countLineCars(@Param("category") int category);

	/**
	 * 
	 * 按线路统计车辆个数
	 *
	 * @param idList
	 * @param category
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	List<GroupVo> countCarByLines(@Param("idList") List<Integer> idList, @Param("category") int category);

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
	int countBusCar(@Param("lineId") int lineId, @Param("modelId") int modelId, @Param("category") int category,
			@Param("enabled") int enabled);

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
	int countOnlineCarList(@Param("lineId") int lineId, @Param("modelId") int modelId, @Param("category") int category,
			@Param("start") String start, @Param("end") String end);

	/**
	 * 
	 * 统计正在锁定中的车辆数量 
	 *
	 * @param lineId
	 * @param modelId
	 * @param category
	 * @param start
	 * @param end
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	Integer countWorkingCarList(@Param("lineId") int lineId, @Param("modelId") int modelId, @Param("stats") int stats,
			@Param("start") String start, @Param("end") String end);

	/**
	 * 
	 * 按合同 线路查已上刊的车辆列表
	 *
	 * @param bodycontract_id
	 * @param modelId
	 * @param lineId
	 * @param category
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	List<BusContract> selectWorkDoneBus(@Param("bodycontract_id") int bodycontract_id, @Param("modelId") int modelId,
			@Param("lineId") int lineId, @Param("category") int category);

	/**
	 * 
	 * 统计车辆总共需要的车辆数量 
	 *
	 * @param idList
	 * @param category
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	List<GroupVo> countContractCar(@Param("idList") List<Integer> idList);

	/**
	 * 
	 * 统计车辆总共需要的车辆数量 
	 *
	 * @param idList
	 * @param category
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	List<GroupVo> countContractDoneCar(@Param("idList") List<Integer> idList);

	/**
	 * 
	 * 按线路统计车辆个数
	 *
	 * @param idList
	 * @param category
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	List<GroupVo> countContractidCarGroupbyLineModel(@Param("bodycontract_id") int bodycontract_id,
			@Param("category") int category);

	void batchUpdateLineCars(List<LineBatchUpdateView> views);
	
	void pdateLineOnineInfo(List<LineBatchUpdateView> views);
	
	/**
	 * 
	 *  按线路一些信息查车
	 *
	 * @param example
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	 public List<Bus> queryAllbusExample(BusExample example);
	 /**
		 * 
		 *  按线路一些信息查车
		 *
		 * @param example
		 * @return
		 * @since pantuo 1.0-SNAPSHOT
		 */
	 public Integer countAllbusExample(BusExample example);
	 
	 
	/**
	 * 
	 *  按线路一些信息查车
	 *
	 * @param example
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public List<Bus> queryAllbusByContract(BusExample example);

	/**
	 * 
	 *  按线路一些信息查车
	 *
	 * @param example
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public Integer countAllbusByContract(BusExample example);


	
	public Integer getGroupUserCount(String groupId);
	public void deleteGroupMEMBERSHIP(String groupId);
	
	
	
	
	
	/**
	 * 
	 *  按线路一些信息查车
	 *
	 * @param example
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	 public List<Bus> queryOnlineExample(@Param("idList") List<Integer > idList);
}