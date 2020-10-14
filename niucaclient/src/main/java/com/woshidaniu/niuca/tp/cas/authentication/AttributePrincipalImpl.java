package com.woshidaniu.niuca.tp.cas.authentication;


import com.woshidaniu.niuca.tp.cas.proxy.ProxyRetriever;
import com.woshidaniu.niuca.tp.cas.util.CommonUtils;
import java.util.Collections;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AttributePrincipalImpl implements AttributePrincipal {
    private static final Log LOG = LogFactory.getLog(AttributePrincipalImpl.class);
    private static final long serialVersionUID = -8810123156070148535L;
    private final String name;
    private final Map attributes;
    private final String proxyGrantingTicket;
    private final ProxyRetriever proxyRetriever;

    public AttributePrincipalImpl(String name) {
        this(name, Collections.EMPTY_MAP);
    }

    public AttributePrincipalImpl(String name, Map attributes) {
        this(name, attributes, (String)null, (ProxyRetriever)null);
    }

    public AttributePrincipalImpl(String name, String proxyGrantingTicket, ProxyRetriever proxyRetriever) {
        this(name, Collections.EMPTY_MAP, proxyGrantingTicket, proxyRetriever);
    }

    public AttributePrincipalImpl(String name, Map attributes, String proxyGrantingTicket, ProxyRetriever proxyRetriever) {
        this.name = name;
        this.attributes = attributes;
        this.proxyGrantingTicket = proxyGrantingTicket;
        this.proxyRetriever = proxyRetriever;
        CommonUtils.assertNotNull(this.name, "name cannot be null.");
        CommonUtils.assertNotNull(this.attributes, "attributes cannot be null.");
    }

    public Map getAttributes() {
        return this.attributes;
    }

    public String getProxyTicketFor(String service) {
        if (this.proxyGrantingTicket != null) {
            return this.proxyRetriever.getProxyTicketIdFor(this.proxyGrantingTicket, service);
        } else {
            LOG.debug("No ProxyGrantingTicket was supplied, so no Proxy Ticket can be retrieved.");
            return null;
        }
    }

    public String getName() {
        return this.name;
    }
}
