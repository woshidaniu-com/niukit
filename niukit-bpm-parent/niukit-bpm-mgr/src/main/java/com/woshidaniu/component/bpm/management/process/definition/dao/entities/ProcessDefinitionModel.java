package com.woshidaniu.component.bpm.management.process.definition.dao.entities;

import java.io.Serializable;
import java.util.List;

import com.woshidaniu.component.bpm.common.BPMQueryModel;


/**
 * 
 * <p>
 *   <h3>niutal框架<h3>
 *   <br>说明：审批流程定义模型
 *	 <br>class：com.woshidaniu.component.bpm.management.process.defination.dao.entities.ProcessDefinationModel.java
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
public class ProcessDefinitionModel implements Serializable{

	private static final long serialVersionUID = 1L;
	
	//**************************************************//
	public BPMQueryModel queryModel = new BPMQueryModel();
	
	public BPMQueryModel getQueryModel() {
		return queryModel;
	}
	public void setQueryModel(BPMQueryModel queryModel) {
		this.queryModel = queryModel;
	}
	//**************************************************//
	
	private String id;
	
	private String version;
	
	private String category;
	
	private String name;
	
	private String key;
	
	private String description;
	
	//1:活动状态，2：挂起状态
	private String state;
	
	private String deploymentId;
	
	private String deploymentCategory;

	private List<TaskDefinitionModel> taskDefinitions;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDeploymentId() {
		return deploymentId;
	}

	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
	}
	@Override
	public String toString() {
		return "ProcessDefModel [id=" + id + ", version=" + version + ", category=" + category + ", name=" + name
				+ ", key=" + key + ", description=" + description + ", state=" + state + "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<TaskDefinitionModel> getTaskDefinations() {
		return taskDefinitions;
	}

	public void setTaskDefinations(List<TaskDefinitionModel> taskDefinations) {
		this.taskDefinitions = taskDefinations;
	}

	public String getDeploymentCategory() {
		return deploymentCategory;
	}

	public void setDeploymentCategory(String deploymentCategory) {
		this.deploymentCategory = deploymentCategory;
	}

}
