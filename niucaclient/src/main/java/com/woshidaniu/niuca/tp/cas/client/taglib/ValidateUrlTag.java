package com.woshidaniu.niuca.tp.cas.client.taglib;


import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class ValidateUrlTag extends BodyTagSupport {
    public ValidateUrlTag() {
    }

    public int doEndTag() throws JspTagException {
        String validateUrl = null;
        if (this.bodyContent != null) {
            validateUrl = this.bodyContent.getString();
        }

        if (validateUrl != null) {
            validateUrl = validateUrl.trim();
        }

        if (!(this.getParent() instanceof AuthTag)) {
            throw new JspTagException("illegal cas:validateUrl outside cas:auth");
        } else {
            ((AuthTag)this.getParent()).setCasValidate(validateUrl);
            return 6;
        }
    }
}
