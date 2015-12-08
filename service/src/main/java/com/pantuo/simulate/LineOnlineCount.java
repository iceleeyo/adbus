package com.pantuo.simulate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.pantuo.mybatis.domain.BusLock;
import com.pantuo.mybatis.domain.BusLockExample;
import com.pantuo.mybatis.persistence.BusLockMapper;
import com.pantuo.mybatis.persistence.BusSelectMapper;
import com.pantuo.vo.LineBatchUpdateView;
import com.pantuo.web.view.LineDateCount;

/**
 * 
 * <b><code>ProductProcessCount</code></b>
 * <p>
 * 统计今天 及未来1,2,3个月的今天的上刑量 
 * 
 * </p>
 * <b>Creation Time:</b> 2015年6月30日 下午7:06:08
 * @author impanxh@gmail.com
 * @since pantuo 1.0-SNAPSHOT
 */
@Component
public class LineOnlineCount implements Runnable ,ScheduleStatsInter{
	private static Logger log = LoggerFactory.getLogger(LineOnlineCount.class);

	public static LineDateCount _emptyCount = new LineDateCount();
	public static Map<Integer, LineDateCount> map = new java.util.concurrent.ConcurrentHashMap<Integer, LineDateCount>();
	@Autowired
	BusLockMapper busLockMapper;
	@Autowired
	BusSelectMapper busSelectMapper;

	//@Scheduled(fixedRate = 5000)
	@Scheduled(cron = "0/50 * * * * ?")
	public void work() {
		if (statControl.isRunning) {
			try {
				countCars();
			} finally {
				statControl.runover();
			}
		}
		
	}

	public void countCars() {
		BusLockExample example = new BusLockExample();
		List<BusLock> list = busLockMapper.selectByExample(example);

		Date today = new Date();

		Date today30 = com.pantuo.util.DateUtil.dateAdd(today, 30);
		Date today60 = com.pantuo.util.DateUtil.dateAdd(today, 60);
		Date today90 = com.pantuo.util.DateUtil.dateAdd(today, 90);
		Map<Integer, LineDateCount> _tempMap = new java.util.concurrent.ConcurrentHashMap<Integer, LineDateCount>();
		for (BusLock busLock : list) {
			if (busLock.getLockExpiredTime() != null && busLock.getLockExpiredTime().after(new Date())
					&& busLock.getEndDate().after(new Date())) {
				int _mapKey = busLock.getLineId();
				if (!_tempMap.containsKey(_mapKey)) {
					_tempMap.put(_mapKey, new LineDateCount(_mapKey));
				}
				LineDateCount countObj = _tempMap.get(_mapKey);
				if (countObj != null) {
					if (busLock.getEndDate().after(today) && busLock.getStartDate().before(today)) {
						countObj.getToday().addAndGet(busLock.getSalesNumber());
					}
					if (busLock.getEndDate().after(today30) && busLock.getStartDate().before(today30)) {
						countObj.getMonth_1_day().addAndGet(busLock.getSalesNumber());
					}
					if (busLock.getEndDate().after(today60) && busLock.getStartDate().before(today60)) {
						countObj.getMonth_2_day().addAndGet(busLock.getSalesNumber());
					}
					if (busLock.getEndDate().after(today90) && busLock.getStartDate().before(today90)) {
						countObj.getMonth_3_day().addAndGet(busLock.getSalesNumber());
					}
				}

			}
		}
		List<LineBatchUpdateView> views = new ArrayList<LineBatchUpdateView>();

		for (Map.Entry<Integer, LineDateCount> entry : _tempMap.entrySet()) {
			LineBatchUpdateView view = new LineBatchUpdateView();
			view.setLine_id(entry.getKey());
			view.set_today(entry.getValue().getToday().get());
			view.setMonth_1_day(entry.getValue().getMonth_1_day().get());
			view.setMonth_2_day(entry.getValue().getMonth_2_day().get());
			view.setMonth_3_day(entry.getValue().getMonth_3_day().get());
			views.add(view);
			if (views.size() == 50) {
				busSelectMapper.pdateLineOnineInfo(views);
				views.clear();
			}
		}
		if (!views.isEmpty()) {
			busSelectMapper.pdateLineOnineInfo(views);
		}
		//map.clear();
		//map.putAll(_tempMap);
		//	log.info("total line:{}", map.size());
	}

	@Override
	public void run() {
		work();
	}

	public LineDateCount getCarsByLines(Integer line_id) {
		return map.containsKey(line_id) ? map.get(line_id) : _emptyCount;
	}
	public StatsMonitor statControl = new StatsMonitor(this);
	@Override
	public StatsMonitor getStatsMonitor() {
		return statControl;
	}

}