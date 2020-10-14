package org.springframework.enhanced.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

@SuppressWarnings("serial")
public class ModuleDispatcherServlet extends DispatcherServlet {
	
	/** Well-known name for the ModuleResolver object in the bean factory for this namespace. */
	public static final String MODULE_RESOLVER_BEAN_NAME = "moduleResolver";
	
	/**
	 * Request attribute to hold the current ModuleResolver, retrievable by views.
	 * @see org.springframework.enhanced.web.servlet.support.RequestContextUtils#getModuleResolver
	 */
	public static final String MODULE_RESOLVER_ATTRIBUTE = ModuleDispatcherServlet.class.getName() + ".MODULE_RESOLVER";
	
	/** ModuleResolver used by this servlet */
	private ModuleResolver moduleResolver;
	
	@Override
	protected void initStrategies(ApplicationContext context) {
		initModuleResolver(context);
		super.initStrategies(context);
	}
	
	/**
	 * Initialize the ModuleResolver used by this class.
	 * <p>If no bean is defined with the given name in the BeanFactory for this namespace,
	 * we default to a FixedModuleResolver.
	 */
	private void initModuleResolver(ApplicationContext context) {
		try {
			this.moduleResolver = context.getBean(MODULE_RESOLVER_BEAN_NAME, ModuleResolver.class);
			if (logger.isDebugEnabled()) {
				logger.debug("Using ModuleResolver [" + this.moduleResolver + "]");
			}
		}
		catch (NoSuchBeanDefinitionException ex) {
			// We need to use the default.
			this.moduleResolver = getDefaultStrategy(context, ModuleResolver.class);
			if (logger.isDebugEnabled()) {
				logger.debug("Unable to locate ModuleResolver with name '" + MODULE_RESOLVER_BEAN_NAME + "': using default [" + this.moduleResolver + "]");
			}
		}
	}
	
	@Override
	protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setAttribute(MODULE_RESOLVER_ATTRIBUTE, this.moduleResolver);
		super.doService(request, response);
	}
	
}
