package com.pantuo.simulate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.pantuo.dao.pojo.JpaBus;
import com.pantuo.mybatis.persistence.BusSelectMapper;
import com.pantuo.service.impl.BusLineCheckServiceImpl;
import com.pantuo.vo.GroupVo;
import com.pantuo.vo.LineBatchUpdateView;

/**
 * 
 * <b><code>ProductProcessCount</code></b>
 * <p>
 * 车辆线路
 * </p>
 * <b>Creation Time:</b> 2015年6月30日 下午7:06:08
 * @author impanxh@gmail.com
 * @since pantuo 1.0-SNAPSHOT
 */
@Component
public class LineCarsCount implements Runnable, ScheduleStatsInter {
	private static Logger log = LoggerFactory.getLogger(BusLineCheckServiceImpl.class);
	public static Map<Integer, Integer> map = new java.util.concurrent.ConcurrentHashMap<Integer, Integer>();
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
		List<GroupVo> list = busSelectMapper.countLineCars(JpaBus.Category.yunyingche.ordinal());

		List<LineBatchUpdateView> views = new ArrayList<LineBatchUpdateView>();
		Random ran1 = new Random();
		for (GroupVo proid : list) {
			//map.put(proid.getGn1(), proid.getCount());
			LineBatchUpdateView view = new LineBatchUpdateView();
			view.setLine_id(proid.getGn1());
			view.set_cars(proid.getCount());
			view.set_persion((int) (proid.getCount() * 1000 * (ran1.nextDouble() * 0.5 + 0.8f)));
			views.add(view);
			if (views.size() == 50) {
				busSelectMapper.batchUpdateLineCars(views);
				views.clear();
			}
		}
		if (!views.isEmpty()) {
			busSelectMapper.batchUpdateLineCars(views);
		}
		//	log.info(" LineCarsCount:{} ", list.size());
	}

	@Override
	public void run() {
		work();
	}

	public int getCarsByLines(Integer line_id) {
		return map.containsKey(line_id) ? map.get(line_id) : 0;

	}

	public StatsMonitor statControl = new StatsMonitor(this);

	@Override
	public StatsMonitor getStatsMonitor() {
		return statControl;
	}

}
