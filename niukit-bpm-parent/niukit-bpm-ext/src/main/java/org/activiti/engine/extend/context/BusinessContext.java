package org.activiti.engine.extend.context;

import org.activiti.engine.extend.event.listener.AbstractEventListener;

/**
 * <p>
 *   <h3>niutal框架<h3>
 *   <br>说明：业务逻辑上下文
 *	 <br>class：org.activiti.engine.extend.context.BusinessContext.java
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月15日上午8:50:29
 */
public class BusinessContext{
	
	/**
	 * 业务回调线程绑定對象
	 */
	protected static ThreadLocal<AbstractEventListener> businessCallbackThreadLocal = new ThreadLocal<AbstractEventListener>();

	
	public static AbstractEventListener get(){
		return businessCallbackThreadLocal.get();
	}
	
	public static void set(AbstractEventListener call){
		businessCallbackThreadLocal.set(call);
	}
	
}
