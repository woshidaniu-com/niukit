package org.springframework.enhanced.context.event;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;

public class EventInvocation {

	/**
	 * 
	 */
	private JoinPoint point;
	/**
	 * 调用此方法的对象
	 */
	private Object target;
	/**
	 * 调用此方法的方法
	 */
	private Method method;
	/**
	 * 调用此方法的方法的参数名称
	 */
	private String[] argNames;
	/**
	 * 调用此方法的方法的参数
	 */
	private Object[] args;
	/**
	 * 调用此方法的方法的返回值
	 */
	private Object returnValue;
	/**
	 * 异常对象
	 */
	private Throwable throwable;

	public EventInvocation(JoinPoint point) {
		this.point = point;
	}
	
	public EventInvocation(JoinPoint point,Object returnValue) {
		this.point = point;
		this.returnValue = returnValue;
	}
	
	public EventInvocation(JoinPoint point,Throwable throwable) {
		this.point = point;
		this.throwable = throwable;
	}

	public JoinPoint getPoint() {
		return point;
	}

	public void setPoint(JoinPoint point) {
		this.point = point;
	}

	public Object getTarget() {
		return target;
	}

	public void setTarget(Object target) {
		this.target = target;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public String[] getArgNames() {
		return argNames;
	}

	public void setArgNames(String[] argNames) {
		this.argNames = argNames;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	public Object getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(Object returnValue) {
		this.returnValue = returnValue;
	}

	public Throwable getThrowable() {
		return throwable;
	}

	public void setThrowable(Throwable throwable) {
		this.throwable = throwable;
	}
	
}
