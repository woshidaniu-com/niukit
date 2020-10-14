package org.activiti.engine.extend.cmd;

import java.io.Serializable;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.extend.event.impl.ExtendAuditorProcessCancellationEvent;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;

/**
 * <p>
 * <h3>niutal框架
 * <h3><br>
 * 说明：流程任务环节撤销流程命令【用于退回到申请人】 <br>
 * class：org.activiti.engine.extend.cmd.AuditorProcessCancellation.java
 * <p>
 * 
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
public class AuditorProcessCancellationCmd implements Command<Void>, Serializable {

	private static final long serialVersionUID = 1L;

	protected static String DELETE_REASON = "Auditor Cancelled";
	
	protected static final String DECISION = "BACK_AC";
	// 流程实例ID
	protected String processInstanceId;
	
	protected String taskId;
	// 取消原因
	protected String cancelReason;

	@Override
	public Void execute(CommandContext commandContext) {
		ExecutionEntity processInstance = commandContext.getExecutionEntityManager()
				.findExecutionById(processInstanceId);

		if (processInstance == null) {
			throw new ActivitiException("BPM_EX_30");
		}

		if (processInstance.isSuspended()) {
			throw new ActivitiException("BPM_EX_31");
		}

		TaskEntity taskEntity = commandContext.getTaskEntityManager().findTaskById(taskId);
		if (taskEntity == null) {
			throw new ActivitiException("BPM_EX_26");
		}
		
		processInstance.setVariable("p_decision", DECISION);
		processInstance.setVariable("p_decision_message", cancelReason);
		commandContext.getExecutionEntityManager().deleteProcessInstance(processInstanceId, DELETE_REASON, false);
		
		if (commandContext.getEventDispatcher().isEnabled()) {
			ExtendAuditorProcessCancellationEvent event = 
					new ExtendAuditorProcessCancellationEvent(taskEntity);
			event.setExecutionId(processInstance.getId());
			event.setProcessDefinitionId(processInstance.getProcessDefinitionId());
			event.setProcessInstanceId(processInstance.getProcessInstanceId());
			commandContext.getEventDispatcher().dispatchEvent(event);
		}

		return null;
	}

	public AuditorProcessCancellationCmd(String processInstanceId, String taskId, String cancelReason) {
		super();
		this.processInstanceId = processInstanceId;
		this.taskId = taskId;
		this.cancelReason = cancelReason;
	}
}
