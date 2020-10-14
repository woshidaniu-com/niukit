package com.woshidaniu.component.bpm.initial;

import java.util.List;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RepositoryServiceTester {

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();
	
	static final Logger LOG = LoggerFactory.getLogger(RepositoryServiceTester.class);
	
	@Test
	public void  deleteProcessDefination(){
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		Deployment singleResult = repositoryService.createDeploymentQuery().processDefinitionKey("SimpleVocationProcess").singleResult();
		repositoryService.deleteDeployment(singleResult.getId(), true);
	}
	
//	@Test
//	public void  deleteProcessDefinations(){
//		RepositoryService repositoryService = activitiRule.getRepositoryService();
//		List<Deployment> list = repositoryService.createDeploymentQuery().processDefinitionKey("p_1").list();
//		for (Deployment deployment : list) {
//			repositoryService.deleteDeployment(deployment.getId(), true);
//		}
//		
//	}
	
	@Test
	public void deployProcessDefination(){
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		repositoryService.createDeployment().addClasspathResource("SimpleVocationProcess.bpmn").deploy();
	}
	
	@Test
	public void queryProcessDefination(){
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		ProcessDefinitionEntity processDef = (ProcessDefinitionEntity) repositoryService.createProcessDefinitionQuery().processDefinitionKey("p_1").singleResult();
		ActivityImpl findActivity = processDef.findActivity("usertask5");
		System.out.println(processDef);
	}
	
	
	@Test
	public void suspendProcessDefination(){
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		repositoryService.suspendProcessDefinitionByKey("p_1");
	}
	
	
}
