package com.pantuo.activiti;


import java.util.HashMap;
import java.util.logging.Logger;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
 
public class CheckMerchantTask implements TaskListener {
 
	/**
	 * Comment here.
	 *
	 * @since pantuotech 1.0-SNAPSHOT
	 */
	private static final long serialVersionUID = 1L;
	private final Logger log = Logger.getLogger(CheckMerchantTask.class.getName());
	
	@SuppressWarnings("unchecked")
	public void notify(DelegateTask delegateTask) {
		log.info("i am CheckMerchantTask.");
		System.out.println("in : " + delegateTask.getVariables());
		((HashMap<String, Object>)delegateTask.getVariables().get("in")).put("previous", "CheckMerchantTask");
	}
}