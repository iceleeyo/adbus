package com.pantuo.web;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.pantuo.service.ActivitiService;
import com.pantuo.service.impl.WorkflowTraceService;

@Controller
@RequestMapping(produces = "application/json;charset=utf-8")
public class WorkFlowController {
	@Autowired
	private WorkflowTraceService traceService;
	@Autowired
	ActivitiService activitiService;

	@Autowired
	protected RuntimeService runtimeService;

	@Autowired
	protected RepositoryService repositoryService;

	@RequestMapping(value = "resetActiviti")
	@ResponseBody
	public String saveOrder(String p) {
		return activitiService.reset(p);
	}

	/**
	 * 请求转发到查看流程图页面
	 * @param taskId
	 * @return
	 */
	@RequestMapping(produces = "application/json;charset=utf-8", value = "/workflow/view/{executionId}/page/{processInstanceId}", method = {
			RequestMethod.GET, RequestMethod.POST })
	public ModelAndView viewImage(@PathVariable("executionId") String executionId,
			@PathVariable("processInstanceId") String processInstanceId) {
		ModelAndView modelAndView = new ModelAndView("workflow/view");
		modelAndView.addObject("executionId", executionId);
		modelAndView.addObject("processInstanceId", processInstanceId);
		return modelAndView;
	}

	/**
	* 输出跟踪流程信息
	* @param processInstanceId 流程实例id
	* @return
	* @throws Exception
	*/
	@RequestMapping(value = "/workflow/process/{pid}/trace", produces = "application/json;charset=utf-8")
	@ResponseBody
	public List<Map<String, Object>> traceProcess(@PathVariable("pid") String processInstanceId) throws Exception {
		List<Map<String, Object>> activityInfos = traceService.traceProcess(processInstanceId);
		return activityInfos;
	}

	/**
	 * 根据流程实例id查询流程图(跟踪流程图)
	 * @param processInstanceId 流程实例id
	 * @param request
	 * @param response
	 */
	@RequestMapping(produces = "application/json;charset=utf-8", value = "/workflow/view/{processInstanceId}", method = {
			RequestMethod.GET, RequestMethod.POST })
	public void viewProcessImageView(@PathVariable("processInstanceId") String processInstanceId,
			HttpServletRequest request, HttpServletResponse response) {
		InputStream resourceAsStream = null;
		try {

			//根据流程实例id查询流程实例
			ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
					.processInstanceId(processInstanceId).singleResult();

			//根据流程定义id查询流程定义
			ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
					.processDefinitionId(processInstance.getProcessDefinitionId()).singleResult();

			String resourceName = processDefinition.getDiagramResourceName();

			//打开流程资源流
			resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), resourceName);

			runtimeService.getActiveActivityIds(processInstance.getId());

			//输出到浏览器
			byte[] byteArray = IOUtils.toByteArray(resourceAsStream);
			ServletOutputStream servletOutputStream = response.getOutputStream();
			servletOutputStream.write(byteArray, 0, byteArray.length);
			servletOutputStream.flush();
			servletOutputStream.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
