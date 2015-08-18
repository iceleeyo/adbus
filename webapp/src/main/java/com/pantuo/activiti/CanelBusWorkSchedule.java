package com.pantuo.activiti;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("canelWorkListSchedule")
public class CanelBusWorkSchedule implements JavaDelegate {
	private static final Logger log = LoggerFactory.getLogger(CanelBusWorkSchedule.class);

	//@Override
	public void execute(DelegateExecution execution) throws Exception {
		// varOutFromMainprocess<->varInSubprocess
		Boolean b = (Boolean) execution.getVariable("_isTest");
		execution.setVariable("scheduleResult", false);
		execution.setVariable("scheduleComments", "1111111");
		System.out.println(execution.getVariables());

		System.out.println(execution.getCurrentActivityName());
	}
}