/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.cas321.shiro.filter;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.woshidaniu.authz.cas321.shiro.token.ZFCasToken;
import com.woshidaniu.web.utils.RemoteAddrUtils;

public class ZFCas321Filter extends AuthenticatingFilter implements InitializingBean{

	private static final Logger log = LoggerFactory.getLogger(ZFCas321Filter.class);
    
    private String failureUrl;
    private boolean redirectToSaveRequestUri = false;
    
	@Override
	public void afterPropertiesSet() throws Exception {
		log.info("init zfCas321Filter");
	}

	@Override
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest httpRequest = WebUtils.toHttp(request);
		String ticket = httpRequest.getParameter("ticket");
		ZFCasToken token = new ZFCasToken(RemoteAddrUtils.getRemoteAddr(httpRequest));
		token.setTicket(ticket);
		token.setUsername(httpRequest.getRemoteUser());
		return token;
	}

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        return executeLogin(request, response);
    }
    
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
    	Subject subject = getSubject(request, response);
    	boolean isAccessAllowed = subject.isAuthenticated();
        return isAccessAllowed;
    }
    
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,ServletResponse response) throws Exception {
    	
    	HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
    	HttpSession httpSession = httpServletRequest.getSession();
    	httpSession.setAttribute(Constants.LOGIN_TYPE_SESSION_KEY, LoginType.FreamworkCas321Login);
    	
    	String savePath = (String) httpSession.getAttribute(Constants.SAVE_FIRST_REQUEST_URI_SESSION_KEY);
    	if(savePath != null && redirectToSaveRequestUri){
    		log.debug("redirect to first access path[" + savePath + "] from session");
    		WebUtils.issueRedirect(request, response, savePath);
    	}else{
    		WebUtils.issueRedirect(request, response, getSuccessUrl());
    	}
        return false;
    }
    
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException ae, ServletRequest request,ServletResponse response) {
        if (log.isDebugEnabled()) {
            log.debug( "Authentication exception", ae );
        }
        Subject subject = getSubject(request, response);
        if (subject.isAuthenticated() || subject.isRemembered()) {
            try {
                issueSuccessRedirect(request, response);
            } catch (Exception e) {
                log.error("Cannot redirect to the default success url", e);
            }
        } else {
            try {
                WebUtils.issueRedirect(request, response, failureUrl);
            } catch (IOException e) {
                log.error("Cannot redirect to failure url : {}", failureUrl, e);
            }
        }
        return false;
    }
    
    public void setFailureUrl(String failureUrl) {
        this.failureUrl = failureUrl;
    }

	public boolean isRedirectToSaveRequestUri() {
		return redirectToSaveRequestUri;
	}

	public void setRedirectToSaveRequestUri(boolean redirectToSaveRequestUri) {
		this.redirectToSaveRequestUri = redirectToSaveRequestUri;
	}

}
