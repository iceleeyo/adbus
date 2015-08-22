package com.pantuo.simulate;

import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.pantuo.dao.pojo.JpaBus;
import com.pantuo.mybatis.persistence.BusSelectMapper;
import com.pantuo.mybatis.persistence.UserAutoCompleteMapper;
import com.pantuo.service.ActivitiService;
import com.pantuo.service.impl.BusLineCheckServiceImpl;
import com.pantuo.util.ProductOrderCount;
import com.pantuo.vo.GroupVo;

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
public class LineCarsCount implements Runnable {
	private static Logger log = LoggerFactory.getLogger(BusLineCheckServiceImpl.class);
	public static Map<Integer, Integer> map = new java.util.concurrent.ConcurrentHashMap<Integer, Integer>();
	@Autowired
	BusSelectMapper busSelectMapper;

	//@Scheduled(fixedRate = 5000)
	@Scheduled(cron = "0/50 * * * * ?")
	public void work() {
		countCars();
	}

	public void countCars() {
		List<GroupVo> list = busSelectMapper.countLineCars(JpaBus.Category.yunyingche.ordinal());
		for (GroupVo proid : list) {
			map.put(proid.getGn1(), proid.getCount());
		}
		log.info(" LineCarsCount:{} ", list.size());
	}

	@Override
	public void run() {
		work();
	}

	public int getCarsByLines(Integer line_id) {
		return map.containsKey(line_id) ? map.get(line_id) : 0;

	}

}
