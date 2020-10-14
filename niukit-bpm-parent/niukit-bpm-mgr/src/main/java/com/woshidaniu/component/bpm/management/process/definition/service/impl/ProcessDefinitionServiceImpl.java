package com.woshidaniu.component.bpm.management.process.definition.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.activiti.bpmn.BpmnAutoLayout;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.EventListener;
import org.activiti.bpmn.model.ExclusiveGateway;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.FormProperty;
import org.activiti.bpmn.model.FormValue;
import org.activiti.bpmn.model.ImplementationType;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.RepositoryService;
import org.activiti.workflow.simple.definition.WorkflowDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.woshidaniu.component.bpm.BPMConstants;
import com.woshidaniu.component.bpm.BPMUtils;
import com.woshidaniu.component.bpm.common.BaseDao;
import com.woshidaniu.component.bpm.common.BaseServiceImpl;
import com.woshidaniu.component.bpm.management.process.definition.BPMManagementDefinitionException;
import com.woshidaniu.component.bpm.management.process.definition.dao.daointerface.IProcessDefinitionDao;
import com.woshidaniu.component.bpm.management.process.definition.dao.entities.ProcessDefinitionModel;
import com.woshidaniu.component.bpm.management.process.definition.dao.entities.TaskDefinitionModel;
import com.woshidaniu.component.bpm.management.process.definition.service.svcinterface.IProcessDefinitionService;

