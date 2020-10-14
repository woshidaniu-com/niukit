package com.woshidaniu.niuca.tp.cas.authentication;

import java.io.Serializable;
import java.security.Principal;
import java.util.Map;

public interface AttributePrincipal extends Principal, Serializable {
    String getProxyTicketFor(String var1);

    Map getAttributes();
}
