package com.woshidaniu.component.bpm.initial;

import java.util.ArrayList;
import java.util.List;

import org.activiti.bpmn.BpmnAutoLayout;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.EventListener;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.ImplementationType;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.test.ActivitiRule;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;

public class BpmnModelTester {

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();
	
	@After
	public void clean(){
		
	}
	
	@Test
	public void test() {
		BpmnModel model = new BpmnModel();
		Process process = new Process();
		model.addProcess(process);
		
		List<EventListener> eventListeners = new ArrayList<EventListener>();
		EventListener listener = new EventListener();
		eventListeners.add(listener);
		listener.setImplementation("org.activiti.engine.extend.event.listener.DefaultActivitiEventListener");
		listener.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_CLASS);
		process.setEventListeners(eventListeners );
		
		process.setId("test-process");
		process.setDocumentation("test-process doc");
		process.setName("test-process name");
		
		process.addFlowElement(createStartEvent());
		process.addFlowElement(createUserTask());
		process.addFlowElement(createEndEvent());
		
		process.addFlowElement(createSequenceFlow("start-event", "user-task"));
		process.addFlowElement(createSequenceFlow("user-task", "end-event"));
		
		BpmnAutoLayout bpmnAutoLayout = new BpmnAutoLayout(model);
		bpmnAutoLayout.execute();
		
		Deployment deployment = activitiRule.getRepositoryService().createDeployment()
				.addBpmnModel("dynamic-model.bpmn", model).name("Dynamic process deployment").deploy();
		
	}


	private FlowElement createSequenceFlow(String from, String to) {
		SequenceFlow flow = new SequenceFlow();
		flow.setSourceRef(from);
		flow.setTargetRef(to);
		return flow;
	}


	private FlowElement createEndEvent() {
		EndEvent endEvent = new EndEvent();
		endEvent.setId("end-event");
		endEvent.setName("end-event name");
		endEvent.setDocumentation("end-event doc");
		return endEvent;
	}


	private FlowElement createUserTask() {
		UserTask userTask = new UserTask();
	    userTask.setName("user-task name");
	    userTask.setId("user-task");
	    userTask.setAssignee("zhangxb");
	    userTask.setDocumentation("user-task doc");
	    return userTask;
	}


	private FlowElement createStartEvent() {
		StartEvent startEvent = new StartEvent();
		startEvent.setDocumentation("start-event doc");
		startEvent.setId("start-event");
		startEvent.setName("start-event name");
		startEvent.setInitiator("_requestor");
		return startEvent;
	}

}
