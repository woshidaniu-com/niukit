package com.codahale.metrics.biz.http.listener;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.biz.MetricsFactory;

/**
 * 
 * @className	： HttpSessionMetricsListener
 * @description	： Session创建、销毁速率监控
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 * @date		： 2017年6月11日 下午2:15:13
 * @version 	V1.0
 */
public class HttpSessionMetricsListener implements HttpSessionListener {

	/**
     * 实例化一个registry，最核心的一个模块，相当于一个应用程序的metrics系统的容器，维护一个Map
     */
	protected MetricRegistry registry = MetricsFactory.getMetricRegistry("http-session");
	
	
	@Override
	public void sessionCreated(HttpSessionEvent event) {
		
		String prefix = MetricRegistry.name(this.getClass(), event.getSession().getServletContext().getContextPath(), "session", "created" );
		registry.meter(prefix).mark();
		
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		
		String prefix = MetricRegistry.name(this.getClass(), event.getSession().getServletContext().getContextPath(), "session", "destroyed" );
		registry.meter(prefix).mark();
		
	}

}
