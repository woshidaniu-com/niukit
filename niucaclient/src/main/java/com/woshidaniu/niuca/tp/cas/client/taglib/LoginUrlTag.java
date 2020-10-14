package com.woshidaniu.niuca.tp.cas.client.taglib;


import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class LoginUrlTag extends BodyTagSupport {
    public LoginUrlTag() {
    }

    public int doEndTag() throws JspTagException {
        String loginUrl = null;
        if (this.bodyContent != null) {
            loginUrl = this.bodyContent.getString();
        }

        if (loginUrl != null) {
            loginUrl = loginUrl.trim();
        }

        if (!(this.getParent() instanceof AuthTag)) {
            throw new JspTagException("illegal cas:loginUrl outside cas:auth");
        } else {
            ((AuthTag)this.getParent()).setCasLogin(loginUrl);
            return 6;
        }
    }
}
