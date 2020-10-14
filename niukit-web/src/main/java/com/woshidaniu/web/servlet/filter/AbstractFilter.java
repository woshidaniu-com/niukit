package com.woshidaniu.web.servlet.filter;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.web.Parameters;

/**
 * <p>
 *   <h3>niutal框架<h3>
 *   说明：servlet filter 基类
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年7月7日下午2:38:09
 */
public abstract class AbstractFilter implements Filter {
	
	protected final Logger LOG = LoggerFactory.getLogger(AbstractFilter.class);
	
	protected ServletContext servletContext;
	
	protected FilterConfig filterConfig;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// 初始化参数取值对象
		this.setFilterConfig(filterConfig);
		this.setServletContext(filterConfig.getServletContext());
		Parameters.initialize(filterConfig);
		try {
            onFilterConfigSet(filterConfig);
        } catch (Exception e) {
            if (e instanceof ServletException) {
                throw (ServletException) e;
            } else {
                if (LOG.isErrorEnabled()) {
                	LOG.error("Unable to start Filter: [" + e.getMessage() + "].", e);
                }
                throw new ServletException(e);
            }
        }
	}
	
	protected void onFilterConfigSet(FilterConfig filterConfig) throws Exception {
    }
	
	@Override
	public void destroy() {
		this.filterConfig = null;
	}

	/**
	 * Make the name of this filter available to subclasses.
	 * Analogous to GenericServlet's <code>getServletName()</code>.
	 * <p>Takes the FilterConfig's filter name by default.
	 * If initialized as bean in a Spring application context,
	 * it falls back to the bean name as defined in the bean factory.
	 * @return the filter name, or <code>null</code> if none available
	 * @see javax.servlet.GenericServlet#getServletName()
	 * @see javax.servlet.FilterConfig#getFilterName()
	 * @see #setBeanName
	 */
	protected final String getFilterName() {
		return (this.filterConfig != null ? this.filterConfig.getFilterName() : this.getClass().getSimpleName());
	}
	
	public final void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	
	public final ServletContext getServletContext() {
		return (this.filterConfig != null ? this.filterConfig.getServletContext() : this.servletContext);
	}

	public FilterConfig getFilterConfig() {
		return filterConfig;
	}

	public void setFilterConfig(FilterConfig filterConfig) {
		this.filterConfig = filterConfig;
	}

	public Logger getLOG() {
		return LOG;
	}

}
