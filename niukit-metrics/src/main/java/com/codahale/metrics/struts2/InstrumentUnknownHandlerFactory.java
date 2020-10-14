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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SharedMetricRegistries;
import com.codahale.metrics.Timer;
import com.opensymphony.xwork2.UnknownHandler;
import com.opensymphony.xwork2.factory.UnknownHandlerFactory;
import com.opensymphony.xwork2.inject.Container;
import com.opensymphony.xwork2.inject.Inject;

public class InstrumentUnknownHandlerFactory implements UnknownHandlerFactory {

	protected Container container;
	protected String metricRegistry;
	protected MetricRegistry registry = null;
	
	public InstrumentUnknownHandlerFactory() {
		this.registry = SharedMetricRegistries.getOrCreate(getMetricRegistry());
	}
	
	public String getMetricRegistry() {
		return metricRegistry;
	}

	@Inject("metric-registry-name")
	public void setMetricRegistry(String metricRegistry) {
		this.metricRegistry = metricRegistry;
	}
	
    @Inject
    public void setContainer(Container container) {
        this.container = container;
    }
	
	@Override
	public UnknownHandler buildUnknownHandler(String unknownHandlerName, Map<String, Object> extraContext)
			throws Exception {
	 	//构建Action对象
    	final Object target = container.getInstance(UnknownHandler.class, unknownHandlerName);
    	final Counter runCounter = registry.counter( MetricRegistry.name(unknownHandlerName, "invoked"));
    	final Counter successCounter = registry.counter( MetricRegistry.name(unknownHandlerName, "successful"));
    	final Counter failCounter = registry.counter( MetricRegistry.name(unknownHandlerName, "failed"));
		//返回Action的代理对象
		return (UnknownHandler) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new InvocationHandler() {
                	
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    	
                    	//访问记录统计
                    	runCounter.inc();
                    	//访问速率统计
                    	String name = MetricRegistry.name(proxy.getClass(), method.getName());
                    	
                    	Timer timer = registry.timer(name);
                    	
                    	final Timer.Context ctx = timer.time();
                        try {
                             //执行目标对象方法
                             Object ret = method.invoke(target, args);
                             //成功记录统计
                             successCounter.inc();
                             return ret;
                        } catch (Throwable e){
                        	//失败记录统计
                        	failCounter.inc();
                        	throw e;
                        } 
                        finally {
                            ctx.stop();
                        }
                    }
                    
                }
        );
	}

}
