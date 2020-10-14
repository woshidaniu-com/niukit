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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;

public class HealthCheckBeanPostProcessor implements BeanPostProcessor, Ordered {

	private static final Logger LOG = LoggerFactory.getLogger(HealthCheckBeanPostProcessor.class);

	private final HealthCheckRegistry healthChecks;

	public HealthCheckBeanPostProcessor(final HealthCheckRegistry healthChecks) {
		this.healthChecks = healthChecks;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) {
		return bean;
	}
	
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) {
		if (bean instanceof HealthCheck) {
			healthChecks.register(beanName, (HealthCheck) bean);
			LOG.debug("Registering HealthCheck bean {}", beanName);
		}
		return bean;
	}

	@Override
	public int getOrder() {
		return LOWEST_PRECEDENCE;
	}

}
