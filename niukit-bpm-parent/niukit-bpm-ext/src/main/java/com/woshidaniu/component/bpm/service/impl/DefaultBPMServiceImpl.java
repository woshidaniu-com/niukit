package com.woshidaniu.component.bpm.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.activiti.engine.DynamicBpmnService;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.extend.cfg.ExtendSpringProcessEnginConfiguration;
import org.activiti.engine.extend.context.BusinessContext;
import org.activiti.engine.extend.service.ExtendService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.woshidaniu.component.bpm.BPMEventListener;
import com.woshidaniu.component.bpm.BPMException;
import com.woshidaniu.component.bpm.BPMUtils;
import com.woshidaniu.component.bpm.service.BPMService;

/**
 * <p>
 * <h3>niutal框架
 * <h3><br>
 * 说明：默认BPM流程服务实现 <br>
 * class：com.woshidaniu.component.bpm.impl.DefaultBPMServiceImpl.java
 * <p>
 * 
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
public class DefaultBPMServiceImpl implements BPMService, InitializingBean {

	static final Logger log = LoggerFactory.getLogger(DefaultBPMServiceImpl.class);

	static final String DEFAULT_FORM_ENGINE_NAME = "alpacaJSONFormEngine";
	
	protected String formEngineName = DEFAULT_FORM_ENGINE_NAME;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if (processEngine.getProcessEngineConfiguration() instanceof ExtendSpringProcessEnginConfiguration) {
			extendService = ((ExtendSpringProcessEnginConfiguration) processEngine.getProcessEngineConfiguration())
					.getExtendService();
		}
	}

	/********************* Activiti Service *************************/
	protected ProcessEngine processEngine;
	protected RepositoryService repositoryService;
	protected TaskService taskService;
	protected IdentityService identityService;
	protected FormService formService;
	protected RuntimeService runtimeService;
	protected HistoryService historyService;
	protected ManagementService managementService;
	protected DynamicBpmnService dynamicBpmnService;
	protected ExtendService extendService;

	/********************* Activiti Service *************************/

	/**
	 * 
	 * <p>
	 * 方法说明：检查流程启动必要条件
	 * <p>
	 * <p>
	 * 作者：a href="#">Zhangxiaobin[1036]<a>
	 * <p>
	 * <p>
	 * 时间：2016年11月14日上午10:35:33
	 * <p>
	 */
	protected void preCheck(String processDefinationKey, String processStarter, String bizId) {
		BPMUtils.isProcessDefinationKeyNULL(processDefinationKey);
		BPMUtils.isProcesssInstanceStarterNULL(processStarter);
		BPMUtils.isBusinessKeyNULL(bizId);
	}

	/**
	 * 
	 * <p>
	 * 方法说明：
	 * <p>
	 * <p>
	 * 作者：a href="#">Zhangxiaobin[1036]<a>
	 * <p>
	 * <p>
	 * 时间：2016年11月17日下午1:30:12
	 * <p>
	 */
	protected void preSetup(String authid, BPMEventListener eventListener) {
		BPMUtils.setupAuthId(authid);
		if (eventListener != null) {
			BusinessContext.set(eventListener);
		}
	}

	protected void preSetup(String authid) {
		BPMUtils.setupAuthId(authid);
	}

	protected void preSetup(BPMEventListener eventListener) {
		if (eventListener != null) {
			BusinessContext.set(eventListener);
		}
	}

	protected void postSetup() {
		BPMUtils.setupAuthId(null);
		BusinessContext.set(null);
	}
	
	@Override
	public List<ProcessDefinition> queryProcessDefinitons() {
		return repositoryService.createProcessDefinitionQuery().orderByProcessDefinitionName().asc().latestVersion()
				.list();
	}

	@Override
	public Map<String, List<ProcessDefinition>> queryProcessDefinationGroups() {
		Map<String,List<ProcessDefinition>> processDefinitions = new TreeMap<String, List<ProcessDefinition>>();
		List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().orderByProcessDefinitionName().asc().latestVersion()
		.list();
		if(list == null){
			return processDefinitions;
		}
		for (ProcessDefinition processDefinition : list) {
			String category = processDefinition.getCategory();
			List<ProcessDefinition> definitions = processDefinitions.get(category);
			if(definitions == null){
				definitions = new ArrayList<ProcessDefinition>();
				processDefinitions.put(category, definitions);
			}
			definitions.add(processDefinition);
		}
		return processDefinitions;
	}

	@Override
	public ProcessInstance startProcessByKey(String processDefinationKey, String processStarter, String bizId,
			Map<String, Object> processVariables, BPMEventListener listener) throws BPMException {
		if (log.isDebugEnabled()) {
			log.debug(
					"流程开始启动,PROCESSDEFINATIONKEY:{},PROCESSSTARTER;{},BIZID:{},PROCESSVARIABLES:{},BUSINESSCALLBACK:{}.",
					new Object[] { processDefinationKey, processStarter, bizId, processVariables, listener });
		}
		ProcessInstance processInstance = null;
		try {
			preCheck(processDefinationKey, processStarter, bizId);
			preSetup(processStarter, listener);
			if (processVariables == null) {
				processVariables = new HashMap<String, Object>();
			}
			processInstance = runtimeService.startProcessInstanceByKey(processDefinationKey, bizId, processVariables);

			log.info("流程启动成功," + "流程定义KEY:{}," + "流程定义ID:{}," + "流程实例ID:{}," + "绑定业务ID:{}," + "启动人:{}," + "时间戳(ms):{}.",
					new Object[] { processInstance.getProcessDefinitionKey(), processInstance.getProcessInstanceId(),
							processInstance.getProcessInstanceId(), processInstance.getBusinessKey(), processStarter,
							System.currentTimeMillis() });
			return processInstance;
		} catch (Exception e) {
			throw new BPMException("BPM_EX_10", e.getMessage(), e);
		} finally {
			postSetup();
		}
	}

	@Override
	public ProcessInstance startProcessById(String processDefinationId, String processStarter, String bizId,
			Map<String, Object> processVariables, BPMEventListener listener) throws BPMException {
		if (log.isDebugEnabled()) {
			log.debug(
					"流程开始启动,PROCESSDEFINATIONID:{},PROCESSSTARTER;{},BIZID:{},PROCESSVARIABLES:{},BUSINESSCALLBACK:{}.",
					new Object[] { processDefinationId, processStarter, bizId, processVariables, listener });
		}
		ProcessInstance processInstance = null;
		try {
			preCheck(processDefinationId, processStarter, bizId);
			preSetup(processStarter, listener);
			if (processVariables == null) {
				processVariables = new HashMap<String, Object>();
			}
			processInstance = runtimeService.startProcessInstanceById(processDefinationId, bizId, processVariables);
			log.info("流程启动成功," + "流程定义KEY:{}," + "流程定义ID:{}," + "流程实例ID:{}," + "绑定业务ID:{}," + "启动人:{}," + "时间戳(ms):{}.",
					new Object[] { processInstance.getProcessDefinitionKey(), processInstance.getProcessInstanceId(),
							processInstance.getProcessInstanceId(), processInstance.getBusinessKey(), processStarter,
							System.currentTimeMillis() });
			return processInstance;
		} catch (Exception e) {
			throw new BPMException("BPM_EX_10", e.getMessage(), e);
		} finally {
			postSetup();
		}
	}

	@Override
	public void claimTask(String taskId, String userId) throws BPMException {
		if (log.isDebugEnabled()) {
			log.debug("流程任务申领：TASKID:{},USERID;{}", new Object[] { taskId, userId });
		}
		try {
			taskService.claim(taskId, userId);
			log.info("流程任务申领成功,任务ID:{},申领人ID:{}.", new Object[] { taskId, userId });
		} catch (Exception e) {
			throw new BPMException("BPM_EX_11", e.getMessage(), e);
		}
	}

	@Override
	public void completeTask(String taskId, String userId, Map<String, Object> taskVaribles, BPMEventListener listener)
			throws BPMException {
		if (log.isDebugEnabled()) {
			log.debug("流程任务执行：TASKID:{},USERID:{}", new Object[] { taskId, userId });
		}
		try {
			BPMUtils.isTaskIdNULL(taskId);
			preSetup(userId, listener);
			if (taskVaribles == null) {
				taskVaribles = new HashMap<String, Object>();
			}
			taskService.complete(taskId, taskVaribles);
			log.info("流程任务执行成功," + "任务ID:{}," + "审核人ID:{}," + "时间戳(ms):{}.",
					new Object[] { taskId, userId, System.currentTimeMillis() });
		} catch (Exception e) {
			throw new BPMException("BPM_EX_12", e.getMessage(), e);
		} finally {
			postSetup();
		}
	}

	@Override
	public void submitTaskFormData(String taskId, String userId, Map<String, String> formProperties,
			BPMEventListener listener) throws BPMException {
		if (log.isDebugEnabled()) {
			log.debug("流程任务执行：TASKID:{},USERID:{}", new Object[] { taskId, userId });
		}
		try {
			BPMUtils.isTaskIdNULL(taskId);
			preSetup(userId, listener);
			if (formProperties == null) {
				formProperties = new HashMap<String, String>();
			}
			formService.submitTaskFormData(taskId, formProperties);
			log.info("流程任务执行成功," + "任务ID:{}," + "审核人ID:{}," + "时间戳(ms):{}.",
					new Object[] { taskId, userId, System.currentTimeMillis() });
		} catch (Exception e) {
			throw new BPMException("BPM_EX_12", e.getMessage(), e);
		} finally {
			postSetup();
		}
	}

	@Override
	public void auditorRevocation(String processInstanceId, String taskId, String reason, String userId,
			BPMEventListener listener) throws BPMException {
		if (log.isDebugEnabled()) {
			log.debug("任务撤销：TASKID:{},USERID:{}", new Object[] { taskId, userId });
		}
		try {
			BPMUtils.isTaskIdNULL(taskId);
			preSetup(userId, listener);
			if (extendService != null) {
				extendService.auditorRevocation(processInstanceId, taskId, reason);
			} else {
				throw new RuntimeException("当前流程组件暂不支持");
			}
			log.info("流程任务撤销成功," + "任务ID:{}," + "撤销人ID:{}," + "时间戳(ms):{}.",
					new Object[] { taskId, userId, System.currentTimeMillis() });
		} catch (Exception e) {
			throw new BPMException(e.getMessage(),e.getMessage(), e);
		} finally {
			postSetup();
		}
	}

	@Override
	public void auditorProcessCancellation(String processInstanceId, String taskId, String reason, String userId,
			BPMEventListener listener) throws BPMException {
		if (log.isDebugEnabled()) {
			log.debug("流程实例撤销：PROCESSINSTANCEID:{},USERID:{}", new Object[] { processInstanceId, userId });
		}
		try {
			BPMUtils.isProcessInstanceIdNULL(processInstanceId);
			preSetup(userId, listener);
			if (extendService != null) {
				extendService.auditorProcessCancellation(processInstanceId, taskId, reason);
			} else {
				throw new RuntimeException("当前流程组件暂不支持");
			}
			log.info("流程实例撤销成功," + "流程ID:{}," + "撤销人ID:{}," + "时间戳(ms):{}.",
					new Object[] { processInstanceId, userId, System.currentTimeMillis() });
		} catch (Exception e) {
			throw new BPMException(e.getMessage(),e.getMessage(), e);
		} finally {
			postSetup();
		}
	}

	@Override
	public void initiatorRevocation(String processInstanceId, String userId, BPMEventListener listener)
			throws BPMException {
		if (log.isDebugEnabled()) {
			log.debug("流程撤销：PROCESSINSTANCEID:{},USERID:{}", new Object[] { processInstanceId, userId });
		}
		try {
			BPMUtils.isProcessInstanceIdNULL(processInstanceId);
			preSetup(userId, listener);
			if (extendService != null) {
				extendService.initiatorRevocation(processInstanceId);
			} else {
				throw new RuntimeException("当前流程组件暂不支持");
			}
			log.info("流程撤销成功," + "流程实例ID:{}," + "撤销人ID:{}," + "时间戳(ms):{}.",
					new Object[] { processInstanceId, userId, System.currentTimeMillis() });
		} catch (Exception e) {
			throw new BPMException(e.getMessage(),e.getMessage(), e);
		} finally {
			postSetup();
		}
	}

	@Override
	public ObjectNode getTaskFormJSONObject(String taskId) {
		Object renderedTaskForm = formService.getRenderedTaskForm(taskId, getFormEngineName());
		return (ObjectNode) renderedTaskForm;
	}
	
	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}

	public void setIdentityService(IdentityService identityService) {
		this.identityService = identityService;
	}

	public void setFormService(FormService formService) {
		this.formService = formService;
	}

	public void setRuntimeService(RuntimeService runtimeService) {
		this.runtimeService = runtimeService;
	}

	public void setHistoryService(HistoryService historyService) {
		this.historyService = historyService;
	}

	public void setManagementService(ManagementService managementService) {
		this.managementService = managementService;
	}

	public void setDynamicBpmnService(DynamicBpmnService dynamicBpmnService) {
		this.dynamicBpmnService = dynamicBpmnService;
	}

	public ProcessEngine getProcessEngine() {
		return processEngine;
	}

	public void setProcessEngine(ProcessEngine processEngin) {
		this.processEngine = processEngin;
	}

	@Override
	public void setFormEngineName(String formEngineName) {
		this.formEngineName = formEngineName;
	}

	public String getFormEngineName() {
		return formEngineName;
	}
	
}
