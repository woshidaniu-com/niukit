package org.activiti.workflow.simple.definition;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;

import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.workflow.simple.definition.HumanStepAssignment.HumanStepAssignmentType;
import org.activiti.workflow.simple.definition.form.FormDefinition;
import org.activiti.workflow.simple.definition.form.TaskDecisionFormProperyHelper;
import org.activiti.workflow.simple.exception.SimpleWorkflowException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("custom-human-step")
public class CustomHumanStepDefinition extends AbstractNamedStepDefinition
		implements FormStepDefinition, BackSupport, InterruptSupport {

	private static final long serialVersionUID = 1L;
	
	protected FormDefinition form;
	protected HumanStepAssignment assignment;

	protected boolean backSupport = true;
	protected boolean interruptSupport = true;
	protected Deque<SequenceFlow> additionalBackSequenceFlow = new ArrayDeque<SequenceFlow>();
	protected Deque<SequenceFlow> additionalNOPASSSequenceFlow = new ArrayDeque<SequenceFlow>();
	
	
	public CustomHumanStepDefinition() {
		super();
		initDefaultFormDefinition();
	}

	public CustomHumanStepDefinition setBackSupport(boolean backSupport){
		this.backSupport = backSupport;
		return this;
	}
	
	public CustomHumanStepDefinition setInterruptSupport(boolean interruptSupport){
		this.interruptSupport = interruptSupport;
		return this;
	}
	
	/**
	 * 
	 * <p>方法说明：初始化默认的任务表单<p>
	 * <p>作者：a href="#">Zhangxiaobin[1036]<a><p>
	 * <p>时间：2016年12月7日上午11:33:37<p>
	 */
	public CustomHumanStepDefinition initDefaultFormDefinition(){
		if(form == null){
			form = new FormDefinition();
		}
		form.addFormProperty(TaskDecisionFormProperyHelper.createDefaultDecisionPropertyDefinition());
		form.addFormProperty(TaskDecisionFormProperyHelper.createDefaultDecisionMessagePropertyDefinition());
		return this;
	}
	
	@JsonIgnore
	public HumanStepAssignmentType getAssignmentType() {
		return ensureAssignment().getType();
	}

	public String getAssignee() {
		return ensureAssignment().getAssignee();
	}

	public CustomHumanStepDefinition setAssignee(String assignee) {
		ensureAssignment().setAssignee(assignee);
		return this;
	}

	public List<String> getCandidateUsers() {
		return ensureAssignment().getCandidateUsers();
	}

	public CustomHumanStepDefinition setCandidateUsers(List<String> candidateUsers) {
		ensureAssignment().setCandidateUsers(candidateUsers);
		return this;
	}

	public List<String> getCandidateGroups() {
		return ensureAssignment().getCandidateGroups();
	}

	public CustomHumanStepDefinition setCandidateGroups(List<String> candidateGroups) {
		ensureAssignment().setCandidateGroups(candidateGroups);
		return this;
	}

	public FormDefinition getForm() {
		return form;
	}

	public CustomHumanStepDefinition addForm(FormDefinition form) {
		this.form = form;
		return this;
	}

	public void setForm(FormDefinition form) {
		this.form = form;
	}

	public HumanStepAssignment getAssignment() {
		return ensureAssignment();
	}

	public void setAssignment(HumanStepAssignment assignment) {
		this.assignment = assignment;
	}

	protected HumanStepAssignment ensureAssignment() {
		if (assignment == null) {
			assignment = new HumanStepAssignment();
		}
		return assignment;
	}

	@Override
	public StepDefinition clone() {
		CustomHumanStepDefinition clone = new CustomHumanStepDefinition();
		clone.setValues(this);
		return clone;
	}

	@Override
	public void setValues(StepDefinition otherDefinition) {
		if (!(otherDefinition instanceof CustomHumanStepDefinition)) {
			throw new SimpleWorkflowException(
					"An instance of BackSupportedHumanStepDefinition is required to set values");
		}

		CustomHumanStepDefinition stepDefinition = (CustomHumanStepDefinition) otherDefinition;
		setAssignee(stepDefinition.getAssignee());
		if (stepDefinition.getCandidateGroups() != null && !stepDefinition.getCandidateGroups().isEmpty()) {
			setCandidateGroups(new ArrayList<String>(stepDefinition.getCandidateGroups()));
		}
		if (stepDefinition.getCandidateUsers() != null && !stepDefinition.getCandidateUsers().isEmpty()) {
			setCandidateUsers(new ArrayList<String>(stepDefinition.getCandidateUsers()));
		}
		setDescription(stepDefinition.getDescription());
		if (stepDefinition.getForm() != null) {
			setForm(stepDefinition.getForm().clone());
		} else {
			setForm(null);
		}
		setId(stepDefinition.getId());
		setName(stepDefinition.getName());
		setStartsWithPrevious(stepDefinition.isStartsWithPrevious());
		getAssignment().setType(stepDefinition.getAssignmentType());

		setParameters(new HashMap<String, Object>(otherDefinition.getParameters()));
	}

	@Override
	public boolean isInterruptSupport() {
		return interruptSupport;
	}

	@Override
	public boolean isBackSupport() {
		return backSupport;
	}

	public Deque<SequenceFlow> getAdditionalBackSequenceFlow() {
		return additionalBackSequenceFlow;
	}

	public void setAdditionalBackSequenceFlow(Deque<SequenceFlow> additionalBackSequenceFlow) {
		this.additionalBackSequenceFlow = additionalBackSequenceFlow;
	}

	public Deque<SequenceFlow> getAdditionalNOPASSSequenceFlow() {
		return additionalNOPASSSequenceFlow;
	}

	public void setAdditionalNOPASSSequenceFlow(Deque<SequenceFlow> additionalNOPASSSequenceFlow) {
		this.additionalNOPASSSequenceFlow = additionalNOPASSSequenceFlow;
	}
	
}
