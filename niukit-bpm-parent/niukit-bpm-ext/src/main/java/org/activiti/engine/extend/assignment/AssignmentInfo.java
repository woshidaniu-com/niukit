package org.activiti.engine.extend.assignment;

import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;

public interface AssignmentInfo {
	String getGroupId();
	
	String getUserId();
	
	String getType();
	
	String getTaskDefinitionId();
	
	String getProcessDefinitionId();
	
	//
	User getUserEntity();
	Group getGroupEntity();
}
