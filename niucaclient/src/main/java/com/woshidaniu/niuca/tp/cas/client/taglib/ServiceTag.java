package com.woshidaniu.niuca.tp.cas.client.taglib;

import java.net.URLEncoder;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class ServiceTag extends BodyTagSupport {
    public ServiceTag() {
    }

    public int doEndTag() throws JspTagException {
        String service = null;
        if (this.bodyContent != null) {
            service = this.bodyContent.getString();
        }

        if (service != null) {
            service = service.trim();
        }

        if (!(this.getParent() instanceof AuthTag)) {
            throw new JspTagException("illegal cas:service outside cas:auth");
        } else {
            ((AuthTag)this.getParent()).setService(URLEncoder.encode(service));
            return 6;
        }
    }
}
