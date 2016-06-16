package com.pantuo.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.pantuo.dao.pojo.JpaBusline;
import com.pantuo.service.BusService;

/**
 * @author tliu
 */
@Service
public class BusServiceImpl implements BusService {
	
	private static Logger log = LoggerFactory.getLogger(BusServiceImpl.class);
	public long getMoneyFromBusModel(JpaBusline.Level level, boolean doubleDecker) {
		long r = 0;
		//  S ("特级"),
		//  APP ("A++"),
		// AP ("A+"),
		// A ("A"),
		if (doubleDecker) {
			if (level == JpaBusline.Level.S) {
				r = 170000;
			} else if (level == JpaBusline.Level.APP) {
				r = 150000;
			} else if (level == JpaBusline.Level.AP) {
				r = 120000;
			}
		} else {
			if (level == JpaBusline.Level.S) {
				r = 39000;
			} else if (level == JpaBusline.Level.APP) {
				r = 26000;
			} else if (level == JpaBusline.Level.AP) {
				r = 20000;
			} else if (level == JpaBusline.Level.A) {
				r = 15000;
			}
		}
		return r;

	}

}


