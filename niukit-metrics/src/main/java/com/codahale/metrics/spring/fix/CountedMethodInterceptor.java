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
package com.codahale.metrics.spring.fix;


import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.util.ReflectionUtils.MethodFilter;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.annotation.Counted;
import com.codahale.metrics.webmvc.aspect.AnnotationFilter;
import com.codahale.metrics.webmvc.utils.MetricUtils;

public class CountedMethodInterceptor extends AbstractMetricMethodInterceptor<Counted, Counter> {

	public static final Class<Counted> ANNOTATION = Counted.class;
	public static final Pointcut POINTCUT = new AnnotationMatchingPointcut(null, ANNOTATION);
	public static final MethodFilter METHOD_FILTER = new AnnotationFilter(ANNOTATION, AnnotationFilter.PROXYABLE_METHODS);
	public CountedMethodInterceptor(final MetricRegistry metricRegistry) {
		super(metricRegistry, ANNOTATION, METHOD_FILTER);
	}

	@Override
	protected Object invoke(MethodInvocation invocation, Counter counter, Counted annotation) throws Throwable {
		try {
			counter.inc();
			return invocation.proceed();
		}
		finally {
			if (!annotation.monotonic()) {
				counter.dec();
			}
		}
	}

	@Override
	protected Counter buildMetric(MetricRegistry metricRegistry, String metricName, Counted annotation) {
		return metricRegistry.counter(metricName);
	}

	@Override
	protected String buildMetricName(Class<?> targetClass, Method method, Counted annotation) {
		return MetricUtils.forCountedMethod(targetClass, method, annotation);
	}
	

}
