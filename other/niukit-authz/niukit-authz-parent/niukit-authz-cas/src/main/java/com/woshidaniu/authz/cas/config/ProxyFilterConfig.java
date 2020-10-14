/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.cas.config;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;


/**
 * 
 * @className	： ProxyFilterConfig
 * @description	： 当需要代理创建Filter时，使用这个类来填充我们的相关配置，然后调用被代理Filter对象的 init方法,方法参数就是这个类的实例对象
 * @author 		：康康（1571）
 * @date		： 2018年4月24日 上午10:10:03
 * @version 	V1.0
 */
public class ProxyFilterConfig implements FilterConfig{
		
	
		private Map<String,String> paramMap;
		private String filterName;
		private ServletContext servletContext;
		
		
		public ProxyFilterConfig(ServletContext servletContext,String filterName) {
			this.servletContext = servletContext;
			this.filterName = filterName;
			this.paramMap = new HashMap<String,String>();
		}

		public ProxyFilterConfig(FilterConfig delegate) {
			super();
			this.filterName = delegate.getFilterName();
			this.servletContext = delegate.getServletContext();
			this.paramMap = new HashMap<String,String>();
			Enumeration<String> enums = delegate.getInitParameterNames();
			while(enums.hasMoreElements()) {
				String key = enums.nextElement();
				String value = delegate.getInitParameter(key);
				this.paramMap.put(key, value);
			}
		}

		@Override
		public String getFilterName() {
			return this.filterName;
		}

		@Override
		public ServletContext getServletContext() {
			return this.servletContext;
		}

		@Override
		public String getInitParameter(String name) {
			String value = this.paramMap.get(name);
			return value;
		}
		
		public void addInitParameter(String name,String value) {
			this.paramMap.put(name, value);
		}

		@Override
		public Enumeration<String> getInitParameterNames() {
			Set<String> set = this.paramMap.keySet();
			Vector<String> vector = new Vector<String>(set);
			return vector.elements();
		}
	}