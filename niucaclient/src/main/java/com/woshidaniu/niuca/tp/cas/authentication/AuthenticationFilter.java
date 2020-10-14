package com.woshidaniu.niuca.tp.cas.authentication;

import com.woshidaniu.niuca.tp.cas.util.AbstractCasFilter;
import com.woshidaniu.niuca.tp.cas.util.CommonUtils;
import com.woshidaniu.niuca.tp.cas.validation.Assertion;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AuthenticationFilter extends AbstractCasFilter {
    public static final String CONST_CAS_GATEWAY = "_const_cas_gateway_";
    private String casServerLoginUrl;
    private boolean renew = false;
    private boolean gateway = false;

    public AuthenticationFilter() {
    }

    protected void initInternal(FilterConfig filterConfig) throws ServletException {
        super.initInternal(filterConfig);
        this.setCasServerLoginUrl(this.getPropertyFromInitParams(filterConfig, "casServerLoginUrl", (String)null));
        this.log.trace("Loaded CasServerLoginUrl parameter: " + this.casServerLoginUrl);
        this.setRenew(Boolean.valueOf(this.getPropertyFromInitParams(filterConfig, "renew", "false").toString()));
        this.log.trace("Loaded renew parameter: " + this.renew);
        this.setGateway(Boolean.valueOf(this.getPropertyFromInitParams(filterConfig, "gateway", "false").toString()));
        this.log.trace("Loaded gateway parameter: " + this.gateway);
    }

    public void init() {
        super.init();
        CommonUtils.assertNotNull(this.casServerLoginUrl, "casServerLoginUrl cannot be null.");
    }

    public final void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        HttpSession session = request.getSession(false);
        String ticket = request.getParameter(this.getArtifactParameterName());
        Assertion assertion = session != null ? (Assertion)session.getAttribute("_const_cas_assertion_") : null;
        boolean wasGatewayed = session != null && session.getAttribute("_const_cas_gateway_") != null;
        if (CommonUtils.isBlank(ticket) && assertion == null && !wasGatewayed) {
            this.log.debug("no ticket and no assertion found");
            if (this.gateway) {
                this.log.debug("setting gateway attribute in session");
                request.getSession(true).setAttribute("_const_cas_gateway_", "yes");
            }

            String serviceUrl = this.constructServiceUrl(request, response);
            if (this.log.isDebugEnabled()) {
                this.log.debug("Constructed service url: " + serviceUrl);
            }

            String urlToRedirectTo = CommonUtils.constructRedirectUrl(this.casServerLoginUrl, this.getServiceParameterName(), serviceUrl, this.renew, this.gateway);
            if (this.log.isDebugEnabled()) {
                this.log.debug("redirecting to \"" + urlToRedirectTo + "\"");
            }

            response.sendRedirect(urlToRedirectTo);
        } else {
            if (session != null) {
                this.log.debug("removing gateway attribute from session");
                session.setAttribute("_const_cas_gateway_", (Object)null);
            }

            filterChain.doFilter(request, response);
        }
    }

    public final void setRenew(boolean renew) {
        this.renew = renew;
    }

    public final void setGateway(boolean gateway) {
        this.gateway = gateway;
    }

    public final void setCasServerLoginUrl(String casServerLoginUrl) {
        this.casServerLoginUrl = casServerLoginUrl;
    }
}
