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
package org.springframework.enhanced.web.servlet;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceEditor;
import org.springframework.core.io.ResourceLoader;
import org.springframework.enhanced.utils.StringUtils;
import org.springframework.util.Assert;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.support.ServletContextResourceLoader;
import org.springframework.web.context.support.StandardServletEnvironment;
import org.springframework.web.util.NestedServletException;

@SuppressWarnings("serial")
public abstract class GenericServletBean extends HttpServlet implements BeanNameAware, EnvironmentAware, ServletContextAware, InitializingBean, DisposableBean {

	/** Logger available to subclasses */
	protected final Logger LOG = LoggerFactory.getLogger(getClass());
	
	/**
	 * Set of required properties (Strings) that must be supplied as config parameters to this Servlet.
	 */
	private final Set<String> requiredProperties = new HashSet<String>();
	
	private ServletConfig servletConfig;
	
	private String beanName;
	
	private Environment environment = new StandardServletEnvironment();
	
	private ServletContext servletContext;
	
	
	/**
	 * Stores the bean name as defined in the Spring bean factory.
	 * <p>Only relevant in case of initialization as bean, to have a name as
	 * fallback to the Servlet name usually provided by a ServletConfig instance.
	 * @see org.springframework.enhanced.beans.factory.BeanNameAware
	 * @see #getServletName()
	 */
	public final void setBeanName(String beanName) {
		this.beanName = beanName;
	}
	
