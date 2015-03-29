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
public class OrderActivitiTest {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    private ProcessInstance instance;
    private SimpleSmtpServer mailServer;

	private UserDetail owner = new UserDetail("tliu", "tliu", "Tony", "Liu", "tliutest@gmail.com");
	private UserDetail sboUser = new UserDetail("sbouser", "sbouser", "Shiba", "OrderManager1", "sbouser@gmail.com");
	private UserDetail sbfUser = new UserDetail("sbfuser", "sbfuser", "Shiba", "FinancialManager1", "sbfuser@gmail.com");
	private UserDetail bgmUser = new UserDetail("bgmuser", "bgmuser", "Beiguang", "MaterialManager1", "bgmuser@gmail.com");
	private UserDetail bgsUser = new UserDetail("bgsuser", "bgsuser", "Beiguang", "ScheduleManager1", "bgsuser@gmail.com");
	private String bgm = "BeiguangMaterialManager";
	private String bgs = "BeiguangScheduleManager";
	private String sbo = "ShibaOrderManager";
	private String sbf = "ShibaFinancialManager";

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

    private void checkLastMailContent( int expectedEmailNumber, String expectSubject, String expectBodySnippet) {
    	int mailNumber = mailServer.getReceivedEmailSize();
    	Assert.assertEquals(expectedEmailNumber, mailNumber);
    	if (expectedEmailNumber > 0) {
	    	Iterator mailIter = mailServer.getReceivedEmail();
	    	for (int i = 0; i < expectedEmailNumber - 1; i++)
	    		mailIter.next();
	    	SmtpMessage mail = (SmtpMessage) mailIter.next();
	    	try {
				String expectedSubject = MimeUtility.fold(9,
					    MimeUtility.encodeText(expectSubject, "utf-8", null));
		    	Assert.assertEquals(expectedSubject.toUpperCase(), mail.getHeaderValue("Subject").toUpperCase());
			} catch (Exception e) {
				e.printStackTrace();
				Assert.fail();
			}
	    	Assert.assertTrue(mail.getHeaderValue("From").indexOf("pantuo-service@gmail.com") >= 0);
	    	Assert.assertTrue(mail.getHeaderValue("To").indexOf(owner.getUser().getEmail()) >= 0 );
    	}
    }
    
    private void checkTaskNumbers (int ownerNumber, int sboNumber, int sbfNumber, int bgmNumber, int bgsNumber) {
    	List<Task> tasks = taskService.createTaskQuery().taskCandidateUser("tliu").list();
    	Assert.assertEquals(ownerNumber, tasks.size());
    	//check shiba order manager's list
    	List<Task> mtasks = taskService.createTaskQuery().taskCandidateGroup(sbo).list();
    	Assert.assertEquals(sboNumber, mtasks.size());
    	//check shiba financial manager's list
    	mtasks = taskService.createTaskQuery().taskCandidateGroup(sbf).list();
    	Assert.assertEquals(sbfNumber, mtasks.size());
    	//check beiguang material manager's list
    	mtasks = taskService.createTaskQuery().taskCandidateGroup(bgm).list();
    	Assert.assertEquals(bgmNumber, mtasks.size());
    	//check beiguang schedule manager's list
    	mtasks = taskService.createTaskQuery().taskCandidateGroup(bgs).list();
    	Assert.assertEquals(bgsNumber, mtasks.size());
    }
    
    private void checkInitialTaskNumbers() {
    	checkTaskNumbers(0,0,0,0,0);
    }
    
    @Test
    public void testNormalProccess() {
    	Map<String, Object> initParams = new HashMap<String, Object> ();
    	initParams.put("_owner", owner);
    	initParams.put("_now", new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date()));

    	List<Task> tasks, mtasks;
    	Task task;
    	
    	/** 0.检查任务列表 **/
    	checkInitialTaskNumbers();
    	
    	//user start material process
    	instance = runtimeService.startProcessInstanceByKey("order", initParams);
    	
