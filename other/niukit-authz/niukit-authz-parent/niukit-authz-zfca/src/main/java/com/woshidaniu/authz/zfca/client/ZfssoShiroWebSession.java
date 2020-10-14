/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.zfca.client;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.shiro.SubjectUtils;
import com.woshidaniu.shiro.token.ZfSsoToken;
import com.woshidaniu.web.context.WebContext;
import com.woshidaniu.web.utils.RemoteAddrUtils;
import com.woshidaniu.web.utils.WebUtils;
import com.woshidaniu.niuca.tp.cas.client.ZfssoBean;
import com.woshidaniu.niuca.tp.cas.client.ZfssoSetsessionService;

/**
 * 
 * @className	： ZfssoShiroWebSession
 * @description	： ZfssoShiroWebSession
 * @author 		：康康（1571）
 * @date		： 2018年4月24日 上午8:43:10
 * @version 	V1.0
 */
public class ZfssoShiroWebSession implements ZfssoSetsessionService {

	private static Logger log = LoggerFactory.getLogger(ZfssoShiroWebSession.class);

	/*
	 * 检测SESSION里面的用户是否存在,存在返回TRUE,反之为FALSE
	 * 
	 * @see com.woshidaniu.niuca.tp.cas.client.ZfssoSetsessionService#chkUserSession(com.
	 * woshidaniu.niuca.tp.cas.client.ZfssoBean)
	 */
	public synchronized Boolean chkUserSession(ZfssoBean zfssobean) {
		Subject subject = SubjectUtils.getWebSubject();
		boolean isAuth = subject.isAuthenticated();
		return Boolean.valueOf(isAuth);
	}

	/*
	 * 验证用户是否存在,存在则读取权限并返回TRUE,反之为FALSE
	 * 
	 * @see com.woshidaniu.niuca.tp.cas.client.ZfssoSetsessionService#setUserSession(com.
	 * woshidaniu.niuca.tp.cas.client.ZfssoBean)
	 */
	public synchronized Boolean setUserSession(ZfssoBean zfssobean) {
		HttpServletRequest request = WebUtils.toHttp(WebContext.getRequest());
		String host = RemoteAddrUtils.getRemoteAddr(request);
		Subject subject = SubjectUtils.getWebSubject();

		boolean result = false;
		try {
			ZfSsoToken token = new ZfSsoToken();
			token.setUsername(zfssobean.getYhm());
			// token.setRememberMe(true);
			token.setHost(host);

			subject.login(token);
			result = true;
			if (log.isDebugEnabled()) {
				log.debug("setUserSession login by username:{} success", zfssobean.getYhm());
			}
		} catch (AuthenticationException e) {
			log.error("sso登录异常", e);
		}
		return Boolean.valueOf(result);
	}
}
