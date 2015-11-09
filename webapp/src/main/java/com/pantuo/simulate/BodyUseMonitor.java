package com.pantuo.simulate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.pantuo.dao.BusRepository;
import com.pantuo.dao.BuslineRepository;
import com.pantuo.dao.pojo.JpaBusline;
import com.pantuo.mybatis.domain.Bus;
import com.pantuo.mybatis.domain.BusExample;
import com.pantuo.mybatis.domain.PublishLine;
import com.pantuo.mybatis.domain.PublishLineExample;
import com.pantuo.mybatis.persistence.BusLineMapper;
import com.pantuo.mybatis.persistence.BusMapper;
import com.pantuo.mybatis.persistence.BusSelectMapper;
import com.pantuo.mybatis.persistence.PublishLineMapper;
import com.pantuo.simulate.QueryBusInfo.Qstats;
import com.pantuo.web.view.BodyUseView;

@Component
public class BodyUseMonitor implements Runnable, ScheduleStatsInter {
	private static Logger log = LoggerFactory.getLogger(BodyUseMonitor.class);
	public static Map<Integer, BodyUseView> map = new HashMap<Integer, BodyUseView>();;
	public static  Lock lock = new ReentrantLock();// 锁  
	@Autowired
	BusSelectMapper busSelectMapper;
	@Autowired
	BusLineMapper busLineMapper;
	@Autowired
	BusRepository busRepository;
	@Autowired
	BusMapper busMapper;
	@Autowired
	QueryBusInfo queryBusInfo;
	@Autowired
	BuslineRepository lineRep;

	@Autowired
	PublishLineMapper publishLineMapper;

	@Scheduled(cron = "0/45 * * * * ?")
	public void work() {
		if (statControl.isRunning) {
			try {
				runWork(-1);
			} finally {
				statControl.runover();
			}
		}

	}
	
	public void updateLineCount(int _lineId) {
		lock.lock();
		try {
			runWork(_lineId);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 
	 * 当linedId<0更新所有线路的统计信息
	 *
	 * @param lineId
	 * @since pantuo 1.0-SNAPSHOT
	 */
	public void runWork(int _lineId) {
		long t = System.currentTimeMillis();

		Map<Integer, BodyUseView> tempMap = map;
		List<JpaBusline> jpaLines = null;
		if (_lineId < 0) {//
			map.clear();//如果不是更新一条线路就先清除记录

			jpaLines = lineRep.findAll();
			for (JpaBusline jpaBusline : jpaLines) {
				map.put(jpaBusline.getId(), new BodyUseView(jpaBusline.getId()));
			}
		} else {
			tempMap = new HashMap<Integer, BodyUseView>();
			jpaLines = new ArrayList<JpaBusline>();
			JpaBusline jbe = lineRep.findOne(_lineId);
			if (jbe != null) {
				jpaLines.add(jbe);
				tempMap.put(_lineId, new BodyUseView(_lineId));
			} else {
				return;
			}

		}

		int fetchsize = 100;//一次查100条记录
		int beginIndex = 0;
		while (true) {
			BusExample busExample = new BusExample();
			if (_lineId > 0) {//
				busExample.createCriteria().andLineIdEqualTo(_lineId);
			}
			busExample.setOrderByClause("id asc");
			busExample.setLimitStart(beginIndex);
			busExample.setLimitEnd(fetchsize);

			List<Bus> bus = busMapper.selectByExample(busExample);
			reduceOneBus(tempMap, bus);
			beginIndex += fetchsize;
			if (bus.size() < fetchsize) {
				break;
			}
		}

		int fetchsize2 = 100;
		int beginIndex2 = 0;
		while (true) {
			PublishLineExample publishExample = new PublishLineExample();
			if (_lineId > 0) {//如果只更新一条线路
				publishExample.createCriteria().andLineIdEqualTo(_lineId);
			}
			publishExample.setOrderByClause("id asc");
			publishExample.setLimitStart(beginIndex2);
			publishExample.setLimitEnd(fetchsize2);
			List<PublishLine> orders = publishLineMapper.selectByExample(publishExample);

			for (PublishLine publishLine : orders) {
				Integer lineId = publishLine.getLineId();
				if (tempMap.containsKey(lineId)) {
					BodyUseView view = tempMap.get(lineId);
					int notInstall = publishLine.getSalesNumber() - publishLine.getRemainNuber();
					view.orders.addAndGet(notInstall);
				}
			}
			beginIndex2 += fetchsize2;
			if (orders.size() < fetchsize2) {
				break;
			}
		}
		if (_lineId < 0) {//
			for (BodyUseView bsView : map.values()) {
				bsView.reduceTotalInfo();
				//System.out.println(bsView.toString());
			}
		} else {
			for (BodyUseView bsView : tempMap.values()) {
				bsView.reduceTotalInfo();
			}
			System.out.println(tempMap);
			map.putAll(tempMap);
		}

//		log.info("runWork time: {},lineId: {} ", System.currentTimeMillis() - t, _lineId);
	}

	private void reduceOneBus(Map<Integer, BodyUseView> tempMap, List<Bus> bus) {
		String nextMonty = getNextMonty();
		for (Bus bus2 : bus) {
			Integer busId = bus2.getId();
			if (tempMap.containsKey(bus2.getLineId())) {
				BodyUseView view = tempMap.get(bus2.getLineId());
				view.cars.incrementAndGet();//总车辆数

				Qstats q = queryBusInfo.getStatus(busId, nextMonty);
				if (q == Qstats.notDown) {
					view.notDown.incrementAndGet();//到期未下刊车辆
				} else if (q == Qstats.online || q == Qstats.nextMonthOver) {
					view.online.incrementAndGet();//累计在刊
					if (q == Qstats.nextMonthOver) {
						view.nextMonthEndCount.incrementAndGet();//在刊 下月到期	
					}
				}
			}
		}
	}

	public BodyUseView getUserView(int line_id) {

		return map.get(line_id);
	}

	public static String getNextMonty() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM"); //制定日期格式
		Calendar c = Calendar.getInstance();
		Date date = new Date();
		c.setTime(date);
		c.add(Calendar.MONTH, 1); //将当前日期加一个月
		String validityDate = df.format(c.getTime()); //返回String型的时间

		return validityDate;
	}

	@Override
	public void run() {
		work();
	}

	public StatsMonitor statControl = new StatsMonitor(this);

	@Override
	public StatsMonitor getStatsMonitor() {
		return statControl;
	}

}
