package com.pantuo.activiti;


import java.util.logging.Logger;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import com.pantuo.dao.pojo.UserDetail;

public class GenerateSchedule implements JavaDelegate {
	
	private static final Logger log = Logger.getLogger(GenerateSchedule.class.getName());
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// varOutFromMainprocess<->varInSubprocess
		UserDetail owner = (UserDetail)execution.getVariable("_owner");
		log.info("Generating schedule for order owned by " + owner);
	}
}