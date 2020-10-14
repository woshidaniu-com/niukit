package com.woshidaniu.niuca.tp.cas.client.filter;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SimpleCASAuthorizationFilter implements Filter {
    public static final String AUTHORIZED_USER_STRING = "com.woshidaniu.niuca.tp.cas.client.filter.authorizedUsers";
    public static final String FILTER_NAME = "SimpleCASAuthorizationFilter";
    private String authorizedUsersString;
    private List authorizedUsers;

    public SimpleCASAuthorizationFilter() {
    }

    public void init(FilterConfig config) throws ServletException {
        this.authorizedUsersString = config.getInitParameter("com.woshidaniu.niuca.tp.cas.client.filter.authorizedUsers");
        StringTokenizer tokenizer = new StringTokenizer(this.authorizedUsersString);
        this.authorizedUsers = new ArrayList();

        while(tokenizer.hasMoreTokens()) {
            this.authorizedUsers.add(tokenizer.nextElement());
        }

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc) throws ServletException, IOException {
        if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
            HttpSession session = ((HttpServletRequest)request).getSession();
            if (this.authorizedUsers.isEmpty()) {
                throw new ServletException("SimpleCASAuthorizationFilter: no authorized users set.");
            } else if (!this.authorizedUsers.contains((String)session.getAttribute("com.woshidaniu.niuca.tp.cas.client.filter.user"))) {
                throw new ServletException("SimpleCASAuthorizationFilter: user " + session.getAttribute("com.woshidaniu.niuca.tp.cas.client.filter.user") + " not authorized.");
            } else {
                fc.doFilter(request, response);
            }
        } else {
            throw new ServletException("SimpleCASAuthorizationFilter: protects only HTTP resources");
        }
    }

    public void destroy() {
    }
}
