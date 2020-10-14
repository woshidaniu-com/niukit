/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.cas.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.jasig.cas.client.Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.woshidaniu.authz.cas.token.ZFCasToken;
import com.woshidaniu.web.utils.RemoteAddrUtils;

public class ZFCasFilter extends AuthenticatingFilter {

	private static Logger logger = LoggerFactory.getLogger(ZFCasFilter.class);
    
	// the name of the parameter service ticket in url (i.e. ticket)
    private static final String TICKET_PARAMETER = Protocol.CAS2.getArtifactParameterName();
    
    // the url where the application is redirected if the CAS service ticket validation failed (example : /mycontextpatch/cas_error.jsp)
    private String failureUrl;
    
	@Override
	public void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		HttpServletRequest httpRequest = WebUtils.toHttp(request);
		if(logger.isDebugEnabled()) {
			logger.debug("filter url:{}",httpRequest.getRequestURI());
		}
		super.doFilterInternal(request, response, chain);
	}

	@Override
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest httpRequest = WebUtils.toHttp(request);
		String ticket = httpRequest.getParameter(TICKET_PARAMETER);
		ZFCasToken token = new ZFCasToken(RemoteAddrUtils.getRemoteAddr(httpRequest));
		if(StringUtils.hasText(ticket)) {
			token.setTicket(ticket);
		}
		token.setUsername(httpRequest.getRemoteUser());
		return token;
	}

	 /**
     * Execute login by creating {@link #createToken(javax.servlet.ServletRequest, javax.servlet.ServletResponse) token} and logging subject
     * with this token.
     * 
     * @param request the incoming request
     * @param response the outgoing response
     * @throws Exception if there is an error processing the request.
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        return executeLogin(request, response);
    }
    
    /**
     * Returns <code>false</code> to always force authentication (user is never considered authenticated by this filter).
     * 
     * @param request the incoming request
     * @param response the outgoing response
     * @param mappedValue the filter-specific config value mapped to this filter in the URL rules mappings.
     * @return <code>false</code>
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
    	Subject subject = getSubject(request, response);
    	boolean isAccessAllowed = subject.isAuthenticated();
        return isAccessAllowed;
    }
    
    /**
     * If login has been successful, redirect user to the original protected url.
     * 
     * @param token the token representing the current authentication
     * @param subject the current authenticated subjet
     * @param request the incoming request
     * @param response the outgoing response
     * @throws Exception if there is an error processing the request.
     */
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
                                     ServletResponse response) throws Exception {
        issueSuccessRedirect(request, response);
        return false;
    }
    
    /**
     * If login has failed, redirect user to the CAS error page (no ticket or ticket validation failed) except if the user is already
     * authenticated, in which case redirect to the default success url.
     * 
     * @param token the token representing the current authentication
     * @param ae the current authentication exception
     * @param request the incoming request
     * @param response the outgoing response
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException ae, ServletRequest request,
                                     ServletResponse response) {
        if (logger.isDebugEnabled()) {
            logger.debug( "Authentication exception", ae );
        }
        // is user authenticated or in remember me mode ?
        Subject subject = getSubject(request, response);
        if (subject.isAuthenticated() || subject.isRemembered()) {
            try {
                issueSuccessRedirect(request, response);
            } catch (Exception e) {
                logger.error("Cannot redirect to the default success url", e);
            }
        } else {
            try {
                WebUtils.issueRedirect(request, response, failureUrl);
            } catch (IOException e) {
                logger.error("Cannot redirect to failure url : {}", failureUrl, e);
            }
        }
        return false;
    }
    
    public void setFailureUrl(String failureUrl) {
        this.failureUrl = failureUrl;
    }
    
}
