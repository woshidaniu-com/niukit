package com.woshidaniu.niuca.tp.cas.util;


import com.woshidaniu.niuca.tp.cas.validation.Assertion;
import java.io.IOException;
import java.security.Principal;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

public final class HttpServletRequestWrapperFilter implements Filter {
    public HttpServletRequestWrapperFilter() {
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Principal principal = this.retrievePrincipalFromSessionOrRequest(servletRequest);
        filterChain.doFilter(new HttpServletRequestWrapperFilter.CasHttpServletRequestWrapper((HttpServletRequest)servletRequest, principal), servletResponse);
    }

    protected Principal retrievePrincipalFromSessionOrRequest(ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpSession session = request.getSession(false);
        Assertion assertion = (Assertion)(session == null ? request.getAttribute("_const_cas_assertion_") : session.getAttribute("_const_cas_assertion_"));
        return assertion == null ? null : assertion.getPrincipal();
    }

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    final class CasHttpServletRequestWrapper extends HttpServletRequestWrapper {
        private final Principal principal;

        CasHttpServletRequestWrapper(HttpServletRequest request, Principal principal) {
            super(request);
            this.principal = principal;
        }

        public Principal getUserPrincipal() {
            return this.principal;
        }

        public String getRemoteUser() {
            return this.principal != null ? this.principal.getName() : null;
        }
    }
}
