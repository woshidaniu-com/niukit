package com.woshidaniu.niuca.tp.cas.client.logout;


import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class SingleSignOutHttpSessionListener implements HttpSessionListener {
    private Log log = LogFactory.getLog(this.getClass());
    private SessionMappingStorage SESSION_MAPPING_STORAGE;

    public SingleSignOutHttpSessionListener() {
    }

    public void sessionCreated(HttpSessionEvent event) {
    }

    public void sessionDestroyed(HttpSessionEvent event) {
        if (this.SESSION_MAPPING_STORAGE == null) {
            this.SESSION_MAPPING_STORAGE = getSessionMappingStorage();
        }

        HttpSession session = event.getSession();
        if (this.log.isDebugEnabled()) {
            this.log.debug("Removing HttpSession: " + session.getId());
        }

        this.SESSION_MAPPING_STORAGE.removeBySessionById(session.getId());
    }

    protected static SessionMappingStorage getSessionMappingStorage() {
        return SingleSignOutFilter.getSessionMappingStorage();
    }
}

