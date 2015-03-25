package com.pantuo.activiti;


import java.util.logging.Logger;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class BusinessCheck implements JavaDelegate {
	
	private static final Logger log = Logger.getLogger(BusinessCheck.class.getName());
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// varOutFromMainprocess<->varInSubprocess
		String varInSubprocess = (String)execution.getVariable("varInSubprocess");
		log.info("in subprocess get(varInSubprocess): " + varInSubprocess);
		
		log.info("variavles=" + execution.getVariables());
		execution.setVariable("s:bc", "Subprocess:BusinessCheck");
		log.info("I am BusinessCheck in subprocess.");	
		
		execution.setVariable("varInSubprocess", "BBBB");
		log.info("in subprocess set(varInSubprocess): " + varInSubprocess);
	}
}