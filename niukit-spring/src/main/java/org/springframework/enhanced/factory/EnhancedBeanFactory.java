package org.springframework.enhanced.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 
 * @className	： EnhancedBeanFactory
 * @description	： 服务工厂（用于取spring容器中定义的bean）
 * @author 		：大康（743）
 * @date		： 2017年4月18日 下午9:09:57
 * @version 	V1.0
 */
public abstract class EnhancedBeanFactory implements ApplicationContextAware, InitializingBean, DisposableBean  {
	
	protected Logger LOG = LoggerFactory.getLogger(EnhancedBeanFactory.class);
	protected ApplicationContext applicationContext = null;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)throws BeansException {
		this.applicationContext = applicationContext;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	@Override
	public void destroy() throws Exception {
	}
	
	
}
