package com.woshidaniu.component.bpm.management.simpelWorkflow;

import java.io.Serializable;
import java.util.List;

public class SimpleWorkflow implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private String category;
	
	private String name;
	
	private String key;
	
	private String description;
	
	private String deploymentId;

	private List<SimpleHumanStep> taskDefinitions;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public String getDeploymentId() {
		return deploymentId;
	}

	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<SimpleHumanStep> getTaskDefinitions() {
		return taskDefinitions;
	}

	public void setTaskDefinitions(List<SimpleHumanStep> taskDefinitions) {
		this.taskDefinitions = taskDefinitions;
	}
}
