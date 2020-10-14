package org.springframework.enhanced.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.enhanced.aop.aware.AfterAware;
import org.springframework.enhanced.aop.aware.BeforeAware;
import org.springframework.enhanced.aop.aware.ExceptionAware;
import org.springframework.enhanced.aop.aware.Invocation;
import org.springframework.enhanced.factory.EnhancedBeanFactory;
import org.springframework.enhanced.utils.AspectUtils;

/**
 * 
 * @className	： AbstractAspectInterceptor
 * @description	：  基于Spring AOP 的方法切面拦截器
 * @author 		：大康（743）
 * @date		： 2017年4月18日 下午9:13:00
 * @version 	V1.0
 */
public abstract class AbstractAspectInterceptor extends EnhancedBeanFactory {
	
	protected Logger LOG = LoggerFactory.getLogger(getClass());
	
	/***
	 * 
	 * @description	： before 切面 : :方法执行前被调用
	 * @author 		：大康（743）
	 * @date 		：2017年4月18日 下午9:13:11
	 * @param point
	 * @throws Throwable
	 */
	public void before(JoinPoint point) throws Throwable {
		String mathodName = point.getSignature().getName();
		if("setSelf".equalsIgnoreCase(mathodName)){
    		return;
    	}
		Object target = point.getTarget();
		Object[] args = point.getArgs();
		Method method = AspectUtils.getMethod(point);
    	//如果实现了BeforeAware
		if(method.getDeclaringClass().isInstance(BeforeAware.class)){
			//获得前置通知调用方法doBefore
			Method doBefore = BeanUtils.findMethod(method.getDeclaringClass(),"doBefore",Invocation.class);
			//执行doBefore
			if(doBefore!=null){
				doBefore.invoke(target,new Object[]{new Invocation(target, method, args)});
			}
		}else{
			//调用接口
			this.doBefore(point);
		}
	}
	
	public abstract void doBefore(JoinPoint point) throws Throwable;
	
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {  
    	try {
    		Object result = joinPoint.proceed();
    		String mathodName = joinPoint.getSignature().getName();
    		if("setSelf".equalsIgnoreCase(mathodName)){
        		return result;
        	}
    		return this.doAround(joinPoint);
        }catch (Exception e) {
            e.printStackTrace();
            LOG.warn("invoke(ProceedingJoinPoint) - exception ignored", e); //$NON-NLS-1$ 
        }finally {
        	if (LOG.isDebugEnabled()) {
            	LOG.debug("invoke(ProceedingJoinPoint) - end"); //$NON-NLS-1$
            }
        }
        return null; 
    }  
	
	public abstract Object doAround(ProceedingJoinPoint joinPoint) throws Throwable;
	
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
		Object target = point.getTarget();
		Object[] args = point.getArgs();
		Method method = AspectUtils.getMethod(point);
		//如果实现了AfterAware
    	if(method.getDeclaringClass().isInstance(AfterAware.class)){
    		//获得后置通知调用方法doAfter
    		Method doAfter = BeanUtils.findMethod(method.getDeclaringClass(), "doAfter",Invocation.class);
    		//执行doAfter
    		if(doAfter!=null){
    			doAfter.invoke(target,new Object[]{new Invocation(target, method, args, returnValue)});
    		}
    	}else{
    		this.doAfterReturning(point,returnValue);
    	}
    }  
	
    public abstract void doAfterReturning(JoinPoint point,Object returnValue) throws Throwable;
    
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
		Object target = point.getTarget();
		Object[] args = point.getArgs();
		Method method = AspectUtils.getMethod(point);
		//如果实现了ExceptionAware
    	if(method.getDeclaringClass().isInstance(ExceptionAware.class)){
    		//获得异常通知调用方法doException
    		Method doException = BeanUtils.findMethod(method.getDeclaringClass(), "doException",Invocation.class);
    		//执行doException
    		if(doException != null){
    			doException.invoke(target,new Object[]{new Invocation(target, method, args, ex)});
    		}
    	}else{
    		this.doAfterThrowing(point, ex);
    	}
    }
    
    public abstract void doAfterThrowing(JoinPoint point,Throwable ex) throws Throwable;
	
}
