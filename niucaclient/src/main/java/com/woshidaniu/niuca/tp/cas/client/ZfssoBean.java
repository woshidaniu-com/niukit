package com.woshidaniu.niuca.tp.cas.client;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ZfssoBean {
    private String yhm;
    private String yhlx = "";
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;

    public ZfssoBean() {
    }

    public HttpServletRequest getRequest() {
        return this.request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return this.response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public HttpSession getSession() {
        return this.session;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

    public String getYhlx() {
        return this.yhlx;
    }

    public void setYhlx(String yhlx) {
        this.yhlx = yhlx;
    }

    public String getYhm() {
        return this.yhm;
    }

    public void setYhm(String yhm) {
        this.yhm = yhm;
    }
}

