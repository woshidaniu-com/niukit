package com.woshidaniu.web.servlet.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.web.servlet.filter.chain.FilterChainResolver;

/**
 * <p>
 *   <h3>niutal框架<h3>
 *   说明：抽象我是大牛Filter,是入口类
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年7月19日上午11:47:42
 */
public abstract class AbstractRouteableFilter extends OncePerRequestFilter {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractRouteableFilter.class);
	
	/**
	 * 用来判定使用那个FilterChian
	 */
	protected FilterChainResolver filterChainResolver;
	
	public AbstractRouteableFilter() {
		super();
	}

	public AbstractRouteableFilter(FilterChainResolver filterChainResolver) {
		super();
		this.filterChainResolver = filterChainResolver;
	}

	@Override
	protected void doFilterInternal(ServletRequest servletRequest, ServletResponse servletResponse, final FilterChain chain) throws ServletException, IOException {
        Throwable t = null;
        try {
             this.executeChain(servletRequest, servletResponse, chain);
        }catch (Throwable throwable) {
            t = throwable;
        }
        if (t != null) {
            if (t instanceof ServletException) {
                throw (ServletException) t;
            }
            if (t instanceof IOException) {
                throw (IOException) t;
            }
            String msg = "Filtered request failed.";
            throw new ServletException(msg, t);
        }
    }

    protected FilterChain getExecutionChain(ServletRequest request, ServletResponse response, FilterChain origChain) {
        FilterChain chain = origChain;

        FilterChainResolver resolver = getFilterChainResolver();
        if (resolver == null) {
            LOG.debug("No FilterChainResolver configured.  Returning original FilterChain.");
            return origChain;
        }

        FilterChain resolved = resolver.getChain(request, response, origChain);
        if (resolved != null) {
            LOG.trace("Resolved a configured FilterChain for the current request.");
            chain = resolved;
        } else {
            LOG.trace("No FilterChain configured for the current request.  Using the default.");
        }

        return chain;
    }


    protected void executeChain(ServletRequest request, ServletResponse response, FilterChain origChain)
            throws IOException, ServletException {
        FilterChain chain = getExecutionChain(request, response, origChain);
        chain.doFilter(request, response);
    }


	public FilterChainResolver getFilterChainResolver() {
		return filterChainResolver;
	}


	public void setFilterChainResolver(FilterChainResolver filterChainResolver) {
		this.filterChainResolver = filterChainResolver;
	}

}
