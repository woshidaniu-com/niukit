package com.woshidaniu.safety.xss.integration;

import org.owasp.validator.html.Policy;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.util.ResourceUtils;

/**
 * <p>
 *   <h3>niutal框架<h3>
 *   说明：Antisamy Policy 对象生成工厂bean
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月4日下午4:28:08
 */
public class AntisamyPolicyFactoryBean implements FactoryBean<Policy>{

	/**
	 * policy配置文件路径
	 */
	private String policyConfigFilePath;
	
	@Override
	public Policy getObject() throws Exception {
		return Policy.getInstance(ResourceUtils.getFile(policyConfigFilePath));
	}

	@Override
	public Class<?> getObjectType() {
		return Policy.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public String getPolicyConfigFilePath() {
		return policyConfigFilePath;
	}

	public void setPolicyConfigFilePath(String policyConfigFilePath) {
		this.policyConfigFilePath = policyConfigFilePath;
	}

	
	
}
