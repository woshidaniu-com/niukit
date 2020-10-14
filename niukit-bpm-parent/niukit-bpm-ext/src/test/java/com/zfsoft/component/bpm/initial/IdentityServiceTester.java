package com.woshidaniu.component.bpm.initial;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

public class IdentityServiceTester {

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@Test
	public void queryUserTest() {
		IdentityService identityService = activitiRule.getIdentityService();
	
		User singleResult = identityService.createUserQuery().userId("zhangxb").singleResult();
	
		System.out.println(singleResult.getId());
		System.out.println(singleResult.getFirstName());
	}

	@Test
	public void queryGroupTest(){
		IdentityService identityService = activitiRule.getIdentityService();
		Group singleResult = identityService.createGroupQuery().groupId("3CFC5DF7E9C71567E0538713470A4FEA").singleResult();
		System.out.println(singleResult.getId());
		System.out.println(singleResult.getName());
		System.out.println(singleResult.getType());
		System.out.println(singleResult.getClass());
	}
	
}
