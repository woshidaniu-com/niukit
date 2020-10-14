package org.springframework.enhanced.context;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.enhanced.utils.SpringContextUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;
/**
 * 
 * @className	： SpringWebInstanceContext
 * @description	： WebApplicationContext上下文实例;根据ServletContext初始化的实例
 * @author 		：大康（743）
 * @date		： 2017年4月18日 下午8:56:08
 * @version 	V1.0
 */
public class SpringWebInstanceContext extends AbstractSpringInstanceContext{

	public static void initialInstanceContext(ServletContext servletContext) {
		new SpringWebInstanceContext(WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext));
	}

	private SpringWebInstanceContext(ApplicationContext applicationContext) {
		super.setSpringContext(applicationContext);
		SpringContextUtils.setContext(this);
		LOG.info("SpringWebInstanceContext initialization completed.");
	}
}
