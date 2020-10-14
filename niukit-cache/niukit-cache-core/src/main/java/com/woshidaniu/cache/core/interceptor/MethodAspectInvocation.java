package com.woshidaniu.cache.core.interceptor;

import java.lang.reflect.Method;

@SuppressWarnings("serial")
public class MethodAspectInvocation implements AspectInvocation {

	private Method aspectMethod;
	public MethodAspectInvocation(Method aspectMethod){
		this.aspectMethod = aspectMethod;
	}
	
	@Override
	public Method getMethod() {
		return aspectMethod;
	}

}
