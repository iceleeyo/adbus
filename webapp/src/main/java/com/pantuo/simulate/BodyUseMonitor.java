package com.pantuo.simulate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.pantuo.mybatis.domain.BusLine;
import com.pantuo.mybatis.domain.BusLineExample;
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
				runWork();
			} finally {
				statControl.runover();
			}
		}

	}

	public void runWork() {
		log.info("runWork begin: {}", System.currentTimeMillis());

		map.clear();
		//BusLineExample example = new BusLineExample();
		//List<BusLine> lines = busLineMapper.selectByExample(example);

		List<JpaBusline> jpaLines = lineRep.findAll();
		for (JpaBusline jpaBusline : jpaLines) {
			map.put(jpaBusline.getId(), new BodyUseView(jpaBusline.getId()));
		}
		/*for (BusLine busLine : lines) {
			map.put(busLine.getId(), new BodyUseView(busLine.getId()));
		}*/
		BusExample busExample = new BusExample();
		List<Bus> bus = busMapper.selectByExample(busExample);

		String nextMonty = getNextMonty();
		for (Bus bus2 : bus) {
			Integer busId = bus2.getId();
			if (map.containsKey(bus2.getLineId())) {
				BodyUseView view = map.get(bus2.getLineId());
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

		PublishLineExample publishExample = new PublishLineExample();
		List<PublishLine> orders = publishLineMapper.selectByExample(publishExample);

		for (PublishLine publishLine : orders) {
			Integer lineId = publishLine.getLineId();
			if (map.containsKey(lineId)) {
				BodyUseView view = map.get(lineId);
				int notInstall = publishLine.getSalesNumber() - publishLine.getRemainNuber();
				view.orders.addAndGet(notInstall);
			}
		}

		for (BodyUseView bsView : map.values()) {
			bsView.reduceTotalInfo();
			//System.out.println(bsView.toString());
		}
		log.info("runWork end: {}", System.currentTimeMillis());
	}
	
	public BodyUseView getUserView(int line_id){
		
		
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
