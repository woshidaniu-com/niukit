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
package com.codahale.metrics.biz.event.listener;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.biz.MetricsFactory;
import com.codahale.metrics.biz.event.BizCountedEvent;
import com.codahale.metrics.biz.event.BizEventPoint;

@Component
public class BizCountedEventListener extends BizMetricEventListener<BizCountedEvent> {

	protected MetricRegistry metricRegistry;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if(getMetricsFactory() != null){
			metricRegistry = getMetricsFactory().getRegistry();
		} else {
			metricRegistry = MetricsFactory.getMeterMetricRegistry();
		}
	}
	
	@Async
	@Override
	public void onApplicationEvent(BizCountedEvent event) {
		
		//获取绑定数据对象
		BizEventPoint data = event.getBind();
		//计算当前事件的唯一度量名称
		String name = MetricRegistry.name(event.getSource().getClass(), data.getName());
		//增加一次计数
		getMetricRegistry().counter(name).inc();
		
	}

	public MetricRegistry getMetricRegistry() {
		return metricRegistry;
	}

}
