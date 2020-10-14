/**
 * 
 */
package com.woshidaniu.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.shiro.IncorrectCaptchaException;
import com.woshidaniu.shiro.token.CaptchaAuthenticationToken;

/**
 * @author zhangxb
 * @desc 基础过滤器
 */
public abstract class BaseAuthenticationFilter extends ZFAuthenticatingFilter {

	public static final String DEFAULT_SESSION_CAPTCHA_KEY = "KAPTCHA_SESSION_KEY";
	
	private static final Logger log = LoggerFactory.getLogger(BaseAuthenticationFilter.class);
	
	protected boolean validateCaptcha = false;
	
	protected String sessoionCaptchaKey = DEFAULT_SESSION_CAPTCHA_KEY;
	
	public BaseAuthenticationFilter() {
		setLoginUrl(DEFAULT_LOGIN_URL);
	}

	protected void validateCaptcha(Session session, CaptchaAuthenticationToken token) {
		boolean validation = true;
		if (isValidateCaptcha()){
			validation = validateCaptcha((String)session.getAttribute(getSessoionCaptchaKey()), token.getCaptcha());
		}
		if(!validation){
			throw new IncorrectCaptchaException("Captcha validation failed!");
		}
	}

	protected boolean validateCaptcha(String request, String token){
		if(StringUtils.isNull(request)){
			return false;
		}
		return StringUtils.equalsIgnoreCase(request, token);
	}
	 
	@Override
	protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
		Subject subject = getSubject(request, response);
		AuthenticationToken token = createToken(request, response);
		if(subject.isAuthenticated()){
			log.info("User has already been Authenticated!");
			return onLoginSuccess(token, subject, request, response);
		}
        try {
        	if (token == null) {
                String msg = "createToken method implementation returned null. A valid non-null AuthenticationToken " +
                        "must be created in order to execute a login attempt.";
                 throw new AuthenticationException(msg);
            }
        	
        	if(token instanceof CaptchaAuthenticationToken){
        		validateCaptcha(subject.getSession(), (CaptchaAuthenticationToken)token);
        	}
        	
            subject.login(token);
            return onLoginSuccess(token, subject, request, response);
        } catch (AuthenticationException e) {
            return onLoginFailure(token, e, request, response);
        }
	}

	public boolean isValidateCaptcha() {
		return validateCaptcha;
	}


	public void setValidateCaptcha(boolean validateCaptcha) {
		this.validateCaptcha = validateCaptcha;
	}


	public String getSessoionCaptchaKey() {
		return sessoionCaptchaKey;
	}


	public void setSessoionCaptchaKey(String sessoionCaptchaKey) {
		this.sessoionCaptchaKey = sessoionCaptchaKey;
	}
	
}