    	/** 1. 提交订单 task **/
    	//check and complete submitOrder task
    	checkTaskNumbers(1, 1, 0, 0, 0);
    	tasks = taskService.createTaskQuery().taskCandidateUser("tliu").list();
    	Assert.assertEquals(1, tasks.size());
    	task = tasks.get(0);
    	Assert.assertEquals("submitOrder", task.getTaskDefinitionKey());
    	//also check material manager's list
    	mtasks = taskService.createTaskQuery().taskCandidateGroup(sbo).list();
    	Assert.assertEquals(1, mtasks.size());
    	Assert.assertEquals("submitOrder", mtasks.get(0).getTaskDefinitionKey());
    	
    	taskService.claim(task.getId(), owner.getUsername());
    	/* -- 用户提交Info表格 --*/
    	taskService.complete(task.getId());
    	
    	/** 2.经过parallelGateway分别向世巴订单管理员发送审核任务， 以及向客户发送支付任务
    	
    	/** 3.approve1 task **/
    	checkTaskNumbers(1, 1, 0, 0, 0);
    	//check shiba manager's list
    	mtasks = taskService.createTaskQuery().taskCandidateGroup(sbo).includeProcessVariables().list();
    	Assert.assertEquals(1, mtasks.size());
    	task = mtasks.get(0);
    	Assert.assertEquals("approve1", task.getTaskDefinitionKey());
    	Object ownerObj = task.getProcessVariables().get("_owner");
    	Assert.assertTrue(ownerObj instanceof UserDetail);
    	UserDetail owner = (UserDetail)ownerObj;
    	Assert.assertEquals("tliu", owner.getUsername());
    	
    	taskService.claim(task.getId(), sboUser.getUsername());
    	/* -- 管理员提交审核结果 --*/
    	runtimeService.setVariable(task.getExecutionId(), "approve1Result", true);
    	runtimeService.setVariable(task.getExecutionId(), "approve1Comments", "世巴：订单/物料内容无不妥");
    	taskService.complete(task.getId());
    	
    	/** 4.经过approve1Gateway到达北广审核任务 **/
    	
    	/** 5.approve2 task**/
    	checkTaskNumbers(1, 0, 0, 1, 0);
    	//check beiguang material manager's list
    	mtasks = taskService.createTaskQuery().taskCandidateGroup(bgm).includeProcessVariables().list();
    	Assert.assertEquals(1, mtasks.size());
    	task = mtasks.get(0);
    	Assert.assertEquals("approve2", task.getTaskDefinitionKey());
    	ownerObj = task.getProcessVariables().get("_owner");
    	Assert.assertTrue(ownerObj instanceof UserDetail);
    	owner = (UserDetail)ownerObj; 
    	Assert.assertEquals("tliu", owner.getUsername());
    	
    	taskService.claim(task.getId(), bgmUser.getUsername());
    	/* -- 管理员提交审核结果 --*/
    	runtimeService.setVariable(task.getExecutionId(), "approve2Result", true);
    	runtimeService.setVariable(task.getExecutionId(), "approve2Comments", "北广：物料内容无不妥");
    	taskService.complete(task.getId());
    	
    	/** 6. 经过approve2Gateway到达发送审核通过邮件任务. **/
    	
    	/** 7. 发送审核通过邮件给广告主**/
    	//check email content
    	checkLastMailContent( 1, "您的广告素材已通过审核", "您提交的广告素材已于");
   	
    	/** 8. 用户支付任务 **/
    	checkTaskNumbers(1, 0, 0, 0, 0);
    	tasks = taskService.createTaskQuery().taskCandidateUser("tliu").list();
    	Assert.assertEquals(1, tasks.size());
    	task = tasks.get(0);
    	Assert.assertEquals("payment", task.getTaskDefinitionKey());

    	taskService.claim(task.getId(), owner.getUsername());
    	/* -- 用户完成支付 --*/
    	taskService.complete(task.getId());
    	
    	/** 9. 世巴财务确认： financialCheck任务 **/
    	checkTaskNumbers(0, 0, 1, 0, 0);
    	//check shiba manager's list
    	mtasks = taskService.createTaskQuery().taskCandidateGroup(sbf).includeProcessVariables().list();
    	Assert.assertEquals(1, mtasks.size());
    	task = mtasks.get(0);
    	Assert.assertEquals("financialCheck", task.getTaskDefinitionKey());
    	ownerObj = task.getProcessVariables().get("_owner");
    	Assert.assertTrue(ownerObj instanceof UserDetail);
    	owner = (UserDetail)ownerObj;
    	Assert.assertEquals("tliu", owner.getUsername());
    	taskService.claim(task.getId(), sbfUser.getUsername());
    	/* -- 世巴财务确认 -- */
    	taskService.complete(task.getId());
    	
