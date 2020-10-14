/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.cas321.shiro.filter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.servlet.OncePerRequestFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.woshidaniu.shiro.filter.LogoutListener;

/**
 * niutal框架注销过滤器
 * @author 		：康康（1571）
 */
public class ZFCas321FremeworkLogoutFilter extends OncePerRequestFilter implements InitializingBean{
	
	private static final Logger log = LoggerFactory.getLogger(ZFCas321FremeworkLogoutFilter.class);
	
	private boolean enable = true;
	private String service;
	private String casServerLogoutUrl;
	
	/**
	 * 注销回调监听
	 */
	protected List<LogoutListener> logoutListeners = Collections.emptyList();
	
	@Override
	public void afterPropertiesSet() throws Exception {
		
		log.info("init zfCas321FremeworkLogoutFilter");
		log.info("enable:{}",this.enable);
	}
	
	@Override
	protected void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
		
		if(this.enable){
			Object attr = WebUtils.toHttp(request).getSession().getAttribute(Constants.LOGIN_TYPE_SESSION_KEY);
			if(attr == null){
				//这里说明是采用其他方式登录的,交给框架中shiro中的后面的过滤器进行处理
				chain.doFilter(request, response);
			}else{
				//这说明是这种cas321方式登录的,在这里注销就可以了
				doOnCas321Login(request,response,chain);				
			}
		}else{
			chain.doFilter(request, response);
		}
	}
	
	private void doOnCas321Login(ServletRequest request, ServletResponse response, FilterChain chain) {
		Subject subject = SecurityUtils.getSubject();
		
		for (LogoutListener listener : logoutListeners) {
			listener.beforeLogout(subject, request, response);
		}
		try {
			subject.logout();
		} catch (SessionException ise) {
			log.warn("Encountered session exception during logout.  This can generally safely be ignored.", ise);
		}
		try{
			String redirectUrl = String.format("%s?service=%s", casServerLogoutUrl,service);
			WebUtils.issueRedirect(request, response, redirectUrl);
			for (LogoutListener listener : logoutListeners) {
				listener.onLogoutSuccess(request, response);
			}
		}catch (Exception e) {
			for (LogoutListener listener : logoutListeners) {
				listener.onLogoutFail(subject, e);
			}
		}
	}

	public void setService(String service) {
		this.service = service;
	}
	
	public void setCasServerLogoutUrl(String casServerLogoutUrl) {
		this.casServerLogoutUrl = casServerLogoutUrl;
	}

	public void setLogoutListeners(List<LogoutListener> logoutListeners) {
		this.logoutListeners = logoutListeners;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}
}
