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
package org.springframework.enhanced.factory;

import java.lang.reflect.Proxy;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.enhanced.proxy.EnhancedProxy;

@SuppressWarnings("unchecked")
public class EnhancedProxyBeanFactory<T> implements FactoryBean<T>{
	
	private Class<T> kInterface;
	 
    private EnhancedProxy proxy;

    @Override
    public T getObject() throws Exception {
        return newInstance();
    }

    @Override
    public Class<?> getObjectType() {
        return kInterface;
    }

    public EnhancedProxy getProxy() {
        return proxy;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    private T newInstance() {
        return (T) Proxy.newProxyInstance(kInterface.getClassLoader(), new Class[] { kInterface }, proxy);
    }

    public void setProxy(EnhancedProxy proxy) {
        this.proxy = proxy;
    }

    public Class<T> getkInterface() {
        return kInterface;
    }

    public void setkInterface(Class<T> kInterface) {
        this.kInterface = kInterface;
    }
        
}