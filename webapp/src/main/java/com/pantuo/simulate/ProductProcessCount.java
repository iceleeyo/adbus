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

import com.pantuo.mybatis.persistence.ProductMapper;
import com.pantuo.mybatis.persistence.UserAutoCompleteMapper;
import com.pantuo.service.ActivitiService;
import com.pantuo.util.OrdersCount;

@Component
public class ProductProcessCount implements Runnable {

	public static Map<Integer, OrdersCount> map = new HashMap<Integer, OrdersCount>();
	@Autowired
	UserAutoCompleteMapper userAutoCompleteMapper;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private HistoryService historyService;

	//@Scheduled(fixedRate = 5000)
	@Scheduled(cron = "0/5 * * * * ?")
	public void work() {
		ProcessInstanceQuery countQuery = runtimeService.createProcessInstanceQuery().processDefinitionKey("order");
		HistoricProcessInstanceQuery countQuery2 = historyService.createHistoricProcessInstanceQuery()
				.processDefinitionKey("order").finished();
		List<Integer> proidList = userAutoCompleteMapper.selectAllProId();
		for (Integer proid : proidList) {
			countQuery.variableValueEquals(ActivitiService.PRODUCT, proid);
			countQuery2.variableValueEquals(ActivitiService.PRODUCT, proid);
			int runningCount = (int) countQuery.count();
			int finishedCount = (int) countQuery2.count();
			OrdersCount ordersCount = new OrdersCount();
			ordersCount.setProduct_id(proid);
			ordersCount.setRunningCount(runningCount);
			ordersCount.setFinishedCount(finishedCount);
			map.put(proid, ordersCount);
		}
		System.out.println(map.toString());
	}

	@Override
	public void run() {
		work();
	}
}
