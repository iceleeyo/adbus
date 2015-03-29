package com.pantuo.activiti;

import com.dumbster.smtp.SimpleSmtpServer;
import com.dumbster.smtp.SmtpMessage;
import com.pantuo.ActivitiConfiguration;
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
@ContextConfiguration(classes = {DaoBeanConfiguration.class, ActivitiConfiguration.class})
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
    
    
    private void checkLastMailContent( int expectedEmailNumber, String expectSubject, String expectBodySnippet) {
    	int mailNumber = mailServer.getReceivedEmailSize();
    	Assert.assertEquals(expectedEmailNumber, mailNumber);
    	if (expectedEmailNumber > 0) {
	    	Iterator mailIter = mailServer.getReceivedEmail();
	    	for (int i = 0; i < expectedEmailNumber - 1; i++)
	    		mailIter.next();
	    	SmtpMessage mail = (SmtpMessage) mailIter.next();
	    	UserDetail user = new UserDetail("tliu", "tliu", "Tony", "Liu", "tliutest@gmail.com");
	    	try {
				String expectedSubject = MimeUtility.fold(9,
					    MimeUtility.encodeText(expectSubject, "utf-8", null));
		    	Assert.assertEquals(expectedSubject.toUpperCase(), mail.getHeaderValue("Subject").toUpperCase());
			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail();
			}
	    	Assert.assertTrue(mail.getHeaderValue("From").indexOf("pantuo-service@gmail.com") >= 0);
	    	Assert.assertTrue(mail.getHeaderValue("To").indexOf(user.getUser().getEmail()) >= 0 );
    	}
    }
    @Test
    public void testMaterialProccess() {
    	UserDetail user = new UserDetail("tliu", "tliu", "Tony", "Liu", "tliutest@gmail.com");
    	UserDetail sbUser = new UserDetail("sbuser", "sbuser", "Shiba", "MaterialManager1", "sbuser@gmail.com");
    	UserDetail bgUser = new UserDetail("bguser", "bguser", "Beiguang", "Manager1", "bguser@gmail.com");
    	String beiguangManager = "BeiguangMaterialManager";
    	String shibaManager = "ShibaMaterialManager";

    	Map<String, Object> initParams = new HashMap<String, Object> ();
    	initParams.put("_owner", user);
    	initParams.put("_now", new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date()));

    	/** 0.检查任务列表 **/
    	List<Task> tasks = taskService.createTaskQuery().taskCandidateUser("tliu").list();
    	Assert.assertEquals(0, tasks.size());
    	//check shiba manager's list
    	List<Task> mtasks = taskService.createTaskQuery().taskCandidateGroup(shibaManager).list();
    	Assert.assertEquals(0, mtasks.size());
    	//check beiguang manager's list
    	mtasks = taskService.createTaskQuery().taskCandidateGroup(beiguangManager).list();
    	Assert.assertEquals(0, mtasks.size());
    	
    	//user start material process
    	instance = runtimeService.startProcessInstanceByKey("material", initParams);
    	
    	/** 1. submitInfo task **/
    	//check and complete submitInfo task
    	tasks = taskService.createTaskQuery().taskCandidateUser("tliu").list();
    	Task task = null;
    	Assert.assertEquals(1, tasks.size());
    	task = tasks.get(0);
    	Assert.assertEquals("submitInfo", task.getTaskDefinitionKey());
    	//also check material manager's list
    	mtasks = taskService.createTaskQuery().taskCandidateGroup(shibaManager).list();
    	Assert.assertEquals(1, mtasks.size());
    	Assert.assertEquals("submitInfo", mtasks.get(0).getTaskDefinitionKey());
    	
    	taskService.claim(task.getId(), user.getUsername());
    	/* -- 用户提交Info表格 --*/
    	taskService.complete(task.getId());
    	
    	/** 2. submitMaterial task **/
    	//check and complete submitMaterial task
    	tasks = taskService.createTaskQuery().taskCandidateUser("tliu").list();
    	Assert.assertEquals(1, tasks.size());
    	task = tasks.get(0);
    	Assert.assertEquals("submitMaterial", task.getTaskDefinitionKey());
    	//also check material manager's list
    	mtasks = taskService.createTaskQuery().taskCandidateGroup(shibaManager).list();
    	Assert.assertEquals(1, mtasks.size());
    	Assert.assertEquals("submitMaterial", mtasks.get(0).getTaskDefinitionKey());
    	
    	taskService.claim(task.getId(), user.getUsername());
    	/* -- 用户提交Material表格 --*/
    	taskService.complete(task.getId());
    	
    	/** 3.approve1 task **/
    	//check user's task list
    	tasks = taskService.createTaskQuery().taskCandidateUser("tliu").list();
    	Assert.assertEquals(0, tasks.size());
    	//check shiba manager's list
    	mtasks = taskService.createTaskQuery().taskCandidateGroup(shibaManager).includeProcessVariables().list();
    	Assert.assertEquals(1, mtasks.size());
    	task = mtasks.get(0);
    	Assert.assertEquals("approve1", task.getTaskDefinitionKey());
    	Object ownerObj = task.getProcessVariables().get("_owner");
    	Assert.assertTrue(ownerObj instanceof UserDetail);
    	UserDetail owner = (UserDetail)ownerObj;
    	Assert.assertEquals("tliu", owner.getUsername());
    	
    	taskService.claim(task.getId(), sbUser.getUsername());
    	/* -- 管理员提交审核结果 --*/
    	runtimeService.setVariable(task.getExecutionId(), "approve1Result", true);
    	runtimeService.setVariable(task.getExecutionId(), "approve1Comments", "世巴：内容无不妥");
    	taskService.complete(task.getId());
    	
    	/** 4.经过approve1Gateway到达北广审核任务 **/
    	
    	/** 5.approve2 task**/
    	//check user's task list
    	tasks = taskService.createTaskQuery().taskCandidateUser("tliu").list();
    	Assert.assertEquals(0, tasks.size());
    	//check shiba manager's list
    	mtasks = taskService.createTaskQuery().taskCandidateGroup(shibaManager).list();
    	Assert.assertEquals(0, mtasks.size());
    	//check beiguang manager's list
    	mtasks = taskService.createTaskQuery().taskCandidateGroup(beiguangManager).includeProcessVariables().list();
    	Assert.assertEquals(1, mtasks.size());
    	task = mtasks.get(0);
    	Assert.assertEquals("approve2", task.getTaskDefinitionKey());
    	ownerObj = task.getProcessVariables().get("_owner");
    	Assert.assertTrue(ownerObj instanceof UserDetail);
    	owner = (UserDetail)ownerObj; 
    	Assert.assertEquals("tliu", owner.getUsername());
    	
    	taskService.claim(task.getId(), sbUser.getUsername());
    	/* -- 管理员提交审核结果 --*/
    	runtimeService.setVariable(task.getExecutionId(), "approve2Result", true);
    	runtimeService.setVariable(task.getExecutionId(), "approve2Comments", "北广：内容无不妥");
    	taskService.complete(task.getId());
    	
    	/** 6. 经过approve2Gateway到达发送审核通过邮件任务. **/
    	
    	/** 7. 发送审核通过邮件给广告主，流程结束**/
    	tasks = taskService.createTaskQuery().taskCandidateUser("tliu").list();
    	Assert.assertEquals(0, tasks.size());
    	//check shiba manager's list
    	mtasks = taskService.createTaskQuery().taskCandidateGroup(shibaManager).list();
    	Assert.assertEquals(0, mtasks.size());
    	//check beiguang manager's list
    	mtasks = taskService.createTaskQuery().taskCandidateGroup(beiguangManager).list();
    	Assert.assertEquals(0, mtasks.size());
    	
    	//check email content
    	checkLastMailContent( 1, "您的广告素材已通过审核", "您提交的广告素材已于");
    	
    	List<HistoricProcessInstance> history = historyService.createHistoricProcessInstanceQuery().processInstanceId(instance.getId()).involvedUser("tliu").includeProcessVariables().list();
    	Assert.assertEquals(1, history.size());
    	Assert.assertEquals("北广：内容无不妥",history.get(0).getProcessVariables().get("approve2Comments"));
    }
    
    @Test
    public void testMaterialBeiguangDisapproveProccess() {
    	UserDetail user = new UserDetail("tliu", "tliu", "Tony", "Liu", "tliutest@gmail.com");
    	UserDetail sbUser = new UserDetail("sbuser", "sbuser", "Shiba", "MaterialManager1", "sbuser@gmail.com");
    	UserDetail bgUser = new UserDetail("bguser", "bguser", "Beiguang", "Manager1", "bguser@gmail.com");
    	String beiguangManager = "BeiguangMaterialManager";
    	String shibaManager = "ShibaMaterialManager";

    	Map<String, Object> initParams = new HashMap<String, Object> ();
    	initParams.put("_owner", user);
    	initParams.put("_now", new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date()));

    	/** 0.检查任务列表 **/
    	List<Task> tasks = taskService.createTaskQuery().taskCandidateUser("tliu").list();
    	Assert.assertEquals(0, tasks.size());
    	//check shiba manager's list
    	List<Task> mtasks = taskService.createTaskQuery().taskCandidateGroup(shibaManager).list();
    	Assert.assertEquals(0, mtasks.size());
    	//check beiguang manager's list
    	mtasks = taskService.createTaskQuery().taskCandidateGroup(beiguangManager).list();
    	Assert.assertEquals(0, mtasks.size());
    	
    	//user start material process
    	instance = runtimeService.startProcessInstanceByKey("material", initParams);
    	
    	/** 1. submitInfo task **/
    	//check and complete submitInfo task
    	tasks = taskService.createTaskQuery().taskCandidateUser("tliu").list();
    	Task task = null;
    	Assert.assertEquals(1, tasks.size());
    	task = tasks.get(0);
    	Assert.assertEquals("submitInfo", task.getTaskDefinitionKey());
    	//also check material manager's list
    	mtasks = taskService.createTaskQuery().taskCandidateGroup(shibaManager).list();
    	Assert.assertEquals(1, mtasks.size());
    	Assert.assertEquals("submitInfo", mtasks.get(0).getTaskDefinitionKey());
    	
    	taskService.claim(task.getId(), user.getUsername());
    	/* -- 用户提交Info表格 --*/
    	taskService.complete(task.getId());
    	
    	/** 2. submitMaterial task **/
    	//check and complete submitMaterial task
    	tasks = taskService.createTaskQuery().taskCandidateUser("tliu").list();
    	Assert.assertEquals(1, tasks.size());
    	task = tasks.get(0);
    	Assert.assertEquals("submitMaterial", task.getTaskDefinitionKey());
    	//also check material manager's list
    	mtasks = taskService.createTaskQuery().taskCandidateGroup(shibaManager).list();
    	Assert.assertEquals(1, mtasks.size());
    	Assert.assertEquals("submitMaterial", mtasks.get(0).getTaskDefinitionKey());
    	
    	taskService.claim(task.getId(), user.getUsername());
    	/* -- 用户提交Material表格 --*/
    	taskService.complete(task.getId());
    	
    	/** 3.approve1 task (审核不通过，需要修改info) **/
    	//check user's task list
    	tasks = taskService.createTaskQuery().taskCandidateUser("tliu").list();
    	Assert.assertEquals(0, tasks.size());
    	//check shiba manager's list
    	mtasks = taskService.createTaskQuery().taskCandidateGroup(shibaManager).includeProcessVariables().list();
    	Assert.assertEquals(1, mtasks.size());
    	task = mtasks.get(0);
    	Assert.assertEquals("approve1", task.getTaskDefinitionKey());
    	Object ownerObj = task.getProcessVariables().get("_owner");
    	Assert.assertTrue(ownerObj instanceof UserDetail);
    	UserDetail owner = (UserDetail)ownerObj;
    	Assert.assertEquals("tliu", owner.getUsername());
    	
    	taskService.claim(task.getId(), sbUser.getUsername());
    	/* -- 管理员提交审核结果 --*/
    	runtimeService.setVariable(task.getExecutionId(), "approve1Result", false);
    	runtimeService.setVariable(task.getExecutionId(), "approve1Modify", "info");
    	runtimeService.setVariable(task.getExecutionId(), "approve1Comments", "世巴：info需要修改，请重新填写");
    	taskService.complete(task.getId());
    	
    	/** 4.经过approve1Gateway自动发送邮件并到达 1. **/
    	//check email content
    	checkLastMailContent(1, "您的广告素材需要修改", "世巴：info需要修改，请重新填写");
    	
    	/** 1.2 submitInfo task **/
    	//check and complete submitInfo task
    	tasks = taskService.createTaskQuery().taskCandidateUser("tliu").list();
    	Assert.assertEquals(1, tasks.size());
    	task = tasks.get(0);
    	Assert.assertEquals("submitInfo", task.getTaskDefinitionKey());
    	//also check material manager's list
    	mtasks = taskService.createTaskQuery().taskCandidateGroup(shibaManager).list();
    	Assert.assertEquals(1, mtasks.size());
    	Assert.assertEquals("submitInfo", mtasks.get(0).getTaskDefinitionKey());
    	
    	taskService.claim(task.getId(), user.getUsername());
    	/* -- 用户提交Info表格 --*/
    	taskService.complete(task.getId());
    	
    	/** 2.2 submitMaterial task **/
    	//check and complete submitMaterial task
    	tasks = taskService.createTaskQuery().taskCandidateUser("tliu").list();
    	Assert.assertEquals(1, tasks.size());
    	task = tasks.get(0);
    	Assert.assertEquals("submitMaterial", task.getTaskDefinitionKey());
    	//also check material manager's list
    	mtasks = taskService.createTaskQuery().taskCandidateGroup(shibaManager).list();
    	Assert.assertEquals(1, mtasks.size());
    	Assert.assertEquals("submitMaterial", mtasks.get(0).getTaskDefinitionKey());
    	
    	taskService.claim(task.getId(), user.getUsername());
    	/* -- 用户提交Material表格 --*/
    	taskService.complete(task.getId());
    	
    	/** 3.2 approve1 task **/
    	//check user's task list
    	tasks = taskService.createTaskQuery().taskCandidateUser("tliu").list();
    	Assert.assertEquals(0, tasks.size());
    	//check shiba manager's list
    	mtasks = taskService.createTaskQuery().taskCandidateGroup(shibaManager).includeProcessVariables().list();
    	Assert.assertEquals(1, mtasks.size());
    	task = mtasks.get(0);
    	Assert.assertEquals("approve1", task.getTaskDefinitionKey());
    	ownerObj = task.getProcessVariables().get("_owner");
    	Assert.assertTrue(ownerObj instanceof UserDetail);
    	owner = (UserDetail)ownerObj;
    	Assert.assertEquals("tliu", owner.getUsername());
    	
    	taskService.claim(task.getId(), sbUser.getUsername());
    	/* -- 管理员提交审核结果 --*/
    	runtimeService.setVariable(task.getExecutionId(), "approve1Result", true);
    	runtimeService.setVariable(task.getExecutionId(), "approve1Comments", "世巴：修改后，内容无不妥");
    	taskService.complete(task.getId());
    	
    	/** 4.2经过approve1Gateway到达北广审核任务. **/
    	
    	/** 5.approve2 task (审核不通过) **/
    	//check user's task list
    	tasks = taskService.createTaskQuery().taskCandidateUser("tliu").list();
    	Assert.assertEquals(0, tasks.size());
    	//check shiba manager's list
    	mtasks = taskService.createTaskQuery().taskCandidateGroup(shibaManager).list();
    	Assert.assertEquals(0, mtasks.size());
    	//check beiguang manager's list
    	mtasks = taskService.createTaskQuery().taskCandidateGroup(beiguangManager).includeProcessVariables().list();
    	Assert.assertEquals(1, mtasks.size());
    	task = mtasks.get(0);
    	Assert.assertEquals("approve2", task.getTaskDefinitionKey());
    	ownerObj = task.getProcessVariables().get("_owner");
    	Assert.assertTrue(ownerObj instanceof UserDetail);
    	owner = (UserDetail)ownerObj; 
    	Assert.assertEquals("tliu", owner.getUsername());
    	
    	taskService.claim(task.getId(), sbUser.getUsername());
    	/* -- 管理员提交审核结果 --*/
    	runtimeService.setVariable(task.getExecutionId(), "approve2Result", false);
    	runtimeService.setVariable(task.getExecutionId(), "approve2Comments", "北广：内容material需要修改");
    	taskService.complete(task.getId());
    	
    	/** 6. 经过approve2Gateway自动发送邮件并到达 3. **/
    	
    	/** 3.3 approve1 task (审核不通过，需要修改material) **/
    	//check user's task list
    	tasks = taskService.createTaskQuery().taskCandidateUser("tliu").list();
    	Assert.assertEquals(0, tasks.size());
    	//check shiba manager's list
    	mtasks = taskService.createTaskQuery().taskCandidateGroup(shibaManager).includeProcessVariables().list();
    	Assert.assertEquals(1, mtasks.size());
    	task = mtasks.get(0);
    	Assert.assertEquals("approve1", task.getTaskDefinitionKey());
    	ownerObj = task.getProcessVariables().get("_owner");
    	Assert.assertTrue(ownerObj instanceof UserDetail);
    	owner = (UserDetail)ownerObj;
    	Assert.assertEquals("tliu", owner.getUsername());
    	
    	taskService.claim(task.getId(), sbUser.getUsername());
    	/* -- 管理员提交审核结果 --*/
    	runtimeService.setVariable(task.getExecutionId(), "approve1Result", false);
    	runtimeService.setVariable(task.getExecutionId(), "approve1Modify", "material");
    	runtimeService.setVariable(task.getExecutionId(), "approve1Comments", "世巴：北广审核结果需要修改material，请重新填写");
    	taskService.complete(task.getId());
    	
    	/** 4.3.经过approve1Gateway自动发送邮件并到达2. **/
    	//check email content
    	checkLastMailContent(2, "您的广告素材需要修改", "世巴：北广审核结果需要修改material，请重新填写");
    	
    	/** 2.3 submitMaterial task **/
    	//check and complete submitMaterial task
    	tasks = taskService.createTaskQuery().taskCandidateUser("tliu").list();
    	Assert.assertEquals(1, tasks.size());
    	task = tasks.get(0);
    	Assert.assertEquals("submitMaterial", task.getTaskDefinitionKey());
    	//also check material manager's list
    	mtasks = taskService.createTaskQuery().taskCandidateGroup(shibaManager).list();
    	Assert.assertEquals(1, mtasks.size());
    	Assert.assertEquals("submitMaterial", mtasks.get(0).getTaskDefinitionKey());
    	
    	taskService.claim(task.getId(), user.getUsername());
    	/* -- 用户提交Material表格 --*/
    	taskService.complete(task.getId());
    	
    	/** 3.3 approve1 task **/
    	//check user's task list
    	tasks = taskService.createTaskQuery().taskCandidateUser("tliu").list();
    	Assert.assertEquals(0, tasks.size());
    	//check shiba manager's list
    	mtasks = taskService.createTaskQuery().taskCandidateGroup(shibaManager).includeProcessVariables().list();
    	Assert.assertEquals(1, mtasks.size());
    	task = mtasks.get(0);
    	Assert.assertEquals("approve1", task.getTaskDefinitionKey());
    	ownerObj = task.getProcessVariables().get("_owner");
    	Assert.assertTrue(ownerObj instanceof UserDetail);
    	owner = (UserDetail)ownerObj;
    	Assert.assertEquals("tliu", owner.getUsername());
    	
    	taskService.claim(task.getId(), sbUser.getUsername());
    	/* -- 管理员提交审核结果 --*/
    	runtimeService.setVariable(task.getExecutionId(), "approve1Result", true);
    	runtimeService.setVariable(task.getExecutionId(), "approve1Comments", "世巴：修改后，内容无不妥");
    	taskService.complete(task.getId());
    	
    	/** 4.3经过approve1Gateway到达北广审核任务. **/

    	/** 5.2 approve2 task**/
    	//check user's task list
    	tasks = taskService.createTaskQuery().taskCandidateUser("tliu").list();
    	Assert.assertEquals(0, tasks.size());
    	//check shiba manager's list
    	mtasks = taskService.createTaskQuery().taskCandidateGroup(shibaManager).list();
    	Assert.assertEquals(0, mtasks.size());
    	//check beiguang manager's list
    	mtasks = taskService.createTaskQuery().taskCandidateGroup(beiguangManager).includeProcessVariables().list();
    	Assert.assertEquals(1, mtasks.size());
    	task = mtasks.get(0);
    	Assert.assertEquals("approve2", task.getTaskDefinitionKey());
    	ownerObj = task.getProcessVariables().get("_owner");
    	Assert.assertTrue(ownerObj instanceof UserDetail);
    	owner = (UserDetail)ownerObj; 
    	Assert.assertEquals("tliu", owner.getUsername());
    	
    	taskService.claim(task.getId(), sbUser.getUsername());
    	/* -- 管理员提交审核结果 --*/
    	runtimeService.setVariable(task.getExecutionId(), "approve2Result", true);
    	runtimeService.setVariable(task.getExecutionId(), "approve2Comments", "北广：内容无不妥");
    	taskService.complete(task.getId());
    	
    	/** 6.2 经过approve2Gateway到达发送审核通过邮件任务. **/
    	
    	/** 7. 发送审核通过邮件给广告主，流程结束**/
    	tasks = taskService.createTaskQuery().taskCandidateUser("tliu").list();
    	Assert.assertEquals(0, tasks.size());
    	//check shiba manager's list
    	mtasks = taskService.createTaskQuery().taskCandidateGroup(shibaManager).list();
    	Assert.assertEquals(0, mtasks.size());
    	//check beiguang manager's list
    	mtasks = taskService.createTaskQuery().taskCandidateGroup(beiguangManager).list();
    	Assert.assertEquals(0, mtasks.size());
    	
    	//check email content
    	checkLastMailContent(3, "您的广告素材已通过审核", "世巴：修改后，内容无不妥");
    	
    	List<HistoricProcessInstance> history = historyService.createHistoricProcessInstanceQuery().processInstanceId(instance.getId()).involvedUser("tliu").includeProcessVariables().list();
    	Assert.assertEquals(1, history.size());
    	Assert.assertEquals("北广：内容无不妥", history.get(0).getProcessVariables().get("approve2Comments"));
    }
    
    @Test
    public void testMaterialProccessTimeout() {
    	UserDetail user = new UserDetail("tliu", "tliu", "Tony", "Liu", "tliutest@gmail.com");
    	UserDetail sbUser = new UserDetail("sbuser", "sbuser", "Shiba", "MaterialManager1", "sbuser@gmail.com");
    	UserDetail bgUser = new UserDetail("bguser", "bguser", "Beiguang", "Manager1", "bguser@gmail.com");
    	String beiguangManager = "BeiguangMaterialManager";
    	String shibaManager = "ShibaMaterialManager";

    	Map<String, Object> initParams = new HashMap<String, Object> ();
    	initParams.put("_owner", user);
    	initParams.put("_timeout", "PT1S");
    	initParams.put("_now", new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date()));

    	/** 0.检查任务列表 **/
    	List<Task> tasks = taskService.createTaskQuery().taskCandidateUser("tliu").list();
    	Assert.assertEquals(0, tasks.size());
    	//check shiba manager's list
    	List<Task> mtasks = taskService.createTaskQuery().taskCandidateGroup(shibaManager).list();
    	Assert.assertEquals(0, mtasks.size());
    	//check beiguang manager's list
    	mtasks = taskService.createTaskQuery().taskCandidateGroup(beiguangManager).list();
    	Assert.assertEquals(0, mtasks.size());
    	
    	//user start material process
    	instance = runtimeService.startProcessInstanceByKey("material", initParams);
    	
    	/** 1. submitInfo task **/
    	//check and complete submitInfo task
    	tasks = taskService.createTaskQuery().taskCandidateUser("tliu").list();
    	Task task = null;
    	Assert.assertEquals(1, tasks.size());
    	task = tasks.get(0);
    	Assert.assertEquals("submitInfo", task.getTaskDefinitionKey());
    	//also check material manager's list
    	mtasks = taskService.createTaskQuery().taskCandidateGroup(shibaManager).list();
    	Assert.assertEquals(1, mtasks.size());
    	Assert.assertEquals("submitInfo", mtasks.get(0).getTaskDefinitionKey());
    	
    	taskService.claim(task.getId(), user.getUsername());
    	/* -- 用户提交Info表格 --*/
    	taskService.complete(task.getId());
    	
    	/** 2. submitMaterial task **/
    	//check and complete submitMaterial task
    	tasks = taskService.createTaskQuery().taskCandidateUser("tliu").list();
    	Assert.assertEquals(1, tasks.size());
    	task = tasks.get(0);
    	Assert.assertEquals("submitMaterial", task.getTaskDefinitionKey());
    	//also check material manager's list
    	mtasks = taskService.createTaskQuery().taskCandidateGroup(shibaManager).list();
    	Assert.assertEquals(1, mtasks.size());
    	Assert.assertEquals("submitMaterial", mtasks.get(0).getTaskDefinitionKey());
    	
    	taskService.claim(task.getId(), user.getUsername());
    	/* -- 用户提交Material表格 --*/
    	taskService.complete(task.getId());
    	
    	/** 3.approve1 task **/
    	//check user's task list
    	tasks = taskService.createTaskQuery().taskCandidateUser("tliu").list();
    	Assert.assertEquals(0, tasks.size());
    	//check shiba manager's list
    	mtasks = taskService.createTaskQuery().taskCandidateGroup(shibaManager).includeProcessVariables().list();
    	Assert.assertEquals(1, mtasks.size());
    	task = mtasks.get(0);
    	Assert.assertEquals("approve1", task.getTaskDefinitionKey());
    	Object ownerObj = task.getProcessVariables().get("_owner");
    	Assert.assertTrue(ownerObj instanceof UserDetail);
    	UserDetail owner = (UserDetail)ownerObj;
    	Assert.assertEquals("tliu", owner.getUsername());
    	
    	taskService.claim(task.getId(), sbUser.getUsername());
    	/* -- 管理员提交审核结果 --*/
    	runtimeService.setVariable(task.getExecutionId(), "approve1Result", true);
    	runtimeService.setVariable(task.getExecutionId(), "approve1Comments", "审核通过");
    	taskService.complete(task.getId());
    	
    	/** 4.经过approve1Gateway时抛出timeout事件，被外层捕捉，进入timeoutTask **/
    	try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
		}
    	
    	/** 5.error task**/
    	//check user's task list
    	tasks = taskService.createTaskQuery().taskCandidateUser("tliu").list();
    	Assert.assertEquals(0, tasks.size());
    	//check beiguang manager's list
    	mtasks = taskService.createTaskQuery().taskCandidateGroup(beiguangManager).list();
    	/* the main process still continues */
    	Assert.assertEquals(1, mtasks.size());
    	//check shiba manager's list
    	mtasks = taskService.createTaskQuery().taskCandidateGroup(shibaManager).includeProcessVariables().list();
    	Assert.assertEquals(1, mtasks.size());
    	task = mtasks.get(0);
    	Assert.assertEquals("timeoutTask", task.getTaskDefinitionKey());
    	ownerObj = task.getProcessVariables().get("_owner");
    	Assert.assertTrue(ownerObj instanceof UserDetail);
    	owner = (UserDetail)ownerObj; 
    	Assert.assertEquals("tliu", owner.getUsername());
    	
    	taskService.claim(task.getId(), sbUser.getUsername());
    	
    	/* -- 管理员处理业务异常 --*/
    	runtimeService.setVariable(task.getExecutionId(), "timeout", "Handled");
    	taskService.complete(task.getId());
    	
    	runtimeService.deleteProcessInstance(instance.getId(), "timeout");
    }

}
