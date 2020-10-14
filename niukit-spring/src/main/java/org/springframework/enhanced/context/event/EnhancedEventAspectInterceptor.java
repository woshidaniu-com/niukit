package org.springframework.enhanced.context.event;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.enhanced.context.event.aspect.JoinPointAfterEvent;
import org.springframework.enhanced.context.event.aspect.JoinPointAroundEvent;
import org.springframework.enhanced.context.event.aspect.JoinPointBeforeEvent;
import org.springframework.enhanced.context.event.aspect.JoinPointThrowingEvent;
import org.springframework.enhanced.factory.EnhancedBeanFactory;
import org.springframework.enhanced.utils.AspectUtils;

public class EnhancedEventAspectInterceptor extends EnhancedBeanFactory implements ApplicationEventPublisherAware{
	
	protected ApplicationEventPublisher eventPublisher;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		
	}  
	
	/***
	 * 
	 * @description	： before 切面 : :方法执行前被调用
	 * @author 		：大康（743）
	 * @date 		：2017年4月18日 下午9:13:11
	 * @param point
	 */
	public void before(JoinPoint point) throws Throwable {
		
		String mathodName = point.getSignature().getName();
		if("setSelf".equalsIgnoreCase(mathodName)){
    		return;
    	}
		
		EventInvocation invocation = new EventInvocation(point);
		
		Signature signature = point.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		invocation.setArgNames(methodSignature.getParameterNames());
		invocation.setArgs(point.getArgs());
		invocation.setMethod(AspectUtils.getMethod(point));
		invocation.setTarget(point.getTarget());
		
		//推送异常事件
 		getEventPublisher().publishEvent(new JoinPointBeforeEvent( this , invocation ));
 		
	}
	
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {  
    	try {
    		
    		Object returnValue = joinPoint.proceed();
    		String mathodName = joinPoint.getSignature().getName();
    		if("setSelf".equalsIgnoreCase(mathodName)){
        		return returnValue;
        	}
    		
    		EventInvocation invocation = new EventInvocation(joinPoint, returnValue);
    		
    		Signature signature = joinPoint.getSignature();
    		MethodSignature methodSignature = (MethodSignature) signature;
    		invocation.setArgNames(methodSignature.getParameterNames());
    		invocation.setArgs(joinPoint.getArgs());
    		invocation.setMethod(AspectUtils.getMethod(joinPoint));
    		invocation.setReturnValue(returnValue);
    		invocation.setTarget(joinPoint.getTarget());
    		
    		//推送异常事件
     		getEventPublisher().publishEvent(new JoinPointAroundEvent( this , invocation ));
     		
     		return returnValue;
     		
        }catch (Exception e) {
            LOG.warn("invoke(ProceedingJoinPoint) - exception ignored", e); //$NON-NLS-1$ 
        }finally {
        	if (LOG.isDebugEnabled()) {
            	LOG.debug("invoke(ProceedingJoinPoint) - end"); //$NON-NLS-1$
            }
        }
        return null; 
    }  
	
	/**
	 * 
	 * @description	： after 切面 :方法执行完后被调用
	 * @author 		：大康（743）
	 * @date 		：2017年4月18日 下午9:13:21
	 * @param point
	 * @param returnValue
	 * @throws Throwable
	 */
    public void afterReturning(JoinPoint point,Object returnValue) throws Throwable {  
    	String mathodName = point.getSignature().getName();
		if("setSelf".equalsIgnoreCase(mathodName)){
    		return;
    	}
		
		EventInvocation invocation = new EventInvocation(point, returnValue);
		
		Signature signature = point.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		invocation.setArgNames(methodSignature.getParameterNames());
		invocation.setArgs(point.getArgs());
		invocation.setMethod(AspectUtils.getMethod(point));
		invocation.setReturnValue(returnValue);
		invocation.setTarget(point.getTarget());
		
		//推送异常事件
 		getEventPublisher().publishEvent(new JoinPointAfterEvent( this , invocation ));
 		
    }  
	
    
    /**
	 * 
	 * @description	： 异常切面  :方法执行完后如果抛出异常则被调用
	 * @author 		：大康（743）
	 * @date 		：2017年4月18日 下午9:13:31
	 * @param point
	 * @param ex
	 * @throws Throwable
	 */
    public void afterThrowing(JoinPoint point,Throwable ex) throws Throwable {  
    	
    	String mathodName = point.getSignature().getName();
		if("setSelf".equalsIgnoreCase(mathodName)){
    		return;
    	}
		
		EventInvocation invocation = new EventInvocation(point, ex);
		
		Signature signature = point.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		invocation.setArgNames(methodSignature.getParameterNames());
		invocation.setArgs(point.getArgs());
		invocation.setMethod(AspectUtils.getMethod(point));
		invocation.setTarget(point.getTarget());
		
		//推送异常事件
 		getEventPublisher().publishEvent(new JoinPointThrowingEvent( this , invocation ));
 		
    }
    
	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	public ApplicationEventPublisher getEventPublisher() {
		return eventPublisher;
	}
	
}
