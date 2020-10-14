package com.woshidaniu.niuca.tp.cas.client.taglib;

import com.woshidaniu.niuca.tp.cas.client.ProxyTicketValidator;
import com.woshidaniu.niuca.tp.cas.client.Util;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class AuthTag extends TagSupport {
    private String var;
    private int scope;
    private String casLogin;
    private String casValidate;
    private String service;
    private List acceptedProxies;
    private HttpServletRequest request;
    private HttpServletResponse response;

    public int doStartTag() throws JspException {
        this.request = (HttpServletRequest)this.pageContext.getRequest();
        this.response = (HttpServletResponse)this.pageContext.getResponse();
        this.casLogin = null;
        this.casValidate = null;

        try {
            this.service = Util.getService(this.request, this.pageContext.getServletContext().getInitParameter("com.woshidaniu.niuca.tp.cas.serverName"));
        } catch (ServletException var2) {
            throw new JspException(var2);
        }

        this.acceptedProxies = new ArrayList();
        return 1;
    }

    public int doEndTag() throws JspTagException {
        try {
            if (this.pageContext.getAttribute(this.var, this.scope) != null) {
                return 6;
            } else {
                String ticket = this.request.getParameter("ticket");
                if (ticket != null && !ticket.equals("")) {
                    String netid = this.getAuthenticatedNetid(ticket);
                    if (netid == null) {
                        throw new JspTagException("Unexpected CAS authentication error");
                    } else {
                        this.pageContext.setAttribute(this.var, netid, this.scope);
                        return 6;
                    }
                } else if (this.casLogin == null) {
                    throw new JspTagException("for pages that expect to be called without 'ticket' parameter, cas:auth must have a cas:loginUrl subtag");
                } else {
                    this.response.sendRedirect(this.casLogin + "?service=" + this.service);
                    return 5;
                }
            }
        } catch (IOException var3) {
            throw new JspTagException(var3.getMessage());
        } catch (SAXException var4) {
            throw new JspTagException(var4.getMessage());
        } catch (ParserConfigurationException var5) {
            throw new JspTagException(var5.getMessage());
        }
    }

    public void setVar(String var) {
        this.var = var;
    }

    public void setScope(String scope) {
        if (scope.equals("page")) {
            this.scope = 1;
        } else if (scope.equals("request")) {
            this.scope = 2;
        } else if (scope.equals("session")) {
            this.scope = 3;
        } else {
            if (!scope.equals("application")) {
                throw new IllegalArgumentException("invalid scope");
            }

            this.scope = 4;
        }

    }

    public void setCasLogin(String url) {
        this.casLogin = url;
    }

    public void setCasValidate(String url) {
        this.casValidate = url;
    }

    public void addAuthorizedProxy(String proxyId) {
        this.acceptedProxies.add(proxyId);
    }

    public void setService(String service) {
        this.service = service;
    }

    public AuthTag() {
        this.init();
    }

    public void release() {
        super.release();
        this.init();
    }

    private void init() {
        this.var = null;
        this.scope = 1;
        this.casLogin = null;
        this.casValidate = null;
        this.acceptedProxies = null;
    }

    private String getAuthenticatedNetid(String ticket) throws ParserConfigurationException, SAXException, IOException, JspTagException {
        ProxyTicketValidator pv = new ProxyTicketValidator();
        pv.setCasValidateUrl(this.casValidate);
        pv.setServiceTicket(ticket);
        pv.setService(this.service);
        pv.validate();
        if (!pv.isAuthenticationSuccesful()) {
            throw new JspTagException("CAS authentication error: " + pv.getErrorCode());
        } else {
            if (pv.getProxyList().size() != 0) {
                if (this.acceptedProxies.size() == 0) {
                    throw new JspTagException("this page does not accept proxied tickets");
                }

                if (!this.acceptedProxies.contains(pv.getProxyList().get(0))) {
                    throw new JspTagException("unauthorized top-level proxy: '" + pv.getProxyList().get(0) + "'");
                }
            }

            return pv.getUser();
        }
    }
}
