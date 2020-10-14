package com.codahale.metrics.biz.http.listener;

import javax.servlet.ServletRequestAttributeEvent;
import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.biz.MetricsFactory;

public class HttpServletRequestMetricsListener implements ServletRequestListener,ServletRequestAttributeListener {

	/**
     * 实例化一个registry，最核心的一个模块，相当于一个应用程序的metrics系统的容器，维护一个Map
     */
	protected MetricRegistry registry = MetricsFactory.getMetricRegistry("http-request");

	
	@Override
	public void requestInitialized(ServletRequestEvent requestEvent) {

		if (!(requestEvent.getServletRequest() instanceof HttpServletRequest)) {
			throw new IllegalArgumentException( "Request is not an HttpServletRequest: " + requestEvent.getServletRequest());
		}
		HttpServletRequest oRequest = (HttpServletRequest) requestEvent.getServletRequest();
		
		// uri去掉web上下文
		String uri = oRequest.getRequestURI().substring(oRequest.getContextPath().length());
		String prefix = MetricRegistry.name(this.getClass(), uri );
		/*
	     * 实例化一个Meter
	     */
		registry.meter(prefix).mark();
		
	}
	
	@Override
	public void attributeAdded(ServletRequestAttributeEvent srae) {
		// TODO Auto-generated method stub

	}

	@Override
	public void attributeRemoved(ServletRequestAttributeEvent srae) {
		// TODO Auto-generated method stub

	}

	@Override
	public void attributeReplaced(ServletRequestAttributeEvent srae) {
		// TODO Auto-generated method stub

	}

	@Override
	public void requestDestroyed(ServletRequestEvent event) {
		
	}
	
}
