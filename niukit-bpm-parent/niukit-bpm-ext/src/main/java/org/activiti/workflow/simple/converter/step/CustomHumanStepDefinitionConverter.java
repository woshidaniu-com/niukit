package org.activiti.workflow.simple.converter.step;

import java.util.Iterator;
import java.util.List;

import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.UserTask;
import org.activiti.workflow.simple.converter.ConversionConstants;
import org.activiti.workflow.simple.converter.WorkflowDefinitionConversion;
import org.activiti.workflow.simple.definition.CustomHumanStepDefinition;
import org.activiti.workflow.simple.definition.HumanStepAssignment.HumanStepAssignmentType;
import org.activiti.workflow.simple.definition.StepDefinition;
import org.activiti.workflow.simple.definition.form.FormDefinition;
import org.activiti.workflow.simple.definition.form.FormPropertyDefinition;
import org.activiti.workflow.simple.definition.form.ListPropertyDefinition;
import org.activiti.workflow.simple.definition.form.TaskDecisionFormProperyHelper;
import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 * <h3>niutal框架
 * <h3><br>
 * 说明：TODO <br>
 * class：org.activiti.workflow.simple.converter.step.CustomStepDefinitionConverter.java
 * <p>
 * 
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
public class CustomHumanStepDefinitionConverter
		extends BaseStepDefinitionConverter<CustomHumanStepDefinition, UserTask> {

	private static final long serialVersionUID = 1L;

	private static final String DEFAULT_INITIATOR_VARIABLE = "initiator";
	private static final String DEFAULT_INITIATOR_ASSIGNEE_EXPRESSION = "${initiator}";

	public Class<? extends StepDefinition> getHandledClass() {
		return CustomHumanStepDefinition.class;
	}

	protected UserTask createProcessArtifact(CustomHumanStepDefinition stepDefinition,
			WorkflowDefinitionConversion conversion) {
		// 支持退回，设置表单数据
		if (stepDefinition.isBackSupport()) {
			List<UserTask> addedUserTasks = conversion.getAddedUserTasks();
			FormDefinition form = stepDefinition.getForm();
			if (addedUserTasks != null && addedUserTasks.size() > 0) {
				List<FormPropertyDefinition> formPropertyDefinitions = form.getFormPropertyDefinitions();
				for (FormPropertyDefinition formPropertyDefinition : formPropertyDefinitions) {
					if (formPropertyDefinition.getName()
							.equals(TaskDecisionFormProperyHelper.DEFAULT_DECISION_PROPERTY_NAME)) {
						ListPropertyDefinition listPropertyDefinition = (ListPropertyDefinition) formPropertyDefinition;
						for (UserTask uertask : addedUserTasks) {
							listPropertyDefinition.addEntry(TaskDecisionFormProperyHelper.createBackListPropertyEntry(
									"BACK_" + uertask.getId(),
									uertask.getName()));

							stepDefinition.getAdditionalBackSequenceFlow()
									.push(createSequenceFlow(conversion, null, uertask.getId(),
											"${" + TaskDecisionFormProperyHelper.DEFAULT_DECISION_PROPERTY_NAME + "=='"
													+ "BACK_" + uertask.getId() + "'}"));
						}
						break;
					}
				}
			}
		}
		// 支持终止，设置表单数据
		if (stepDefinition.isInterruptSupport()) {
			FormDefinition form = stepDefinition.getForm();
			List<FormPropertyDefinition> formPropertyDefinitions = form.getFormPropertyDefinitions();
			for (FormPropertyDefinition formPropertyDefinition : formPropertyDefinitions) {
				if (formPropertyDefinition.getName()
						.equals(TaskDecisionFormProperyHelper.DEFAULT_DECISION_PROPERTY_NAME)) {
					((ListPropertyDefinition) formPropertyDefinition)
							.addEntry(TaskDecisionFormProperyHelper.createNoPassListPropertyEntry());

					stepDefinition.getAdditionalNOPASSSequenceFlow().push(createSequenceFlow(conversion, null, null,
							"${" + TaskDecisionFormProperyHelper.DEFAULT_DECISION_PROPERTY_NAME + "=='NOPASS'}"));
					break;
				}
			}
		}

		UserTask userTask = createUserTask(stepDefinition, conversion);
		addFlowElement(conversion, userTask, true);

		if (stepDefinition.getAdditionalBackSequenceFlow().size() > 0) {
			while (!stepDefinition.getAdditionalBackSequenceFlow().isEmpty()) {
				SequenceFlow pop = stepDefinition.getAdditionalBackSequenceFlow().pop();
				pop.setSourceRef(userTask.getId());
				conversion.getProcess().addFlowElement(pop);
			}
		}

		if (stepDefinition.getAdditionalNOPASSSequenceFlow().size() > 0) {
			Iterator<SequenceFlow> iterator = stepDefinition.getAdditionalNOPASSSequenceFlow().iterator();
			while (iterator.hasNext()) {
				SequenceFlow pop = iterator.next();
				pop.setSourceRef(userTask.getId());
			}
		}

		return userTask;
	}

	@Override
	protected void addFlowElement(WorkflowDefinitionConversion conversion, FlowElement flowElement,
			boolean addSequenceFlowToLastActivity) {
		if (conversion.isSequenceflowGenerationEnabled() && addSequenceFlowToLastActivity) {

			if (conversion.getLastActivityId().startsWith(ConversionConstants.USER_TASK_ID_PREFIX)) {
				SequenceFlow sequenceFlow = addSequenceFlow(conversion, conversion.getLastActivityId(), flowElement.getId(),
						"${" + TaskDecisionFormProperyHelper.DEFAULT_DECISION_PROPERTY_NAME + "=='PASS'}");
				UserTask lastActivity = (UserTask) conversion.getProcess()
						.getFlowElement(conversion.getLastActivityId());
				lastActivity.setDefaultFlow(sequenceFlow.getId());
			} else {
				addSequenceFlow(conversion, conversion.getLastActivityId(), flowElement.getId());
			}
		}
		conversion.getProcess().addFlowElement(flowElement);

		if (conversion.isUpdateLastActivityEnabled()) {
			conversion.setLastActivityId(flowElement.getId());
		}

		if (conversion.isAddUserTaskEnabled() && (flowElement instanceof UserTask)) {
			conversion.addUserTask((UserTask) flowElement);
		}

	}

	/**
	 * Add a sequence-flow to the current process from source to target.
	 * Sequence-flow name is set to a user-friendly name, containing an
	 * incrementing number.
	 *
	 * @param conversion
	 * @param sourceActivityId
	 * @param targetActivityId
	 * @param condition
	 */
	@Override
	protected SequenceFlow addSequenceFlow(WorkflowDefinitionConversion conversion, String sourceActivityId,
			String targetActivityId, String condition) {

		SequenceFlow sequenceFlow = new SequenceFlow();
		sequenceFlow.setId(conversion.getUniqueNumberedId(getSequenceFlowPrefix()));
		sequenceFlow.setSourceRef(sourceActivityId);
		sequenceFlow.setTargetRef(targetActivityId);

		if (StringUtils.isNotEmpty(condition)) {
			sequenceFlow.setConditionExpression(condition);
		}

		conversion.getProcess().addFlowElement(sequenceFlow);
		return sequenceFlow;
	}

	protected SequenceFlow createSequenceFlow(WorkflowDefinitionConversion conversion, String sourceActivityId,
			String targetActivityId, String condition) {

		SequenceFlow sequenceFlow = new SequenceFlow();
		sequenceFlow.setId(conversion.getUniqueNumberedId(getSequenceFlowPrefix()));
		sequenceFlow.setSourceRef(sourceActivityId);
		sequenceFlow.setTargetRef(targetActivityId);

		if (StringUtils.isNotEmpty(condition)) {
			sequenceFlow.setConditionExpression(condition);
		}

		return sequenceFlow;
	}

	protected UserTask createUserTask(CustomHumanStepDefinition humanStepDefinition,
			WorkflowDefinitionConversion conversion) {

		// TODO: validate and throw exception on missing properties

		UserTask userTask = new UserTask();
		userTask.setId(conversion.getUniqueNumberedId(ConversionConstants.USER_TASK_ID_PREFIX));
		userTask.setName(humanStepDefinition.getName());
		userTask.setDocumentation(humanStepDefinition.getDescription());
		userTask.setCategory(conversion.getLastActivityId());
		// null
		if(humanStepDefinition.getAssignmentType() == HumanStepAssignmentType.NULL){
			//do nothing
		}
		// Initiator
		else if (humanStepDefinition.getAssignmentType() == HumanStepAssignmentType.INITIATOR) {
			userTask.setAssignee(getInitiatorExpression());

			// Add the initiator variable declaration to the start event
			for (StartEvent startEvent : conversion.getProcess().findFlowElementsOfType(StartEvent.class)) {
				startEvent.setInitiator(getInitiatorVariable());
			}

			// Assignee
		} else if (humanStepDefinition.getAssignmentType() == HumanStepAssignmentType.USER) {
			userTask.setAssignee(humanStepDefinition.getAssignee());
		}

		// Candidate Users
		if (humanStepDefinition.getAssignmentType() == HumanStepAssignmentType.USERS
				|| humanStepDefinition.getAssignmentType() == HumanStepAssignmentType.MIXED) {
			userTask.setCandidateUsers(humanStepDefinition.getCandidateUsers());
		}

		// Candidate groups
		if (humanStepDefinition.getAssignmentType() == HumanStepAssignmentType.GROUPS
				|| humanStepDefinition.getAssignmentType() == HumanStepAssignmentType.MIXED) {
			userTask.setCandidateGroups(humanStepDefinition.getCandidateGroups());
		}

		// Form
		if (humanStepDefinition.getForm() != null) {

			FormDefinition formDefinition = humanStepDefinition.getForm();

			// Form properties
			userTask.setFormProperties(convertProperties(formDefinition));

			if (formDefinition.getFormKey() != null) {
				userTask.setFormKey(formDefinition.getFormKey());
			}
		}

		return userTask;
	}

	// Extracted in a method such that subclasses can override if needed
	protected String getInitiatorVariable() {
		return DEFAULT_INITIATOR_VARIABLE;
	}

	// Extracted in a method such that subclasses can override if needed
	protected String getInitiatorExpression() {
		return DEFAULT_INITIATOR_ASSIGNEE_EXPRESSION;
	}

}
