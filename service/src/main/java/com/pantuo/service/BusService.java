package com.pantuo.service;

import com.pantuo.dao.pojo.JpaBusline;

public interface BusService {

	public long getMoneyFromBusModel(JpaBusline.Level level,boolean doubleDecker);
	
}
