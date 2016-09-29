package com.pantuo.mybatis.persistence;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.pantuo.mybatis.domain.BusFunction;
import com.pantuo.vo.MediaInventory;
import com.pantuo.vo.ModelCountView;

public interface UserAutoCompleteMapper {

	List<String> getUserIdLike(@Param("uname") String uname,@Param("groupId") String groupId);
	
	List<String> getSalesIdLike(@Param("uname") String uname,@Param("groupId") String groupId);

	List<Integer> selectAllProId();

	List<Integer> selectBusidsByPid(@Param("pid") int pid);
	
	List<BusFunction> selectFunidsByPid(@Param("gidlist") List<String> gidlist);
	Collection<ModelCountView> selectBusModelGroupView(@Param("pubidList") List<Integer> pubidList);
	
	//根据营销中心查找合同数量
	int getContactCountBycomid(@Param("companyid") int companyid, @Param("monthstr") String monthstr);
	//根据营销中心查找各个线路级别对应的车辆数
	List<ModelCountView>  getlevelbusnumBycomid(@Param("sktype") int sktype,@Param("companyid") int companyid, @Param("monthstr") String monthstr);
	//根据营销中心查找各个刊期对应的车辆数
	List<ModelCountView> getdaysbusnumBycomid(@Param("companyid") int companyid, @Param("monthstr") String monthstr);
	//根据日期查媒体库存
	List<MediaInventory> getMediaInventory( @Param("datestr") String datestr);
	//根据日期和orerid查找goods
	List<MediaInventory> getScheduleViewByDateStr( @Param("orderid") int orderid,@Param("datestr") String datestr);
}