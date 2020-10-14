package org.springframework.enhanced.web.servlet.support;

import javax.servlet.http.HttpServletRequest;

import org.springframework.enhanced.web.servlet.ModuleDispatcherServlet;
import org.springframework.enhanced.web.servlet.ModuleResolver;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public abstract class RequestContextUtils extends org.springframework.web.servlet.support.RequestContextUtils {

	/**
	 * Return the ModuleResolver that has been bound to the request by the MuduleDispatcherServlet.
	 * @param request current HTTP request
	 * @return the current ModuleResolver, or {@code null} if not found
	 */
	public static ModuleResolver getModuleResolver(HttpServletRequest request) {
		return (ModuleResolver) request.getAttribute(ModuleDispatcherServlet.MODULE_RESOLVER_ATTRIBUTE);
	}

	/**
	 * Retrieves the current module from the given request, using the ModuleResolver
	 * and ModuleSource bound to the request by the MuduleDispatcherServlet.
	 * @param request current HTTP request
	 * @return the current module, or {@code null} if not found
	 * @see #getModuleResolver
	 */
	public static String getModule(HttpServletRequest request) {
		ModuleResolver moduleResolver = getModuleResolver(request);
		if (moduleResolver != null) {
			return moduleResolver.resolveModule(request);
		}
		else {
			return null;
		}
	}
	
	public static String getModule() {
		return getModule(getRequest());
	}
	
	public static HttpServletRequest getRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();   
	}
	
}
