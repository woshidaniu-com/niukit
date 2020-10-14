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
package com.codahale.metrics.webmvc.handler;

import java.lang.reflect.Method;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

public class InstrumentAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

	@Override
	public void handleUncaughtException(Throwable ex, Method method, Object... params) {
		
		System.out.println("调用异步任务出错了, message : " + ex.getMessage());
		
		
		StackTraceElement[] st = ex.getStackTrace();
		for (StackTraceElement stackTraceElement : st) {
			String exclass = stackTraceElement.getClassName();
			String method2 = stackTraceElement.getMethodName();
			System.out.println(exclass);
			System.out.println(method2);
		}
	
		
		ExceptionUtils.getCause(ex).printStackTrace();
		
	}

}
