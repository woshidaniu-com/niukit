package com.woshidaniu.configuration.config;

import javax.servlet.ServletContext;

/**
 * Wrapper interface for ServletConfig and FilterConfig.
 */
public interface Config {

    /**
     * Get ServletContext object.
     */
    public ServletContext getServletContext();

    /**
     * Get init parameter value by name.
     */
    public String getInitParameter(String name);
    

}
