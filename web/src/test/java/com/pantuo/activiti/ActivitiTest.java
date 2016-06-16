package com.pantuo.activiti;

import com.dumbster.smtp.SimpleSmtpServer;
import com.dumbster.smtp.SmtpMessage;
import com.pantuo.ActivitiConfiguration;
import com.pantuo.TestServiceConfiguration;
import com.pantuo.dao.DaoBeanConfiguration;
import com.pantuo.dao.pojo.UserDetail;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.mail.internet.MimeUtility;

/**
 * @author tliu
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DaoBeanConfiguration.class, ActivitiConfiguration.class, TestServiceConfiguration.class})
public class ActivitiTest {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    private Deployment testDeploy;
    private ProcessInstance instance;
    private SimpleSmtpServer mailServer;

    @Before
    public void before() {
    	System.out.println("starting mock mail server");
    	mailServer = SimpleSmtpServer.start();
    	

    }
    
    @After
    public void after() {
    	System.out.println("stopping mock mail server");
    	mailServer.stop();
    }

    @Test
    public void testProcess() {
        System.out.println("deploying test process");
        testDeploy = repositoryService.createDeployment()
                .addClasspathResource("test.bpmn20.xml")
                .deploy();
        System.out.println("starting test report process");
        instance = runtimeService.startProcessInstanceByKey("testReport");

        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("accountancy").list();
        Assert.assertEquals(1, tasks.size());

        //fozzie claim task
        taskService.claim(tasks.get(0).getId(), "fozzie");

        tasks = taskService.createTaskQuery().taskAssignee("fozzie").list();
        Assert.assertEquals(1, tasks.size());

        //check management's list
        List<Task> mtasks = taskService.createTaskQuery().taskCandidateGroup("management").list();
        Assert.assertEquals(0, mtasks.size());

        //fozzie complete task
        taskService.complete(tasks.get(0).getId());
        tasks = taskService.createTaskQuery().taskAssignee("fozzie").list();
        Assert.assertEquals(0, tasks.size());

        //check management's list again
        mtasks = taskService.createTaskQuery().taskCandidateGroup("management").list();
        Assert.assertEquals(1, mtasks.size());

        //tom claim task
        taskService.claim(mtasks.get(0).getId(), "tom");

        mtasks = taskService.createTaskQuery().taskAssignee("tom").list();
        Assert.assertEquals(1, mtasks.size());

        //tom complete task
        taskService.complete(mtasks.get(0).getId());

        //check all roles list
        tasks = taskService.createTaskQuery().taskCandidateGroup("accountancy").list();
        Assert.assertEquals(0, tasks.size());
        tasks = taskService.createTaskQuery().taskAssignee("fozzie").list();
        Assert.assertEquals(0, tasks.size());
        mtasks = taskService.createTaskQuery().taskCandidateGroup("management").list();
        Assert.assertEquals(0, mtasks.size());
        mtasks = taskService.createTaskQuery().taskAssignee("tom").list();
        Assert.assertEquals(0, mtasks.size());

        HistoricProcessInstance historicProcessInstance =
                historyService.createHistoricProcessInstanceQuery().processInstanceId(instance.getId()).singleResult();
        Assert.assertNotNull(historicProcessInstance);

        System.out.println("Process instance (id = " + historicProcessInstance.getId() + ") end time: " + historicProcessInstance.getEndTime());
        
    	System.out.println("destroying test process");
    	repositoryService.deleteDeployment(testDeploy.getId(), true);

    }
 
}
