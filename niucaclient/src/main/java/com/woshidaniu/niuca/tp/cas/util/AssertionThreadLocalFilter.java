package com.woshidaniu.niuca.tp.cas.util;

import com.woshidaniu.niuca.tp.cas.validation.Assertion;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public final class AssertionThreadLocalFilter implements Filter {
    public AssertionThreadLocalFilter() {
    }

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpSession session = request.getSession(false);
        Assertion assertion = (Assertion)(session != null ? session.getAttribute("_const_cas_assertion_") : null);

        try {
            AssertionHolder.setAssertion(assertion);
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            AssertionHolder.clear();
        }

    }

    public void destroy() {
    }
}
