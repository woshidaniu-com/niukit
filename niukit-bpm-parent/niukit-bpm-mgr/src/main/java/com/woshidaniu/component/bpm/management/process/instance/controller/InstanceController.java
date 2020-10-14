package com.woshidaniu.component.bpm.management.process.instance.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.woshidaniu.component.bpm.management.BaseBPMController;
import com.woshidaniu.component.bpm.management.process.instance.service.PorcessInstanceService;

/**
 * <p>
 *   <h3>niutal框架<h3>
 *   <br>说明：流程实例控制器
 *	 <br>class：com.woshidaniu.component.bpm.management.process.instance.controller.InstanceController.java
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
@Controller
@RequestMapping("/processInstance")
public class InstanceController extends BaseBPMController {

	@Autowired
	protected PorcessInstanceService service;
	@Autowired
	protected RuntimeService runtimeService;
	@Autowired
	protected HistoryService historyService;
	
	@RequestMapping("/{taskId}/processTask")
	public String processTask(@PathVariable String taskId, HttpServletRequest request){
		
		return null;
	}
	
	@RequestMapping("/doProcessTask")
	public String doProcessTask(@PathVariable String taskId, HttpServletRequest request){
		
		return null;
	}
	
	/**
	 * 
	 * <p>方法说明：简单流程跟踪，文字形式<p>
	 * <p>作者：a href="#">Zhangxiaobin[1036]<a><p>
	 * <p>时间：2016年12月12日上午11:18:55<p>
	 */
	@RequestMapping("/{processInstanceId}/simpleTrace")
	public String simpleTrace(@PathVariable String processInstanceId, HttpServletRequest request){
		List<Map<String, String>> traceProcess = service.traceProcessInstance(processInstanceId);
		HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		request.setAttribute("processInstnceId", processInstanceId);
		request.setAttribute("processDefinitionId", processInstance.getProcessDefinitionId());
		request.setAttribute("traceProcess", traceProcess);
		return "/processManagement/instance/simpleTrace";
	}
	
	/**
	 * 
	 * <p>方法说明：高级流程跟踪，图片形式<p>
	 * <p>作者：a href="#">Zhangxiaobin[1036]<a><p>
	 * <p>时间：2016年12月12日上午11:18:55<p>
	 */
	@RequestMapping("/{processInstanceId}/advancedTrace")
	public String advancedTrace(@PathVariable String processInstanceId, HttpServletRequest request){
		return null;
	}
	
}
