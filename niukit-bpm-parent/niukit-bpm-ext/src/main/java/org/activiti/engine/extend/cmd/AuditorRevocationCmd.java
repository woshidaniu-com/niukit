package org.activiti.engine.extend.cmd;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.extend.event.impl.ExtendAuditorRevocationEventImpl;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.DeploymentEntity;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;

/**
 * <p>
 * <h3>niutal框架
 * <h3><br>
 * 说明：任务撤销命令【只有在下一任务环节没有完成时才能撤销】 <br>
 * class：org.activiti.engine.extend.cmd.RevocationTaskCmd.java
 * <p>
 * 
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
public class AuditorRevocationCmd implements Command<Void>, Serializable {
	private static final long serialVersionUID = 1L;

	static final String DELETE_REASON_REVOCATION = "Auditor Revocation";

	protected String processInstanceId;

	protected String hisTaskInstanceId;

	protected String revocationReason;

	public AuditorRevocationCmd(String processInstanceId, String hisTaskInstanceId, String revocationReason) {
		super();
		this.processInstanceId = processInstanceId;
		this.hisTaskInstanceId = hisTaskInstanceId;
		this.revocationReason = revocationReason;
	}

	@Override
	public Void execute(CommandContext commandContext) {
		ExecutionEntity execution = commandContext.getExecutionEntityManager().findExecutionById(processInstanceId);
		if (execution == null || execution.isEnded()) {
			throw new ActivitiException("BPM_EX_20");
		}
		if (execution.isSuspended()) {
			throw new ActivitiException("BPM_EX_21");
		}
		if(execution.isConcurrent()){
			throw new ActivitiException("BPM_EX_22");
		}
		
		ProcessDefinitionEntity processDefinitionEntity = commandContext.getProcessDefinitionEntityManager().findProcessDefinitionById(execution.getProcessDefinitionId());
		
		DeploymentEntity deploymentEntity = commandContext.getDeploymentEntityManager().findDeploymentById(processDefinitionEntity.getDeploymentId());
		
		if(!"simple".equals(deploymentEntity.getCategory())){
			throw new ActivitiException("BPM_EX_23");
		}
		
		HistoricTaskInstanceEntity hisTaskInstance = commandContext.getHistoricTaskInstanceEntityManager()
				.findHistoricTaskInstanceById(hisTaskInstanceId);
		if (hisTaskInstance == null) {
			throw new ActivitiException("BPM_EX_24");
		}

		TaskEntity instanceTask = commandContext.getTaskEntityManager()
				.findTasksByProcessInstanceId(execution.getProcessInstanceId()).get(0);
		if(instanceTask.getTaskDefinitionKey().equals(hisTaskInstance.getTaskDefinitionKey())){
			throw new ActivitiException("BPM_EX_25");
		}
		
		if (instanceTask.getCategory() != null
				&& instanceTask.getCategory().equals(hisTaskInstance.getTaskDefinitionKey())) {
			Map<String, String> variables = new HashMap<String, String>();
			variables.put("p_decision", "BACK_" + instanceTask.getCategory());
			variables.put("p_decision_message", "[" + hisTaskInstance.getName() + "]发起撤销");
			instanceTask.setVariableLocal("p_auditor_revocation", "1");
			commandContext.getProcessEngineConfiguration().getFormService().submitTaskFormData(instanceTask.getId(),
					variables);
			
			instanceTask.setVariable("p_decision", "BACK_AR");
			/**
			 * 派发自定义事件,触发业务逻辑操作
			 */
			if (commandContext.getEventDispatcher().isEnabled()) {
				ExtendAuditorRevocationEventImpl extendRevocationEvent = new ExtendAuditorRevocationEventImpl(
						execution);
				extendRevocationEvent.setExecutionId(execution.getId());
				extendRevocationEvent.setProcessDefinitionId(execution.getProcessDefinitionId());
				extendRevocationEvent.setProcessInstanceId(execution.getProcessInstanceId());
				extendRevocationEvent.setTaskEntity(instanceTask);
				extendRevocationEvent.setHisTaskEntity(hisTaskInstance);
				commandContext.getEventDispatcher().dispatchEvent(extendRevocationEvent);
			}
		} else {
			throw new ActivitiException("BPM_EX_23");
		}
		return null;
	}

}
