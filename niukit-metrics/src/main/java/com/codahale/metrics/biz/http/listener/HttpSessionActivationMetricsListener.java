package com.codahale.metrics.biz.http.listener;

import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionEvent;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.biz.MetricsFactory;

/**
 * 
 * @className	： HttpSessionActivationMetricsListener
 * @description	：Session激活钝化速率监控
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 * @date		： 2017年6月11日 下午2:07:55
 * @version 	V1.0
 */
public class HttpSessionActivationMetricsListener implements HttpSessionActivationListener {

	/**
     * 实例化一个registry，最核心的一个模块，相当于一个应用程序的metrics系统的容器，维护一个Map
     */
	protected MetricRegistry registry = MetricsFactory.getMetricRegistry("http-session-activation");
	
	@Override
	public void sessionWillPassivate(HttpSessionEvent event) {
		
		String prefix = MetricRegistry.name(this.getClass(), event.getSession().getServletContext().getContextPath(), "session", "willPassivate" );
		registry.meter(prefix).mark();
		
	}

	@Override
	public void sessionDidActivate(HttpSessionEvent event) {

		String prefix = MetricRegistry.name(this.getClass(), event.getSession().getServletContext().getContextPath(), "session", "didActivate" );
		registry.meter(prefix).mark();
		
	}

}
