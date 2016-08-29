package com.pantuo.service;

import org.springframework.stereotype.Service;

import com.pantuo.web.schedule.OrderRequest;
import com.pantuo.web.schedule.SchedUltResult;

@Service
public interface NewScheduleService {
	/**
	 * 
	 * 
	 *
	 * @param orderRequest
	 * @return
	 * @since pantuo 1.0-SNAPSHOT
	 */
	SchedUltResult checkTimeSole(OrderRequest orderRequest);
 

}
