package com.pantuo.service.impl;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pantuo.pojo.TableRequest;
import com.pantuo.service.ActivitiService;
import com.pantuo.service.ActivitiService.TaskQueryType;
import com.pantuo.service.security.Request;
import com.pantuo.service.OrderService;
import com.pantuo.service.PayContractService;

@Service
public class TaskCountService {

	@Autowired
	PayContractService payContractService;

	@Autowired
	ActivitiService activitiService;

	@Autowired
	OrderService orderService;

	public Map<String, Long> countTaskByType(String query, TableRequest req, Principal principal) {
		Map<String, Long> map = new HashMap<String, Long>();
		if (StringUtils.contains(query, "notPayContracts")) {
			map.put("notPayContracts", payContractService.getAllNotPayContractCount(req, principal));
		}

		if (StringUtils.contains(query, "orderTaskCount")) {
			map.put("orderTaskCount", activitiService.findTaskCount(1, principal, req, TaskQueryType.task));
		}

		if (StringUtils.contains(query, "planOrders")) {
			map.put("planOrders", orderService.countPlanOrders(req));
		}
		if (StringUtils.contains(query, "planContract")) {
			Map<String, String> filter = new HashMap<String, String>();
			filter.put("q", "contract");
			req.setFilter(filter);
			map.put("planContract", orderService.countPlanOrders(req));
		}
		if (StringUtils.contains(query, "payPlanOrders")) {
			map.put("payPlanOrders", orderService.countPayPlanOrders(req, principal));
		}
		return map;
	}

}
