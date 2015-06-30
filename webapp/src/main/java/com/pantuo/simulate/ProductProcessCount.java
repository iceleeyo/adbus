package com.pantuo.simulate;

import java.util.HashMap;
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

@Component
public class ProductProcessCount implements Runnable {

	public static Map<Integer, ProductOrderCount> map = new HashMap<Integer, ProductOrderCount>();
	@Autowired
	UserAutoCompleteMapper userAutoCompleteMapper;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private HistoryService historyService;

	//@Scheduled(fixedRate = 5000)
	@Scheduled(cron = "0/5 * * * * ?")
	public void work() {
		List<Integer> proidList = userAutoCompleteMapper.selectAllProId();
		for (Integer proid : proidList) {
			ProcessInstanceQuery countQuery = runtimeService.createProcessInstanceQuery().processDefinitionKey("order");
			HistoricProcessInstanceQuery countQuery2 = historyService.createHistoricProcessInstanceQuery()
					.processDefinitionKey("order").finished();
			countQuery.variableValueEquals(ActivitiService.PRODUCT, proid.intValue());
			countQuery2.variableValueEquals(ActivitiService.PRODUCT, proid.intValue());
			int runningCount = (int) countQuery.count();
			int finishedCount = (int) countQuery2.count();
			ProductOrderCount ordersCount = new ProductOrderCount();
			ordersCount.setProduct_id(proid);
			ordersCount.setRunningCount(runningCount);
			ordersCount.setFinishedCount(finishedCount);
			map.put(proid, ordersCount);
		}
	}

	@Override
	public void run() {
		work();
	}
}
