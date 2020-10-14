package com.woshidaniu.shiro.filter;

import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 *@类名称		： ZFLogoutFilter.java
 *@类描述		：账号注销过滤器
 *@创建人		：kangzhidong
 *@创建时间	：2017年8月28日 下午7:36:47
 *@修改人		：
 *@修改时间	：
 *@版本号		: v1.0
 */
public class ZFLogoutFilter extends LogoutFilter {
	
	private static final Logger log = LoggerFactory.getLogger(ZFLogoutFilter.class);
	
	/**
	 * 注销回调监听
	 */
	protected List<LogoutListener> logoutListeners;
	/**
	 * 是否单点登录
	 */
	protected boolean casLogin = false;
	
	
	public String getCasRedirectUrl(ServletRequest request, ServletResponse response) {
		return super.getRedirectUrl();
	}
	
	@Override
	protected boolean preHandle(ServletRequest request, ServletResponse response)
			throws Exception {
		
		Subject subject = getSubject(request, response);
		
		//调用事件监听器
		if(getLogoutListeners() != null && getLogoutListeners().size() > 0){
			for (LogoutListener logoutListener : getLogoutListeners()) {
				logoutListener.beforeLogout(subject, request, response);
			}
		}
		
		Exception ex = null;
		boolean result = false;
		try {
			// 如果是单点登录，需要重新构造登出的重定向地址
			if(this.isCasLogin(subject)){
		        String redirectUrl = getCasRedirectUrl(request, response);
		        //try/catch added for SHIRO-298:
		        try {
		            subject.logout();
		        } catch (SessionException ise) {
		            log.debug("Encountered session exception during logout.  This can generally safely be ignored.", ise);
		        }
		        // 重定向到单点登出地址
		        issueRedirect(request, response, redirectUrl);
		        result = false;
			} else {
				// do real thing
				result = super.preHandle(request, response);
			}
		} catch (Exception e) {
			ex = e;
		} finally {
			
			//调用事件监听器
			if(getLogoutListeners() != null && getLogoutListeners().size() > 0){
				for (LogoutListener logoutListener : getLogoutListeners()) {
					if(ex != null){
						logoutListener.onLogoutFail(subject, ex);
					}else{
						logoutListener.onLogoutSuccess(request, response);
					}
				}
			}
			
		}
		
		if(ex != null){
			throw ex;
		}
		
		return result;
	}
	
	public List<LogoutListener> getLogoutListeners() {
		return logoutListeners;
	}

	public void setLogoutListeners(List<LogoutListener> logoutListeners) {
		this.logoutListeners = logoutListeners;
	}

	public boolean isCasLogin(Subject subject) {
		return isCasLogin();
	}

	public void setCasLogin(boolean casLogin) {
		this.casLogin = casLogin;
	}
	
	public boolean isCasLogin()  {
		return casLogin;
	}
	
}
