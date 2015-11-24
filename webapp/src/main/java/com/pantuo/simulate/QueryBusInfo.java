package com.pantuo.simulate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.pantuo.dao.BusRepository;
import com.pantuo.dao.pojo.JpaBus;
import com.pantuo.mybatis.domain.Bodycontract;
import com.pantuo.mybatis.domain.Bus;
import com.pantuo.mybatis.domain.BusContract;
import com.pantuo.mybatis.domain.BusContractExample;
import com.pantuo.mybatis.domain.BusOnline;
import com.pantuo.mybatis.domain.BusOnlineExample;
import com.pantuo.mybatis.domain.Offlinecontract;
import com.pantuo.mybatis.persistence.BodycontractMapper;
import com.pantuo.mybatis.persistence.BusContractMapper;
import com.pantuo.mybatis.persistence.BusMapper;
import com.pantuo.mybatis.persistence.BusOnlineMapper;
import com.pantuo.mybatis.persistence.OfflinecontractMapper;
import com.pantuo.web.view.BusInfo;
import com.pantuo.web.view.BusModelGroupView;

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
	
	@Autowired
	BodyUseMonitor bodyUseMonitor;
	@Autowired
	BusMapper busMapper;
	
	@Autowired
	BusRepository busRepository;
	@Value("${bus.FreshContractToDb}")
	private String freshContractToDb;

	

	//@Scheduled(fixedRate = 5000)
	@Scheduled(cron = "0 5 0 * * ?")
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
		
		boolean needFresh = BooleanUtils.toBooleanObject(freshContractToDb);
		Date today = new Date();
		BusOnlineExample example = new BusOnlineExample();
		BusOnlineExample.Criteria c = example.createCriteria();
		c.andEnableEqualTo(true);
		example.setOrderByClause("busid asc ");
		List<BusOnline> list = busOnlineMapper.selectByExample(example);
		//example.setOrderByClause("start_date asc");
		//	map2.clear();
		
		
		Set<Integer> busids = new HashSet<Integer>(33750);
		for (BusOnline busOnline : list) {
			if (!busids.contains(busOnline.getBusid())) {
				map2.remove(busOnline.getBusid());
				busids.add(busOnline.getBusid());
			}
			if (busOnline != null) {
				updateBusContractCache(today, busOnline, needFresh);
			}
		}
	}

	public void updateBusContractCache(int busId) {
		BusOnlineExample example = new BusOnlineExample();
		BusOnlineExample.Criteria c = example.createCriteria();
		c.andBusidEqualTo(busId);
		c.andEnableEqualTo(true);
		List<BusOnline> list = busOnlineMapper.selectByExample(example);
		Date today = new Date();
		map2.remove(busId);
		for (BusOnline busOnline : list) {
			if (busOnline != null) {
				updateBusContractCache(today, busOnline,true);
			}
		}
		JpaBus _bus = busRepository.findOne(busId);
		if (_bus != null) {
			bodyUseMonitor.updateLineCount(_bus.getLine().getId());
		}
		
	}

	private void updateBusContractCache(Date today, BusOnline busOnline,boolean needUpdateDb) {
		if (busOnline.getStartDate().before(today) && busOnline.getEndDate().after(today)) {
			putInMap(busOnline, BusInfo.Stats.now,needUpdateDb);
		} else if (busOnline.getStartDate().after(today)) {
			busOnline = findAfterLatestBusContract(busOnline);
			putInMap(busOnline, BusInfo.Stats.future,needUpdateDb);
		} else {
			busOnline = findBefLatestBusContract(busOnline);
			putInMap(busOnline, BusInfo.Stats.past,needUpdateDb);
		}
		//return busOnline;
	}

	private BusOnline findAfterLatestBusContract(BusOnline busContract) {
		BusOnlineExample example = new BusOnlineExample();
		BusOnlineExample.Criteria criteria = example.createCriteria();
		criteria.andBusidEqualTo(busContract.getBusid());
		criteria.andStartDateGreaterThan(new Date()).andEnableEqualTo(true);
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
		criteria.andEndDateLessThan(new Date()).andEnableEqualTo(true);
		example.setOrderByClause("end_date asc");
		List<BusOnline> list = busOnlineMapper.selectByExample(example);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	private void putInMap(BusOnline busContract, BusInfo.Stats status,boolean needUpdateDb) {
		if (busContract != null) {
			Offlinecontract bodycontract = offlinecontractMapper.selectByPrimaryKey(busContract.getContractid());
			int busid = busContract.getBusid();
			if (!map2.containsKey(busid)) {
				map2.put(busid, new BusInfo(busid));
			}
			BusInfo busInfo = map2.get(busid);
			busInfo.addPlan(busContract);
			if (busInfo.getStats() == BusInfo.Stats.empty) {
			/*	busInfo.setStartD(busContract.getStartDate());
				busInfo.setEndD(busContract.getEndDate());
				busInfo.setStats(status);
				busInfo.setBusOnline(busContract);*/
				putCache(busContract, status, busInfo,bodycontract,needUpdateDb);
			} else if (status == BusInfo.Stats.now) {
				putCache(busContract, status, busInfo,bodycontract,needUpdateDb);
			} else if (status == BusInfo.Stats.future && (busInfo.getStats() == BusInfo.Stats.past)) {
				putCache(busContract, status, busInfo,bodycontract,needUpdateDb);
				//busInfo.setStats(BusInfo.Stats.future);
			} else if (status == BusInfo.Stats.future && (busInfo.getStats() == BusInfo.Stats.future)) {
				if (busContract.getStartDate().before(busInfo.getStartD())) {
					putCache(busContract, status, busInfo,bodycontract,needUpdateDb);
				}
			} else if (status == BusInfo.Stats.past && busInfo.getStats() == BusInfo.Stats.past) {
				if (busContract.getEndDate().after(busInfo.getEndD())) {
					putCache(busContract, status, busInfo,bodycontract,needUpdateDb);
				}
			}
			
		}
	}

	private void putCache(BusOnline busContract, BusInfo.Stats status, BusInfo busInfo, Offlinecontract bodycontract,
			boolean needUpdateDb) {
		busInfo.setStartD(busContract.getStartDate());
		busInfo.setEndD(busContract.getEndDate());
		busInfo.setBusOnline(busContract);
		busInfo.setStats(status);
		if (bodycontract != null) {
			busInfo.setContractCode(bodycontract.getContractCode());
			busInfo.setOfflinecontract(bodycontract);
		}
		if (needUpdateDb) {
			Bus record = new Bus();
			record.setId(busInfo.getBusid());
			record.setStartDay(busContract.getStartDate());
			record.setEndDay(busContract.getEndDate());
			record.setRealEndDate(busContract.getRealEndDate());
			busMapper.updateByPrimaryKeySelective(record);
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

	public boolean ishaveAd(Integer busid) {
		if (map2.containsKey(busid)) {
			List<BusOnline> list = map2.get(busid).getAllPlan();
			if (list == null || list.isEmpty()) {
				return false;
			}
			for (BusOnline busOnline : list) {
				if (busOnline.getStartDate().before(new Date())) {
					if (busOnline.getRealEndDate() == null) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public void fullBusModelGroupView(Integer busid, BusModelGroupView countTools) {
		countTools.ascTotal();
		if (map2.containsKey(busid)) {
			List<BusOnline> list = map2.get(busid).getAllPlan();
			if (list == null || list.isEmpty()) {
				//countTools.ascFree();
				return;
			}
			Date now = new Date();
			boolean isFree=true;
			for (BusOnline busOnline : list) {
				if (busOnline.getRealEndDate() != null && busOnline.getRealEndDate().before(now)) {
					continue;
				}
				
				if (busOnline.getStartDate().before(now) && busOnline.getEndDate().after(now)) {
					countTools.ascOnline();
					isFree=false;
					break;
				}
				if (busOnline.getEndDate().before(now)) {
					if (busOnline.getRealEndDate() == null) {
						countTools.ascNowDown();
						isFree=false;
						break;
					}
				}
			}
			if(isFree){
				//countTools.ascFree();;	
			}
		}
	}

	public static enum Qstats {
		online,nextMonthOver, empty, notDown;
	}
	
	

	public Qstats getStatus(Integer busid, String nextMonthParams) {
		Qstats r = Qstats.empty;
		if (map2.containsKey(busid)) {
			List<BusOnline> list = map2.get(busid).getAllPlan();
			if (list == null || list.isEmpty()) {
				return Qstats.empty;
			}
			Date now = new Date();
			boolean isFree = true;
			for (BusOnline busOnline : list) {
				if (busOnline.getRealEndDate() != null && busOnline.getRealEndDate().before(now)) {
					continue;
				} else {
					if (busOnline.getStartDate().before(now) && busOnline.getEndDate().after(now)) {
						isFree = false;
						r = Qstats.online;
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
						if (StringUtils.equals(nextMonthParams, df.format(busOnline.getEndDate()))) {
							r = Qstats.nextMonthOver;
						}
						break;
					}
					if (busOnline.getEndDate().before(now)) {
						if (busOnline.getRealEndDate() == null) {
							isFree = false;
							r = Qstats.notDown;
							break;
						}
					}

				}
			}
			if (isFree) {
				r = Qstats.empty;
			}
		}
		return r;
	}
	
	
	
	
	public StatsMonitor statControl = new StatsMonitor(this);

	@Override
	public StatsMonitor getStatsMonitor() {
		return statControl;
	}

}
