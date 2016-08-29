package com.pantuo.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.pantuo.dao.pojo.JpaTimeslot;
import com.pantuo.service.NewScheduleService;
import com.pantuo.service.TimeslotService;
import com.pantuo.web.schedule.OrderRequest;
import com.pantuo.web.schedule.SchedUltResult;

@Service
public class NewScheduleServiceImpl implements NewScheduleService {
	@Autowired
	private TimeslotService timeslotService;

	@Override
	public SchedUltResult checkTimeSole(OrderRequest orderRequest) {

		SchedUltResult result = new SchedUltResult(StringUtils.EMPTY, false, null, false);
		Page<JpaTimeslot> slots = timeslotService.getAllTimeslots(1, null, 0, 9999, null, false);
		return result;
	}

}
