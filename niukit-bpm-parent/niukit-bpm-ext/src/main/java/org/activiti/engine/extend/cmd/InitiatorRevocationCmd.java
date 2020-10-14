package org.activiti.engine.extend.cmd;

import java.io.Serializable;
import java.util.List;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.extend.event.impl.ExtendInitiatorRevocationEventImpl;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.DeploymentEntity;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;

/**
 * <p>
 * <h3>niutal框架
 * <h3><br>
 * 说明：申请人撤销CMD <br>
 * class：org.activiti.engine.extend.cmd.CancelProcessInstanceCmd.java
 * <p>
 * 
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
public class InitiatorRevocationCmd implements Command<Void>, Serializable {

	private static final long serialVersionUID = 1L;
	protected String processInstanceId;

	protected static String DELETE_REASON = "Initiator Cancelled";

	public InitiatorRevocationCmd(String processInstanceId) {
		super();
		this.processInstanceId = processInstanceId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.activiti.engine.impl.interceptor.Command#execute(org.activiti.engine.
	 * impl.interceptor.CommandContext)
	 */
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
		List<TaskEntity> taskList = commandContext.getTaskEntityManager()
				.findTasksByProcessInstanceId(processInstanceId);

		ProcessDefinitionEntity processDefinitionEntity = commandContext.getProcessDefinitionEntityManager().findProcessDefinitionById(processInstance.getProcessDefinitionId());
		
		DeploymentEntity deploymentEntity = commandContext.getDeploymentEntityManager().findDeploymentById(processDefinitionEntity.getDeploymentId());
		
		if(!"simple".equals(deploymentEntity.getCategory())){
			throw new ActivitiException("BPM_EX_32");
		}
		
		if (taskList != null && taskList.size() == 1) {
			TaskEntity taskEntity = taskList.get(0);
			if (taskEntity != null && taskEntity.getCategory().equals("start")) {
				commandContext.getExecutionEntityManager().deleteProcessInstance(processInstanceId, DELETE_REASON,
						false);
				if (commandContext.getEventDispatcher().isEnabled()) {
					ExtendInitiatorRevocationEventImpl event = new ExtendInitiatorRevocationEventImpl(processInstance);
					event.setExecutionId(processInstance.getId());
					event.setProcessDefinitionId(processInstance.getProcessDefinitionId());
					event.setProcessInstanceId(processInstance.getProcessInstanceId());
					event.setTaskEntity(taskEntity);
					commandContext.getEventDispatcher().dispatchEvent(event);
				}
			} else {
				throw new ActivitiException("BPM_EX_33");
			}
		} else {
			throw new ActivitiException("BPM_EX_32");
		}

		return null;
	}

}
