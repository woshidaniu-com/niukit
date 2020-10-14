/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.shiro.servlet.filter;

import java.io.IOException;

import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.util.WebUtils;

import com.woshidaniu.web.servlet.filter.AbstractAccessControlFilter;

public abstract class AbstractAuthenticatingFilter extends AbstractAccessControlFilter {

	/**
     * Simple default login URL equal to <code>/login.jsp</code>, which can be overridden by calling the
     * {@link #setLoginUrl(String) setLoginUrl} method.
     */
    public static final String DEFAULT_LOGIN_URL = "/login.jsp";

	public static final String DEFAULT_SUCCESS_URL = "/";
	
    /**
     * The login url to used to authenticate a user, used when redirecting users if authentication is required.
     */
    private String loginUrl = DEFAULT_LOGIN_URL;
    
    private String successUrl = DEFAULT_SUCCESS_URL;
    
	@Override
	protected void onFilterConfigSet(FilterConfig filterConfig) throws Exception {
		super.onFilterConfigSet(filterConfig);
		this.setLoginUrl(filterConfig.getInitParameter("loginUrl"));
		this.setSuccessUrl(filterConfig.getInitParameter("successUrl"));
	}
    
    /**
     * Redirects to user to the previously attempted URL after a successful login.  This implementation simply calls
     * <code>{@link org.apache.shiro.web.util.WebUtils WebUtils}.{@link WebUtils#redirectToSavedRequest(javax.servlet.ServletRequest, javax.servlet.ServletResponse, String) redirectToSavedRequest}</code>
     * using the {@link #getSuccessUrl() successUrl} as the {@code fallbackUrl} argument to that call.
     *
     * @param request  the incoming request
     * @param response the outgoing response
     * @throws IOException if there is a problem redirecting.
     */
    protected void issueSuccessRedirect(ServletRequest request, ServletResponse response) throws IOException {
        WebUtils.redirectToSavedRequest(request, response, getSuccessUrl());
    }
    
    /**
     * Convenience method for subclasses to use when a login redirect is required.
     * <p/>
     * This implementation simply calls {@link #saveRequest(javax.servlet.ServletRequest) saveRequest(request)}
     * and then {@link #redirectToLogin(javax.servlet.ServletRequest, javax.servlet.ServletResponse) redirectToLogin(request,response)}.
     *
     * @param request  the incoming <code>ServletRequest</code>
     * @param response the outgoing <code>ServletResponse</code>
     * @throws IOException if an error occurs.
     */
    protected void saveRequestAndRedirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
        saveRequest(request);
        redirectToLogin(request, response);
    }

    /**
     * Convenience method merely delegates to
     * {@link WebUtils#saveRequest(javax.servlet.ServletRequest) WebUtils.saveRequest(request)} to save the request
     * state for reuse later.  This is mostly used to retain user request state when a redirect is issued to
     * return the user to their originally requested url/resource.
     * <p/>
     * If you need to save and then immediately redirect the user to login, consider using
     * {@link #saveRequestAndRedirectToLogin(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
     * saveRequestAndRedirectToLogin(request,response)} directly.
     *
     * @param request the incoming ServletRequest to save for re-use later (for example, after a redirect).
     */
    protected void saveRequest(ServletRequest request) {
        WebUtils.saveRequest(request);
    }

    /**
     * Convenience method for subclasses that merely acquires the {@link #getLoginUrl() getLoginUrl} and redirects
     * the request to that url.
     * <p/>
     * <b>N.B.</b>  If you want to issue a redirect with the intention of allowing the user to then return to their
     * originally requested URL, don't use this method directly.  Instead you should call
     * {@link #saveRequestAndRedirectToLogin(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
     * saveRequestAndRedirectToLogin(request,response)}, which will save the current request state so that it can
     * be reconstructed and re-used after a successful login.
     *
     * @param request  the incoming <code>ServletRequest</code>
     * @param response the outgoing <code>ServletResponse</code>
     * @throws IOException if an error occurs.
     */
    protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
        String loginUrl = getLoginUrl();
        WebUtils.issueRedirect(request, response, loginUrl);
    }
    
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response)  {
    	// 已登录会话不进行拦截处理
    	return SecurityUtils.getSubject().isAuthenticated();
    }
    
	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public String getSuccessUrl() {
		return successUrl;
	}

	public void setSuccessUrl(String successUrl) {
		this.successUrl = successUrl;
	}
    
}
