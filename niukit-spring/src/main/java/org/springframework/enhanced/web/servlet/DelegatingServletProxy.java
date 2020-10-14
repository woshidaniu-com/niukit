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

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * 
 * *******************************************************************
 * @className	： DelegatingServletProxy
 * @description	： TODO(描述这个类的作用)
 * @author 		： <a href="mailto:hnxyhcwdl1003@163.com">kangzhidong</a>
 * @date		： Dec 27, 2016 11:43:51 AM
 * @version 	V1.0
 * @see #setTargetBeanName
 * @see #setTargetServletLifecycle
 * @see javax.servlet.Servlet#init
 * @see javax.servlet.Servlet#destroy
 * @see #DelegatingServletProxy(Servlet)
 * @see #DelegatingServletProxy(String)
 * @see #DelegatingServletProxy(String, WebApplicationContext)
 * @see javax.servlet.ServletContext#addServlet(String, Servlet)
 * @see org.springframework.enhanced.web.WebApplicationInitializer 
 * *******************************************************************
 */
@SuppressWarnings("serial")
public class DelegatingServletProxy extends GenericServletBean {
	
	private String contextAttribute;

	private WebApplicationContext webApplicationContext;

	private String targetBeanName;

	private boolean targetServletLifecycle = true;

	private Servlet delegate;

	private final Object delegateMonitor = new Object();


	/**
	 * Create a new {@code DelegatingServletProxy}. For traditional (pre-Servlet 3.0) use in {@code web.xml}.
	 * @see #setTargetBeanName(String)
	 */
	public DelegatingServletProxy() {
	}

	/**
	 * Create a new {@code DelegatingServletProxy} with the given {@link Servlet} delegate.
	 * Bypasses entirely the need for interacting with a Spring application context,
	 * specifying the {@linkplain #setTargetBeanName target bean name}, etc.
	 * <p>For use in Servlet 3.0+ environments where instance-based registration of
	 * filters is supported.
	 * @param delegate the {@code Servlet} instance that this proxy will delegate to and
	 * manage the lifecycle for (must not be {@code null}).
	 * @see #doServlet(ServletRequest, ServletResponse, ServletChain)
	 * @see #invokeDelegate(Servlet, ServletRequest, ServletResponse, ServletChain)
	 * @see #destroy()
	 * @see #setEnvironment(org.springframework.enhanced.core.env.Environment)
	 */
	public DelegatingServletProxy(Servlet delegate) {
		Assert.notNull(delegate, "delegate Servlet object must not be null");
		this.delegate = delegate;
	}

	/**
	 * Create a new {@code DelegatingServletProxy} that will retrieve the named target bean from the Spring {@code WebApplicationContext} found in the {@code ServletContext}
	 * (either the 'root' application context or the context named by {@link #setContextAttribute}).
	 * <p>For use in Servlet 3.0+ environments where instance-based registration of filters is supported.
	 * <p>The target bean must implement the standard Servlet Servlet.
	 * @param targetBeanName name of the target filter bean to look up in the Spring application context (must not be {@code null}).
	 * @see #findWebApplicationContext()
	 * @see #setEnvironment(org.springframework.enhanced.core.env.Environment)
	 */
	public DelegatingServletProxy(String targetBeanName) {
		this(targetBeanName, null);
	}

	/**
	 * Create a new {@code DelegatingServletProxy} that will retrieve the named target
	 * bean from the given Spring {@code WebApplicationContext}.
	 * <p>For use in Servlet 3.0+ environments where instance-based registration of filters is supported.
	 * <p>The target bean must implement the standard Servlet Servlet interface.
	 * <p>The given {@code WebApplicationContext} may or may not be refreshed when passed
	 * in. If it has not, and if the context implements {@link ConfigurableApplicationContext},
	 * a {@link ConfigurableApplicationContext#refresh() refresh()} will be attempted before
	 * retrieving the named target bean.
	 * <p>This proxy's {@code Environment} will be inherited from the given {@code WebApplicationContext}.
	 * @param targetBeanName name of the target filter bean in the Spring application context (must not be {@code null}).
	 * @param wac the application context from which the target filter will be retrieved; if {@code null}, an application context will be looked up from {@code ServletContext} as a fallback.
	 * @see #findWebApplicationContext()
	 * @see #setEnvironment(org.springframework.enhanced.core.env.Environment)
	 */
	public DelegatingServletProxy(String targetBeanName, WebApplicationContext wac) {
		Assert.hasText(targetBeanName, "target Servlet bean name must not be null or empty");
		this.setTargetBeanName(targetBeanName);
		this.webApplicationContext = wac;
		if (wac != null) {
			this.setEnvironment(wac.getEnvironment());
		}
	}

	/**
	 * Set the name of the ServletContext attribute which should be used to retrieve the
	 * {@link WebApplicationContext} from which to load the delegate {@link Servlet} bean.
	 */
	public void setContextAttribute(String contextAttribute) {
		this.contextAttribute = contextAttribute;
	}

	/**
	 * Return the name of the ServletContext attribute which should be used to retrieve the
	 * {@link WebApplicationContext} from which to load the delegate {@link Servlet} bean.
	 */
	public String getContextAttribute() {
		return this.contextAttribute;
	}

