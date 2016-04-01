package com.pantuo.activiti;

import java.util.HashMap;
import java.util.logging.Logger;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
public class CheckBankTask implements TaskListener {

	private final Logger log = Logger.getLogger(CheckBankTask.class.getName());
	
	@SuppressWarnings("unchecked")
	public void notify(DelegateTask delegateTask) {
		log.info("i am CheckBankTask.");
		System.out.println("in : " + delegateTask.getVariables());
		((HashMap<String, Object>)delegateTask.getVariables().get("in")).put("next", "CheckBankTask");
		((HashMap<String, Object>)delegateTask.getVariables().get("out")).put("reponse", "subprocess:CheckBankTask->CheckMerchantTask");
	}
}