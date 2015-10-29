package com.pantuo.mybatis.persistence;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.pantuo.mybatis.domain.BusFunction;
import com.pantuo.vo.ModelCountView;

public interface UserAutoCompleteMapper {

	List<String> getUserIdLike(@Param("uname") String uname,@Param("groupId") String groupId);

	List<Integer> selectAllProId();

	List<Integer> selectBusidsByPid(@Param("pid") int pid);
	
	List<BusFunction> selectFunidsByPid(@Param("gidlist") List<String> gidlist);
	Collection<ModelCountView> selectBusModelGroupView(@Param("pubidList") List<Integer> pubidList);
}