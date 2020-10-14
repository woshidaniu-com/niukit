package org.springframework.enhanced.web.servlet.module;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.util.WebUtils;

public class SessionModuleResolver extends AbstractModuleResolver {

	/**
	 * Name of the session attribute that holds the module name. Only used internally by this implementation.
	 * Use {@code RequestContext(Utils).getModule()}
	 * to retrieve the current module in controllers or views.
	 * @see org.springframework.enhanced.web.servlet.support.RequestContextUtils#getModule
	 */
	public static final String MODULE_SESSION_ATTRIBUTE_NAME = SessionModuleResolver.class.getName() + ".MODULE";

	@Override
	public String resolveModule(HttpServletRequest request) {
		String moduleName = (String) WebUtils.getSessionAttribute(request, MODULE_SESSION_ATTRIBUTE_NAME);
		// A specific module indicated, or do we need to fallback to the default?
		return (moduleName != null ? moduleName : getDefaultModule());
	}

	@Override
	public void setModule(HttpServletRequest request, HttpServletResponse response, String moduleName) {
		WebUtils.setSessionAttribute(request, MODULE_SESSION_ATTRIBUTE_NAME, (StringUtils.hasText(moduleName) ? moduleName : null));
	}

}
