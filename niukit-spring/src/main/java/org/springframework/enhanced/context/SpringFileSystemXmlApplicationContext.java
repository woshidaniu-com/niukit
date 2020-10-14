package org.springframework.enhanced.context;

import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.enhanced.utils.SpringContextUtils;
/**
 * 
 * @className	： SpringFileSystemXmlApplicationContext
 * @description	： Spring 文件路径XML配置上下文实例
 * @author 		：大康（743）
 * @date		： 2017年4月18日 下午8:56:32
 * @version 	V1.0
 */
public class SpringFileSystemXmlApplicationContext  extends AbstractSpringInstanceContext {

	public SpringFileSystemXmlApplicationContext(String[] locations) {
		if (locations.length < 1) {
			return;
		} else {
			super.setSpringContext(new FileSystemXmlApplicationContext(locations));
			SpringContextUtils.setContext(this);
			LOG.info("SpringFileSystemXmlApplicationContext initialization completed.");
			return;
		}
	}
}
