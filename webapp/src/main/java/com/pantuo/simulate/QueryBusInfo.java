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

import com.google.common.collect.Maps;
import com.pantuo.mybatis.domain.Bodycontract;
import com.pantuo.mybatis.domain.BusContract;
import com.pantuo.mybatis.domain.BusContractExample;
import com.pantuo.mybatis.domain.BusLock;
import com.pantuo.mybatis.domain.BusLockExample;
import com.pantuo.mybatis.domain.BusOnline;
import com.pantuo.mybatis.domain.BusOnlineExample;
import com.pantuo.mybatis.domain.Offlinecontract;
import com.pantuo.mybatis.domain.OfflinecontractExample;
import com.pantuo.mybatis.persistence.BodycontractMapper;
import com.pantuo.mybatis.persistence.BusContractMapper;
import com.pantuo.mybatis.persistence.BusLockMapper;
import com.pantuo.mybatis.persistence.BusOnlineMapper;
import com.pantuo.mybatis.persistence.BusSelectMapper;
import com.pantuo.mybatis.persistence.OfflinecontractMapper;
import com.pantuo.vo.LineBatchUpdateView;
import com.pantuo.web.view.BusInfo;
import com.pantuo.web.view.LineDateCount;

/**
 * 
 * <b><code>queryBusInfo</code></b>
 * <p>
 * 查询车辆上下刊时间和当前绑定的合同
 * </p>
 * <b>Creation Time:</b> 2015年9月6日 上午11:46:47
 * @author xiaoli
 * @since pantuo 1.0-SNAPSHOT
 */
@Component
public class QueryBusInfo implements Runnable, ScheduleStatsInter {
	private static Logger log = LoggerFactory.getLogger(QueryBusInfo.class);

	public static BusInfo emptybusInfo = new BusInfo();
	public static Map<Integer, BusInfo> map = new java.util.concurrent.ConcurrentHashMap<Integer, BusInfo>();
	public static Map<Integer, BusInfo> map2 = new java.util.concurrent.ConcurrentHashMap<Integer, BusInfo>();
	@Autowired
	BusContractMapper busContractMapper;
	@Autowired
	OfflinecontractMapper offlinecontractMapper;
	@Autowired
	BusOnlineMapper busOnlineMapper;
	@Autowired
	BodycontractMapper bodycontractMapper;

	//@Scheduled(fixedRate = 5000)
	@Scheduled(cron = "0/50 * * * * ?")
	public void work() {
		if (statControl.isRunning) {
			try {
				countCars();
				countCars2();
			} finally {
				statControl.runover();
			}
		}

	}

	public void countCars() {
		Date today = new Date();
		BusContractExample example = new BusContractExample();
		BusContractExample.Criteria criteria = example.createCriteria();
		criteria.andStartDateLessThan(today);
		criteria.andEndDateGreaterThan(today);
		criteria.andUseridIsNotNull();
		List<BusContract> list = busContractMapper.selectByExample(example);
		for (BusContract busContract : list) {
			if (busContract != null) {
				Bodycontract bodycontract = bodycontractMapper.selectByPrimaryKey(busContract.getContractid());
				int busid = busContract.getBusid();
				if (!map.containsKey(busid)) {
					map.put(busid, new BusInfo(busid));
				}
				BusInfo busInfo = map.get(busid);
				busInfo.setStartD(busContract.getStartDate());
				busInfo.setEndD(busContract.getEndDate());
				if (bodycontract != null) {
					busInfo.setContractCode(bodycontract.getContractCode());
				}
			}
		}
	}

	public void countCars2() {
		Date today = new Date();
		BusOnlineExample example = new BusOnlineExample();
		List<BusOnline> list = busOnlineMapper.selectByExample(example);
		//example.setOrderByClause("start_date asc");
		map2.clear();
		for (BusOnline busOnline : list) {
			if (busOnline != null) {
				if (busOnline.getStartDate().before(today) && busOnline.getEndDate().after(today)) {
					putInMap(busOnline, BusInfo.Stats.now);
				} else if (busOnline.getStartDate().after(today)) {
					busOnline = findAfterLatestBusContract(busOnline);
					putInMap(busOnline, BusInfo.Stats.future);
				} else {
					busOnline = findBefLatestBusContract(busOnline);
					putInMap(busOnline, BusInfo.Stats.past);
				}
			}
		}
	}

	private BusOnline findAfterLatestBusContract(BusOnline busContract) {
		BusOnlineExample example = new BusOnlineExample();
		BusOnlineExample.Criteria criteria = example.createCriteria();
		criteria.andBusidEqualTo(busContract.getBusid());
		criteria.andStartDateGreaterThan(new Date());
		example.setOrderByClause("start_date asc");
		List<BusOnline> list = busOnlineMapper.selectByExample(example);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	private BusOnline findBefLatestBusContract(BusOnline busContract) {
		BusOnlineExample example = new BusOnlineExample();
		BusOnlineExample.Criteria criteria = example.createCriteria();
		criteria.andBusidEqualTo(busContract.getBusid());
		criteria.andEndDateLessThan(new Date());
		example.setOrderByClause("end_date asc");
		List<BusOnline> list = busOnlineMapper.selectByExample(example);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	private void putInMap(BusOnline busContract, BusInfo.Stats status) {
		if (busContract != null) {
			Offlinecontract bodycontract = offlinecontractMapper.selectByPrimaryKey(busContract.getContractid());
			int busid = busContract.getBusid();
			if (!map2.containsKey(busid)) {
				map2.put(busid, new BusInfo(busid));
			}
			BusInfo busInfo = map2.get(busid);
			if (busInfo.getStats() == BusInfo.Stats.empty) {
				busInfo.setStartD(busContract.getStartDate());
				busInfo.setEndD(busContract.getEndDate());
				busInfo.setStats(status);
			} else if (status == BusInfo.Stats.now) {
				busInfo.setStartD(busContract.getStartDate());
				busInfo.setEndD(busContract.getEndDate());
			} else if (status == BusInfo.Stats.future && (busInfo.getStats() == BusInfo.Stats.past)) {
				busInfo.setStartD(busContract.getStartDate());
				busInfo.setEndD(busContract.getEndDate());
			} else if (status == BusInfo.Stats.future && (busInfo.getStats() == BusInfo.Stats.future)) {
				if (busContract.getStartDate().before(busInfo.getStartD())) {
					busInfo.setStartD(busContract.getStartDate());
					busInfo.setEndD(busContract.getEndDate());
				}
			} else if (status == BusInfo.Stats.past && busInfo.getStats() == BusInfo.Stats.past) {
				if (busContract.getEndDate().after(busInfo.getEndD())) {
					busInfo.setStartD(busContract.getStartDate());
					busInfo.setEndD(busContract.getEndDate());
				}
			}
			if (bodycontract != null) {
				busInfo.setContractCode(bodycontract.getContractCode());
				busInfo.setOfflinecontract(bodycontract);
			}
		}
	}

	@Override
	public void run() {
		work();
	}

	public BusInfo getBusInfo(Integer line_id) {
		return map.containsKey(line_id) ? map.get(line_id) : emptybusInfo;
	}

	public BusInfo getBusInfo2(Integer busid) {
		return map2.containsKey(busid) ? map2.get(busid) : emptybusInfo;
	}

	public StatsMonitor statControl = new StatsMonitor(this);

	@Override
	public StatsMonitor getStatsMonitor() {
		return statControl;
	}

}
