package org.springframework.enhanced.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ModuleResolver {

	String resolveModule(HttpServletRequest request);

	void setModule(HttpServletRequest request, HttpServletResponse response, String moduleName);

	
}
