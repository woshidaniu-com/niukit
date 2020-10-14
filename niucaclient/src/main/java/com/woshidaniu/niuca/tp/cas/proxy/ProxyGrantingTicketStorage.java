package com.woshidaniu.niuca.tp.cas.proxy;


public interface ProxyGrantingTicketStorage {
    void save(String var1, String var2);

    String retrieve(String var1);
}
