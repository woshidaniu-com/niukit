package com.codahale.metrics.biz.http.listener;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.biz.MetricsFactory;
import com.codahale.metrics.servlets.MetricsServlet;

public class MetricsServletContextListener extends MetricsServlet.ContextListener {

    @Override
    protected MetricRegistry getMetricRegistry() {
        return MetricsFactory.getContextMetricRegistry();
    }
    
    @Override
    protected String getAllowedOrigin() {
    	return "*";
    }
    
}