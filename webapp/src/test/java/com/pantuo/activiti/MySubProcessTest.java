package com.pantuo.activiti;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.pantuo.ActivitiConfiguration;
import com.pantuo.dao.DaoBeanConfiguration;

/**
 * @author shirdrn
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DaoBeanConfiguration.class, ActivitiConfiguration.class })
public class MySubProcessTest {

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private HistoryService historyService;

	private ProcessInstance instance;

	@Before
	public void before() {
		System.out.println("deploying test process");
		Deployment deployment = repositoryService.createDeployment().addClasspathResource("MySubprocess.bpmn20.xml")
				.deploy();
		System.out.println("starting test report process");

	}

	protected void destroy() {
		//repositoryService.deleteDeployment(deploymentId, true);	
	}

	@Test
	public void testSubProcess() {
		// prepare data packet
		Map<String, Object> variables = new HashMap<String, Object>();
		Map<String, Object> subVariables = new HashMap<String, Object>();
		variables.put("maxTransCount", 1000000);
		variables.put("merchant", "ICBC");
		variables.put("protocol", "UM32");
		variables.put("repository", "10.10.38.99:/home/shirdrn/repository");
		variables.put("in", subVariables);
		variables.put("out", new HashMap<String, Object>());

		// start process instance
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("MySubprocess", variables);

		// enter subprocess
		List<Task> tasks = taskService.createTaskQuery().processInstanceId(pi.getId()).orderByTaskName().asc().list();
		Assert.assertEquals(2, tasks.size());

		for (Task task : tasks) { 
			taskService.complete(task.getId());
		}

		// leave subprocess
		Task collectTask = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
		Assert.assertEquals("Collect message", collectTask.getName());

		Map<String, Object> taskVariables = new HashMap<String, Object>();
		taskVariables.put("att", "anything you need");
		taskService.setVariable(collectTask.getId(), "oper", "shirdrn");
		taskService.complete(collectTask.getId(), taskVariables);
	}

}