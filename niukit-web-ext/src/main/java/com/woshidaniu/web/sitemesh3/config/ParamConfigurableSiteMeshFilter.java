/*
 * Copyright (c) 2010-2020, kangzhidong (hnxyhcwdl1003@163.com).
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
package com.woshidaniu.web.sitemesh3.config;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import org.sitemesh.DecoratorSelector;
import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.webapp.WebAppContext;

import com.woshidaniu.web.sitemesh3.config.selector.ParamDecoratorSelector;

/**
 * 
 *@类名称	: ParamConfigurableSiteMeshFilter.java
 *@类描述	：扩展实现注入基于request参数decorator值进行动态定位装饰器的选择器
 *@创建人	：kangzhidong
 *@创建时间	：Jul 27, 2016 2:53:54 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class ParamConfigurableSiteMeshFilter extends ConfigurableSiteMeshFilter {

	public void init(FilterConfig filterConfig) throws ServletException {
		super.init(filterConfig);
	}
	
    protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
    	super.applyCustomConfiguration(builder);
        //获取原有默认配置装饰选择器
        DecoratorSelector<WebAppContext> defaultDecoratorSelector = builder.getDecoratorSelector();
        //赋给自定义装饰选择器，则自定义规则未匹配时调用默认选择器获取
        builder.setCustomDecoratorSelector(new ParamDecoratorSelector(filterConfig,defaultDecoratorSelector));
    }
}
