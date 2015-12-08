package dynamic;

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
import com.pantuo.service.ScheduleAlgorithm;
import com.pantuo.service.ScheduleService;
import com.pantuo.service.ScheduleService.ScheduleContent;
import com.pantuo.service.ScheduleService.ScheduleType;
import com.pantuo.util.DateUtil;
import com.pantuo.web.schedule.SchedUltResult;
@Service
public class ScheduleNormalAlgImpl extends com.pantuo.service.impl.ScheduleAbstract implements ScheduleAlgorithm {

	public SchedUltResult scheduleNormal(ScheduleContent command) {
		Date start = command.order.getStartTime();
		int days = command.order.getProduct().getDays();
		Calendar cal = DateUtil.newCalendar();
		int duration = (int) command.order.getProduct().getDuration();
		SchedUltResult schedUltResult = new SchedUltResult(StringUtils.EMPTY, true, start, true);
		cal.setTime(start);
		//int _playerNumber = numberPlayer.get();
		Map<Date, AtomicInteger> jiMap = new HashMap<Date, AtomicInteger>();
		for (int i = 0; i < days; i++) {
			Date day = cal.getTime();
			//	listener.update("开始检查 ["+DateUtil.longDf.get().format(day) +"] 库存情况.");
			int k = 0;
			List<Box> list2 = command.boxMap.get(day);
			//----初始化每天每个box排了几次 限定只能排2次		
			if (!command.boxScheduleCount.containsKey(day)) {
				command.boxScheduleCount.put(day, new HashMap<Integer, AtomicInteger>());
			}
			Map<Integer, AtomicInteger> boxScheduleCount = command.boxScheduleCount.get(day);
			//-------记录每天排了多少次
			AtomicInteger dateCount = new AtomicInteger(command.numberPlayer);
			jiMap.put(day, dateCount);

			for (int j = 0; j < command.numberPlayer; j++) {
				Collections.sort(list2, NORMAL_COMPARATOR);
				Box box = list2.get(0);
				int boxId = box.getId();
				if (box.getSize() - box.getRemain() >= duration
						&& box.getTmpAbsoluteWight() == ScheduleService.DEFAULT_ROLE) {//控制1个box不能超过2次以上排
					JpaGoods goods = new JpaGoods();
					goods.setOrder(command.order);
					goods.setCity(command.order.getCity());
					goods.setCreated(new Date());
					goods.setSize(duration);
					goods.setInboxPosition(box.getRemain());
					//-----
					//goods.setBox(box);
					JpaBox storeBox = getJpaBoxFromEntity(command.order, box);
					goods.setBox(storeBox);
					//--------
					command.gs.add(goods);
					command.boxEx.put(box.getId(), box);

					box.setRemain(box.getRemain() + duration);
					box.setSort(box.getSort() - duration);
					k++;
					//记录排了多少
					dateCount.decrementAndGet();

					//--------------
					AtomicInteger r = null;
					if (!boxScheduleCount.containsKey(boxId)) {
						boxScheduleCount.put(boxId, r = new AtomicInteger(0));
					} else {
						r = boxScheduleCount.get(boxId);
					}
					if (r.incrementAndGet() >= 2) {
						box.setTmpAbsoluteWight((int) (box.getTmpAbsoluteWight() - box.getTmpAbsoluteWight() / 10));
					}
					//--------------------
				} else {
					break;
				}
			}
			if (k < command.numberPlayer) {
				if (ScheduleType.ALLNORMAL != command.type) {
					return new SchedUltResult("可上刊库存:" + k + " 订单上刊次数" + command.numberPlayer, false, day, false);
				} else {
					schedUltResult = new SchedUltResult("可上刊库存:" + k + " 订单上刊次数" + command.numberPlayer, false, day,
							false);
				}
			}
			cal.add(Calendar.DATE, 1);
		}
		if (!schedUltResult.isScheduled) {
			command.setNeedSchedule(jiMap);
			//	schedUltResult.debugMap();
		}
		return schedUltResult;
	}


	Comparator<Box> NORMAL_COMPARATOR = new Comparator<Box>() {
		public int compare(Box o1, Box o2) {
			int m = o2.getTmpAbsoluteWight() - o1.getTmpAbsoluteWight();
			if (m == 0) {
				int w = o2.getSort() - o1.getSort();
				if (w == 0) {
					return (int) ((o2.getSize() - o2.getRemain()) - (o1.getSize() - o1.getRemain()));
				} else {
					return w;
				}
			} else {
				return m;
			}
		}
	};
	Comparator<Box> NORMAL_COMPARATORbak = new Comparator<Box>() {
		public int compare(Box o1, Box o2) {
			
			int w = o2.getSort() - o1.getSort();
			if (w == 0) {
				return (int) ((o2.getSize() - o2.getRemain()) - (o1.getSize() - o1.getRemain()));
			} else {
				return w;
			}
		}
	};
}
