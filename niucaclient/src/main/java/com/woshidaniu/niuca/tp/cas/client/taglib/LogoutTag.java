package com.woshidaniu.niuca.tp.cas.client.taglib;


import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

public class LogoutTag extends TagSupport {
    private String var;
    private String logoutUrl;
    private int scope;

    public int doStartTag() throws JspException {
        try {
            HttpServletResponse response = (HttpServletResponse)this.pageContext.getResponse();
            this.pageContext.removeAttribute(this.var, this.scope);
            if (this.scope == 3) {
                this.pageContext.getSession().invalidate();
            }

            response.sendRedirect(this.logoutUrl);
            return 0;
        } catch (IOException var2) {
            throw new JspTagException(var2.getMessage());
        }
    }

    public int doEndTag() {
        return 5;
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

    public void setLogoutUrl(String logoutUrl) {
        this.logoutUrl = logoutUrl;
    }

    public LogoutTag() {
        this.init();
    }

    public void release() {
        super.release();
        this.init();
    }

    private void init() {
        this.var = this.logoutUrl = null;
        this.scope = 1;
    }
}

