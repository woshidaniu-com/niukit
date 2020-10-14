package com.woshidaniu.authz.zjucookie.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.authz.zjucookie.shiro.token.ZjuCookie;
import com.woshidaniu.authz.zjucookie.utils.CookieSsoApi;



/**
 * 
 * @className	： ZjuCookieLogoutFilter
 * @description	： ZjuCookie认证的注销Filter
 * @author 		：康康（1571）
 * @date		： 2018年5月10日 上午10:46:20
 * @version 	V1.0
 */
public class ZjuCookieLogoutShiroFilter extends LogoutFilter {

	private static Logger log = LoggerFactory.getLogger(ZjuCookieLogoutShiroFilter.class);
	
	protected CookieSsoApi cookieSsoApi;
	
	@Override
	protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
		Subject subject = getSubject(request, response);
		if(subject.isAuthenticated() && subject.getPrincipal() instanceof ZjuCookie){
			
			ZjuCookie cookie = (ZjuCookie) subject.getPrincipal();
			
			this.cookieSsoApi.logout(cookie.getUid());
			
			if(log.isDebugEnabled()) {
				log.debug("Cookie SSO Logout .");				
			}
		}
		
		return super.preHandle(request, response);
	}

	public void setCookieSsoApi(CookieSsoApi cookieSsoApi) {
		this.cookieSsoApi = cookieSsoApi;
	}
	
}