    	/** 10. 经过“生成排期表”的serviceTask，到达submitSchedule任务**/
    	checkTaskNumbers(0, 1, 0, 0, 0);
    	//check shiba manager's list
    	mtasks = taskService.createTaskQuery().taskCandidateGroup(sbo).includeProcessVariables().list();
    	Assert.assertEquals(1, mtasks.size());
    	task = mtasks.get(0);
    	Assert.assertEquals("submitSchedule", task.getTaskDefinitionKey());
    	ownerObj = task.getProcessVariables().get("_owner");
    	Assert.assertTrue(ownerObj instanceof UserDetail);
    	owner = (UserDetail)ownerObj;
    	Assert.assertEquals("tliu", owner.getUsername());
    	taskService.claim(task.getId(), sboUser.getUsername());
    	/* -- 世巴确认排期表 -- */
    	taskService.complete(task.getId());
    	
    	/** 11. 到达inputSchedule任务 **/
    	checkTaskNumbers(0, 0, 0, 0, 1);
    	//check beiguang schedule manager's list
    	mtasks = taskService.createTaskQuery().taskCandidateGroup(bgs).includeProcessVariables().list();
    	Assert.assertEquals(1, mtasks.size());
    	task = mtasks.get(0);
    	Assert.assertEquals("inputSchedule", task.getTaskDefinitionKey());
    	ownerObj = task.getProcessVariables().get("_owner");
    	Assert.assertTrue(ownerObj instanceof UserDetail);
    	owner = (UserDetail)ownerObj;
    	Assert.assertEquals("tliu", owner.getUsername());
    	
    	taskService.claim(task.getId(), bgsUser.getUsername());
    	/* -- 北广完成排期表录入 --*/
    	taskService.complete(task.getId());
    	
    	/** 12. 发送订单生效邮件 **/
    	checkLastMailContent(2, "您的广告投放订单已经生效", "您提交的广告已于");
    	
    	/** 13. 世巴填写上播报告**/
    	checkTaskNumbers(0, 1, 0, 0, 0);
    	//check beiguang schedule manager's list
    	mtasks = taskService.createTaskQuery().taskCandidateGroup(sbo).includeProcessVariables().list();
    	Assert.assertEquals(1, mtasks.size());
    	task = mtasks.get(0);
    	Assert.assertEquals("shangboReport", task.getTaskDefinitionKey());
    	ownerObj = task.getProcessVariables().get("_owner");
    	Assert.assertTrue(ownerObj instanceof UserDetail);
    	owner = (UserDetail)ownerObj;
    	Assert.assertEquals("tliu", owner.getUsername());
    	
    	taskService.claim(task.getId(), sboUser.getUsername());
    	/* -- 世巴完成上播报告填写 --*/
    	taskService.complete(task.getId());
    	
    	/** 14. 发送上播报告邮件 **/
    	checkLastMailContent(3, "您的广告订单已经上播", "您提交的广告已于");
 
    	/** 15. 世巴填写监播报告**/
    	checkTaskNumbers(0, 1, 0, 0, 0);
    	//check beiguang schedule manager's list
    	mtasks = taskService.createTaskQuery().taskCandidateGroup(sbo).includeProcessVariables().list();
    	Assert.assertEquals(1, mtasks.size());
    	task = mtasks.get(0);
    	Assert.assertEquals("jianboReport", task.getTaskDefinitionKey());
    	ownerObj = task.getProcessVariables().get("_owner");
    	Assert.assertTrue(ownerObj instanceof UserDetail);
    	owner = (UserDetail)ownerObj;
    	Assert.assertEquals("tliu", owner.getUsername());
    	
    	taskService.claim(task.getId(), sboUser.getUsername());
    	/* -- 世巴完成监播报告填写 --*/
    	taskService.complete(task.getId());
    	
    	/** 16. 发送上播报告邮件 **/
    	checkLastMailContent(4, "您的广告已经播出完成", "您提交的广告已于");

