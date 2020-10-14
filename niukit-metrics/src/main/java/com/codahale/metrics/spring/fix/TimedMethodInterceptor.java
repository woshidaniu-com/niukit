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
import org.springframework.core.Ordered;
import org.springframework.util.ReflectionUtils.MethodFilter;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.codahale.metrics.Timer.Context;
import com.codahale.metrics.annotation.Timed;
import com.codahale.metrics.webmvc.aspect.AnnotationFilter;
import com.codahale.metrics.webmvc.utils.MetricUtils;

public class TimedMethodInterceptor extends AbstractMetricMethodInterceptor<Timed, Timer> implements Ordered {

	public static final Class<Timed> ANNOTATION = Timed.class;
	public static final Pointcut POINTCUT = new AnnotationMatchingPointcut(null, ANNOTATION);
	public static final MethodFilter METHOD_FILTER = new AnnotationFilter(ANNOTATION, AnnotationFilter.PROXYABLE_METHODS);

	public TimedMethodInterceptor(final MetricRegistry metricRegistry) {
		super(metricRegistry, ANNOTATION, METHOD_FILTER);
	}

	@Override
	protected Object invoke(MethodInvocation invocation, Timer timer, Timed annotation) throws Throwable {
		final Context timerCtx = timer.time();
		try {
			return invocation.proceed();
		}
		finally {
			timerCtx.close();
		}
	}

	@Override
	protected Timer buildMetric(MetricRegistry metricRegistry, String metricName, Timed annotation) {
		return metricRegistry.timer(metricName);
	}

	@Override
	protected String buildMetricName(Class<?> targetClass, Method method, Timed annotation) {
		return MetricUtils.forTimedMethod(targetClass, method, annotation);
	}

	@Override
	public int getOrder() {
		return HIGHEST_PRECEDENCE;
	}

}
