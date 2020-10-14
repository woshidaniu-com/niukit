package com.woshidaniu.component.bpm.management.modeler.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.EventListener;
import org.activiti.bpmn.model.ImplementationType;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.workflow.simple.converter.WorkflowDefinitionConversion;
import org.activiti.workflow.simple.converter.WorkflowDefinitionConversionFactory;
import org.activiti.workflow.simple.definition.CustomHumanStepDefinition;
import org.activiti.workflow.simple.definition.WorkflowDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.woshidaniu.component.bpm.BPMUtils;
import com.woshidaniu.component.bpm.management.modeler.ModelDataJsonConstants;
import com.woshidaniu.component.bpm.management.simpelWorkflow.SimpleHumanStep;
import com.woshidaniu.component.bpm.management.simpelWorkflow.SimpleWorkflow;
import com.woshidaniu.component.bpm.management.simpelWorkflow.SimpleWorkflowJsonConverter;

/**
 * <p>
 * <h3>niutal框架
 * <h3><br>
 * 说明：TODO <br>
 * class：com.woshidaniu.component.bpm.management.modeler.ModelDeployService.java
 * <p>
 * 
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
@Service
public class ModelService implements ModelDataJsonConstants {

	static final String SIMPLE_WORKFLOW_EVENT_LISTENER = "org.activiti.engine.extend.event.listener.impl.DefaultActivitiEventListener";
	
	protected WorkflowDefinitionConversionFactory conversionFactory = new WorkflowDefinitionConversionFactory();

	@Autowired
	protected RepositoryService repositoryService;

	/**
	 * 
	 * <p>
	 * 方法说明：新增model
	 * <p>
	 * <p>
	 * 作者：a href="#">Zhangxiaobin[1036]<a>
	 * <p>
	 * <p>
	 * 时间：2016年12月8日上午9:21:07
	 * <p>
	 */
	public 	ObjectNode addModuler(String modelName, String modelDesc, String modelEditor) throws Exception {
		Model modelData = repositoryService.newModel();
		if ("advanced".equals(modelEditor)) {
			ObjectMapper objectMapper = new ObjectMapper();
			ObjectNode editorNode = objectMapper.createObjectNode();
			editorNode.put("id", "canvas");
			editorNode.put("resourceId", "canvas");
			ObjectNode stencilSetNode = objectMapper.createObjectNode();
			stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
			editorNode.put("stencilset", stencilSetNode);

			ObjectNode modelObjectNode = objectMapper.createObjectNode();
			modelObjectNode.put(MODEL_NAME, modelName);
			modelObjectNode.put(MODEL_REVISION, 1);
			if (BPMUtils.isEmpty(modelDesc)) {
				modelDesc = "";
			}
			modelDesc = modelDesc.replaceAll("\\s+", " ");
			modelObjectNode.put(MODEL_DESCRIPTION, modelDesc);
			modelData.setMetaInfo(modelObjectNode.toString());
			modelData.setName(modelName);
			modelData.setCategory(modelEditor);
			repositoryService.saveModel(modelData);
			repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));
		} else if ("simple".equals(modelEditor)) {
			ObjectMapper objectMapper = new ObjectMapper();
			ObjectNode modelObjectNode = objectMapper.createObjectNode();
			modelObjectNode.put(MODEL_NAME, modelName);
			modelObjectNode.put(MODEL_REVISION, 1);
			if (BPMUtils.isEmpty(modelDesc)) {
				modelDesc = "";
			}
			modelDesc = modelDesc.replaceAll("\\s+", " ");
			modelObjectNode.put(MODEL_DESCRIPTION, modelDesc);
			modelData.setMetaInfo(modelObjectNode.toString());
			modelData.setName(modelName);
			modelData.setCategory(modelEditor);
			modelData.setKey("_" + BPMUtils.getUniqIDHash().toUpperCase());
			repositoryService.saveModel(modelData);

			ObjectNode editorNode = objectMapper.createObjectNode();
			editorNode.put("name", modelName);
			editorNode.put("description", modelDesc);
			repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));
		}

		ObjectNode node = new ObjectMapper().createObjectNode();
		node.put("modelId", modelData.getId());
		node.put("modelName", modelData.getName());
		node.put("modelDescription", modelDesc);
		node.put("editor", modelEditor);
		return node;
	}

	public Deployment deployModuler(String modelId) throws Exception {
		Model modelData = repositoryService.getModel(modelId);
		if ("simple".equals(modelData.getCategory())) {
			return deploySimpleModel(modelData);
		} else {
			return deployAdvancedModel(modelData);
		}

	}

	protected Deployment deploySimpleModel(Model modelData) throws Exception {
		byte[] modelEditorSource = repositoryService.getModelEditorSource(modelData.getId());
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode readTree = (ObjectNode) mapper.readTree(modelEditorSource);
		SimpleWorkflowJsonConverter converter = new SimpleWorkflowJsonConverter(readTree);
		converter.reverseConvert();
		SimpleWorkflow workflow = converter.getSimpleWorkflow();
		List<CustomHumanStepDefinition> customHumanSteps = new ArrayList<CustomHumanStepDefinition>();
		List<SimpleHumanStep> taskDefinitions = workflow.getTaskDefinitions();
		Iterator<SimpleHumanStep> iterator = taskDefinitions.iterator();
		while (iterator.hasNext()) {
			SimpleHumanStep task = iterator.next();
			CustomHumanStepDefinition humanStep = new CustomHumanStepDefinition();
			customHumanSteps.add(humanStep);
			humanStep.setName(task.getTaskName());
			humanStep.setInterruptSupport(task.isInterruptSupport());
			humanStep.setBackSupport(task.isBackSupport());
			humanStep.setDescription(task.getTaskDesc());
			List<String> candidateGroups = task.getCandidateGroups();
			if (candidateGroups != null && candidateGroups.size() > 0) {
				List<String> groups = new ArrayList<String>();
				for (String group : candidateGroups) {
					groups.add(group.split("\\|")[0]);
				}
				humanStep.setCandidateGroups(groups);
			}

			List<String> candidateUsers = task.getCandidateUsers();
			if (candidateUsers != null && candidateUsers.size() > 0) {
				List<String> users = new ArrayList<String>();
				for (String user : candidateUsers) {
					users.add(user.split("\\|")[0]);
				}
				humanStep.setCandidateUsers(users);
			}
		}
		if (BPMUtils.isBlank(modelData.getKey())) {
			modelData.setKey(BPMUtils.getUniqIDHash());
		}
		WorkflowDefinition definition = new WorkflowDefinition();
		definition.name(workflow.getName()).id(modelData.getKey()).description(workflow.getDescription())
				.category(workflow.getCategory());

		for (CustomHumanStepDefinition customHumanStepDefinition : customHumanSteps) {
			definition.addCustomHumanStep(customHumanStepDefinition);
		}

		WorkflowDefinitionConversion conversion = conversionFactory.createWorkflowDefinitionConversion(definition);
		//TODO 加入事件监听器
		EventListener listener = new EventListener();
		listener.setImplementation(SIMPLE_WORKFLOW_EVENT_LISTENER);
		listener.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_CLASS);
		conversion.addEventListener(listener);
		conversion.convert();

		BpmnModel bpmnModel = conversion.getBpmnModel();
		String processName = modelData.getName() + ".bpmn20.xml";
		Deployment deployment = repositoryService.createDeployment().name(modelData.getName())
				.addBpmnModel(processName, bpmnModel).category("simple").deploy();
		modelData.setDeploymentId(deployment.getId());
		repositoryService.saveModel(modelData);

		return deployment;
	}

	protected Deployment deployAdvancedModel(Model modelData) throws Exception {
		com.fasterxml.jackson.databind.node.ObjectNode modelNode = (com.fasterxml.jackson.databind.node.ObjectNode) new com.fasterxml.jackson.databind.ObjectMapper()
				.readTree(repositoryService.getModelEditorSource(modelData.getId()));
		BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
		byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(model);

		String processName = modelData.getName() + ".bpmn20.xml";
		Deployment deployment = repositoryService.createDeployment().name(modelData.getName())
				.addString(processName, new String(bpmnBytes, "utf-8")).category("advanced").deploy();
		modelData.setDeploymentId(deployment.getId());
		repositoryService.saveModel(modelData);
		return deployment;
	}

}
