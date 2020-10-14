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


import java.util.Arrays;
import java.util.Set;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AssignableTypeFilter;

public class EnhancedClassPathMapperScanner extends ClassPathBeanDefinitionScanner  {

   public EnhancedClassPathMapperScanner(BeanDefinitionRegistry registry,Class<?> kInterface) {
       super(registry);
       addIncludeFilter(new AssignableTypeFilter(kInterface));
   }

   @Override
   public Set<BeanDefinitionHolder> doScan(String... basePackages) {
       Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);

       if (beanDefinitions.isEmpty()) {
           logger.warn("No IRequest mapper was found in '" + Arrays.toString(basePackages)+ "' package. Please check your configuration.");
       }
       GenericBeanDefinition definition;
       for (BeanDefinitionHolder holder : beanDefinitions) {
           definition = (GenericBeanDefinition) holder.getBeanDefinition();
           definition.getPropertyValues().add("proxy",getRegistry().getBeanDefinition("enhancedProxy"));
           definition.getPropertyValues().add("kInterface", definition.getBeanClassName());
           definition.setBeanClass(EnhancedProxyBeanFactory.class);
       }
       return beanDefinitions;
   }

   /**
    * 默认不允许接口的,这里重写,覆盖下
    */
   @Override
   protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
       return beanDefinition.getMetadata().isInterface()
              && beanDefinition.getMetadata().isIndependent();
   }
}