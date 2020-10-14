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
package org.springframework.enhanced.proxy;


import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
/**
 * 
 * @className	： EnhancedProxy
 * @description	： 
 * <p class="detail">
 * 功能：代理类：统一处理时间——约定大于配置
 * </p>
 * @author 		：大康（743）
 * @date		： 2017年4月18日 下午9:05:49
 * @version 	V1.0
 */
public class EnhancedProxy implements InvocationHandler, Serializable {
 
    private static final long serialVersionUID = 1993713162421775843L;
 
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (!Object.class.equals(method.getDeclaringClass())) {
            try {
                return method.invoke(this, args);
            } catch (Exception e) {
                throw e;
            }
        }
        /**
         * 统一处理事情
         * */
        return null;
    }
 
}