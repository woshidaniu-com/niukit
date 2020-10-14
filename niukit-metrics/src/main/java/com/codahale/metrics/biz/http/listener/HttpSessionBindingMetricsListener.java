package com.codahale.metrics.biz.http.listener;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.biz.MetricsFactory;

public class HttpSessionBindingMetricsListener implements HttpSessionBindingListener {

	/**
     * 实例化一个registry，最核心的一个模块，相当于一个应用程序的metrics系统的容器，维护一个Map
     */
	protected MetricRegistry registry = MetricsFactory.getMetricRegistry("http-session-binding");
	
	@Override
	public void valueBound(HttpSessionBindingEvent event) {
		
		String prefix = MetricRegistry.name(this.getClass(), event.getSession().getServletContext().getContextPath(), "session", "valueBound" );
		registry.meter(prefix).mark();
		
	}

	@Override
	public void valueUnbound(HttpSessionBindingEvent event) {
		
		String prefix = MetricRegistry.name(this.getClass(), event.getSession().getServletContext().getContextPath(), "session", "valueUnbound" );
		registry.meter(prefix).mark();
		
	}

}
