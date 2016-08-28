package com.pantuo.util;

import org.activiti.engine.delegate.event.ActivitiEntityEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiVariableEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pantuo.service.impl.MailServiceImpl;

//Event listener implementation 事件监听实现

//实现事件监听器的唯一要求是实现org.activiti.engine.delegate.event.ActivitiEventListener。 西面是一个实现监听器的例子，它会把所有监听到的事件打印到标准输出中，包括job执行的事件异常：

public class MyEventListener implements ActivitiEventListener {
	private static Logger log = LoggerFactory.getLogger(MyEventListener.class);

	@Override
	public void onEvent(ActivitiEvent event) {
		switch (event.getType()) {

		case TASK_CREATED :
		case TASK_COMPLETED:
			
			
			ActivitiEntityEvent activitiEntityEvent = (ActivitiEntityEvent) event;
			log.info("Event: " + event.getType() + " " + activitiEntityEvent.getExecutionId() + activitiEntityEvent.getEntity() );
			break;

		case JOB_EXECUTION_FAILURE:
			System.out.println("A job has failed...");
			break;
		case VARIABLE_CREATED:
			ActivitiVariableEvent variableEvent = (ActivitiVariableEvent) event;
			log.debug("Event: " + event.getType() + " " + variableEvent.getVariableName() + " (" + variableEvent.getVariableType().getTypeName() + ") = "
					+ variableEvent.getVariableValue());
			break;

		case VARIABLE_DELETED:
			log.debug("Event: " + event.getType());
			break;
		case VARIABLE_UPDATED:
			log.debug("Event: " + event.getType());
			break;
		default:
			System.out.println("Event received: " + event.getType()+" from :"+event.toString());
		}
	}

	@Override
	public boolean isFailOnException() {
		// The logic in the onEvent method of this listener is not critical, exceptions
		// can be ignored if logging fails...
		return false;
	}
}
