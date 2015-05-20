package com.pantuo.mybatis.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface UserAutoCompleteMapper {

	List<String> getUserIdLike(@Param("uname") String uname,@Param("groupId") String groupId);

}