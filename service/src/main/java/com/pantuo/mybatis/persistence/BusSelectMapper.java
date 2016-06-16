package com.pantuo.mybatis.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface BusSelectMapper {



	
	public Integer getGroupUserCount(String groupId);
	public void deleteGroupMEMBERSHIP(String groupId);
	
	
	
	
}