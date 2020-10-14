package org.springframework.enhanced.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * 
 * @className	： AbstractQuartzTask
 * @description	： 抽象的Quartz定时任务对象
 * @author 		：大康（743）
 * @date		： 2017年4月18日 下午9:05:31
 * @version 	V1.0
 */
public abstract class AbstractQuartzTask implements InitializingBean{

	protected static Logger LOG = LoggerFactory.getLogger(AbstractQuartzTask.class);
	
	@Override
	public void afterPropertiesSet() throws Exception {
		try {
			//调用抽象方法
			doAfterPropertiesSet();
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
	}
	
	/**
	 * 
	 *@描述		：定义实现类需要实现的抽象方法
	 *@创建人	: kangzhidong
	 *@创建时间	: Mar 21, 201611:07:10 AM
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	public abstract void doAfterPropertiesSet() throws Exception;
	
	
	/**
	 * 
	 *@描述		：任务执行方法
	 *@创建人	: kangzhidong
	 *@创建时间	: Mar 21, 201611:07:16 AM
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	public abstract void task();
	
	 
	
}