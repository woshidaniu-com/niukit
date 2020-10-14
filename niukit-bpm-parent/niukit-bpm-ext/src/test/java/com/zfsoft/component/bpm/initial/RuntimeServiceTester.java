package com.woshidaniu.component.bpm.initial;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.RuntimeServiceImpl;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

public class RuntimeServiceTester {
	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();
	
	@Test
	public void startProcessInstance(){
//		VocationModel model = new VocationModel();
//		model.setId(UniqID.getInstance().getUniqIDHash());
//		model.setNumberOfDays(3);
//		model.setCreateTime(new Timestamp(new Date().getTime()));
//		model.setEndDate(new Date());
//		model.setStartDate(new Date());
//		model.setMotivation("回家");
//		model.setProposer("110413113");
//		model.setVocationType(VocationType.PERSONAL_LEAVE);
		RuntimeService runtimeService = activitiRule.getRuntimeService();
//		Map<String,Object> variables = new HashMap<String,Object>();
//		variables.put("businessData", model);
		activitiRule.getIdentityService().setAuthenticatedUserId("zhangxb");
//		runtimeService.startProcessInstanceByKey("myProcess", model.getId(), variables);
		Map<String,Object> variables = new HashMap<String,Object>();
		runtimeService.startProcessInstanceByKey("advanced-test-process", variables);
	}
	
	
	@Test
	public void queryVocationProcessInstance(){
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		List<ProcessInstance> processList = runtimeService.createProcessInstanceQuery().active().list();
		for (ProcessInstance processInstance : processList) {
			String activityId = processInstance.getActivityId();
			System.out.println(activityId);
		}
//		List<Execution> list = runtimeService.createExecutionQuery().processDefinitionKey("VocationProcessDef").list();
//		for (Execution execution : list) {
//			System.out.println(execution.getActivityId());
//			System.out.println(execution.getDescription());
//			System.out.println(execution.getId());
//			System.out.println(execution.getName());
//			System.out.println(execution.getProcessInstanceId());
//		}
	}
	
	@Test
	public void queryVocationProcessInstanceTask(){
		TaskService taskService = activitiRule.getTaskService();
		List<Task> list = taskService.createTaskQuery().
			processDefinitionKey("VocationProcessDef").taskCandidateUser("zhangxb").
			list();
		for (Task task : list) {
			System.out.println(task + " : " +  task.getProcessInstanceId());
		}
	}
	
	
	@Test
	public void deleteVocationProcessInstance(){
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().processDefinitionKey("myProcess").list();
		for (ProcessInstance processInstance : list) {
			if(processInstance!=null){
				String processInstanceId = processInstance.getProcessInstanceId();
				runtimeService.deleteProcessInstance(processInstanceId, "User Cancel!");
			}
		}
		
	}
	
	
	@Test
	public void queryUserTaskByAssignee(){
		TaskService taskService = activitiRule.getTaskService();
		List<Task> list = taskService.createTaskQuery().processDefinitionKey("myProcess")
					.taskAssignee("kermit").list();
		System.out.println(list);
	}
	
	@Test
	public void queryUserTaskByCandidateOrAssignee(){
		TaskService taskService = activitiRule.getTaskService();
		List<Task> list = taskService.createTaskQuery().processDefinitionKey("myProcess")
					.taskCandidateOrAssigned("zhangxb").list();
		System.out.println(list);
	}
	
	@Test
	public void queryUserTaskByGroupCandidate(){
		TaskService taskService = activitiRule.getTaskService();
		List<Task> list = taskService.createTaskQuery().processDefinitionKey("myProcess")
					.taskCandidateGroup("managerment").list();
		System.out.println(list);
	}
	
	@Test
	public void queryUserTaskByInvolvedUser(){
		TaskService taskService = activitiRule.getTaskService();
		List<Task> list = taskService.createTaskQuery().processDefinitionKey("myProcess")
				.taskInvolvedUser("kermit").list();
		System.out.println(list);
	}
	
	@Test
	public void queryFinishedProcess(){
		
		HistoryService historyService = activitiRule.getHistoryService();
		List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery().processDefinitionKey("myProcess").finished().list();
		System.out.println(list);
	}
	
	@Test
	public void queryProcessInstanceByInvolvedUser(){
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().processDefinitionKey("myProcess")
			.involvedUser("kermit").list();
		System.out.println(list);
	}
	
	
	@Test
	public void claimVolcationProcessTask(){
		TaskService taskService = activitiRule.getTaskService();
		Task singleResult = taskService.createTaskQuery().processDefinitionKey("myProcess").singleResult();
		taskService.claim(singleResult.getId(), "zhangxb");
	}
	
	@Test
	public void delegateVocationProcessTask(){
		TaskService taskService = activitiRule.getTaskService();
		Task singleResult = taskService.createTaskQuery().processDefinitionKey("myProcess").singleResult();
		taskService.delegateTask(singleResult.getId(), "penghui");
	}
	
	@Test
	public void completeProcessTask(){
		TaskService taskService = activitiRule.getTaskService();
		FormService formService = activitiRule.getFormService();
		Task singleResult = taskService.createTaskQuery().taskId("700005").singleResult();
		formService.submitTaskFormData("700005", new HashMap());
	}
	
	@Test
	public void completeProcessTask2(){
		TaskService taskService = activitiRule.getTaskService();
		Task singleResult = taskService.createTaskQuery().taskId("435010").singleResult();
		RuntimeServiceImpl runtimeService = (RuntimeServiceImpl) activitiRule.getRuntimeService();
//		runtimeService.getCommandExecutor().execute(new ExtendCompleteTaskCmd(singleResult.getId(), null, true, true));
		
	}
	
	@Test
	public void completeProcess(){
		RuntimeServiceImpl runtimeService = (RuntimeServiceImpl) activitiRule.getRuntimeService();
		runtimeService.getCommandExecutor().execute(new Command<Void>() {

			@Override
			public Void execute(CommandContext commandContext) {
				ExecutionEntity findExecutionById = commandContext.getExecutionEntityManager().findExecutionById("420001");
				findExecutionById.end();
				//ProcessInstance singleResult = runtimeService.createProcessInstanceQuery().processInstanceId("420001").singleResult();
				return null;
			}
		});
		
	}
	
	@Test 
	public void deleteVocationProcessDeployment(){
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		Deployment singleResult = repositoryService.createDeploymentQuery().processDefinitionKey("VocationProcessDef").singleResult();
		repositoryService.deleteDeployment(singleResult.getId(), true);
	}
	
	@Test
	public void queryAvaliableTaskList(){
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		TaskService taskService = activitiRule.getTaskService();
		Task task = taskService.createTaskQuery().taskId("285002").singleResult();
		ProcessDefinition processDef = repositoryService.createProcessDefinitionQuery().processDefinitionId(task.getProcessDefinitionId()).singleResult();
		ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)  
                .getDeployedProcessDefinition(processDef.getId());
		
		List<ActivityImpl> activities = processDefinition.getActivities();
	}
	
//	@Test
//	public void revocationUserTask(){
//		TaskServiceImpl taskService = (TaskServiceImpl) activitiRule.getTaskService();
//		CommandExecutor commandExecutor = taskService.getCommandExecutor();
//		Command<Void> command = new AuditorRevocationCmd("392510");
//		commandExecutor.execute(command);
//	}
	
	
}