	/**
	 * Set the name of the target bean in the Spring application context. The target bean must implement the standard Servlet 2.3 Servlet interface.
	 * <p>By default, the {@code filter-name} as specified for the
	 * DelegatingServletProxy in {@code web.xml} will be used.
	 */
	public void setTargetBeanName(String targetBeanName) {
		this.targetBeanName = targetBeanName;
	}

	/**
	 * Return the name of the target bean in the Spring application context.
	 */
	protected String getTargetBeanName() {
		return this.targetBeanName;
	}

	/**
	 * Set whether to invoke the {@code Servlet.init} and {@code Servlet.destroy} lifecycle methods on the target bean.
	 * <p>Default is "false"; target beans usually rely on the Spring application
	 * context for managing their lifecycle. Setting this flag to "true" means
	 * that the servlet container will control the lifecycle of the target
	 * Servlet, with this proxy delegating the corresponding calls.
	 */
	public void setTargetServletLifecycle(boolean targetServletLifecycle) {
		this.targetServletLifecycle = targetServletLifecycle;
	}

	/**
	 * Return whether to invoke the {@code Servlet.init} and
	 * {@code Servlet.destroy} lifecycle methods on the target bean.
	 */
	protected boolean isTargetServletLifecycle() {
		return this.targetServletLifecycle;
	}

	@Override
	protected void initServletBean() throws ServletException {
		synchronized (this.delegateMonitor) {
			if (this.delegate == null) {
				// If no target bean name specified, use filter name.
				if (this.targetBeanName == null) {
					this.targetBeanName = getServletName();
				}

				// Fetch Spring root application context and initialize the delegate early,
				// if possible. If the root application context will be started after this
				// filter proxy, we'll have to resort to lazy initialization.
				WebApplicationContext wac = findWebApplicationContext();
				if (wac != null) {
					this.delegate = initDelegate(wac);
				}
			}
		}
	}
	 
	public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {  
		// Lazily initialize the delegate if necessary.
		Servlet delegateToUse = null;
		synchronized (this.delegateMonitor) {
			if (this.delegate == null) {
				WebApplicationContext wac = findWebApplicationContext();
				if (wac == null) {
					throw new IllegalStateException("No WebApplicationContext found: no ContextLoaderListener registered?");
				}
				this.delegate = initDelegate(wac);
			}
			delegateToUse = this.delegate;
		}

		// Let the delegate perform the actual service operation.
		invokeDelegate(delegateToUse, request, response);
    }  
	
	@Override
	public void destroy() {
		Servlet delegateToUse = null;
		synchronized (this.delegateMonitor) {
			delegateToUse = this.delegate;
		}
		if (delegateToUse != null) {
			destroyDelegate(delegateToUse);
		}
	}


	protected WebApplicationContext findWebApplicationContext() {
		if (this.webApplicationContext != null) {
			// the user has injected a context at construction time -> use it
			if (this.webApplicationContext instanceof ConfigurableApplicationContext) {
				if (!((ConfigurableApplicationContext)this.webApplicationContext).isActive()) {
					// the context has not yet been refreshed -> do so before returning it
					((ConfigurableApplicationContext)this.webApplicationContext).refresh();
				}
			}
			return this.webApplicationContext;
		}
		String attrName = getContextAttribute();
		if (attrName != null) {
			return WebApplicationContextUtils.getWebApplicationContext(getServletContext(), attrName);
		}
		else {
			return WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		}
	}

	/**
	 * Initialize the Servlet delegate, defined as bean the given Spring application context.
	 * <p>The default implementation fetches the bean from the application context and calls the standard {@code Servlet.init} method on it, passing in the ServletConfig of this Servlet proxy.
	 * @param wac the root application context
	 * @return the initialized delegate Servlet
	 * @throws ServletException if thrown by the Servlet
	 * @see #getTargetBeanName()
	 * @see #isTargetServletLifecycle()
	 * @see #getServletConfig()
	 * @see javax.servlet.Servlet#init(javax.servlet.ServletConfig)
	 */
	protected Servlet initDelegate(WebApplicationContext wac) throws ServletException {
		Servlet delegate = wac.getBean(getTargetBeanName(), Servlet.class);
		if (isTargetServletLifecycle()) {
			delegate.init(getServletConfig());
		}
		return delegate;
	}

	/**
	 * Actually invoke the delegate Servlet with the given request and response.
	 * @param delegate the delegate Servlet
	 * @param request the current HTTP request
	 * @param response the current HTTP response
	 * @throws ServletException if thrown by the Servlet
	 * @throws IOException if thrown by the Servlet
	 */
	protected void invokeDelegate(Servlet delegate, ServletRequest request, ServletResponse response)
			throws ServletException, IOException {
		delegate.service(request, response);
	}

	/**
	 * Destroy the Servlet delegate.
	 * Default implementation simply calls {@code Servlet.destroy} on it.
	 * @param delegate the Servlet delegate (never {@code null})
	 * @see #isTargetServletLifecycle()
	 * @see javax.servlet.Servlet#destroy()
	 */
	protected void destroyDelegate(Servlet delegate) {
		if (isTargetServletLifecycle()) {
			delegate.destroy();
		}
	}

	public String getServletInfo() {
		return null;
	}
	
	
}
