package com.woshidaniu.niuca.tp.cas.util;


import javax.servlet.Filter;
import javax.servlet.FilterConfig;

public abstract class AbstractConfigurationFilter implements Filter {
    public AbstractConfigurationFilter() {
    }

    protected final String getPropertyFromInitParams(FilterConfig filterConfig, String propertyName, String defaultValue) {
        String value = filterConfig.getInitParameter(propertyName);
        if (CommonUtils.isNotBlank(value)) {
            return value;
        } else {
            String value2 = filterConfig.getServletContext().getInitParameter(propertyName);
            return CommonUtils.isNotBlank(value2) ? value2 : defaultValue;
        }
    }
}
