package org.activiti.engine.extend.cmd;

import java.io.Serializable;
import java.util.List;

import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.extend.assignment.Assignment;
import org.activiti.engine.extend.persistence.entity.AssignmentEntityManager;
import org.activiti.engine.impl.db.PersistentObject;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;

/**
 * <p>
 *   <h3>niutal框架<h3>
 *   <br>说明：保存办理人员设置
 *	 <br>class：org.activiti.engine.extend.cmd.SetupAssignmentCmd.java
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
public class SetupTaskAssignmentCmd implements Command<Void>, Serializable{

	private static final long serialVersionUID = 1L;

	protected List<Assignment> taskAssignment;
	
	protected String processDefinitionId;
	
	@Override
	public Void execute(CommandContext commandContext) {
		if(processDefinitionId == null || processDefinitionId.length() == 0){
			throw new ActivitiIllegalArgumentException("processDefinitionId can not be null");
		}
		if(taskAssignment == null || taskAssignment.size() == 0){
			throw new ActivitiIllegalArgumentException("taskAssignment can not be empty");
		}
		AssignmentEntityManager assignmentEntityManager = commandContext.getSession(AssignmentEntityManager.class);
		//先删除老的设置
		assignmentEntityManager.deleteAssignmentByProcessDefinitionId(processDefinitionId);
		//插入新的设置
		for (Assignment assignment : taskAssignment) {
			assignmentEntityManager.insert((PersistentObject) assignment);
		}
		return null;
	}

	public SetupTaskAssignmentCmd(String processDefinitionId, List<Assignment> taskAssignment) {
		super();
		this.processDefinitionId = processDefinitionId;
		this.taskAssignment = taskAssignment;
	}
}
