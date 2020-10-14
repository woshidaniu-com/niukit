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
package com.woshidaniu.web.sitemesh3.config.selector;

import java.io.IOException;

import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;

import org.sitemesh.DecoratorSelector;
import org.sitemesh.config.PathBasedDecoratorSelector;
import org.sitemesh.content.Content;
import org.sitemesh.webapp.WebAppContext;

import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.web.ExtParameter;
import com.woshidaniu.web.ExtParameters;
 	
/**
 * 
 * *******************************************************************
 * @className	： ParamDecoratorSelector
 * @description	： 基于request参数decorator值进行动态定位装饰器的选择器;如果decorator参数有值，则返回"/WEB-INF/views/layouts/" + decorator + ".jsp"作为目标装饰模板页面
 * @author 		： kangzhidong
 * @date		： Jul 27, 2016 2:39:12 PM
 * @version 	V1.0 
 * *******************************************************************
 */

public class ParamDecoratorSelector extends PathBasedDecoratorSelector<WebAppContext> {
 
	protected DecoratorSelector<WebAppContext> defaultDecoratorSelector;
	protected FilterConfig filterConfig;
	
    public ParamDecoratorSelector(FilterConfig filterConfig,DecoratorSelector<WebAppContext> defaultDecoratorSelector) {
        this.defaultDecoratorSelector = defaultDecoratorSelector;
        this.filterConfig = filterConfig;
    }
 
    public String[] selectDecoratorPaths(Content content, WebAppContext context) throws IOException {
    	
    	// 为了取到每个Filter或Servlet自己的参数，需要在方调用前初始化webapp参数取值对象
		ExtParameters.initialize(filterConfig);
    	
    	// build decorator based on the request
        HttpServletRequest request = context.getRequest();
        // 根据取值参数取值
        String originalDecorator = request.getParameter(ExtParameters.getString( getFilterName(), ExtParameter.REQUEST_DECORATOR_NAME));
        
        //有参数的情况
    	if (StringUtils.isNotBlank(originalDecorator)) {
    		//安全的参数值,避免被../../WEB-INF/xxx.yyy直接获取WEB-INF内文件
    		String safeDecorator = originalDecorator;
    		if(StringUtils.isNotEmpty(safeDecorator)) {
    			safeDecorator = safeDecorator.trim();
    			safeDecorator = safeDecorator.replace("..", "");
    			safeDecorator = safeDecorator.replace("/", "");
    		}
            //按照参数值返回对应路径下面的装饰模板页码
    		//"/WEB-INF/views/layouts/" + decorator + ".jsp"
    		return new String[] { String.format(ExtParameters.getString( getFilterName(),ExtParameter.REQUEST_DECORATOR_PATH), safeDecorator) };
        }
        // Otherwise, fallback to the standard configuration
        return defaultDecoratorSelector.selectDecoratorPaths(content, context);
    }
    
	/**
	 * Make the name of this filter available to subclasses.
	 * Analogous to GenericServlet's <code>getServletName()</code>.
	 * <p>Takes the FilterConfig's filter name by default.
	 * If initialized as bean in a Spring application context,
	 * it falls back to the bean name as defined in the bean factory.
	 * @return the filter name, or <code>null</code> if none available
	 * @see javax.servlet.GenericServlet#getServletName()
	 * @see javax.servlet.FilterConfig#getFilterName()
	 * @see #setBeanName
	 */
	protected final String getFilterName() {
		return (this.filterConfig != null ? this.filterConfig.getFilterName() : this.getClass().getSimpleName());
	}
	
}