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

import java.util.Map;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.MessageSource;
import org.springframework.enhanced.context.MultipleMessageSource;

public class MultipleMessageSourceFactory extends EnhancedBeanFactory implements FactoryBean<MessageSource> {

	@Override
	public void afterPropertiesSet() throws Exception {
		
	}

	@Override
	public MessageSource getObject() throws Exception {
		// 获取所有资源对象的子类实现
		Map<String, MessageSource> sourceMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(getApplicationContext(), MessageSource.class);
		// 转成数组
		MessageSource[] sources = sourceMap.values().toArray(new MessageSource[sourceMap.size()]);
		// 返回整合后的资源对象
		return new MultipleMessageSource(sources);
	}

	@Override
	public Class<?> getObjectType() {
		return MessageSource.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
