package com.woshidaniu.shiro.filter;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.shiro.authenticator.ModularRealmAuthenticationExceptionHelper;

public abstract class ZFAuthenticatingFilter extends AuthenticatingFilter {

	private static final Logger LOG = LoggerFactory.getLogger(ZFAuthenticatingFilter.class);

	public static final String DEFAULT_ERROR_KEY_ATTRIBUTE_NAME = "shiroLoginFailure";
	public static final String DEFAULT_ERROR_INSTANCE_KEY_ATTRIBUTE_NAME = "shiroLoginFailureException";

	protected String failureKeyAttribute = DEFAULT_ERROR_KEY_ATTRIBUTE_NAME;
	protected String failureExceptionKeyAttribute = DEFAULT_ERROR_INSTANCE_KEY_ATTRIBUTE_NAME;

	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
			ServletResponse response) throws Exception {
		issueSuccessRedirect(request, response);
		return false;
	}

	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request,
			ServletResponse response) {
		setFailureAttribute(request, e);
		return true;
	}

	@Override
	protected void postHandle(ServletRequest request, ServletResponse response) throws Exception {
		ModularRealmAuthenticationExceptionHelper.clear();
	}
	
	/**
	 * 登陆成功后重新生成session【基于安全考虑】
	 * 
	 * @param oldSession
	 */
	protected Session newSession(Subject subject, Session oldSession) {
		// retain Session attributes to put in the new session after login:
		Map<Object, Object> attributes = new LinkedHashMap<Object, Object>();

		Collection<Object> keys = oldSession.getAttributeKeys();

		for (Object key : keys) {
			Object value = oldSession.getAttribute(key);
			if (value != null) {
				attributes.put(key, value);
			}
		}
		oldSession.stop();
		// restore the attributes:
		Session newSession = subject.getSession();

		for (Object key : attributes.keySet()) {
			newSession.setAttribute(key, attributes.get(key));
		}
		return newSession;
	}

	@Override
	public void setLoginUrl(String loginUrl) {
		String previous = getLoginUrl();
		if (previous != null) {
			this.appliedPaths.remove(previous);
		}
		super.setLoginUrl(loginUrl);
		if (LOG.isTraceEnabled()) {
			LOG.trace("Adding login url to applied paths.");
		}
		this.appliedPaths.put(getLoginUrl(), null);
	}

	public String getFailureKeyAttribute() {
		return failureKeyAttribute;
	}

	public void setFailureKeyAttribute(String failureKeyAttribute) {
		this.failureKeyAttribute = failureKeyAttribute;
	}

	public String getFailureExceptionKeyAttribute() {
		return failureExceptionKeyAttribute;
	}

	public void setFailureExceptionKeyAttribute(String failureExceptionKeyAttribute) {
		this.failureExceptionKeyAttribute = failureExceptionKeyAttribute;
	}

	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		if (isLoginRequest(request, response)) {
			if (isLoginSubmission(request, response)) {
				if (LOG.isTraceEnabled()) {
					LOG.trace("Login submission detected.  Attempting to execute login.");
				}
				return executeLogin(request, response);
			} else {
				if (LOG.isTraceEnabled()) {
					LOG.trace("Login page view.");
				}
				// allow them to see the login page ;)
				return true;
			}
		} else {
			if (LOG.isTraceEnabled()) {
				LOG.trace("Attempting to access a path which requires authentication.  Forwarding to the "
						+ "Authentication url [" + getLoginUrl() + "]");
			}
			saveRequestAndRedirectToLogin(request, response);
			return false;
		}
	}

	/**
	 * This default implementation merely returns <code>true</code> if the
	 * request is an HTTP <code>POST</code>, <code>false</code> otherwise. Can
	 * be overridden by subclasses for custom login submission detection
	 * behavior.
	 *
	 * @param request
	 *            the incoming ServletRequest
	 * @param response
	 *            the outgoing ServletResponse.
	 * @return <code>true</code> if the request is an HTTP <code>POST</code>,
	 *         <code>false</code> otherwise.
	 */
	protected boolean isLoginSubmission(ServletRequest request, ServletResponse response) {
		return (request instanceof HttpServletRequest)
				&& WebUtils.toHttp(request).getMethod().equalsIgnoreCase(POST_METHOD);
	}

	protected void setFailureAttribute(ServletRequest request, AuthenticationException ae) {
		String className = ae.getClass().getName();
		request.setAttribute(getFailureKeyAttribute(), className);
		request.setAttribute(getFailureExceptionKeyAttribute(), ae);
	}

}
