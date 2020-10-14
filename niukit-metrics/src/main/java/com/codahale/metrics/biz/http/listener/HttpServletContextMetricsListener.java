package com.codahale.metrics.biz.http.listener;

import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SharedMetricRegistries;
import com.codahale.metrics.biz.MetricsFactory;

/**
 * @className	： ServletContextMetricsListener
 * @description	： ServletContext上下文属性绑定、移除、更新速率监控
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 * @date		： 2017年6月11日 下午2:19:06
 * @version 	V1.0
 */
public class HttpServletContextMetricsListener implements ServletContextAttributeListener, ServletContextListener {

	/**
     * 实例化一个registry，最核心的一个模块，相当于一个应用程序的metrics系统的容器，维护一个Map
     */
	protected MetricRegistry registry = null;
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		this.registry = MetricsFactory.getContextMetricRegistry();
		event.getServletContext().setAttribute(MetricsFactory.SERVLET_CONTEXT_METRIC_REGISTRY, this.registry);
	}
	
	@Override
	public void attributeAdded(ServletContextAttributeEvent event) {
		
		String prefix = MetricRegistry.name(this.getClass(), event.getServletContext().getContextPath(), "ServletContext", "attributeAdded" );
		registry.meter(prefix).mark();

	}

	@Override
	public void attributeRemoved(ServletContextAttributeEvent event) {

		String prefix = MetricRegistry.name(this.getClass(), event.getServletContext().getContextPath(), "ServletContext", "attributeRemoved" );
		registry.meter(prefix).mark();

	}

	@Override
	public void attributeReplaced(ServletContextAttributeEvent event) {
		
		String prefix = MetricRegistry.name(this.getClass(), event.getServletContext().getContextPath(), "ServletContext", "attributeReplaced" );
		registry.meter(prefix).mark();

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		//清除MetricRegistry
		SharedMetricRegistries.clear();
	}

}
