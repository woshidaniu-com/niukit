package com.woshidaniu.component.bpm;

import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.VariableScope;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.impl.ActivitiEntityEventImpl;
import org.activiti.engine.delegate.event.impl.ActivitiEntityWithVariablesEventImpl;
import org.activiti.engine.delegate.event.impl.ActivitiProcessStartedEventImpl;
import org.activiti.engine.extend.assignment.Assignment;
import org.activiti.engine.extend.cfg.ExtendSpringProcessEnginConfiguration;
import org.activiti.engine.extend.event.impl.ExtendAuditorProcessCancellationEvent;
import org.activiti.engine.extend.event.impl.ExtendAuditorRevocationEventImpl;
import org.activiti.engine.extend.event.impl.ExtendInitiatorRevocationEventImpl;
import org.activiti.engine.extend.event.listener.AbstractEventListener;
import org.activiti.engine.extend.log.ProcessLog;
import org.activiti.engine.extend.log.ProcessLoggerManager;
import org.activiti.engine.extend.log.impl.DefaultProcessLog;
import org.activiti.engine.extend.persistence.entity.AssignmentEntityManager;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;

import com.woshidaniu.component.bpm.listener.ExtendEventListener;
import com.woshidaniu.component.bpm.listener.ProcessInstanceEventListener;
import com.woshidaniu.component.bpm.listener.TaskEventListener;

/**
 * <p>
 * <h3>niutal框架
 * <h3><br>
 * 说明：流程事件监听接口 <br>
 * class：com.woshidaniu.component.bpm.BPMEventListener.java
 * <p>
 * 
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
public abstract class BPMEventListener extends AbstractEventListener
		implements ProcessInstanceEventListener, TaskEventListener, ExtendEventListener {
	
	protected void recordLog(Object entity) {
		ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
		if (processEngineConfiguration instanceof ExtendSpringProcessEnginConfiguration) {
			ProcessLoggerManager processLoggerManager = ((ExtendSpringProcessEnginConfiguration) processEngineConfiguration)
					.getProcessLoggerManager();
			if (entity instanceof VariableScope) {
				ProcessLog processLog = getProcessLog(entity);
				processLoggerManager.log(processLog);
			}
		}
	}
	
	protected ProcessLog getProcessLog(Object entity) {
		DefaultProcessLog defaultProcessLog = new DefaultProcessLog("simpleLogger", (VariableScope) entity);
		defaultProcessLog.setLogMessage((String) ((VariableScope)entity).getVariable("p_decision"));
		defaultProcessLog.setLogMoreMessage((String) ((VariableScope)entity).getVariable("p_decision_message"));
		if(defaultProcessLog != null && entity instanceof TaskEntity){
			defaultProcessLog.setProcessInstanceId(((TaskEntity)entity).getProcessInstanceId());
			defaultProcessLog.setTaskId(((TaskEntity)entity).getId());
		}
		return defaultProcessLog;
	}


	@Override
	public void onAuditorRevocation(ActivitiEvent event) {
		ExtendAuditorRevocationEventImpl autitorRevocationEvent = (ExtendAuditorRevocationEventImpl) event;
		recordLog(autitorRevocationEvent.getTaskEntity());

		ExtendAuditorRevocationEventImpl auditorRevocationEvent = (ExtendAuditorRevocationEventImpl) event;
		onAuditorRevocation((DelegateExecution) auditorRevocationEvent.getEntity(),
				(DelegateTask) auditorRevocationEvent.getTaskEntity(), auditorRevocationEvent);
	}

	@Override
	public void onAuditorProcessCancellation(ActivitiEvent event) {
		ExtendAuditorProcessCancellationEvent auditorCancellationEvent = (ExtendAuditorProcessCancellationEvent)event;
		recordLog(auditorCancellationEvent.getEntity());
		onAuditorProcessCancellation((DelegateTask) auditorCancellationEvent.getEntity(), event);
	}

	@Override
	public void onInitiatorRevocation(ActivitiEvent event) {
		ExtendInitiatorRevocationEventImpl initiatorRevocationEvent = (ExtendInitiatorRevocationEventImpl) event;
		recordLog(initiatorRevocationEvent.getTaskEntity());

		onInitiatorRevocation((DelegateExecution) initiatorRevocationEvent.getEntity(), initiatorRevocationEvent);
	}

	@Override
	public void onTaskCreated(ActivitiEvent event) {
		ActivitiEntityEventImpl taskCompleteEvent = (ActivitiEntityEventImpl) event;
		
		DelegateTask delegateTask = (DelegateTask) taskCompleteEvent.getEntity();
		
		List<Assignment> assignments = Context.getCommandContext().getSession(AssignmentEntityManager.class)
				.findAssignmentByProcessDefinitionIdAndTaskDefintionId(delegateTask.getProcessDefinitionId(),
						delegateTask.getTaskDefinitionKey());

		// 设置任务操办人员，如果有的话
		if (assignments != null && assignments.size() > 0) {
			for (Assignment assignment : assignments) {
				if (BPMUtils.isNotBlank(assignment.getUserId())) {
					delegateTask.addCandidateUser(assignment.getUserId());
				}
				if (BPMUtils.isNotBlank(assignment.getGroupId())) {
					delegateTask.addCandidateGroup(assignment.getGroupId());
				}
			}
		}
		onTaskCreated(delegateTask, event);
	}

	@Override
	public void onTaskCompleted(ActivitiEvent event) {
		ActivitiEntityWithVariablesEventImpl taskCompleteEvent = (ActivitiEntityWithVariablesEventImpl) event;
		TaskEntity taskEntity = (TaskEntity) taskCompleteEvent.getEntity();
		// 如果不是用户撤销操作才记录操作日志
		if (!"1".equals(taskEntity.getVariableLocal("p_auditor_revocation"))) {
			recordLog(taskEntity);
		}
		onTaskCompleted((DelegateTask) taskCompleteEvent.getEntity(), event);
	}

	@Override
	public void onProcessStarted(ActivitiEvent event) {
		ActivitiProcessStartedEventImpl processStartedEvent = ((ActivitiProcessStartedEventImpl) event);
		ExecutionEntity entity = (ExecutionEntity) processStartedEvent.getEntity();
		onProcessInstanceStarted(entity, event);
	}

	@Override
	public void onProcessCompleted(ActivitiEvent event) {
		ActivitiEntityEventImpl processCompleteEvent = ((ActivitiEntityEventImpl) event);
		ExecutionEntity entity = (ExecutionEntity) processCompleteEvent.getEntity();
		onProcessInstanceEnded(entity, event);
	}

	@Override
	public void onProcessCancelled(ActivitiEvent event) {
		// super.onProcessCancelled(event);
		// ActivitiProcessCancelledEventImpl processCancelledEvent =
		// ((ActivitiProcessCancelledEventImpl)event);
		// ExecutionEntity entity = (ExecutionEntity)processCancelledEvent.
		// onProcessInstanceCancelled(entity, event);
	}

}
