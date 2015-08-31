package com.pantuo.simulate;

import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.pantuo.mybatis.persistence.UserAutoCompleteMapper;
import com.pantuo.service.ActivitiService;
import com.pantuo.util.ProductOrderCount;

/**
 * 
 * <b><code>ProductProcessCount</code></b>
 * <p>
 * 产品 相应的订单(分运行中的订单和结束的订单个数)个数统计
 * </p>
 * <b>Creation Time:</b> 2015年6月30日 下午7:06:08
 * @author impanxh@gmail.com
 * @since pantuo 1.0-SNAPSHOT
 */
@Component
public class ProductProcessCount implements Runnable, ScheduleStatsInter {
	private StatsMonitor statControl = new StatsMonitor(this);
	public static Map<Integer, ProductOrderCount> map = new java.util.concurrent.ConcurrentHashMap<Integer, ProductOrderCount>();
	@Autowired
	UserAutoCompleteMapper userAutoCompleteMapper;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private HistoryService historyService;

	//@Scheduled(fixedRate = 5000)
	@Scheduled(cron = "0/50 * * * * ?")
	public void work() {
		if (statControl.isRunning) {
			try {
				List<Integer> proidList = userAutoCompleteMapper.selectAllProId();
				for (Integer proid : proidList) {
					ProcessInstanceQuery runningQuery = runtimeService.createProcessInstanceQuery()
							.processDefinitionKey("order");
					HistoricProcessInstanceQuery overQuery = historyService.createHistoricProcessInstanceQuery()
							.processDefinitionKey("order").finished();
					runningQuery.variableValueEquals(ActivitiService.PRODUCT, proid.intValue());
					overQuery.variableValueEquals(ActivitiService.PRODUCT, proid.intValue());
					int runningCount = (int) runningQuery.count();
					int finishedCount = (int) overQuery.count();
					ProductOrderCount ordersCount = new ProductOrderCount();
					ordersCount.setProduct_id(proid);
					ordersCount.setRunningCount(runningCount);
					ordersCount.setFinishedCount(finishedCount);
					map.put(proid, ordersCount);
				}
			} finally {
				statControl.runover();
			}
		}
	}

	@Override
	public void run() {
		work();
	}

	@Override
	public StatsMonitor getStatsMonitor() {
		return statControl;
	}
}
