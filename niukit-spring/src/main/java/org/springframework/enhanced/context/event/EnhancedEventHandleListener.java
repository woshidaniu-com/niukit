package org.springframework.enhanced.context.event;

import java.lang.reflect.Method;
import java.util.Map;

import org.springframework.context.ApplicationListener;
import org.springframework.enhanced.context.event.aspect.AbstractJoinPointEvent;
import org.springframework.enhanced.context.event.handler.EventHandler;
import org.springframework.enhanced.context.event.handler.ExceptionEventHandler;
import org.springframework.enhanced.factory.EnhancedBeanFactory;

/**
 * 
 * @className	： EnhancedEventHandleListener
 * @description	： * Spring中ApplicationContext的事件机制--- 内定事件)
	在Spring中已经定义了五个标准事件，分别介绍如下：
	
	1)      ContextRefreshedEvent：当ApplicationContext初始化或者刷新时触发该事件。
	
	2)      ContextClosedEvent：当ApplicationContext被关闭时触发该事件。容器被关闭时，其管理的所有单例Bean都被销毁。
	
	3)      RequestHandleEvent：在Web应用中，当一个http请求（request）结束触发该事件。
	
		ContestStartedEvent：Spring2.5新增的事件，当容器调用ConfigurableApplicationContext的Start()方法开始/重新开始容器时触发该事件。
	
	5) ContestStopedEvent：Spring2.5新增的事件，当容器调用ConfigurableApplicationContext的Stop()方法停止容器时触发该事件。

 * @author 		：大康（743）
 * @date		： 2017年4月19日 下午9:43:50
 * @version 	V1.0
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class EnhancedEventHandleListener  extends EnhancedBeanFactory implements ApplicationListener<EnhancedEvent> {

	@Override
	public void afterPropertiesSet() throws Exception {
		
	}
	
	@Override
	public void onApplicationEvent(EnhancedEvent event) {
		// 异常事件推送
		if (event instanceof ExceptionEvent) {
			//获取所有异常处理接口实现
			Map<String, ExceptionEventHandler>  handlers = getApplicationContext().getBeansOfType(ExceptionEventHandler.class);
			if( null != handlers){
				for (String alias : handlers.keySet()) {
					handlers.get(alias).handle((ExceptionEvent)event);
				}
			}
		}
		// JoinPoint 事件推送
		else if (event instanceof AbstractJoinPointEvent) {
			//类型转换
			AbstractJoinPointEvent joinPointEvent =  (AbstractJoinPointEvent) event;
			//获取事件环境对象
			EventInvocation invocation = joinPointEvent.getBind();
			//当前访问的方法
			Method method = invocation.getMethod();
			//事件切面注解
			EventAspect eventAspect = method.getAnnotation(EventAspect.class);
			if( null != eventAspect){
				//获取指定的所有事件处理接口实现
				Map<String, ?>  handlers = getApplicationContext().getBeansOfType(eventAspect.handler());
				if( null != handlers){
					for (String alias : handlers.keySet()) {
						((EventHandler)handlers.get(alias)).handle(event);
					}
				}
			}
		}
	}
	
}