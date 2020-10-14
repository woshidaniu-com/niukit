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

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeanUtils;

public class AspectUtils {

	/*
	 * 1)JoinPoint  java.lang.Object[] getArgs()：获取连接点方法运行时的入参列表；  Signature
	 * getSignature() ：获取连接点的方法签名对象；  java.lang.Object getTarget()
	 * ：获取连接点所在的目标对象；  java.lang.Object getThis() ：获取代理对象本身；
	 * 2)ProceedingJoinPoint
	 * ProceedingJoinPoint继承JoinPoint子接口，它新增了两个用于执行连接点方法的方法：  java.lang.Object
	 * proceed() throws java.lang.Throwable：通过反射执行目标对象的连接点处的方法； 
	 * java.lang.Object proceed(java.lang.Object[] args) throws
	 * java.lang.Throwable：通过反射执行目标对象连接点处的方法，不过使用新的入参替换原来的入参。
	 */
	public static Method getMethod(JoinPoint point) {
		//AntPathMatcher s = new  AntPathMatcher();
		String mathodName = point.getSignature().getName();
		Object target = point.getTarget();
		Object[] args = point.getArgs();
		Method method = null;
		if (args != null) {
			Class<?>[] paramTypes = new Class<?>[args.length];
			for (int i = 0; i < args.length; i++) {
				paramTypes[i] = (args[i] == null) ? null : args[i] .getClass();
			}
			method = BeanUtils.findDeclaredMethod(target.getClass(),mathodName, paramTypes);
		}
		if (method == null && point.getSignature() instanceof MethodSignature) {
			method = ((MethodSignature) point.getSignature()).getMethod();
		}
		return method;
	}
	
}
