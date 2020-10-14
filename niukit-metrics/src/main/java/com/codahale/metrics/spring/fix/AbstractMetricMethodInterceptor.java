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


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.MethodCallback;
import org.springframework.util.ReflectionUtils.MethodFilter;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.webmvc.aspect.MethodKey;

public abstract class AbstractMetricMethodInterceptor<A extends Annotation, M> implements MethodInterceptor, MethodCallback, ApplicationContextAware, InitializingBean {

	protected final Logger LOG = LoggerFactory.getLogger(getClass());
	protected final MetricRegistry metricRegistry;
	protected final Class<A> annotationClass;
	protected final MethodFilter methodFilter;
	protected final ConcurrentMap<MethodKey, AnnotationMetricPair<A, M>> metrics;
	protected final ConcurrentMap<Class<?>, Object> metricObject;
	
	/** ApplicationContext this object runs in */
	protected ApplicationContext applicationContext;
	
	public AbstractMetricMethodInterceptor(final MetricRegistry metricRegistry, final Class<A> annotationClass,
			final MethodFilter methodFilter) {
		this.metricRegistry = metricRegistry;
		this.annotationClass = annotationClass;
		this.methodFilter = methodFilter;
		this.metrics = new ConcurrentHashMap<MethodKey, AnnotationMetricPair<A, M>>();
		this.metricObject = new ConcurrentHashMap<Class<?>, Object>();
		LOG.debug("Scanning for @{} annotated methods", annotationClass.getSimpleName());
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		
		Map<String, Object> beanMap1 =  getApplicationContext().getBeansWithAnnotation(Controller.class);
		if(beanMap1 != null){
			for (String key : beanMap1.keySet()) {
				Object source = beanMap1.get(key);
				/*if(AopUtils.isAopProxy(source)){
					source = AopTargetUtils.getTarget(source);
				}*/
				ReflectionUtils.doWithMethods(source.getClass(), this, methodFilter);
			}
		}
		
		Map<String, Object> beanMap2 =  getApplicationContext().getBeansWithAnnotation(RestController.class);
		if(beanMap2 != null){
			for (String key : beanMap2.keySet()) {
				Object source = beanMap1.get(key);
				/*if(AopUtils.isAopProxy(source)){
					source = AopTargetUtils.getTarget(source);
				}*/
				ReflectionUtils.doWithMethods(source.getClass(), this, methodFilter);
			}
		}
		
	}
	
	
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		final AnnotationMetricPair<A, M> annotationMetricPair = metrics.get(MethodKey.forMethod(invocation.getMethod()));
		if (annotationMetricPair != null) {
			return invoke(invocation, annotationMetricPair.getMeter(), annotationMetricPair.getAnnotation());
		}
		else {
			return invocation.proceed();
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
			final String metricName = buildMetricName(targetClass, method, annotation);
			final M metric = buildMetric(metricRegistry, metricName, annotation);
			if (metric != null) {
				metrics.putIfAbsent(methodKey, new AnnotationMetricPair<A, M>(annotation, metric));

				if (LOG.isDebugEnabled()) {
					LOG.debug("Created {} {} for method {}", metric.getClass().getSimpleName(), metricName, methodKey);
				}
			}
		}
	}

	protected abstract String buildMetricName(Class<?> targetClass, Method method, A annotation);

	protected abstract M buildMetric(MetricRegistry metricRegistry, String metricName, A annotation);

	protected abstract Object invoke(MethodInvocation invocation, M metric, A annotation) throws Throwable;

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

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

}

