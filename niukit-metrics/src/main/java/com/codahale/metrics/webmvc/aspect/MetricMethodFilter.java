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

import static java.lang.reflect.Modifier.ABSTRACT;
import static java.lang.reflect.Modifier.FINAL;
import static java.lang.reflect.Modifier.PRIVATE;
import static java.lang.reflect.Modifier.STATIC;
import static org.springframework.util.ReflectionUtils.USER_DECLARED_METHODS;

import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils.MethodFilter;

public class MetricMethodFilter implements MethodFilter{

	private static final Logger LOG = LoggerFactory.getLogger(MetricMethodFilter.class);

	public static final int METHODS =
	        Modifier.PUBLIC         | Modifier.PROTECTED    | Modifier.PRIVATE |
	        Modifier.ABSTRACT       | Modifier.STATIC       | Modifier.FINAL   |
	        Modifier.SYNCHRONIZED   | Modifier.NATIVE       | Modifier.STRICT  |
	        Modifier.TRANSIENT; 
	// TRANSIENT flag is the same as the VARARGS flag

	public static final int INSTANCE_METHODS = METHODS ^ (ABSTRACT | STATIC);
	public static final int PROXYABLE_METHODS = METHODS ^ (ABSTRACT | FINAL | PRIVATE | STATIC);

	private final int methodModifiers;
	
	public MetricMethodFilter() {
		this(METHODS);
	}

	public MetricMethodFilter(final int methodModifiers) {
		this.methodModifiers = methodModifiers & METHODS;
	}
	
	@Override
	public boolean matches(Method method) {
		if (USER_DECLARED_METHODS.matches(method) ) {
			if (checkModifiers(method, methodModifiers)) {
				return true;
			}
			else {
				LOG.warn("Ignoring @{} on method {}.{} due to illegal modifiers: {}", method.getDeclaringClass().getSimpleName(), method.getDeclaringClass().getCanonicalName(),
						method.getName(), Modifier.toString(method.getModifiers() & ~methodModifiers));
			}
		}
		return false;
	}
	
	private boolean checkModifiers(Member member, int allowed) {
		int modifiers = member.getModifiers();
		return (modifiers & allowed) == modifiers;
	}

	@Override
	public String toString() {
		return "[MetricMethodFilter: @" + this.getClass().getSimpleName() + ", methodModifiers: (" + Modifier.toString(methodModifiers) + ") ]";
	}
	
}
