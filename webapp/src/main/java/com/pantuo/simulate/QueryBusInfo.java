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

import com.pantuo.mybatis.domain.Bodycontract;
import com.pantuo.mybatis.domain.BusContract;
import com.pantuo.mybatis.domain.BusContractExample;
import com.pantuo.mybatis.domain.BusLock;
import com.pantuo.mybatis.domain.BusLockExample;
import com.pantuo.mybatis.persistence.BodycontractMapper;
import com.pantuo.mybatis.persistence.BusContractMapper;
import com.pantuo.mybatis.persistence.BusLockMapper;
import com.pantuo.mybatis.persistence.BusSelectMapper;
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
public class QueryBusInfo implements Runnable ,ScheduleStatsInter{
	private static Logger log = LoggerFactory.getLogger(QueryBusInfo.class);

	public static BusInfo emptybusInfo = new BusInfo();
	public static Map<Integer, BusInfo> map = new java.util.concurrent.ConcurrentHashMap<Integer, BusInfo>();
	@Autowired
	BusContractMapper busContractMapper;
	@Autowired
	BodycontractMapper bodycontractMapper;

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
		Date today = new Date();
		BusContractExample example=new BusContractExample();
		BusContractExample.Criteria criteria=example.createCriteria();
		criteria.andStartDateLessThan(today);
		criteria.andEndDateGreaterThan(today);
		List<BusContract> list=busContractMapper.selectByExample(example);
		for (BusContract busContract : list) {
			if(busContract!=null){
				Bodycontract bodycontract=bodycontractMapper.selectByPrimaryKey(busContract.getContractid());
				int busid = busContract.getBusid();
				if (!map.containsKey(busid)) {
					map.put(busid, new BusInfo(busid));
				}
				BusInfo busInfo=map.get(busid);
				busInfo.setStartD(busContract.getStartDate());
				busInfo.setEndD(busContract.getEndDate());
				if(bodycontract!=null){
					busInfo.setContractCode(bodycontract.getContractCode());
				}
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
	public StatsMonitor statControl = new StatsMonitor(this);
	@Override
	public StatsMonitor getStatsMonitor() {
		return statControl;
	}

}
