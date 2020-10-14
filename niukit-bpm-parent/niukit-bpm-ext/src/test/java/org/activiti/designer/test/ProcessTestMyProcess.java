package org.activiti.designer.test;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.FormService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

public class ProcessTestMyProcess {

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@Test
	public void startProcess() throws Exception {
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		repositoryService.createDeployment().addClasspathResource("FormDataProcess.bpmn").deploy();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("name", "Activiti");
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("formDataProcess", variableMap);
		assertNotNull(processInstance.getId());
		System.out.println("id " + processInstance.getId() + " "
				+ processInstance.getProcessDefinitionId());
	}
	
	@Test
	public void startProcess3() throws Exception {
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		repositoryService.createDeployment().addClasspathResource("FormDataProcess.bpmn").deploy();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Map<String, Object> variableMap = new HashMap<String, Object>();
		//variableMap.put("name", "Activiti");
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("dm1", variableMap);
		assertNotNull(processInstance.getId());
		System.out.println("id " + processInstance.getId() + " "
				+ processInstance.getProcessDefinitionId());
	}
	
	@Test
	public void getTaskFormData(){
		FormService formService = activitiRule.getFormService();
		Object renderedTaskForm = formService.getRenderedTaskForm("610005", "alpacaJSONFormEngine");
		System.out.println(renderedTaskForm);
	}
	
	@Test
	public void startProcessWithFormData() throws Exception {
//		RepositoryService repositoryService = activitiRule.getRepositoryService();
//		repositoryService.createDeployment().addClasspathResource("FormDataProcess.bpmn").deploy();
//		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Map<String, String> variableMap = new HashMap<String, String>();
//		variableMap.put("name", "Activiti");
//		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("formDataProcess", variableMap);
//		assertNotNull(processInstance.getId());
//		System.out.println("id " + processInstance.getId() + " "
//				+ processInstance.getProcessDefinitionId());
		FormService formService = activitiRule.getFormService();
		ProcessInstance processInstance = formService.submitStartFormData("formDataProcess:1:582504", variableMap);
		System.out.println("id " + processInstance.getId() + " "
				+ processInstance.getProcessDefinitionId());
	}
	@Test
	public void startProcess2() throws Exception {
//		RepositoryService repositoryService = activitiRule.getRepositoryService();
//		repositoryService.createDeployment().addClasspathResource("FormDataProcess.bpmn").deploy();
//		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Map<String, String> variableMap = new HashMap<String, String>();
//		variableMap.put("name", "Activiti");
//		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("formDataProcess", variableMap);
//		assertNotNull(processInstance.getId());
//		System.out.println("id " + processInstance.getId() + " "
//				+ processInstance.getProcessDefinitionId());
		ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("dm1");
		System.out.println("id " + processInstance.getId() + " "
				+ processInstance.getProcessDefinitionId());
	}
	@Test
	public void queryProcessFormData()throws Exception {
		FormService formService = activitiRule.getFormService();
		StartFormData startFormData = formService.getStartFormData("formDataProcess:1:582504");
		System.out.println(startFormData.getFormProperties());
		Object renderedStartForm = formService.getRenderedStartForm("formDataProcess:1:582504");
		System.out.println(renderedStartForm);
	}
	
	@Test
	public void getProcessDefinationXML(){
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		BpmnModel bpmnModel = repositoryService.getBpmnModel("dm1:1:607504");
		BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
		System.out.println(new String(bpmnXMLConverter.convertToXML(bpmnModel)));
		
	}
	
	@Test
	public void queryTaskForm(){
		FormService formService = activitiRule.getFormService();
		
		Object renderedTaskForm = formService.getRenderedTaskForm("610005");
		System.out.println(renderedTaskForm);
	}
	
	@Test
	public void completeTast(){
		TaskService taskService = activitiRule.getTaskService();
		taskService.complete("587509");
	}
	
}