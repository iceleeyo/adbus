package com.pantuo.activiti;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
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
public class MainprocessTest {
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
		Deployment deployment = repositoryService.createDeployment()
				.addClasspathResource("Subprocess.Check.bpmn20.xml").deploy();
		//.add(deployment.getId());

		deployment = repositoryService.createDeployment()
				.addClasspathResource("Subprocess.Mainprocess.bpmn20.xml").deploy();
		//	deploymentIdList.add(deployment.getId());
	}

	//	@Override
	protected void destroy() throws Exception {
		/*for (String deployment : deploymentIdList) {
				repositoryService.deleteDeployment(deployment, true);
			}*/
	}

	@Test
	public void testSubProcess() {
		System.out.println(1);
		// prepare data packet
		Map<String, Object> variables = new HashMap<String, Object>();
		Map<String, Object> subVariables = new HashMap<String, Object>();
		variables.put("protocol", "UM32");
		variables.put("repository", "10.10.38.99:/home/shirdrn/repository");
		variables.put("in", subVariables);
		variables.put("out", new HashMap<String, Object>());

		// start process instance
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("Mainprocess", variables);
		System.out.println();
		Assert.assertEquals(true, pi.isEnded());
	}

}