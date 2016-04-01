package com.pantuo.activiti;

import java.util.Map;
import java.util.logging.Logger;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
public class CollectMessageTask implements TaskListener {  
	  
    private final Logger log = Logger.getLogger(CollectMessageTask.class.getName());  
      
    @SuppressWarnings("unchecked")  
    public void notify(DelegateTask delegateTask) {  
        log.info("i am CollectMessageTask.");  
        System.out.println("out : " + (Map<String, Object>)delegateTask.getVariables().get("out"));  
        System.out.println("all : " + delegateTask.getVariables());  
    }  
} 