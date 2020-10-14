package org.activiti.engine.extend.service.impl;

import java.util.List;

import org.activiti.engine.extend.assignment.Assignment;
import org.activiti.engine.extend.cmd.AuditorProcessCancellationCmd;
import org.activiti.engine.extend.cmd.AuditorRevocationCmd;
import org.activiti.engine.extend.cmd.GetProcessAssignmentCmd;
import org.activiti.engine.extend.cmd.GetTaskAssignmentCmd;
import org.activiti.engine.extend.cmd.InitiatorRevocationCmd;
import org.activiti.engine.extend.cmd.SetupTaskAssignmentCmd;
import org.activiti.engine.extend.service.ExtendService;
import org.activiti.engine.impl.ServiceImpl;

/**
 * <p>
 *   <h3>niutal框架<h3>
 *   <br>说明：扩展activit服务
 *	 <br>class：org.activiti.engine.extend.service.impl.ExtendServiceImpl.java
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
public class ExtendServiceImpl extends ServiceImpl implements ExtendService{

	@Override
	public void auditorRevocation(String processInstanceId, String taskId, String reason) {
		commandExecutor.execute(new AuditorRevocationCmd(processInstanceId, taskId, reason));
	}

	@Override
	public void auditorProcessCancellation(String processInstanceId, String taskId, String reason) {
		commandExecutor.execute(new AuditorProcessCancellationCmd(processInstanceId, taskId, reason));
	}

	@Override
	public void initiatorRevocation(String processInstanceId) {
		commandExecutor.execute(new InitiatorRevocationCmd(processInstanceId));
	}

	@Override
	public List<Assignment> getAssignmentForTask(String processDefinitionId, String taskDefinitionId) {
		return commandExecutor.execute(new GetTaskAssignmentCmd(processDefinitionId, taskDefinitionId));
	}

	@Override
	public List<Assignment> getAssignmentForProcess(String processDefinitionId) {
		return commandExecutor.execute(new GetProcessAssignmentCmd(processDefinitionId));
	}

	@Override
	public void setupTaskAssignment(String processDefinitionId, List<Assignment> taskAssignment) {
		commandExecutor.execute(new SetupTaskAssignmentCmd(processDefinitionId, taskAssignment));
	}

}
