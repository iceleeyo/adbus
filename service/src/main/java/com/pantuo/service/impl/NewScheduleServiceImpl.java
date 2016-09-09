package com.pantuo.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.pantuo.dao.pojo.JpaTimeslot;
import com.pantuo.service.NewScheduleService;
import com.pantuo.service.TimeslotService;
import com.pantuo.util.DateUtil;
import com.pantuo.web.schedule.DayRule;
import com.pantuo.web.schedule.OrderRequest;
import com.pantuo.web.schedule.SchedUltResult;

@Service
public class NewScheduleServiceImpl implements NewScheduleService {
	@Autowired
	private TimeslotService timeslotService;

	@Override
	public SchedUltResult checkTimeSole(OrderRequest orderRequest) {
		SchedUltResult result = new SchedUltResult(StringUtils.EMPTY, true, 0);
		Page<JpaTimeslot> slots = timeslotService.getAllTimeslots(1, null, 0, 9999, null, false);
		List<JpaTimeslot> list = slots.getContent();
		int size = list.size();
		if (orderRequest == null) {
			result = new SchedUltResult("参数错误,请刷新页面", false, 0);
		}
		if (orderRequest.getOplayNumbers() > size) {
			result = new SchedUltResult("库存不足,请尝试降低每天播放次数", false, 0);
		}
		List<DayRule> dayRules = orderRequest.getDayRule();
		String type = orderRequest.getType().name();

		int slotNum = 0;
		for (DayRule dayRule : dayRules) {
			if (dayRule.getOStartTime() != null && dayRule.getOEndTime() != null) {
				SchedUltResult result2 = getSlotNum(dayRule, list);
				if (result2.isScheduled) {
					if (StringUtils.equals("selectTime", type)) {
						slotNum += result2.getSlotNum();
					} else if (StringUtils.equals("select_timeAndNumber", type)) {
						if (result2.getSlotNum() < dayRule.getOPlayNumber()) {
							return new SchedUltResult("库存不足,请尝试降低播放次数", false, 0);
						}
					}
				} else {
					return result2;
				}
			}
		}
		if (StringUtils.equals("selectTime", type) && slotNum < orderRequest.getOplayNumbers()) {
			result = new SchedUltResult("库存不足,请尝试降低播放次数", false, 0);
		}
		return result;

	}

	// 根据时间段算出多少个广告包
	public SchedUltResult getSlotNum(DayRule dayRule, List<JpaTimeslot> list) {
		int size = list.size();

		Date min = DateUtil.hourAdd(list.get(0).getStartTime(), -8);
		Date max = DateUtil.hourAdd(list.get(size - 1).getStartTime(), -8);

		int startSlotId = 0;
		int endSlotId = 0;
		Date start = dayRule.getOStartTime();
		Date end = dayRule.getOEndTime();
		if (start.before(min) || start.after(max) || end.before(min) || end.after(max)) {
			return new SchedUltResult("所选时间段超出边界值,请重新选择", false, 0);
		}
		for (JpaTimeslot jpaTimeslot : list) {
			Date start1 = DateUtil.hourAdd(jpaTimeslot.getStartTime(), -8);
			Date end1 = DateUtil.secondAdd(start1, (int) jpaTimeslot.getDuration());
			if (start.compareTo(start1) != -1 && start.compareTo(end1) != 1) {
				startSlotId = jpaTimeslot.getId();
			}
			if (end.compareTo(start1) != -1 && end.compareTo(end1) != 1) {
				endSlotId = jpaTimeslot.getId();
			}

		}
		return new SchedUltResult("", true, endSlotId - startSlotId + 1);
	}

}
