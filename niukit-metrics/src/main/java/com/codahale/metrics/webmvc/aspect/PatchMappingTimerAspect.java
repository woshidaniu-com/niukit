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
package com.codahale.metrics.webmvc.aspect;


import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.util.ReflectionUtils.MethodFilter;
import org.springframework.web.bind.annotation.PatchMapping;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.codahale.metrics.Timer.Context;
import com.codahale.metrics.webmvc.utils.MetricUtils;

public class PatchMappingTimerAspect extends AbstractMetricMethodAspect<PatchMapping, Timer> {

	public static final Class<PatchMapping> ANNOTATION = PatchMapping.class;
	public static final Pointcut POINTCUT = new AnnotationMatchingPointcut(null, ANNOTATION);
	public static final MethodFilter METHOD_FILTER = new AnnotationFilter(ANNOTATION, AnnotationFilter.PROXYABLE_METHODS);
	
	public PatchMappingTimerAspect(final MetricRegistry metricRegistry) {
		this(metricRegistry, false);
	}
	
	public PatchMappingTimerAspect(final MetricRegistry metricRegistry, boolean absolute) {
		super(metricRegistry, absolute, ANNOTATION, METHOD_FILTER);
	}

	@Override
	protected Object invoke(ProceedingJoinPoint joinPoint, Timer timer, PatchMapping annotation) throws Throwable {
		final Context timerCtx = timer.time();
		try {
			return joinPoint.proceed();
		}
		finally {
			timerCtx.close();
		}
	}

	@Override
	protected Timer buildMetric(MetricRegistry metricRegistry, String metricName, PatchMapping annotation) {
		return metricRegistry.timer(metricName);
	}

	@Override
	protected String buildMetricName(Class<?> targetClass, Method method, PatchMapping annotation, boolean absolute) {
		return MetricUtils.forMetricMethod(targetClass, method, annotation, absolute);
	}
	

}
