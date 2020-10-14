package com.woshidaniu.component.bpm.management.process.definition.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.extend.assignment.Assignment;
import org.activiti.engine.extend.persistence.entity.AssignmentEntity;
import org.activiti.engine.extend.service.ExtendService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.woshidaniu.component.bpm.management.BaseBPMController;

/**
 * <p>
 * <h3>niutal框架
 * <h3><br>
 * 说明：TODO <br>
 * class：com.woshidaniu.component.bpm.management.process.definition.controller.AssignmentController.java
 * <p>
 * 
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
@Controller
@RequestMapping("/processDefinition/assignment")
public class AssignmentController extends BaseBPMController {

	@Autowired
	protected RepositoryService repositoryService;
	@Autowired
	protected ExtendService extendService;
	
	protected ObjectMapper objectMapper = new ObjectMapper();
	
	@RequestMapping("/{processDefinitionId}/setup.zf")
	public ModelAndView setup(@PathVariable String processDefinitionId, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		try {
			Map<String, TaskDefinition> taskDefinitions = null;
			ProcessDefinition processDefinition = repositoryService.getProcessDefinition(processDefinitionId);
			if(processDefinition != null){
				taskDefinitions = ((ProcessDefinitionEntity)processDefinition).getTaskDefinitions();
			}
			modelAndView.addObject("processDefinition", processDefinition);
			modelAndView.addObject("taskDefinitions", taskDefinitions);
			modelAndView.setViewName("/processManagement/definition/assignment_setup");
			return modelAndView;
		} catch (Exception e) {
			logException(e);
			modelAndView.setViewName(ERROR_VIEW);
			return modelAndView;
		}
	}
	
	@ResponseBody
	@RequestMapping("/{processDefinitionId}/getAssignment.zf")
	public Object getAssignment(@PathVariable String processDefinitionId, HttpServletRequest request){
		
		try {
			List<Assignment> assignmentForProcess = extendService.getAssignmentForProcess(processDefinitionId);
			ObjectNode json = BPMMessageKey.ASSIGNMENT_QUERY_SUCCESS.getJson();
			ArrayNode assignmentArrayNode = json.putArray("assignment");
			for (Assignment assignment : assignmentForProcess) {
				ObjectNode assignmentObjectMapper = objectMapper.createObjectNode();
				assignmentObjectMapper.put("taskDefinitionId", assignment.getTaskDefinitionId());
				assignmentObjectMapper.put("processDefinitionId", assignment.getProcessDefinitionId());
				assignmentObjectMapper.put("userId", assignment.getUserId());
				assignmentObjectMapper.put("groupId", assignment.getGroupId());
				assignmentObjectMapper.put("type", assignment.getType());
				User userEntity = assignment.getUserEntity();
				Group groupEntity = assignment.getGroupEntity();
				assignmentObjectMapper.put("userName", userEntity == null ? null : userEntity.getFirstName());
				assignmentObjectMapper.put("groupName", groupEntity == null ? null : groupEntity.getName());
				assignmentArrayNode.add(assignmentObjectMapper);
			}
			return json.toString();
		} catch (Exception e) {
			logException(e);
			return BPMMessageKey.ASSIGNMENT_QUERY_FAIL.getJson();
		}
	}
	
	@ResponseBody
	@RequestMapping("/{processDefinitionId}/save.zf")
	public Object save(@PathVariable
						String processDefinitionId, 
						@RequestBody 
						String assignmentData, 
						HttpServletRequest request){
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode assignmentDataJson = objectMapper.readTree(assignmentData);
			Iterator<JsonNode> iterator = assignmentDataJson.iterator();
			
			List<Assignment> taskAssignment = new ArrayList<Assignment>();
			
			while(iterator.hasNext()){
				JsonNode next = iterator.next();
				String taskDefintionId = next.get("taskDefinitionId").asText();
				ArrayNode candidateUsers = (ArrayNode) next.get("taskCandidateUsers");
				ArrayNode candidateGroups = (ArrayNode) next.get("taskCandidateGroups");
				
				if(candidateUsers != null && candidateUsers.size() > 0){
					for (JsonNode jsonNode : candidateUsers) {
						String candidateUser = jsonNode.asText();
						AssignmentEntity assignemntEntity = AssignmentEntity.create(processDefinitionId, taskDefintionId);
						assignemntEntity.setUserId(candidateUser);
						taskAssignment.add(assignemntEntity);
					}
				}
				if(candidateGroups != null && candidateGroups.size() > 0){
					for (JsonNode jsonNode : candidateGroups) {
						String candidateGroup = jsonNode.asText();
						AssignmentEntity assignemntEntity = AssignmentEntity.create(processDefinitionId, taskDefintionId);
						assignemntEntity.setGroupId(candidateGroup);
						taskAssignment.add(assignemntEntity);
					}
				}
			}
			extendService.setupTaskAssignment(processDefinitionId, taskAssignment);
			return BPMMessageKey.ASSIGNMENT_QUERY_SUCCESS.getJson();
		} catch (Exception e) {
			logException(e);
			return BPMMessageKey.ASSIGNMENT_QUERY_FAIL.getJson();
		}
	}

}
