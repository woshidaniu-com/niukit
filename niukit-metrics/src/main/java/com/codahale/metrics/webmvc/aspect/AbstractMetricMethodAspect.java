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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.MethodCallback;
import org.springframework.util.ReflectionUtils.MethodFilter;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.webmvc.utils.AspectUtils;

public abstract class AbstractMetricMethodAspect<A extends Annotation, M> implements MethodCallback {

	protected final Logger LOG = LoggerFactory.getLogger(getClass());

	protected final MetricRegistry metricRegistry;
	protected final MethodFilter methodFilter;
	protected final Class<A> annotationClass;
	protected final boolean absolute;
	protected final ConcurrentMap<MethodKey, AnnotationMetricPair<A, M>> metrics;

	protected AbstractMetricMethodAspect(final MetricRegistry metricRegistry, final boolean absolute, final Class<A> annotationClass, 
			final MethodFilter methodFilter) {
		this.metricRegistry = metricRegistry;
		this.absolute  = absolute;
		this.annotationClass = annotationClass;
		this.methodFilter = methodFilter;
		this.metrics = new ConcurrentHashMap<MethodKey, AnnotationMetricPair<A, M>>();
		LOG.debug("Scanning for @{} annotated methods", annotationClass.getSimpleName());
	}

	public void doBefore(JoinPoint point) throws Throwable {
		Class<?> targetClass = point.getTarget().getClass();
		LOG.debug("Creating method interceptor for class {}", targetClass.getCanonicalName());
		ReflectionUtils.doWithMethods(targetClass, this, methodFilter);
	}
	
	public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {  
		Method method = AspectUtils.getMethod(joinPoint);
		final AnnotationMetricPair<A, M> annotationMetricPair = metrics.get(MethodKey.forMethod(method));
		if (annotationMetricPair != null) {
			return invoke(joinPoint, annotationMetricPair.getMeter(), annotationMetricPair.getAnnotation());
		}
		else {
			return joinPoint.proceed();
		}
    }

	@Override
	public void doWith(Method method) throws IllegalAccessException {
		final A annotation = method.getAnnotation(annotationClass);
		if (annotation != null) {

			final MethodKey methodKey = MethodKey.forMethod(method);
			AnnotationMetricPair<A, M> ret = metrics.get(methodKey);
			if( ret != null){
				return;
			}
			
			final Class<?> targetClass = method.getDeclaringClass();
			final String metricName = buildMetricName(targetClass, method, annotation, absolute);
			final M metric = buildMetric(metricRegistry, metricName, annotation);
			if (metric != null) {
				metrics.putIfAbsent(methodKey, new AnnotationMetricPair<A, M>(annotation, metric));
				if (LOG.isDebugEnabled()) {
					LOG.debug("Created {} {} for method {}", metric.getClass().getSimpleName(), metricName, methodKey);
				}
			}
		}
	}

	protected abstract String buildMetricName(Class<?> targetClass, Method method, A annotation, boolean absolute);

	protected abstract M buildMetric(MetricRegistry metricRegistry, String metricName, A annotation);

	protected abstract Object invoke(ProceedingJoinPoint joinPoint, M metric, A annotation) throws Throwable;

	public static final class AnnotationMetricPair<A extends Annotation, M> {

		private final A annotation;
		private final M meter;

		public AnnotationMetricPair(final A annotation, final M meter) {
			this.annotation = annotation;
			this.meter = meter;
		}

		public A getAnnotation() {
			return annotation;
		}

		public M getMeter() {
			return meter;
		}

	}

}