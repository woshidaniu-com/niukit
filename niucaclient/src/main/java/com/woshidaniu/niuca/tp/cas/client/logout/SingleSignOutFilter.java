package com.woshidaniu.niuca.tp.cas.client.logout;


import com.woshidaniu.niuca.tp.cas.util.AbstractConfigurationFilter;
import com.woshidaniu.niuca.tp.cas.util.CommonUtils;
import com.woshidaniu.niuca.tp.cas.util.XmlUtils;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class SingleSignOutFilter extends AbstractConfigurationFilter {
    private String artifactParameterName = "ticket";
    private static SessionMappingStorage SESSION_MAPPING_STORAGE = new HashMapBackedSessionMappingStorage();
    private static Log log = LogFactory.getLog(SingleSignOutFilter.class);

    public SingleSignOutFilter() {
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        this.setArtifactParameterName(this.getPropertyFromInitParams(filterConfig, "artifactParameterName", "ticket"));
        this.init();
    }

    public void init() {
        CommonUtils.assertNotNull(this.artifactParameterName, "artifactParameterName cannot be null.");
        CommonUtils.assertNotNull(SESSION_MAPPING_STORAGE, "sessionMappingStorage cannote be null.");
    }

    public void setArtifactParameterName(String artifactParameterName) {
        this.artifactParameterName = artifactParameterName;
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        String logoutRequest;
        if ("POST".equals(request.getMethod())) {
            logoutRequest = request.getParameter("logoutRequest");
            if (CommonUtils.isNotBlank(logoutRequest)) {
                if (log.isTraceEnabled()) {
                    log.trace("Logout request=[" + logoutRequest + "]");
                }

                String sessionIdentifier = XmlUtils.getTextForElement(logoutRequest, "SessionIndex");
                if (CommonUtils.isNotBlank(sessionIdentifier)) {
                    HttpSession session = SESSION_MAPPING_STORAGE.removeSessionByMappingId(sessionIdentifier);
                    System.out.println("SESSION_MAPPING_STORAGE.removeSessionByMappingId(sessionIdentifier)");
                    if (session != null) {
                        String sessionID = session.getId();
                        if (log.isDebugEnabled()) {
                            log.debug("Invalidating session [" + sessionID + "] for ST [" + sessionIdentifier + "]");
                        }

                        try {
                            session.invalidate();
                        } catch (IllegalStateException var10) {
                            log.debug(var10, var10);
                        }
                    }

                    return;
                }
            }
        } else {
            logoutRequest = request.getParameter(this.artifactParameterName);
            HttpSession session = request.getSession();
            if (log.isDebugEnabled() && session != null) {
                log.debug("Storing session identifier for " + session.getId());
            }

            if (CommonUtils.isNotBlank(logoutRequest)) {
                SESSION_MAPPING_STORAGE.addSessionById(logoutRequest, session);
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void setSessionMappingStorage(SessionMappingStorage storage) {
        SESSION_MAPPING_STORAGE = storage;
    }

    public static SessionMappingStorage getSessionMappingStorage() {
        return SESSION_MAPPING_STORAGE;
    }

    public void destroy() {
    }
}
