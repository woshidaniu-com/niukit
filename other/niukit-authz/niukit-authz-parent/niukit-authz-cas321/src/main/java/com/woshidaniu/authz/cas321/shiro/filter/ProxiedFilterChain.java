/**
 * <p>Copyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.cas321.shiro.filter;

import javax.servlet.*;

import java.io.IOException;
import java.util.List;

/**
 *代理过滤器链,用于在一个过滤器中执行多个过滤器
 */
class ProxiedFilterChain implements FilterChain {

    private FilterChain orig;
    private List<Filter> filters;
    private int index = 0;

    public ProxiedFilterChain(FilterChain orig, List<Filter> filters) {
        if (orig == null) {
            throw new NullPointerException("original FilterChain cannot be null.");
        }
        this.orig = orig;
        this.filters = filters;
        this.index = 0;
    }

    public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
        if (this.filters == null || this.filters.size() == this.index) {
            this.orig.doFilter(request, response);
        } else {
            Filter f = this.filters.get(this.index++);
            f.doFilter(request, response, this);
        }
    }
}
