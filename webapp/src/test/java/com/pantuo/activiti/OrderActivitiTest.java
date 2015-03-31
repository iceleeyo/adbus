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
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import scala.actors.threadpool.Arrays;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    	checkLastMailContent( 1, "您的订单已通过审核", "您提交的订单已于");
   	
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
    	runtimeService.setVariable(task.getExecutionId(), "paymentResult", true);
    	runtimeService.setVariable(task.getExecutionId(), "paymentComments", "财务：支付OK");
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

    private Task checkUserTask(String user, String taskId) {
    	List<Task> tasks = checkUserTasks(user, taskId);
    	if (!tasks.isEmpty())
    		return tasks.get(0);
    	return null;
    }

    private List<Task> checkUserTasks(String user, String... taskIds) {
    	List<Task> tasks = taskService.createTaskQuery().taskCandidateUser(user).includeProcessVariables().list();
    	int taskNumber = taskIds == null ? 0 : taskIds.length;
    	Assert.assertEquals(taskNumber, tasks.size());
    	if (taskNumber > 0) {
    		List<String> tasksIdss = new ArrayList<String> (Arrays.asList(taskIds));
    		for (Task task : tasks) {
		    	Assert.assertTrue(tasksIdss.contains(task.getTaskDefinitionKey()));
		    	tasksIdss.remove(task.getTaskDefinitionKey());
		    	Object ownerObj = task.getProcessVariables().get("_owner");
		    	Assert.assertTrue(ownerObj instanceof UserDetail);
		    	UserDetail owner = (UserDetail)ownerObj;
		    	Assert.assertEquals(user, owner.getUsername());
    		}
	    	return tasks;
    	}
    	return Collections.EMPTY_LIST;
    }

    private Task checkGroupTask(String group, String owner, String taskId) {
    	List<Task> tasks =  checkGroupTasks(group, owner, taskId);
    	if (!tasks.isEmpty())
    		return tasks.get(0);
    	return null;
    }

    private List<Task> checkGroupTasks(String group, String owner, String... taskIds) {
    	List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup(group).includeProcessVariables().list();
    	int taskNumber = taskIds == null ? 0 : taskIds.length;
    	Assert.assertEquals(taskNumber, tasks.size());
    	if (taskNumber > 0) {
    		List<String> tasksIdss = new ArrayList<String> (Arrays.asList(taskIds));
    		for (Task task : tasks) {
		    	Assert.assertTrue(tasksIdss.contains(task.getTaskDefinitionKey()));
		    	tasksIdss.remove(task.getTaskDefinitionKey());
		    	Object ownerObj = task.getProcessVariables().get("_owner");
		    	Assert.assertTrue(ownerObj instanceof UserDetail);
		    	UserDetail owneru = (UserDetail)ownerObj;
		    	Assert.assertEquals(owner, owneru.getUsername());
    		}
	    	return tasks;
    	}
    	return Collections.EMPTY_LIST;
    }


    @Test
    public void testDisapproveProccess() {
    	int mailNumber = 0;
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
    	task = checkUserTask("tliu", "submitOrder");
    	checkGroupTask(sbo, "tliu", "submitOrder");
    	
    	taskService.claim(task.getId(), owner.getUsername());
    	/* -- 用户提交Info表格 --*/
    	taskService.complete(task.getId());
    	
    	/** 2.经过inclusiveGateway分别向世巴订单管理员发送审核任务， 以及向客户发送支付任务*/
    	
    	/** 3.approve1 task **/
    	checkTaskNumbers(1, 1, 0, 0, 0);
    	//check shiba manager's list
    	task = checkGroupTask(sbo, "tliu", "approve1");
    	taskService.claim(task.getId(), sboUser.getUsername());
    	/* -- 管理员提交审核结果 --*/
    	runtimeService.setVariable(task.getExecutionId(), "approve1Result", false);
    	runtimeService.setVariable(task.getExecutionId(), "approve1Comments", "世巴：请重新填写订单内容");
    	taskService.complete(task.getId());
    	
    	/** 4.经过approve1Gateway到达发送审核不通过邮件任务 **/
    	checkLastMailContent(++mailNumber, "您的订单需要修改", "需要修改");
    	
    	/** 5. 广告主修改订单 **/
    	checkTaskNumbers(2, 1, 0, 0, 0);	//支付和修改订单任务
    	tasks = checkUserTasks("tliu", "modifyOrder", "payment");
    	/* -- 修改订单 -- */
    	for (Task t : tasks) {
    		if (t.getTaskDefinitionKey().equals("modifyOrder")) {
    			taskService.claim(t.getId(), "tliu");
    			taskService.complete(t.getId());
    		}
    	}
    	
    	/** 6. 世巴收到再次审核任务 **/
    	checkTaskNumbers(1, 1, 0, 0, 0);
    	task = checkGroupTask(sbo, "tliu", "approve1");
    	taskService.claim(task.getId(), sboUser.getUsername());
    	/* -- 管理员提交审核结果 --*/
    	runtimeService.setVariable(task.getExecutionId(), "approve1Result", true);
    	runtimeService.setVariable(task.getExecutionId(), "approve1Comments", "世巴：订单无不妥");
    	taskService.complete(task.getId());
    	
    	/** 7.经过approve1Gateway到达背光审核任务 **/
    	
    	/** 8.approve2 task**/
    	checkTaskNumbers(1, 0, 0, 1, 0);
    	task = checkGroupTask(bgm, "tliu", "approve2");
    	
    	taskService.claim(task.getId(), bgmUser.getUsername());
    	/* -- 管理员提交审核结果 --*/
    	runtimeService.setVariable(task.getExecutionId(), "approve2Result", false);
    	runtimeService.setVariable(task.getExecutionId(), "approve2Comments", "北广：修改物料内容");
    	taskService.complete(task.getId());
    	
    	/** 9. 返回approve1 task **/
    	checkTaskNumbers(1, 1, 0, 0, 0);
    	task = checkGroupTask(sbo, "tliu", "approve1");
    	taskService.claim(task.getId(), sboUser.getUsername());
    	/* -- 管理员提交审核结果 --*/
    	runtimeService.setVariable(task.getExecutionId(), "approve1Result", false);
    	runtimeService.setVariable(task.getExecutionId(), "approve1Comments", "世巴：北广认为物料需要修改");
    	taskService.complete(task.getId());
    	checkLastMailContent(++mailNumber, "您的订单需要修改", "需要修改");
    	
    	/** 10. 广告主修改物料 **/
    	checkTaskNumbers(2, 1, 0, 0, 0);	//支付和修改订单任务
    	tasks = checkUserTasks("tliu", "modifyOrder", "payment");
    	/* -- 修改订单 -- */
    	for (Task t : tasks) {
    		if (t.getTaskDefinitionKey().equals("modifyOrder")) {
    			taskService.claim(t.getId(), "tliu");
    			taskService.complete(t.getId());
    		}
    	}
    	
    	/** 11. 世巴收到再次审核任务 **/
    	checkTaskNumbers(1, 1, 0, 0, 0);
    	task = checkGroupTask(sbo, "tliu", "approve1");
    	taskService.claim(task.getId(), sboUser.getUsername());
    	/* -- 管理员提交审核结果 --*/
    	runtimeService.setVariable(task.getExecutionId(), "approve1Result", true);
    	runtimeService.setVariable(task.getExecutionId(), "approve1Comments", "世巴：订单物料无不妥");
    	taskService.complete(task.getId());
    	
    	/** 12.approve2 task**/
    	checkTaskNumbers(1, 0, 0, 1, 0);
    	task = checkGroupTask(bgm, "tliu", "approve2");
    	
    	taskService.claim(task.getId(), bgmUser.getUsername());
    	/* -- 管理员提交审核结果 --*/
    	runtimeService.setVariable(task.getExecutionId(), "approve2Result", true);
    	runtimeService.setVariable(task.getExecutionId(), "approve2Comments", "北广：物料内容无不妥");
    	taskService.complete(task.getId());

    	/** 13. 经过approve2Gateway到达发送审核通过邮件任务. **/
    	
    	/** 14. 发送审核通过邮件给广告主**/
    	//check email content
    	checkLastMailContent(++mailNumber, "您的订单已通过审核", "您提交的订单已于");
    	
    	/** 15. 用户支付任务 **/
    	checkTaskNumbers(1, 0, 0, 0, 0);
    	task = checkUserTask("tliu", "payment");
    	taskService.claim(task.getId(), owner.getUsername());
    	/* -- 用户完成支付 --*/
    	taskService.complete(task.getId());
    	
    	/** 16. 世巴财务确认： financialCheck任务 **/
    	checkTaskNumbers(0, 0, 1, 0, 0);
    	task = checkGroupTask(sbf, "tliu", "financialCheck");
    	taskService.claim(task.getId(), sbfUser.getUsername());
    	/* -- 世巴财务确认 -- */
    	runtimeService.setVariable(task.getExecutionId(), "paymentResult", false);
    	runtimeService.setVariable(task.getExecutionId(), "paymentComments", "财务：支付有问题");
    	taskService.complete(task.getId());

    	/** 17. 发送支付失败邮件，并重新回到用户支付任务 **/
    	checkLastMailContent(++mailNumber, "您的订单支付失败", "您提交的订单支付失败");
   	
    	/** 18. 用户支付任务 **/
    	checkTaskNumbers(1, 0, 0, 0, 0);
    	task = checkUserTask("tliu", "payment");
    	taskService.claim(task.getId(), owner.getUsername());
    	/* -- 用户完成支付 --*/
    	taskService.complete(task.getId());
    	
    	/** 19. 世巴财务确认： financialCheck任务 **/
    	checkTaskNumbers(0, 0, 1, 0, 0);
    	task = checkGroupTask(sbf, "tliu", "financialCheck");
    	taskService.claim(task.getId(), sbfUser.getUsername());
    	/* -- 世巴财务确认 -- */
    	runtimeService.setVariable(task.getExecutionId(), "paymentResult", true);
    	runtimeService.setVariable(task.getExecutionId(), "paymentComments", "财务：支付OK");
    	/* -- 模拟排期错误 -- */
    	runtimeService.setVariable(task.getExecutionId(), "mockScheduleResult", false);
    	taskService.complete(task.getId());
    	
    	/** 20. 经过“生成排期表”的serviceTask，到达approve1任务**/
    	checkTaskNumbers(0, 1, 0, 0, 0);
    	task = checkGroupTask(sbo, "tliu", "approve1");
    	taskService.claim(task.getId(), sboUser.getUsername());
    	/* -- 管理员提交审核结果 --*/
    	runtimeService.setVariable(task.getExecutionId(), "approve1Result", false);
    	runtimeService.setVariable(task.getExecutionId(), "approve1Comments", "世巴：排期冲突，请重新排期");
    	taskService.complete(task.getId());
    	checkLastMailContent(++mailNumber, "您的订单需要修改", "需要修改");
    	
    	/** 21. 广告主修改排期 **/
    	checkTaskNumbers(1, 1, 0, 0, 0);
    	task = checkUserTask("tliu", "modifyOrder");
    	/* -- 修改订单 -- */
		taskService.claim(task.getId(), "tliu");
		taskService.complete(task.getId());
    	
    	/** 22. 世巴收到再次审核任务 **/
    	checkTaskNumbers(0, 1, 0, 0, 0);
    	task = checkGroupTask(sbo, "tliu", "approve1");
    	taskService.claim(task.getId(), sboUser.getUsername());
    	/* -- 管理员提交审核结果 --*/
    	runtimeService.setVariable(task.getExecutionId(), "approve1Result", true);
    	runtimeService.setVariable(task.getExecutionId(), "approve1Comments", "世巴：排期OK");
    	/* -- 模拟排期OK-- */
    	runtimeService.setVariable(task.getExecutionId(), "mockScheduleResult", true);
    	taskService.complete(task.getId());

    	/** 10. 经过“生成排期表”的serviceTask，到达submitSchedule任务**/
    	checkTaskNumbers(0, 1, 0, 0, 0);
    	task = checkGroupTask(sbo, "tliu", "submitSchedule");
    	taskService.claim(task.getId(), sboUser.getUsername());
    	/* -- 世巴确认排期表 -- */
    	taskService.complete(task.getId());
    	
    	/** 11. 到达inputSchedule任务 **/
    	checkTaskNumbers(0, 0, 0, 0, 1);
    	task = checkGroupTask(bgs, "tliu", "inputSchedule");
    	taskService.claim(task.getId(), bgsUser.getUsername());
    	/* -- 北广完成排期表录入 --*/
    	taskService.complete(task.getId());
    	
    	/** 12. 发送订单生效邮件 **/
    	checkLastMailContent(++mailNumber, "您的广告投放订单已经生效", "您提交的广告已于");
    	
    	/** 13. 世巴填写上播报告**/
    	checkTaskNumbers(0, 1, 0, 0, 0);
    	task = checkGroupTask(sbo, "tliu", "shangboReport");
    	taskService.claim(task.getId(), sboUser.getUsername());
    	/* -- 世巴完成上播报告填写 --*/
    	taskService.complete(task.getId());
    	
    	/** 14. 发送上播报告邮件 **/
    	checkLastMailContent(++mailNumber, "您的广告订单已经上播", "您提交的广告已于");
 
    	/** 15. 世巴填写监播报告**/
    	checkTaskNumbers(0, 1, 0, 0, 0);
    	task = checkGroupTask(sbo, "tliu", "jianboReport");
    	taskService.claim(task.getId(), sboUser.getUsername());
    	/* -- 世巴完成监播报告填写 --*/
    	taskService.complete(task.getId());
    	
    	/** 16. 发送上播报告邮件 **/
    	checkLastMailContent(++mailNumber, "您的广告已经播出完成", "您提交的广告已于");

    	List<HistoricProcessInstance> history = historyService.createHistoricProcessInstanceQuery().processInstanceId(instance.getId()).involvedUser("tliu").includeProcessVariables().list();
    	Assert.assertEquals(1, history.size());
    	Assert.assertEquals("北广：物料内容无不妥",history.get(0).getProcessVariables().get("approve2Comments"));
    }
    

}
