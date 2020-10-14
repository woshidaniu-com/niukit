package com.woshidaniu.component.bpm.management.process.definition.dao.entities;

import java.io.Serializable;
import java.util.List;

import org.activiti.bpmn.model.UserTask;

/**
 * <p>
 *   <h3>niutal框架<h3>
 *   <br>说明：TODO
 *	 <br>class：com.woshidaniu.component.bpm.management.process.defination.dao.entities.TaskDefination.java
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
public class TaskDefinitionModel implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String taskId;
	
	private String taskName;
	
	private String taskDesc;
	
	private String taskCategory;
	
	private String assignee;
	
	private List<String> candidateUsers;
	
	private List<String> candidateGroups;

	private TaskDefinitionModel previous;
	private TaskDefinitionModel next;
	
	private UserTask bpmnUserTaskModel;
	
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskDesc() {
		return taskDesc;
	}

	public void setTaskDesc(String taskDesc) {
		this.taskDesc = taskDesc;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public List<String> getCandidateUsers() {
		return candidateUsers;
	}

	public void setCandidateUsers(List<String> candidateUsers) {
		this.candidateUsers = candidateUsers;
	}

	public List<String> getCandidateGroups() {
		return candidateGroups;
	}

	public void setCandidateGroups(List<String> candidateGroups) {
		this.candidateGroups = candidateGroups;
	}

	public String getTaskCategory() {
		return taskCategory;
	}

	public void setTaskCategory(String taskCategory) {
		this.taskCategory = taskCategory;
	}

	public TaskDefinitionModel getPrevious() {
		return previous;
	}

	public void setPrevious(TaskDefinitionModel previous) {
		this.previous = previous;
	}

	public TaskDefinitionModel getNext() {
		return next;
	}

	public void setNext(TaskDefinitionModel next) {
		this.next = next;
	}

	public UserTask getBpmnUserTaskModel() {
		return bpmnUserTaskModel;
	}

	public void setBpmnUserTaskModel(UserTask bpmnUserTaskModel) {
		this.bpmnUserTaskModel = bpmnUserTaskModel;
	}

	
	
}
