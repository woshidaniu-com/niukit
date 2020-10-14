package com.woshidaniu.niuca.tp.cas.validation;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public final class ProxyList {
    private final List proxyChains;

    public ProxyList(List proxyChains) {
        this.proxyChains = proxyChains;
    }

    public ProxyList() {
        this(new ArrayList());
    }

    public boolean contains(String[] proxiedList) {
        Iterator iter = this.proxyChains.iterator();

        while(iter.hasNext()) {
            if (Arrays.equals(proxiedList, (String[])iter.next())) {
                return true;
            }
        }

        return false;
    }

    public String toString() {
        return this.proxyChains.toString();
    }
}
