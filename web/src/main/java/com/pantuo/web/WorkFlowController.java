package com.pantuo.web;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.pantuo.service.ActivitiService;
import com.pantuo.service.impl.WorkflowTraceService;

@Controller
@RequestMapping(produces = "application/json;charset=utf-8")
public class WorkFlowController {
	
	@Autowired
	ProcessEngine processEngine;
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
	public String reset(String p, @CookieValue(value = "city", defaultValue = "-1") int city) {
		return activitiService.reset(city, p) ;
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
			//resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), resourceName);
			resourceAsStream =  getDiagram(processInstanceId);
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

	
	
	private InputStream getDiagram(String processInstanceId){
		//查询流程实例
		ProcessInstance pi =this.runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		BpmnModel bpmnModel = this.repositoryService.getBpmnModel(pi.getProcessDefinitionId());
		//得到正在执行的环节
		List<String> activeIds = this.runtimeService.getActiveActivityIds(pi.getId());
		InputStream is = new DefaultProcessDiagramGenerator().generateDiagram(
		bpmnModel, "png",
		activeIds, Collections.<String>emptyList(), 
		processEngine.getProcessEngineConfiguration().getActivityFontName(), 
		processEngine.getProcessEngineConfiguration().getLabelFontName(), 
		null, 1.0);
		return is;
		}
}
