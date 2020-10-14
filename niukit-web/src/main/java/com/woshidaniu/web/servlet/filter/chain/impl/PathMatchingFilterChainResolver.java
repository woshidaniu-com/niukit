/**
 * 
 */
package com.woshidaniu.web.servlet.filter.chain.impl;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.web.core.AntPathMatcher;
import com.woshidaniu.web.core.PathMatcher;
import com.woshidaniu.web.servlet.filter.chain.FilterChainManager;
import com.woshidaniu.web.servlet.filter.chain.FilterChainResolver;
import com.woshidaniu.web.utils.WebUtils;


/**
 * <p>
 *   <h3>niutal框架<h3>
 *   说明：
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年7月19日下午2:31:45
 */
public class PathMatchingFilterChainResolver implements FilterChainResolver {

	private static final Logger log = LoggerFactory.getLogger(PathMatchingFilterChainResolver.class);
	/**
	 * filterChain管理器
	 */
	private FilterChainManager filterChainManager;
	
	/**
	 * 路径匹配器
	 */
	private PathMatcher pathMatcher;
	
	 public PathMatchingFilterChainResolver() {
        this.pathMatcher = new AntPathMatcher();
        this.filterChainManager = new DefaultFilterChainManager();
    }

    public PathMatchingFilterChainResolver(FilterConfig filterConfig) {
        this.pathMatcher = new AntPathMatcher();
        this.filterChainManager = new DefaultFilterChainManager(filterConfig);
    }

	public FilterChainManager getFilterChainManager() {
		return filterChainManager;
	}

	public void setFilterChainManager(FilterChainManager filterChainManager) {
		this.filterChainManager = filterChainManager;
	}

	public PathMatcher getPathMatcher() {
		return pathMatcher;
	}

	public void setPathMatcher(PathMatcher pathMatcher) {
		this.pathMatcher = pathMatcher;
	}
	
	
	public FilterChain getChain(ServletRequest request, ServletResponse response, FilterChain originalChain) {
        FilterChainManager filterChainManager = getFilterChainManager();
        if (!filterChainManager.hasChains()) {
            return null;
        }
        String requestURI = getPathWithinApplication(request);
        for (String pathPattern : filterChainManager.getChainNames()) {
            if (pathMatches(pathPattern, requestURI)) {
                if (log.isTraceEnabled()) {
                    log.trace("Matched path pattern [" + pathPattern + "] for requestURI [" + requestURI + "].  " +
                            "Utilizing corresponding filter chain...");
                }
                return filterChainManager.proxy(originalChain, pathPattern);
            }
        }
        return null;
    }

    protected boolean pathMatches(String pattern, String path) {
        PathMatcher pathMatcher = getPathMatcher();
        return pathMatcher.match(pattern, path);
    }

    protected String getPathWithinApplication(ServletRequest request) {
        return WebUtils.getPathWithinApplication(WebUtils.toHttp(request));
    }
	
}
