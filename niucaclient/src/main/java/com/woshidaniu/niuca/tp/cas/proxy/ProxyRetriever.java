package com.woshidaniu.niuca.tp.cas.proxy;

import java.io.Serializable;

public interface ProxyRetriever extends Serializable {
    String getProxyTicketIdFor(String var1, String var2);
}
