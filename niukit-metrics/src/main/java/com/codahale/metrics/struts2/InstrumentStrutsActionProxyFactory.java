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
package com.codahale.metrics.struts2;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SharedMetricRegistries;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.DefaultActionProxyFactory;
import com.opensymphony.xwork2.inject.Inject;

public class InstrumentStrutsActionProxyFactory extends DefaultActionProxyFactory {

	protected String metricRegistry;
	protected MetricRegistry registry = null;
	
	public InstrumentStrutsActionProxyFactory() {
		super();
		this.registry = SharedMetricRegistries.getOrCreate(getMetricRegistry());
	}
	
	public String getMetricRegistry() {
		return metricRegistry;
	}

	@Inject("metric-registry-name")
	public void setMetricRegistry(String metricRegistry) {
		this.metricRegistry = metricRegistry;
	}
	
    @Override
    public ActionProxy createActionProxy(ActionInvocation inv, String namespace, String actionName, String methodName, boolean executeResult, boolean cleanupContext) {
        
    	InstrumentStrutsActionProxy proxy = new InstrumentStrutsActionProxy(registry , inv, namespace, actionName, methodName, executeResult, cleanupContext);
        container.inject(proxy);
        proxy.prepare();
        return proxy;
        
    }
    
}
