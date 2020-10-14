package com.woshidaniu.niuca.tp.cas.proxy;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class ProxyGrantingTicketStorageImpl implements ProxyGrantingTicketStorage {
    private final Log log;
    private static final long DEFAULT_TIMEOUT = 60000L;
    private final Map cache;

    public ProxyGrantingTicketStorageImpl() {
        this(60000L);
    }

    public ProxyGrantingTicketStorageImpl(long timeout) {
        this.log = LogFactory.getLog(this.getClass());
        this.cache = new HashMap();
        Thread thread = new ProxyGrantingTicketStorageImpl.ProxyGrantingTicketCleanupThread(timeout, this.cache);
        thread.setDaemon(true);
        thread.start();
    }

    public String retrieve(String proxyGrantingTicketIou) {
        ProxyGrantingTicketStorageImpl.ProxyGrantingTicketHolder holder = (ProxyGrantingTicketStorageImpl.ProxyGrantingTicketHolder)this.cache.get(proxyGrantingTicketIou);
        if (holder == null) {
            this.log.info("No Proxy Ticket found for " + proxyGrantingTicketIou);
            return null;
        } else {
            this.cache.remove(holder);
            if (this.log.isDebugEnabled()) {
                this.log.debug("Returned ProxyGrantingTicket of " + holder.getProxyGrantingTicket());
            }

            return holder.getProxyGrantingTicket();
        }
    }

    public void save(String proxyGrantingTicketIou, String proxyGrantingTicket) {
        ProxyGrantingTicketStorageImpl.ProxyGrantingTicketHolder holder = new ProxyGrantingTicketStorageImpl.ProxyGrantingTicketHolder(proxyGrantingTicket);
        if (this.log.isDebugEnabled()) {
            this.log.debug("Saving ProxyGrantingTicketIOU and ProxyGrantingTicket combo: [" + proxyGrantingTicketIou + ", " + proxyGrantingTicket + "]");
        }

        this.cache.put(proxyGrantingTicketIou, holder);
    }

    private final class ProxyGrantingTicketCleanupThread extends Thread {
        private final long timeout;
        private final Map cache;

        public ProxyGrantingTicketCleanupThread(long timeout, Map cache) {
            this.timeout = timeout;
            this.cache = cache;
        }

        public void run() {
            while(true) {
                try {
                    Thread.sleep(this.timeout);
                } catch (InterruptedException var6) {
                }

                List itemsToRemove = new ArrayList();
                synchronized(this.cache) {
                    Iterator iter = this.cache.keySet().iterator();

                    while(iter.hasNext()) {
                        Object key = iter.next();
                        ProxyGrantingTicketStorageImpl.ProxyGrantingTicketHolder holder = (ProxyGrantingTicketStorageImpl.ProxyGrantingTicketHolder)this.cache.get(key);
                        if (holder.isExpired(this.timeout)) {
                            itemsToRemove.add(key);
                        }
                    }

                    iter = itemsToRemove.iterator();

                    while(iter.hasNext()) {
                        this.cache.remove(iter.next());
                    }
                }
            }
        }
    }

    private final class ProxyGrantingTicketHolder {
        private final String proxyGrantingTicket;
        private final long timeInserted;

        protected ProxyGrantingTicketHolder(String proxyGrantingTicket) {
            this.proxyGrantingTicket = proxyGrantingTicket;
            this.timeInserted = System.currentTimeMillis();
        }

        public String getProxyGrantingTicket() {
            return this.proxyGrantingTicket;
        }

        final boolean isExpired(long timeout) {
            return System.currentTimeMillis() - this.timeInserted > timeout;
        }
    }
}
