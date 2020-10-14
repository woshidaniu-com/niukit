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
package com.codahale.metrics.webmvc.utils;

import static com.codahale.metrics.MetricRegistry.name;

import java.lang.reflect.Member;
import java.lang.reflect.Method;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.codahale.metrics.annotation.CachedGauge;
import com.codahale.metrics.annotation.Counted;
import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Gauge;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Metric;
import com.codahale.metrics.annotation.Timed;

public class MetricUtils {

	private MetricUtils() {}

	public static String forTimedMethod(Class<?> klass, Member member, Timed annotation) {
		return chooseName(annotation.name(), annotation.absolute(), klass, member);
	}

	public static String forMeteredMethod(Class<?> klass, Member member, Metered annotation) {
		return chooseName(annotation.name(), annotation.absolute(), klass, member);
	}

	public static String forGauge(Class<?> klass, Member member, Gauge annotation) {
		return chooseName(annotation.name(), annotation.absolute(), klass, member);
	}

	public static String forCachedGauge(Class<?> klass, Member member, CachedGauge annotation) {
		return chooseName(annotation.name(), annotation.absolute(), klass, member);
	}

	public static String forExceptionMeteredMethod(Class<?> klass, Member member, ExceptionMetered annotation) {
		return chooseName(annotation.name(), annotation.absolute(), klass, member, ExceptionMetered.DEFAULT_NAME_SUFFIX);
	}

	public static String forCountedMethod(Class<?> klass, Member member, Counted annotation) {
		return chooseName(annotation.name(), annotation.absolute(), klass, member);
	}

	public static String forMetricMethod(Class<?> klass, Member member, Method method) {
		return chooseName(method.getName(), false, klass, member);
	}
	
	public static String forMetricField(Class<?> klass, Member member, Metric annotation) {
		return chooseName(annotation.name(), annotation.absolute(), klass, member);
	}

	public static String forMetricMethod(Class<?> klass, Member member, RequestMapping annotation, boolean absolute) {
		return chooseName(name( "*" , annotation.value() ), absolute, klass, member );
	}

	public static String forMetricMethod(Class<?> klass, Member member, GetMapping annotation, boolean absolute) {
		return chooseName(name( RequestMethod.GET.name() , annotation.value() ), absolute, klass, member );
	}
	
	public static String forMetricMethod(Class<?> klass, Member member, PostMapping annotation, boolean absolute) {
		return chooseName(name( RequestMethod.POST.name() , annotation.value() ), absolute, klass, member );
	}
	
	public static String forMetricMethod(Class<?> klass, Member member, PutMapping annotation, boolean absolute) {
		return chooseName(name( RequestMethod.PUT.name() , annotation.value() ), absolute, klass, member );
	}
	
	public static String forMetricMethod(Class<?> klass, Member member, DeleteMapping annotation, boolean absolute) {
		return chooseName(name( RequestMethod.DELETE.name() , annotation.value() ), absolute, klass, member );
	}
	
	public static String forMetricMethod(Class<?> klass, Member member, PatchMapping annotation, boolean absolute) {
		return chooseName(name( RequestMethod.PATCH.name() , annotation.value() ), absolute, klass, member );
	}

	static String chooseName(String explicitName, boolean absolute, Class<?> klass, Member member, String... suffixes) {
		if (explicitName != null && !explicitName.isEmpty()) {
			if (absolute) {
				return explicitName;
			}
			return name(klass.getCanonicalName(), explicitName);
		}
		return name(name(klass.getCanonicalName(), member.getName()), suffixes);
	}
	
}
