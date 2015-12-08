package com.pantuo.service.impl;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.pantuo.dao.pojo.JpaBox;
import com.pantuo.dao.pojo.JpaGoods;
import com.pantuo.mybatis.domain.Box;
import com.pantuo.service.ScheduleFristAlgorithm;
import com.pantuo.service.ScheduleService;
import com.pantuo.service.ScheduleService.ScheduleContent;
import com.pantuo.util.DateUtil;
import com.pantuo.web.schedule.SchedUltResult;

@Service
public class ScheduleFristAlgImpl extends ScheduleAbstract implements ScheduleFristAlgorithm {

	public SchedUltResult scheduleFrist(ScheduleContent command) {
		int numberCopy = command.numberPlayer;
		Date start = command.order.getStartTime();
		int days = command.order.getProduct().getDays();

		int duration = (int) command.order.getProduct().getDuration();
		Calendar cal = DateUtil.newCalendar();
		cal.setTime(start);

		boolean isEmpty = command.getNeedSchedule().isEmpty();
		//临时变量 播放数次
		for (int i = 0; i < days; i++) {
			Date day = cal.getTime();
			int k = 0;
			List<Box> list2 = command.boxMap.get(day);

			//----初始化每天每个box排了几次 限定只能排2次		
			if (!command.boxScheduleCount.containsKey(day)) {
				command.boxScheduleCount.put(day, new HashMap<Integer, AtomicInteger>());
			}
			Map<Integer, AtomicInteger> boxScheduleCount = command.boxScheduleCount.get(day);

			//----取每天的常规时间排期后还需要 时间 次数需要排

			AtomicInteger r = command.getNeedSchedule().get(day);
			if (!isEmpty) {
				command.numberPlayer = r.get();
			}
			//----
			for (int j = 0; j < command.numberPlayer; j++) {
				Collections.sort(list2, FRIST_SLOT_COMPARATOR);
				Box box = list2.get(0);
				int boxId = box.getId();
				if (30 - box.getFremain() >= duration && box.getTmpAbsoluteWight() == ScheduleService.DEFAULT_ROLE) {
					JpaGoods goods = new JpaGoods();
					goods.setOrder(command.order);
					goods.setCity(command.order.getCity());
					goods.setCreated(new Date());
					goods.setSize(duration);
					goods.setInboxPosition((int) box.getFremain());

					//-----
					//goods.setBox(box);
					JpaBox storeBox = getJpaBoxFromEntity(command.order, box);
					goods.setBox(storeBox);
					//--------

					//goods.setBox(box);
					command.gs.add(goods);
					command.boxEx.put(box.getId(), box);

					box.setFremain(box.getFremain() + duration);
					box.setFsort(box.getFsort() - duration);
					k++;
					if (!isEmpty) {
						r.decrementAndGet();
					}

					//--------------
					AtomicInteger oneBoxScheduleCount = null;
					if (!boxScheduleCount.containsKey(boxId)) {
						boxScheduleCount.put(boxId, oneBoxScheduleCount = new AtomicInteger(0));
					} else {
						oneBoxScheduleCount = boxScheduleCount.get(boxId);
					}
					if (oneBoxScheduleCount.incrementAndGet() >= 2) {
						box.setTmpAbsoluteWight(box.getTmpAbsoluteWight() - box.getTmpAbsoluteWight() / 10);
					}
					//--------------------

				} else {
					break;
				}
			}
			if (isEmpty) {//计算是要求有首播时的库存
				if (k < command.numberPlayer) {
					return new SchedUltResult("实际可上刊次数:" + k + " 订单上刊次数" + command.numberPlayer, false, day, true);
				}
			} else {//计算常规时间段 排期后排首播时的库存情况 
				if (k < command.numberPlayer) {
					return new SchedUltResult("实际可上刊次数:" + (numberCopy - r.get()) + " 订单上刊次数" + numberCopy, false, day,
							true);
				}

			}
			cal.add(Calendar.DATE, 1);
		}
		return new SchedUltResult(StringUtils.EMPTY, true, start, true);

	}

	Comparator<Box> FRIST_SLOT_COMPARATOR = new Comparator<Box>() {
		public int compare(Box o1, Box o2) {
			int m = o2.getTmpAbsoluteWight() - o1.getTmpAbsoluteWight();
			if (m == 0) {
				int w = o2.getFsort() - o1.getFsort();
				if (w == 0) {
					return (int) ((30 - o2.getFremain()) - (30 - o1.getFremain()));
				} else {
					return w;
				}
			}
			return m;
		}
	};
}
