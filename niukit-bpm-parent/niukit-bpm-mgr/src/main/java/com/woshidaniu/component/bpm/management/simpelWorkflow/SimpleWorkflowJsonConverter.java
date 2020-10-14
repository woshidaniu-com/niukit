package com.woshidaniu.component.bpm.management.simpelWorkflow;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * <p>
 *   <h3>niutal框架<h3>
 *   <br>说明：TODO
 *	 <br>class：com.woshidaniu.component.bpm.management.simpelWorkflow.SimpleWorkflowJsonConverter.java
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
public class SimpleWorkflowJsonConverter {

	private SimpleWorkflow wokeflow;
	
	private ObjectNode objectNode;

	public SimpleWorkflowJsonConverter(SimpleWorkflow wokeflow) {
		super();
		this.wokeflow = wokeflow;
	}

	public SimpleWorkflowJsonConverter(ObjectNode objectNode) {
		super();
		this.objectNode = objectNode;
	}
	
	public void convert(){
		if(wokeflow!= null)
			doConvert();
	}
	
	public void reverseConvert(){
		if(objectNode!=null)
			doReverseConvert();
	}
	
	protected void doReverseConvert() {
		wokeflow = new SimpleWorkflow();
		String name = objectNode.get("name").textValue();
		String description = objectNode.get("description").textValue();
		String category = objectNode.get("category").textValue();
		wokeflow.setCategory(category);
		wokeflow.setDescription(description);
		wokeflow.setName(name);
		wokeflow.setTaskDefinitions(new ArrayList<SimpleHumanStep>());
		ArrayNode steps = (ArrayNode)objectNode.get("taskDefinitions");
		Iterator<JsonNode> iterator = steps.iterator();
		while(iterator.hasNext()){
			SimpleHumanStep huamnStep = new SimpleHumanStep();
			wokeflow.getTaskDefinitions().add(huamnStep);
			JsonNode step = iterator.next();
			String taskName = step.get("taskName").textValue();
			String taskDesc = step.get("taskDesc").textValue();
			String assignee = step.get("assignee").textValue();
			String taskCategory = step.get("taskCategory").textValue();
			boolean backSupport = step.get("backSupport").booleanValue();
			boolean interruptSupport = step.get("interruptSupport").booleanValue();
			huamnStep.setAssignee(assignee);
			huamnStep.setBackSupport(backSupport);
			huamnStep.setInterruptSupport(interruptSupport);
			huamnStep.setTaskCategory(taskCategory);
			huamnStep.setTaskDesc(taskDesc);
			huamnStep.setTaskName(taskName);
			
			huamnStep.setCandidateGroups(new ArrayList<String>());
			huamnStep.setCandidateUsers(new ArrayList<String>());
			
			ArrayNode candidateUsersNodes = (ArrayNode)step.get("candidateUsers");
			if(candidateUsersNodes != null){
				Iterator<JsonNode> candidateUsersIterator = candidateUsersNodes.iterator();
				while(candidateUsersIterator.hasNext()){
					JsonNode catdidateUser = candidateUsersIterator.next();
					huamnStep.getCandidateUsers().add(catdidateUser.get("userId").textValue());
				}
			}
			
			ArrayNode candidateGroupsNodes= (ArrayNode)step.get("candidateGroups");
			if(candidateGroupsNodes != null){
				Iterator<JsonNode> candidateGroupsIterator = candidateGroupsNodes.iterator();
				while(candidateGroupsIterator.hasNext()){
					JsonNode catdidateGroup = candidateGroupsIterator.next();
					huamnStep.getCandidateGroups().add(catdidateGroup.get("groupId").textValue());
				}
			}
		}
	}

	public ObjectNode getObjectNode(){
		return objectNode;
	} 
	
	public SimpleWorkflow getSimpleWorkflow(){
		return wokeflow;
	} 
	
	protected void doConvert(){
		ObjectMapper objectMapper = new ObjectMapper();
		objectNode = objectMapper.createObjectNode();
		objectNode.put("name",  wokeflow.getName());
		objectNode.put("description",  wokeflow.getDescription());
		objectNode.put("category",  wokeflow.getCategory());
		ArrayNode taskArrayNode = objectMapper.createArrayNode();
		objectNode.put("taskDefinitions", taskArrayNode);
		List<SimpleHumanStep> steps = wokeflow.getTaskDefinitions();
		for (SimpleHumanStep step : steps) {
			taskArrayNode.add(getHumanStepObjectNode(step));
		}
	}
	
	protected ObjectNode getHumanStepObjectNode(SimpleHumanStep step) {
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode taskNode = objectMapper.createObjectNode();
		taskNode.put("taskId", step.getTaskId());
		taskNode.put("taskName", step.getTaskName());
		taskNode.put("taskDesc", step.getTaskDesc());
		taskNode.put("taskCategory", step.getTaskCategory());
		taskNode.put("assignee", step.getAssignee());
		taskNode.put("backSupport", step.isBackSupport());
		taskNode.put("interruptSupport", step.isInterruptSupport());
		if (step.getCandidateUsers() != null && step.getCandidateUsers().size() > 0) {
			ArrayNode taskUsersNode = objectMapper.createArrayNode();
			taskNode.put("candidateUsers", taskUsersNode);
			for (String candidateUser : step.getCandidateUsers()) {
				String userId = candidateUser.split("\\|")[0];
				String userName = candidateUser.split("\\|")[1];
				ObjectNode candidateUserNode = objectMapper.createObjectNode();
				candidateUserNode.put("userId", userId);
				candidateUserNode.put("userName", userName);
				taskUsersNode.add(candidateUserNode);
			}
		}

		if (step.getCandidateGroups() != null && step.getCandidateGroups().size() > 0) {
			ArrayNode taskGroupNode = objectMapper.createArrayNode();
			taskNode.put("candidateGroups", taskGroupNode);
			for (String candidateGroup : step.getCandidateGroups()) {
				String groupId = candidateGroup.split("\\|")[0];
				String groupName = candidateGroup.split("\\|")[1];
				ObjectNode candidateGroupNode = objectMapper.createObjectNode();
				candidateGroupNode.put("groupId", groupId);
				candidateGroupNode.put("groupName", groupName);
				taskGroupNode.add(candidateGroupNode);
			}
		}

		return taskNode;
	}
	
}
