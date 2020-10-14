package com.woshidaniu.niuca.tp.cas.client.logout;


import javax.servlet.http.HttpSession;

public interface SessionMappingStorage {
    HttpSession removeSessionByMappingId(String var1);

    void removeBySessionById(String var1);

    void addSessionById(String var1, HttpSession var2);
}
