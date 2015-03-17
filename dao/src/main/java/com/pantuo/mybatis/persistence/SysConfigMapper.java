package com.pantuo.mybatis.persistence;

import com.pantuo.mybatis.domain.SysConfig;

public interface SysConfigMapper {
	SysConfig selectByPrimaryKey(Integer id);
}