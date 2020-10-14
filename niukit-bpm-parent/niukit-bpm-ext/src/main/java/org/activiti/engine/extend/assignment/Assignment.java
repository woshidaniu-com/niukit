package org.activiti.engine.extend.assignment;

/**
 * <p>
 *   <h3>niutal框架<h3>
 *	 <br>class：org.activiti.engine.extend.persistence.entity.Assignment.java
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
public interface Assignment extends AssignmentInfo{

	void setGroupId(String groupId);
	
	void setUserId(String userId);
	
	void setType(String type);
	
	void setTaskDefinitionId(String taskDefinitionId);
	
	void setProcessDefinitionId(String processDefintionId);
	
}
