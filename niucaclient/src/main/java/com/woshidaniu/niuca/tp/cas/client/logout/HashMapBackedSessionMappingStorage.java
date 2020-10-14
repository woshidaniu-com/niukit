package com.woshidaniu.niuca.tp.cas.client.logout;


import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpSession;

public final class HashMapBackedSessionMappingStorage implements SessionMappingStorage {
    private final Map MANAGED_SESSIONS = new HashMap();
    private final Map ID_TO_SESSION_KEY_MAPPING = new HashMap();

    public HashMapBackedSessionMappingStorage() {
    }

    public void addSessionById(String mappingId, HttpSession session) {
        this.ID_TO_SESSION_KEY_MAPPING.put(session.getId(), mappingId);
        this.MANAGED_SESSIONS.put(mappingId, session);
    }

    public void removeBySessionById(String sessionId) {
        String key = (String)this.ID_TO_SESSION_KEY_MAPPING.get(sessionId);
        this.MANAGED_SESSIONS.remove(key);
        this.ID_TO_SESSION_KEY_MAPPING.remove(sessionId);
    }

    public HttpSession removeSessionByMappingId(String mappingId) {
        HttpSession session = (HttpSession)this.MANAGED_SESSIONS.get(mappingId);
        if (session != null) {
            this.removeBySessionById(session.getId());
        }

        return session;
    }
}
