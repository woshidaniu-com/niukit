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

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.enhanced.proxy.EnhancedProxy;
import org.springframework.enhanced.utils.StringUtils;

public class EnhancedBeanScannerConfigurer implements BeanDefinitionRegistryPostProcessor {
    /**
     * ,; \t\n
     */
    private Class<?> kInterface;
    private String basePackage;
 
    public String getBasePackage() {
        return basePackage;
    }
 
     
    public void setkInterface(Class<?> kInterface) {
        this.kInterface = kInterface;
    }
 
 
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        registerRequestProxyHandler(registry);
        EnhancedClassPathMapperScanner scanner = new EnhancedClassPathMapperScanner(registry,kInterface);
        scanner.scan(StringUtils.tokenizeToStringArray(this.basePackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
    }
 
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    }
 
    /**
     * RequestProxyHandler 手工注册代理类,减去了用户配置XML的烦恼
     * @param registry
     */
    private void registerRequestProxyHandler(BeanDefinitionRegistry registry) {
        GenericBeanDefinition requestProxyDefinition = new GenericBeanDefinition();
        requestProxyDefinition.setBeanClass(EnhancedProxy.class);
        registry.registerBeanDefinition("kingProxy", requestProxyDefinition);
    }
 
    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }
}