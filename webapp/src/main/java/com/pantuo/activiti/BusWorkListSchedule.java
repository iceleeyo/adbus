package com.pantuo.activiti;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pantuo.mybatis.domain.Bodycontract;
import com.pantuo.mybatis.persistence.BodycontractMapper;
import com.pantuo.service.ActivitiService;

@Service("gusWorkListSchedule")
public class BusWorkListSchedule implements JavaDelegate {
	private static final Logger log = LoggerFactory.getLogger(BusWorkListSchedule.class);
	@Autowired
	BodycontractMapper bodycontractMapper;

	//@Override
	public void execute(DelegateExecution execution) throws Exception {
		// varOutFromMainprocess<->varInSubprocess
		/*
		Boolean b = (Boolean) execution.getVariable("_isTest");
		execution.setVariable("scheduleResult", true);
		execution.setVariable("scheduleComments", "BusWorkListSchedule");
		System.out.println(execution.getVariables());
		System.out.println(execution.getCurrentActivityName());
		*/
		Integer bodyContract_id = (Integer) execution.getVariable(ActivitiService.ORDER_ID);
		if (bodyContract_id != null) {
			Bodycontract obj = bodycontractMapper.selectByPrimaryKey(bodyContract_id);
			if (obj != null) {
				obj.setIsSchedule(true);
				bodycontractMapper.updateByPrimaryKey(obj);
				log.info("set bodyContract schedule!");
			}
		}
	}
}