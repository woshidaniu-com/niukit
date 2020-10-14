package com.codahale.metrics.biz.http.listener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.codahale.metrics.biz.MetricsFactory;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.servlets.HealthCheckServlet;

/**
 * 
 * @className	： HealthCheckServletContextListener
 * @description	： TODO(描述这个类的作用)
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 * @date		： 2017年6月14日 下午12:34:42
 * @version 	V1.0
 * http://blog.csdn.net/bairrfhoinn/article/details/16848785
 */
public class HealthCheckServletContextListener extends HealthCheckServlet.ContextListener{

    @Override
    protected HealthCheckRegistry getHealthCheckRegistry() {
        return MetricsFactory.getContextHealthCheckRegistry();
    }
    
    @Override
    protected ExecutorService getExecutorService() {
    	return Executors.newScheduledThreadPool(10);
    }
    
}
