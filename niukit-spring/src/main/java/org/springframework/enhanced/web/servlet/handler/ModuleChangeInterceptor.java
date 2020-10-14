package org.springframework.enhanced.web.servlet.handler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.enhanced.web.servlet.module.SessionModuleResolver;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;

public class ModuleChangeInterceptor extends HandlerInterceptorAdapter {

	/**
	 * Default name of the locale specification parameter: "module".
	 */
	public static final String DEFAULT_PARAM_NAME = "module";

	protected final Logger LOG = LoggerFactory.getLogger(getClass());

	private String paramName = DEFAULT_PARAM_NAME;

	private String[] httpMethods;

	/**
	 * Set the name of the parameter that contains a locale specification in a
	 * locale change request. Default is "locale".
	 */
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	/**
	 * Return the name of the parameter that contains a locale specification in
	 * a locale change request.
	 */
	public String getParamName() {
		return this.paramName;
	}

	/**
	 * Configure the HTTP method(s) over which the locale can be changed.
	 * 
	 * @param httpMethods the methods
	 * @since 4.2
	 */
	public void setHttpMethods(String... httpMethods) {
		this.httpMethods = httpMethods;
	}

	/**
	 * Return the configured HTTP methods.
	 * 
	 * @since 4.2
	 */
	public String[] getHttpMethods() {
		return this.httpMethods;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException {

		String newModule = request.getParameter(getParamName());
		if (newModule != null) {
			if (checkHttpMethod(request.getMethod())) {
				WebUtils.setSessionAttribute(request, SessionModuleResolver.MODULE_SESSION_ATTRIBUTE_NAME, newModule );
			}
		}
		// Proceed in any case.
		return true;
	}

	private boolean checkHttpMethod(String currentMethod) {
		String[] configuredMethods = getHttpMethods();
		if (ObjectUtils.isEmpty(configuredMethods)) {
			return true;
		}
		for (String configuredMethod : configuredMethods) {
			if (configuredMethod.equalsIgnoreCase(currentMethod)) {
				return true;
			}
		}
		return false;
	}
	
}
