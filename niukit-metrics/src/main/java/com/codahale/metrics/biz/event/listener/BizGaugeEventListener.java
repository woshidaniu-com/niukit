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

import java.util.Queue;
import java.util.SortedMap;
import java.util.concurrent.LinkedBlockingDeque;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.biz.MetricsFactory;
import com.codahale.metrics.biz.event.BizEventPoint;
import com.codahale.metrics.biz.event.BizGaugeEvent;
import com.codahale.metrics.biz.filter.MetricNamedFilter;

@Component
public class BizGaugeEventListener extends BizMetricEventListener<BizGaugeEvent> {

	protected Queue<Long> queue = new LinkedBlockingDeque<Long>();

	protected MetricNamedFilter filter = new MetricNamedFilter();
	
	protected MetricRegistry metricRegistry;
	
	public MetricRegistry getMetricRegistry() {
		return metricRegistry;
	}
	
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
	@SuppressWarnings("rawtypes")
	public void onApplicationEvent(BizGaugeEvent event) {
		
		//获取绑定数据对象
		BizEventPoint data = event.getBind();
		
		queue.add(data.getValue());
		
		String metrics_key = MetricRegistry.name(event.getSource().getClass(), data.getName());
		filter.setMetrics(metrics_key);
		SortedMap<String, Gauge> gauges = getMetricRegistry().getGauges(filter);
		boolean notRegister =  (gauges == null || gauges.isEmpty());
		if(notRegister){
			
			//实例化一个Gauge
	        Gauge<Long> gauge = new Gauge<Long>() {
	        	
	            @Override
	            public Long getValue() {
	            	if(queue.size() == 1){
	            		return queue.peek();
	            	} else{
	            		return queue.poll();
	            	}
	            }
	            
	        };
	        
			//注册到容器中
	        getMetricRegistry().register(metrics_key, gauge);
			
		}
		
		
	}
	
}