    	List<HistoricProcessInstance> history = historyService.createHistoricProcessInstanceQuery().processInstanceId(instance.getId()).involvedUser("tliu").includeProcessVariables().list();
    	Assert.assertEquals(1, history.size());
    	Assert.assertEquals("北广：物料内容无不妥",history.get(0).getProcessVariables().get("approve2Comments"));
    }
    
  
    @Test
    public void testOrderProccessTimeout() {
    	Map<String, Object> initParams = new HashMap<String, Object> ();
    	initParams.put("_owner", owner);
    	initParams.put("_timeout", "PT1S");
    	initParams.put("_now", new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date()));

    	List<Task> tasks, mtasks;
    	Task task;
    	
    	/** 0.检查任务列表 **/
    	checkInitialTaskNumbers();
    	
    	//user start material process
    	instance = runtimeService.startProcessInstanceByKey("order", initParams);
    	
    	/** 1. 提交订单 task **/
    	//check and complete submitOrder task
    	checkTaskNumbers(1, 1, 0, 0, 0);
    	tasks = taskService.createTaskQuery().taskCandidateUser("tliu").list();
    	Assert.assertEquals(1, tasks.size());
    	task = tasks.get(0);
    	Assert.assertEquals("submitOrder", task.getTaskDefinitionKey());
    	//also check material manager's list
    	mtasks = taskService.createTaskQuery().taskCandidateGroup(sbo).list();
    	Assert.assertEquals(1, mtasks.size());
    	Assert.assertEquals("submitOrder", mtasks.get(0).getTaskDefinitionKey());
    	
    	taskService.claim(task.getId(), owner.getUsername());
    	/* -- 用户提交Info表格 --*/
    	taskService.complete(task.getId());
    	
    	/** 2.经过parallelGateway分别向世巴订单管理员发送审核任务， 以及向客户发送支付任务
    	
    	/** 3.approve1 task **/
    	checkTaskNumbers(1, 1, 0, 0, 0);
    	//check shiba manager's list
    	mtasks = taskService.createTaskQuery().taskCandidateGroup(sbo).includeProcessVariables().list();
    	Assert.assertEquals(1, mtasks.size());
    	task = mtasks.get(0);
    	Assert.assertEquals("approve1", task.getTaskDefinitionKey());
    	Object ownerObj = task.getProcessVariables().get("_owner");
    	Assert.assertTrue(ownerObj instanceof UserDetail);
    	UserDetail owner = (UserDetail)ownerObj;
    	Assert.assertEquals("tliu", owner.getUsername());
    	
    	taskService.claim(task.getId(), sboUser.getUsername());
    	/* -- 管理员提交审核结果 --*/
    	runtimeService.setVariable(task.getExecutionId(), "approve1Result", true);
    	runtimeService.setVariable(task.getExecutionId(), "approve1Comments", "世巴：订单/物料内容无不妥");
    	taskService.complete(task.getId());
   	
    	/** 4.经过approve1Gateway时抛出timeout事件，被外层捕捉，进入timeoutTask **/
    	try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
		}
    	
    	/** 4.经过approve1Gateway到达北广审核任务 **/
    	
    	/** 5.error task**/
    	checkTaskNumbers(1, 1, 0, 1, 0);
    	//check shiba manager's list
    	mtasks = taskService.createTaskQuery().taskCandidateGroup(sbo).includeProcessVariables().list();
    	Assert.assertEquals(1, mtasks.size());
    	task = mtasks.get(0);
    	Assert.assertEquals("timeoutTask", task.getTaskDefinitionKey());
    	ownerObj = task.getProcessVariables().get("_owner");
    	Assert.assertTrue(ownerObj instanceof UserDetail);
    	owner = (UserDetail)ownerObj; 
    	Assert.assertEquals("tliu", owner.getUsername());
    	
    	taskService.claim(task.getId(), sboUser.getUsername());
    	
    	/* -- 管理员处理业务异常 --*/
    	runtimeService.setVariable(task.getExecutionId(), "timeout", "Handled");
    	taskService.complete(task.getId());
    	
    	runtimeService.deleteProcessInstance(instance.getId(), "timeout");
    }

}
