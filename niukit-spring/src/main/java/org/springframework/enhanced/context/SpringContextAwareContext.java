package org.springframework.enhanced.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.enhanced.utils.SpringContextUtils;

/**
 * 
 * @className	： SpringContextAwareContext
 * @description	： ApplicationContext 注入型上下文实例（用于取spring容器中定义的bean）
 * <b>Example:</b>
 * <pre>
 * 配置：
 * &lt;bean id="springAwareContext" class="org.springframework.enhanced.context.SpringContextAwareContext"/&gt;
 * </pre>
 * @author 		：大康（743）
 * @date		： 2017年4月18日 下午8:57:06
 * @version 	V1.0
 */
public class SpringContextAwareContext extends AbstractSpringInstanceContext implements ApplicationContextAware{
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		super.setSpringContext(applicationContext);
		SpringContextUtils.setContext(this);
		LOG.info("SpringContextAwareContext initialization completed.");
	}

}
