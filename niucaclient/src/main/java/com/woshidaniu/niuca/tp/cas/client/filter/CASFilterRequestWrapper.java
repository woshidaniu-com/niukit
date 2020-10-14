package com.woshidaniu.niuca.tp.cas.client.filter;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class CASFilterRequestWrapper extends HttpServletRequestWrapper {
    public CASFilterRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    public String getRemoteUser() {
        return (String)this.getSession().getAttribute("com.woshidaniu.niuca.tp.cas.client.filter.user");
    }
}
