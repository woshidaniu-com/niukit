package com.woshidaniu.niuca.tp.cas.client.taglib;


import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class AuthorizedProxyTag extends BodyTagSupport {
    public AuthorizedProxyTag() {
    }

    public int doEndTag() throws JspTagException {
        String authorizedProxy = null;
        if (this.bodyContent != null) {
            authorizedProxy = this.bodyContent.getString();
        }

        if (authorizedProxy != null) {
            authorizedProxy = authorizedProxy.trim();
        }

        if (!(this.getParent() instanceof AuthTag)) {
            throw new JspTagException("illegal cas:authorizedProxy outside cas:auth");
        } else {
            ((AuthTag)this.getParent()).addAuthorizedProxy(authorizedProxy);
            return 6;
        }
    }
}
