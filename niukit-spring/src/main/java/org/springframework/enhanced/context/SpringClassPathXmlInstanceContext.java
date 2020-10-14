package org.springframework.enhanced.context;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.enhanced.utils.SpringContextUtils;

/**
 * 
 * @className	： SpringClassPathXmlInstanceContext
 * @description	： Spring 类路径XML配置上下文实例
 * @author 		：大康（743）
 * @date		： 2017年4月18日 下午8:58:45
 * @version 	V1.0
 */
public class SpringClassPathXmlInstanceContext extends AbstractSpringInstanceContext {

	public SpringClassPathXmlInstanceContext(String[] locations) {
		if (locations.length < 1) {
			return;
		} else {
			super.setSpringContext(new ClassPathXmlApplicationContext(locations));
			SpringContextUtils.setContext(this);
			LOG.info("SpringClassPathXmlInstanceContext initialization completed.");
			return;
		}
	}
}
