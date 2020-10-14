package com.woshidaniu.component.bpm.management.simpelWorkflow;

import java.io.Serializable;
import java.util.List;

public class SimpleHumanStep implements Serializable {
	private static final long serialVersionUID = 1L;

	private String taskId;

	private String taskName;

	private String taskDesc;

	private String taskCategory;

	private String assignee;
	
	private boolean backSupport = true;
	
	private boolean interruptSupport = true;

	private List<String> candidateUsers;
	
	private List<String> candidateGroups;


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

	public String getTaskCategory() {
		return taskCategory;
	}

	public void setTaskCategory(String taskCategory) {
		this.taskCategory = taskCategory;
	}

	public boolean isBackSupport() {
		return backSupport;
	}

	public void setBackSupport(boolean backSupport) {
		this.backSupport = backSupport;
	}

	public boolean isInterruptSupport() {
		return interruptSupport;
	}

	public void setInterruptSupport(boolean interruptSupport) {
		this.interruptSupport = interruptSupport;
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

	
	
}
