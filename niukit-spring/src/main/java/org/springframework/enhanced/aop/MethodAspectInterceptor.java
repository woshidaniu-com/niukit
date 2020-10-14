package org.springframework.enhanced.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.enhanced.aop.annotations.After;
import org.springframework.enhanced.aop.annotations.Before;
import org.springframework.enhanced.aop.annotations.Comment;
import org.springframework.enhanced.aop.annotations.Exceptional;
import org.springframework.enhanced.aop.annotations.Logger;
import org.springframework.enhanced.aop.annotations.Validation;
import org.springframework.enhanced.aop.aware.Invocation;
import org.springframework.enhanced.utils.AspectUtils;
import org.springframework.enhanced.utils.StringUtils;

/**
 * 
 * @className	： MethodAspectInterceptor
 * @description	： 基于Spring AOP 的方法切面拦截器
 * @author 		：大康（743）
 * @date		： 2017年4月18日 下午9:13:48
 * @version 	V1.0
 */
public class MethodAspectInterceptor extends AbstractAspectInterceptor{
	
	protected org.slf4j.Logger LOG = LoggerFactory.getLogger(getClass());
	
	@Override
	public void afterPropertiesSet() throws Exception {
		
	}
	
	/***
	 * 
	 *@描述		：before 切面 : :方法执行前被调用
	 *@创建人	: kangzhidong
	 *@创建时间	: Mar 21, 20169:40:57 AM
	 *@param point
	 *@throws Throwable
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	@Override
	public void doBefore(JoinPoint point) throws Throwable {
		Object target = point.getTarget();
		Object[] args = point.getArgs();
		Method method = AspectUtils.getMethod(point);
    	 
    	//log.info("调用[" + target.getClass() + "] 类 [ " + method.getName() + "]方法的before通知");  
    	
    	Invocation invocation = new Invocation(target, method, args);
    	//获得对象上的before注解
		Before before =  target.getClass().getAnnotation(Before.class);
    	invocation.setBefore(before);
    	
    	String beanName = null;
    	//获得执行方法
		String methodName = before == null ? "doBefore" : before.method();
		
		 //查询日志注解
		Comment comment = method.getAnnotation(Comment.class);
		if(comment != null){
			invocation.setComment(comment);
		}
		
    	//获得对象上的Logger注解
 		Logger logger = target.getClass().getAnnotation(Logger.class);
 		if(logger!=null){
 			beanName = logger.name();
 			invocation.setLogger(logger);
 		}
		
 		//查询验证注解
		Validation validation = method.getAnnotation(Validation.class);
		if(validation!=null){
			beanName = validation.name();
			invocation.setValidation(validation);
		}
		
		//如果定义了方法名称，且其他参数有一个不为空则表明用户自定义调用方法
		if(StringUtils.hasText(beanName)&&StringUtils.hasText(methodName)){
			//从spring上下文获得bean
			Object bean = getApplicationContext().getBean(beanName);
			if(bean!=null){
				//获得前置通知调用方法
	    		Method doBefore = BeanUtils.findDeclaredMethod(bean.getClass(), methodName,Invocation.class);
	    		//执行doBefore
	    		if(doBefore!=null){
	    			doBefore.invoke(bean,new Object[]{ invocation });
	    		}else{
	    			LOG.error("在["+ bean.getClass() + " ]类中没有找到["+ methodName + " ]方法!");
	    		}
			}else{
				LOG.error("在 Spring Context 中没有找到 ID或者name 为["+ beanName + "]的bean!");
			}
		}
		
	}
	
	@Override
	public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {  
	  /* 
	  try {
    		Object result = joinPoint.proceed();
    		String mathodName = joinPoint.getSignature().getName();
    		if("setSelf".equalsIgnoreCase(mathodName)){
        		return result;
        	}
    		
     		Object target = joinPoint.getTarget();
     		Object[] args = joinPoint.getArgs();
     		//获得对象上的Logger注解
     		Logger logger = target.getClass().getAnnotation(Logger.class);
     		if(logger!=null){
     			
     			
     	    	Class<?>[] paramTypes = new  Class<?>[args.length];
     	    	for (int i = 0; i < args.length; i++) {
     	    		paramTypes[i] = args[i].getClass();
     			}
     	    	Method method = BeanUtils.findDeclaredMethod(target.getClass(), mathodName, paramTypes);
     	    	if(BlankUtil.isBlank(method)){
     	    		method = ((MethodSignature)joinPoint.getSignature()).getMethod();
     	    	}
     	    	//log.info("调用[" + target.getClass() + "] 类 [ " + method.getName() + "]方法的around通知"); 
     			String serviceID = logger.name();
				//查询日志注解
				Comment comment = method.getAnnotation(Comment.class);
				//为空表示进行日志记录
				if(comment!=null){
		        	String methodName = logger.method();
		        	//如果定义了方法名称，且其他参数有一个不为空则表明用户自定义调用方法
		        	if(StringUtils.hasText(serviceID)&&StringUtils.hasText(methodName)){
		        		//从spring上下文获得bean
		        		Object bean = InstanceContextUtil.getContext().getInstance(serviceID);
		        		if(bean!=null){
		        			//获得异常通知调用方法
		            		Method doException = BeanUtils.findMethod(bean.getClass(), methodName);
		            		//执行doException
		            		if(doException!=null){
		            			Invocation invocation = new Invocation(target, method, args);
		            			invocation.setLogger(logger);
 		            			invocation.setComment(comment);
		        				doException.invoke(bean,new Object[]{ invocation });
		            		}else{
		            			log.error("在["+ bean.getClass() + " ]类中没有找到["+ methodName + " ]方法!");
		            		}
		        		}else{
		        			log.error("在 Spring Context 中没有找到 ID或者name 为["+ serviceID + "]的bean!");
		        		}
		        	}
				}
	        }else{
	        	Method method = ((MethodSignature)joinPoint.getSignature()).getMethod();
	    		//如果实现了ExceptionAware
	        	if(method.getDeclaringClass().isInstance(ExceptionAware.class)){
	        		//获得异常通知调用方法doException
	        		Method doException = BeanUtils.findMethod(method.getDeclaringClass(), "doException");
	        		//执行doException
	        		if(doException!=null){
	        			doException.invoke(target,new Object[]{new Invocation(target, method, args )});
	        		}
	        	}
	    	}
            return result;
        }catch (Exception e) {
            e.printStackTrace();
            log.warn("invoke(ProceedingJoinPoint) - exception ignored", e); //$NON-NLS-1$ 
        }finally {
        }
        if (log.isDebugEnabled()) {
        	log.debug("invoke(ProceedingJoinPoint) - end"); //$NON-NLS-1$
        }*/
        return null; 
    }  
   
	
	/**
	 * 
	 *@描述		：after 切面 :方法执行完后被调用
	 *@创建人	: kangzhidong
	 *@创建时间	: Mar 21, 20169:40:44 AM
	 *@param point
	 *@param returnValue
	 *@throws Throwable
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	@Override
    public void doAfterReturning(JoinPoint point,Object returnValue) throws Throwable {  
		
    	Object target = point.getTarget();
		Object[] args = point.getArgs();
		Method method = AspectUtils.getMethod(point);
    	
		//log.info("调用[" + target.getClass() + "] 类 [ " + method.getName() + "]方法的after通知");  
		//获得对象上的after注解
        After after = target.getClass().getAnnotation(After.class);
 		if(after != null){
 	    	
 	    	Invocation invocation = new Invocation(target, method, args, returnValue);
 	    	invocation.setAfter(after);
 	    	
 			String beanName = null;
 			String methodName = after.method();
 			//获得对象上的Logger注解
 	 		Logger logger = target.getClass().getAnnotation(Logger.class);
 	        if(logger!=null){
 	        	beanName = logger.name();
 	        	invocation.setLogger(logger);
 	        }
 	       //查询日志注解
			Comment comment = method.getAnnotation(Comment.class);
			if(comment != null){
				invocation.setComment(comment);
			}
 	        //如果定义了方法名称，且其他参数有一个不为空则表明用户自定义调用方法
        	if(StringUtils.hasText(beanName)&&StringUtils.hasText(methodName)){
        		//从spring上下文获得bean
        		Object bean = getApplicationContext().getBean(beanName);
        		if(bean!=null){
        			//获得后置通知调用方法
            		Method doAfter = BeanUtils.findDeclaredMethod(bean.getClass(), methodName,Invocation.class);
            		//执行doAfter
            		if(doAfter!=null){
        				doAfter.invoke(bean,new Object[]{invocation});
            		}else{
            			LOG.error("在["+ bean.getClass() + " ]类中没有找到["+ methodName + " ]方法!");
            		}
        		}else{
        			LOG.error("在 Spring Context 中没有找到 ID或者name 为["+ beanName + "]的bean!");
        		}
        	}
 		}
    }  
	
	/**
	 * 
	 *@描述		：异常切面  :方法执行完后如果抛出异常则被调用
	 *@创建人	: kangzhidong
	 *@创建时间	: Mar 21, 20169:40:30 AM
	 *@param point
	 *@param ex
	 *@throws Throwable
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	@Override
    public void doAfterThrowing(JoinPoint point,Throwable ex) throws Throwable {  
		
    	Object target = point.getTarget();
		Object[] args = point.getArgs();
		Method method = AspectUtils.getMethod(point);
       
		//log.info("调用[" + target.getClass() + "] 类 [ " + method.getName() + "]方法的throwing通知"); 
		//获得对象上的Exceptional注解
        Exceptional exceptional = target.getClass().getAnnotation(Exceptional.class);
 		if( exceptional != null ){
 	    	
 	    	Invocation invocation = new Invocation(target, method, args, ex);
 	    	invocation.setExceptional(exceptional);
 	    	
 	    	String beanName = null;
 	    	String methodName = exceptional.method();
 	    	
 	    	 //获得对象上的Logger注解
 	 		Logger logger = target.getClass().getAnnotation(Logger.class);
 	        if(logger!=null){
 	        	beanName = logger.name();
 	        	invocation.setLogger(logger);
 	        }
 	        
 	        //如果定义了方法名称，且其他参数有一个不为空则表明用户自定义调用方法
        	if(StringUtils.hasText(beanName)&&StringUtils.hasText(methodName)){
        		//从spring上下文获得bean
        		Object bean = getApplicationContext().getBean(beanName);
        		if(bean!=null){
        			//获得异常通知调用方法
            		Method doException = BeanUtils.findDeclaredMethod(bean.getClass(), methodName,Invocation.class);
            		//执行doException
            		if(doException!=null){
        				doException.invoke(bean,new Object[]{ invocation });
            		}else{
            			LOG.error("在["+ bean.getClass() + " ]类中没有找到["+ methodName + " ]方法!");
            		}
        		}else{
        			LOG.error("在 Spring Context 中没有找到 ID或者name 为["+ beanName + "]的bean!");
        		}
        	}
        	
 		}
    }

	
}