package com.pantuo.service.impl;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pantuo.dao.pojo.JpaBus;
import com.pantuo.mybatis.persistence.BusSelectMapper;
import com.pantuo.service.BusLineCheckService;

@Service
public class BusLineCheckServiceImpl implements BusLineCheckService {
	@Autowired
	BusSelectMapper busSelectMapper;

	@Override
	public int countByFreeCars(int lineId, JpaBus.Category category, String start, String end) {
	
		int busLineCarCount = busSelectMapper.countBusCar(lineId, category.ordinal(), BooleanUtils.toInteger(true));
		int carIds = busSelectMapper.countOnlineCarList(lineId, category.ordinal(), start, end);
		//总数-被占用数据
		return busLineCarCount - carIds;
	}

}