	/**
	 * {@inheritDoc}
	 * <p>Any environment set here overrides the {@link StandardServletEnvironment}
	 * provided by default.
	 * <p>This {@code Environment} object is used only for resolving placeholders in
	 * resource paths passed into init-parameters for this Servlet. If no init-params are
	 * used, this {@code Environment} can be essentially ignored.
	 * @see #init(ServletConfig)
	 */
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}
	
	/**
	 * Stores the ServletContext that the bean factory runs in.
	 * <p>Only relevant in case of initialization as bean, to have a ServletContext
	 * as fallback to the context usually provided by a ServletConfig instance.
	 * @see org.springframework.enhanced.web.context.ServletContextAware
	 * @see #getServletContext()
	 */
	public final void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	
	/**
	 * Calls the <code>initServletBean()</code> method that might
	 * contain custom initialization of a subclass.
	 * <p>Only relevant in case of initialization as bean, where the
	 * standard <code>init(ServletConfig)</code> method won't be called.
	 * @see #initServletBean()
	 * @see #init(javax.servlet.ServletConfig)
	 */
	public void afterPropertiesSet() throws ServletException {
		initServletBean();
	}
	
	
	/**
	 * Subclasses can invoke this method to specify that this property
	 * (which must match a JavaBean property they expose) is mandatory,
	 * and must be supplied as a config parameter. This should be called
	 * from the constructor of a subclass.
	 * <p>This method is only relevant in case of traditional initialization
	 * driven by a ServletConfig instance.
	 * @param property name of the required property
	 */
	protected final void addRequiredProperty(String property) {
		this.requiredProperties.add(property);
	}
	
	/**
	 * Standard way of initializing this Servlet.
	 * Map config parameters onto bean properties of this Servlet, and
	 * invoke subclass initialization.
	 * @param ServletConfig the configuration for this Servlet
	 * @throws ServletException if bean properties are invalid (or required
	 * properties are missing), or if subclass initialization fails.
	 * @see #initServletBean
	 */
	public final void init(ServletConfig servletConfig) throws ServletException {
		Assert.notNull(servletConfig, "ServletConfig must not be null");
		if (LOG.isDebugEnabled()) {
			LOG.debug("Initializing Servlet '" + servletConfig.getServletName() + "'");
		}
	
		this.servletConfig = servletConfig;
	
		// Set bean properties from init parameters.
		try {
			PropertyValues pvs = new ServletConfigPropertyValues(servletConfig, this.requiredProperties);
			BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(this);
			ResourceLoader resourceLoader = new ServletContextResourceLoader(servletConfig.getServletContext());
			bw.registerCustomEditor(Resource.class, new ResourceEditor(resourceLoader, this.environment));
			initBeanWrapper(bw);
			bw.setPropertyValues(pvs, true);
		}
		catch (BeansException ex) {
			String msg = "Failed to set bean properties on Servlet '" + servletConfig.getServletName() + "': " + ex.getMessage();
			LOG.error(msg, ex);
			throw new NestedServletException(msg, ex);
		}
	
		// Let subclasses do whatever initialization they like.
		initServletBean();
	
		if (LOG.isDebugEnabled()) {
			LOG.debug("Servlet '" + servletConfig.getServletName() + "' configured successfully");
		}
	}
	
	/**
	 * Initialize the BeanWrapper for this GenericServletBean, possibly with custom editors.
	 * <p>This default implementation is empty.
	 * @param bw the BeanWrapper to initialize
	 * @throws BeansException if thrown by BeanWrapper methods
	 * @see org.springframework.enhanced.beans.BeanWrapper#registerCustomEditor
	 */
	protected void initBeanWrapper(BeanWrapper bw) throws BeansException {
	}
	
	
	/**
	 * Make the ServletConfig of this Servlet available, if any.
	 * Analogous to GenericServlet's <code>getServletConfig()</code>.
	 * <p>Public to resemble the <code>getServletConfig()</code> method
	 * of the Servlet Servlet version that shipped with WebLogic 6.1.
	 * @return the ServletConfig instance, or <code>null</code> if none available
	 * @see javax.servlet.GenericServlet#getServletConfig()
	 */
	public final ServletConfig getServletConfig() {
		return this.servletConfig;
	}
	
	/**
	 * Make the name of this filter available to subclasses.
	 * Analogous to GenericServlet's <code>getServletName()</code>.
	 * <p>Takes the ServletConfig's filter name by default.
	 * If initialized as bean in a Spring application context,
	 * it falls back to the bean name as defined in the bean factory.
	 * @return the filter name, or <code>null</code> if none available
	 * @see javax.servlet.GenericServlet#getServletName()
	 * @see javax.servlet.ServletConfig#getFilterName()
	 * @see #setBeanName
	 */
	public String getServletName() {
		return (this.servletConfig != null ? this.servletConfig.getServletName() : this.beanName);
	}

	/**
	 * Make the ServletContext of this filter available to subclasses.
	 * Analogous to GenericServlet's <code>getServletContext()</code>.
	 * <p>Takes the ServletConfig's ServletContext by default.
	 * If initialized as bean in a Spring application context,
	 * it falls back to the ServletContext that the bean factory runs in.
	 * @return the ServletContext instance, or <code>null</code> if none available
	 * @see javax.servlet.GenericServlet#getServletContext()
	 * @see javax.servlet.ServletConfig#getServletContext()
	 * @see #setServletContext
	 */
	public ServletContext getServletContext() {
		return (this.servletConfig != null ? this.servletConfig.getServletContext() : this.servletContext);
	}
	
	/**
	 * Subclasses may override this to perform custom initialization.
	 * All bean properties of this Servlet will have been set before this
	 * method is invoked.
	 * <p>Note: This method will be called from standard Servlet initialization
	 * as well as Servlet bean initialization in a Spring application context.
	 * Servlet name and ServletContext will be available in both cases.
	 * <p>This default implementation is empty.
	 * @throws ServletException if subclass initialization fails
	 * @see #getServletName()
	 * @see #getServletContext()
	 */
	protected void initServletBean() throws ServletException {
	}
	
	/**
	 * Subclasses may override this to perform custom Servlet shutdown.
	 * <p>Note: This method will be called from standard Servlet destruction
	 * as well as Servlet bean destruction in a Spring application context.
	 * <p>This default implementation is empty.
	 */
	public void destroy() {
	}
	
	
	/**
	 * PropertyValues implementation created from ServletConfig init parameters.
	 */
	private static class ServletConfigPropertyValues extends MutablePropertyValues {
	
		/**
		 * Create new ServletConfigPropertyValues.
		 * @param config ServletConfig we'll use to take PropertyValues from
		 * @param requiredProperties set of property names we need, where
		 * we can't accept default values
		 * @throws ServletException if any required properties are missing
		 */
		public ServletConfigPropertyValues(ServletConfig config, Set<String> requiredProperties)
			throws ServletException {
	
			Set<String> missingProps = (requiredProperties != null && !requiredProperties.isEmpty()) ?
					new HashSet<String>(requiredProperties) : null;
	
			Enumeration<?> en = config.getInitParameterNames();
			while (en.hasMoreElements()) {
				String property = (String) en.nextElement();
				Object value = config.getInitParameter(property);
				addPropertyValue(new PropertyValue(property, value));
				if (missingProps != null) {
					missingProps.remove(property);
				}
			}
	
			// Fail if we are still missing properties.
			if (missingProps != null && missingProps.size() > 0) {
				throw new ServletException( "Initialization from ServletConfig for Servlet '" + config.getServletName() +
				    "' failed; the following required properties were missing: " +
				    StringUtils.collectionToDelimitedString(missingProps, ", "));
			}
		}
	}

}