/*
 * Copyright (c) 2010-2020, vindell (https://github.com/vindell).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.codahale.metrics.webmvc.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer.Context;
import com.codahale.metrics.biz.MetricsFactory;

public class InstrumentPerformanceInterceptor implements HandlerInterceptor {

	protected MetricsFactory metricsFactory;
	protected Context invoked,rendered;
	
    /**
     * Set the metrics factory
     */
    @Autowired
    public void setMetricsFactory(MetricsFactory metricsFactory) {
    	this.metricsFactory = metricsFactory;
	}
    
    /**
	 * 该方法将在请求处理之前进行调用，只有该方法返回true，才会继续执行后续的Interceptor和Controller，当返回值为true
	 * 时就会继续调用下一个Interceptor的preHandle 方法，如果已经是最后一个Interceptor的时候就会是调用当前请求的Controller方法；
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		// uri去掉web上下文
		String uri = request.getRequestURI().substring(request.getContextPath().length());
		invoked = metricsFactory.getTimer(MetricRegistry.name(this.getClass(), uri, "method.invoked" )).time();
		
		return true;
	}

	/**
	 * 该方法将在请求处理之后，DispatcherServlet进行视图返回渲染之前进行调用，可以在这个方法中对Controller 处理之后的ModelAndView 对象进行操作。
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
		try {
			
		} catch (Exception e) {
			
		} finally {
			invoked.close();
		}

		// uri去掉web上下文
		String uri = request.getRequestURI().substring(request.getContextPath().length());
		rendered = metricsFactory.getTimer(MetricRegistry.name(this.getClass(), uri, "view.rendered" )).time();
		
	}

	/**
	 * 该方法也是需要当前对应的Interceptor的preHandle方法的返回值为true时才会执行，该方法将在整个请求结束之后，也就是在DispatcherServlet 渲染了对应的视图之后执行。用于进行资源清理。
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		try {
			
		} catch (Exception e) {
			
		} finally {
			rendered.close();
		}
		
	}

}