/**
 * <p>
 * <h3>niutal框架
 * <h3><br>
 * 说明： <br>
 * class：com.woshidaniu.component.bpm.processDefMgr.service.impl.ProcessDefMgrServiceImpl.java
 * <p>
 * 
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
@Service("processDefinationService")
public class ProcessDefinitionServiceImpl extends
		BaseServiceImpl<ProcessDefinitionModel, BaseDao<ProcessDefinitionModel>> implements IProcessDefinitionService {

	public static final String DEFAULT_END_EVENT_ID = "endEvent";
	public static final String DEFAULT_START_EVENT_ID = "startEvent";
	public static final String DEFAULT_USER_TASK_ID = "userTask";
	public static final String DEFAULT_EXCLUSIVE_GATEWAY_ID = "exclusiveGateway";
	public static final String DEFAULT_END_EVENT_NAME = "结束";
	public static final String DEFAULT_START_EVENT_NAME = "开始";
	public static final String DEFAULT_PROCESS_DEFINITION_CATEGORY = "default";
	public static final String DEFAULE_PROCESS_EVENT_LISTENER_CLASSNAME = "org.activiti.engine.extend.event.listener.DefaultActivitiEventListener";

	protected String processEventListenerClassName = DEFAULE_PROCESS_EVENT_LISTENER_CLASSNAME;

	@Autowired
	protected IProcessDefinitionDao dao;
	@Autowired
	protected RepositoryService repositoryService;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		super.dao = this.dao;
	}

	@Override
	public List<String> findProcessDefinitionCategoryList() {

		return dao.getCategoryList();
	}

	@Override
	public boolean createAndDeployProcessDefinition(ProcessDefinitionModel processDefinationModel) {
		checkProcessDefinitionModel(processDefinationModel);

		BpmnModel model = new BpmnModel();
		model.setTargetNamespace(processDefinationModel.getCategory());
		Process process = new Process();
		model.addProcess(process);
		List<EventListener> eventListeners = new ArrayList<EventListener>();
		EventListener listener = new EventListener();
		eventListeners.add(listener);
		listener.setImplementation(getProcessEventListenerClassName());
		listener.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_CLASS);
		process.setEventListeners(eventListeners);

		process.setId(processDefinationModel.getId());
		process.setDocumentation(processDefinationModel.getDescription());
		process.setName(processDefinationModel.getName());
		
		List<FlowElement> flowElements = new ArrayList<FlowElement>();
		
		FlowElement startEvent = createStartEvent(DEFAULT_START_EVENT_ID, DEFAULT_START_EVENT_NAME, null);
		FlowElement endEvent = createEndEvent(DEFAULT_END_EVENT_ID, DEFAULT_END_EVENT_NAME, null);
		flowElements.add(startEvent);
		flowElements.add(endEvent);
		
		List<TaskDefinitionModel> taskDefinations = processDefinationModel.getTaskDefinations();
		//List<SequenceFlow> incomingSeqFlows, outgoingSeqFlows;
		int seq = 0, taskSize = taskDefinations.size();
		//String preNode = DEFAULT_START_EVENT_ID;
		TaskDefinitionModel task = null;
		SequenceFlow previousSeqFlow = null;
		
		for (TaskDefinitionModel taskDefinationModel : taskDefinations) {
			String taskId = DEFAULT_USER_TASK_ID + "_" + seq;
			String exclusiveGatewayId = DEFAULT_EXCLUSIVE_GATEWAY_ID + seq++;
			taskDefinationModel.setPrevious(task);
			UserTask userTaskEl = createUserTask(taskId, taskDefinationModel.getTaskName(),
					taskDefinationModel.getTaskDesc(), taskDefinationModel.getCandidateUsers(),
					taskDefinationModel.getCandidateGroups());
			taskDefinationModel.setBpmnUserTaskModel(userTaskEl);
			flowElements.add(userTaskEl);
			//首任务
			if(taskDefinationModel.getPrevious() == null){
				ExclusiveGateway exclusiveGateway = createExclusiveGateway(exclusiveGatewayId);
				userTaskEl.setCategory("_initial_task");
				List<FormProperty> initialTaskFormPropties = createInitialTaskFormPropties();
				userTaskEl.setFormProperties(initialTaskFormPropties);
				//create exclusive gateway outgoing seq flows
				SequenceFlow outgoingRejectSequenceFlow = createSequenceFlow(exclusiveGateway.getId(), endEvent.getId());
				outgoingRejectSequenceFlow.setConditionExpression("{p_decision_option == 'reject'}");
				SequenceFlow outgoingApprovaltSequenceFlow = createSequenceFlow(exclusiveGateway.getId(), null);
				outgoingApprovaltSequenceFlow.setConditionExpression("{p_decision_option == 'approval'}");
				flowElements.add(createSequenceFlow(startEvent.getId(), taskId));
				flowElements.add(createSequenceFlow(taskId, exclusiveGateway.getId()));
				flowElements.add(outgoingApprovaltSequenceFlow);
				flowElements.add(outgoingRejectSequenceFlow);
				flowElements.add(exclusiveGateway);
				
				previousSeqFlow = outgoingApprovaltSequenceFlow;
			}else{
				previousSeqFlow.setTargetRef(taskId);
				ExclusiveGateway exclusiveGateway = createExclusiveGateway(exclusiveGatewayId);
				List<FormProperty> taskFormPropties = createTaskFormPropties();
				userTaskEl.setFormProperties(taskFormPropties);
				//create exclusive gateway outgoing seq flows
				SequenceFlow outgoingRejectSequenceFlow = createSequenceFlow(exclusiveGateway.getId(), endEvent.getId());
				outgoingRejectSequenceFlow.setConditionExpression("{p_decision_option == 'reject'}");
				SequenceFlow outgoingApprovaltSequenceFlow = createSequenceFlow(exclusiveGateway.getId(), null);
				outgoingApprovaltSequenceFlow.setConditionExpression("{p_decision_option == 'approval'}");
				SequenceFlow outgoingBackPreSequenceFlow = createSequenceFlow(exclusiveGateway.getId(), getPreUserTaskId(taskDefinationModel));
				outgoingBackPreSequenceFlow.setConditionExpression("{p_decision_option == 'back_pre'}");
				
				//exclusiveGateway.setOutgoingFlows(makeSequenceFlowList(outgoingRejectSequenceFlow, outgoingApprovaltSequenceFlow, outgoingBackPreSequenceFlow));
				flowElements.add(createSequenceFlow(taskId, exclusiveGateway.getId()));
				flowElements.add(outgoingRejectSequenceFlow);
				flowElements.add(outgoingApprovaltSequenceFlow);
				flowElements.add(outgoingBackPreSequenceFlow);
				flowElements.add(exclusiveGateway);
				
				previousSeqFlow = outgoingApprovaltSequenceFlow;
			}
			previousSeqFlow.setTargetRef(endEvent.getId());
			
			task = taskDefinationModel;
		}
		
		for (FlowElement el : flowElements) {
			process.addFlowElement(el);
		}
		
		log.info(getBpmn20Xml(model));
		
		BpmnAutoLayout bpmnAutoLayout = new BpmnAutoLayout(model);
		bpmnAutoLayout.execute();
		repositoryService.createDeployment()
				.addBpmnModel(process.getId() +".bpmn", model).name("["+process.getName() + "]流程部署")
				.category("simple-editor").deploy();

		return true;
	}
	
	/**
	   * Returns the BPMN 2.0 xml which is the converted version of the 
	   * provided {@link WorkflowDefinition}. 
	   */
	protected String getBpmn20Xml(BpmnModel bpmnModel) {
	    BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
	    return new String(bpmnXMLConverter.convertToXML(bpmnModel));
	  }
	
	protected String getPreUserTaskId(TaskDefinitionModel taskDefinationModel) {
		return taskDefinationModel.getPrevious().getBpmnUserTaskModel().getId();
	}
	
	protected String getInitUserTaskId(TaskDefinitionModel taskDefinationModel) {
		TaskDefinitionModel previous = taskDefinationModel.getPrevious();
		if(previous == null){
			return taskDefinationModel.getBpmnUserTaskModel().getId();
		}
		return getInitUserTaskId(previous);
	}

	protected List<SequenceFlow> makeSequenceFlowList(SequenceFlow ...flows ){
		List<SequenceFlow> flowList = new ArrayList<SequenceFlow>();
		if(flows == null || flows.length == 0){
			return flowList;
		}
		for (SequenceFlow sequenceFlow : flows) {
			flowList.add(sequenceFlow);
		}
		return flowList;
	}

	protected List<FormProperty> createInitialTaskFormPropties(){
		List<FormProperty> properties = new ArrayList<FormProperty>();
		FormProperty p1 = new FormProperty();
		p1.setId("p_decision_option");
		p1.setName("处理");
		p1.setRequired(true);
		p1.setReadable(true);
		p1.setWriteable(true);
		p1.setType("enum");
		
		List<FormValue> p1Values = new ArrayList<FormValue>();
		FormValue approval = new FormValue();
		approval.setId("approval");
		approval.setName("同意");
		p1Values.add(approval);
		FormValue reject = new FormValue();
		reject.setId("reject");
		reject.setName("不同意");
		p1Values.add(reject);
		p1.setFormValues(p1Values);
		
		FormProperty p2 = new FormProperty();
		p2.setId("p_decision_message");
		p2.setName("意见");
		p2.setRequired(false);
		p2.setReadable(true);
		p2.setWriteable(true);
		p2.setType("textarea");
		
		properties.add(p1);
		properties.add(p2);
		return properties;
	}
	
	protected List<FormProperty> createTaskFormPropties(){
		List<FormProperty> properties = new ArrayList<FormProperty>();
		FormProperty p1 = new FormProperty();
		p1.setId("p_decision_options");
		p1.setName("处理");
		p1.setRequired(true);
		p1.setReadable(true);
		p1.setWriteable(true);
		p1.setType("enum");
		
		List<FormValue> p1Values = new ArrayList<FormValue>();
		FormValue approval = new FormValue();
		approval.setId("approval");
		approval.setName("同意");
		p1Values.add(approval);
		FormValue reject = new FormValue();
		reject.setId("reject");
		reject.setName("不同意");
		p1Values.add(reject);
		FormValue backPre = new FormValue();
		backPre.setId("back_pre");
		backPre.setName("退回[上一级]");
		p1Values.add(backPre);
//		FormValue backInitial = new FormValue();
//		backInitial.setId("back_init");
//		backInitial.setName("退回[第一级]");
//		p1Values.add(backInitial);
		p1.setFormValues(p1Values);
		
		FormProperty p2 = new FormProperty();
		p2.setId("p_decision_message");
		p2.setName("意见");
		p2.setRequired(false);
		p2.setReadable(true);
		p2.setWriteable(true);
		p2.setType("textarea");
		
		properties.add(p1);
		properties.add(p2);
		return properties;
	}
	
	
	
	protected ExclusiveGateway createExclusiveGateway(String id){
		ExclusiveGateway gateway = new ExclusiveGateway();
		gateway.setId(id);
		return gateway;
	}
	
	protected SequenceFlow createSequenceFlow(String from, String to) {
		SequenceFlow flow = new SequenceFlow();
		flow.setSourceRef(from);
		flow.setTargetRef(to);
		return flow;
	}

	protected FlowElement createEndEvent(String id, String name, String desc) {
		EndEvent endEvent = new EndEvent();
		endEvent.setId(id);
		endEvent.setName(name);
		endEvent.setDocumentation(desc);
		return endEvent;
	}

	protected UserTask createUserTask(String id, String name, String desc, List<String> candidateUsers,
			List<String> candidateGroups) {
		UserTask userTask = new UserTask();
		userTask.setName(name);
		userTask.setId(id);
		userTask.setDocumentation(desc);
		if (candidateUsers != null && candidateUsers.size() > 0) {
			userTask.setCandidateUsers(candidateUsers);
		}
		if (candidateGroups != null && candidateGroups.size() > 0) {
			userTask.setCandidateGroups(candidateGroups);
		}
		return userTask;
	}

	protected FlowElement createStartEvent(String id, String name, String desc) {
		StartEvent startEvent = new StartEvent();
		startEvent.setDocumentation(desc);
		startEvent.setId(id);
		startEvent.setName(name);
		startEvent.setInitiator(BPMConstants._INNER_PROCESS_VARIABLE_REQUESTER);
		return startEvent;
	}

	protected void checkProcessDefinitionModel(ProcessDefinitionModel processDefinationModel) {
		if (processDefinationModel == null) {
			throw new BPMManagementDefinitionException("参数[processDefinitionModel]为空");
		}
		if (BPMUtils.isBlank(processDefinationModel.getId())) {
			throw new BPMManagementDefinitionException("参数[processDefinitionModel.id]为空");
		}
		if (BPMUtils.isBlank(processDefinationModel.getName())) {
			throw new BPMManagementDefinitionException("参数[processDefinitionModel.name]为空");
		}
		if (processDefinationModel.getTaskDefinations() == null
				|| processDefinationModel.getTaskDefinations().size() == 0) {
			throw new BPMManagementDefinitionException("参数[processDefinitionModel.taskDefinations]为空");
		}
	}

	public String getProcessEventListenerClassName() {
		return processEventListenerClassName;
	}

	public void setProcessEventListenerClassName(String processEventListenerClassName) {
		this.processEventListenerClassName = processEventListenerClassName;
	}

}
