package com.codahale.metrics.biz.http.servlet;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.biz.MetricsFactory;

public class InstrumentedFilterContextListener extends com.codahale.metrics.servlet.InstrumentedFilterContextListener {
	
    @Override
    protected MetricRegistry getMetricRegistry() {
        return MetricsFactory.getContextMetricRegistry();
    }

}
