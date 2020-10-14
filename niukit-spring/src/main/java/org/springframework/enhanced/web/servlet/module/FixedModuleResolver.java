package org.springframework.enhanced.web.servlet.module;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FixedModuleResolver extends AbstractModuleResolver {

	@Override
	public String resolveModule(HttpServletRequest request) {
		return getDefaultModule();
	}

	@Override
	public void setModule(HttpServletRequest request, HttpServletResponse response, String moduleName) {
		throw new UnsupportedOperationException("Cannot change module - use a different module resolution strategy");
	}

}
