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


import org.apache.struts2.impl.StrutsActionProxy;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;

@SuppressWarnings("serial")
public class InstrumentStrutsActionProxy extends StrutsActionProxy {

	protected MetricRegistry registry = null;
	
	public InstrumentStrutsActionProxy(MetricRegistry registry, ActionInvocation inv, String namespace, String actionName, String methodName,
			boolean executeResult, boolean cleanupContext) {
		super(inv, namespace, actionName, methodName, executeResult, cleanupContext);
		this.registry = registry;
	}
	
	@Override
	public String execute() throws Exception {
		
        ActionContext previous = ActionContext.getContext();
        ActionContext.setContext(invocation.getInvocationContext());

		String prefix = MetricRegistry.name(invocation.getProxy().getNamespace(), invocation.getProxy().getActionName(),
				invocation.getProxy().getMethod());

		Counter runCounter = registry.counter(MetricRegistry.name(prefix, "invoked"));
		Counter successCounter = registry.counter(MetricRegistry.name(prefix, "successful"));
		Counter failCounter = registry.counter(MetricRegistry.name(prefix, "failed"));

    	//访问记录统计
    	runCounter.inc();
    	//访问速率统计
    	Timer timer = registry.timer(prefix);
        final Timer.Context ctx = timer.time();
        try {
            //执行目标对象方法
            String ret = invocation.invoke();
            //成功记录统计
            successCounter.inc();
            return ret;
        } catch (Exception e){
        	//失败记录统计
        	failCounter.inc();
        	throw e;
        } finally {
        	ctx.stop();
            if (cleanupContext){
                ActionContext.setContext(previous);
            }
        }
    }
	
	@Override
	protected void prepare() {
		super.prepare();
	}

}
