package org.activiti.engine.extend.cmd;

import java.io.Serializable;
import java.util.List;

import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.extend.assignment.Assignment;
import org.activiti.engine.extend.persistence.entity.AssignmentEntity;
import org.activiti.engine.extend.persistence.entity.AssignmentEntityManager;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;

/**
 * <p>
 * <h3>niutal框架
 * <h3><br>
 * 说明：获取任务操作人员设置信息Cmd <br>
 * class：org.activiti.engine.extend.cmd.GetTaskAssignmentCmd.java
 * <p>
 * 
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
public class GetTaskAssignmentCmd implements Command<List<Assignment>>, Serializable {

	private static final long serialVersionUID = 1L;

	protected String processDefinitionId;
	protected String taskDefinitionId;

	@Override
	public List<Assignment> execute(CommandContext commandContext) {
		AssignmentEntityManager assignmentEntityManager = commandContext.getSession(AssignmentEntityManager.class);
		if (processDefinitionId == null) {
			throw new ActivitiIllegalArgumentException("process definition id can not be null");
		}
		if (taskDefinitionId == null) {
			throw new ActivitiIllegalArgumentException("task definition id can not be null");
		}
		
		List<Assignment> findAssignmentByProcessDefinitionIdAndTaskDefintionId = assignmentEntityManager.findAssignmentByProcessDefinitionIdAndTaskDefintionId(processDefinitionId,
				taskDefinitionId);
		
		postSetup(findAssignmentByProcessDefinitionIdAndTaskDefintionId);
		
		return findAssignmentByProcessDefinitionIdAndTaskDefintionId;
	}

	protected void postSetup(List<Assignment> findAssignmentByProcessDefinitionId) {
		if(findAssignmentByProcessDefinitionId != null){
			for (Assignment assignment : findAssignmentByProcessDefinitionId) {
				((AssignmentEntity)assignment).getGroupEntity();
				((AssignmentEntity)assignment).getUserEntity();
			}
		}
	}
	
	public GetTaskAssignmentCmd(String processDefinitionId, String taskDefinitionId) {
		super();
		this.processDefinitionId = processDefinitionId;
		this.taskDefinitionId = taskDefinitionId;
	}
}
