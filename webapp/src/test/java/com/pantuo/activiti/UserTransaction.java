package com.pantuo.activiti;


import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import java.util.logging.Logger;

public class UserTransaction implements JavaDelegate {
	
	private static final Logger log = Logger.getLogger(UserTransaction.class.getName());
	
	//@Override
	public void execute(DelegateExecution execution) throws Exception {
		log.info("variavles=" + execution.getVariables());
		execution.setVariable("myorder", "order:1234,staticId:32323");
		execution.setVariable("varOutFromMainprocess", "AAAA");
		log.info("in mainprocess set(varOutFromMainprocess): " + execution.getVariable("varOutFromMainprocess"));
	}
}
