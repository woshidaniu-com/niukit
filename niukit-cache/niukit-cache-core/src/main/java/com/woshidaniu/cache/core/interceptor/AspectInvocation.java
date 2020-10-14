package com.woshidaniu.cache.core.interceptor;

import java.io.Serializable;
import java.lang.reflect.Method;


public interface AspectInvocation extends Serializable {
	
	/**
     * Get the Method associated with this AspectInvocation.
     * @return the Method
     */
	Method getMethod();
  
}
