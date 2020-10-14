package com.woshidaniu.shiro.cas;

import org.apache.shiro.authc.AuthenticationException;

import com.woshidaniu.shiro.SubjectUtils;
import com.woshidaniu.shiro.token.ZfSsoToken;
import com.woshidaniu.web.context.WebContext;
import com.woshidaniu.web.utils.WebRequestUtils;
import com.woshidaniu.web.utils.WebUtils;
import com.woshidaniu.niuca.tp.cas.client.ZfssoBean;
import com.woshidaniu.niuca.tp.cas.client.ZfssoSetsessionService;

public class ZfssoShiroWebSession implements ZfssoSetsessionService {

	/*
	 * 检测SESSION里面的用户是否存在,存在返回TRUE,反之为FALSE
	 * 
	 * @see
	 * com.woshidaniu.niuca.tp.cas.client.ZfssoSetsessionService#chkUserSession(com.
	 * woshidaniu.niuca.tp.cas.client.ZfssoBean)
	 */
	public Boolean chkUserSession(ZfssoBean zfssobean) {
		return SubjectUtils.getWebSubject().isAuthenticated();
	}
	
	/*
	 * 验证用户是否存在,存在则读取权限并返回TRUE,反之为FALSE
	 * 
	 * @see
	 * com.woshidaniu.niuca.tp.cas.client.ZfssoSetsessionService#setUserSession(com.
	 * woshidaniu.niuca.tp.cas.client.ZfssoBean)
	 */
	public Boolean setUserSession(ZfssoBean zfssobean) {
		Boolean res = false;
		try {
			ZfSsoToken token = new ZfSsoToken();
			token.setUsername(zfssobean.getYhm());
			//token.setRememberMe(true);
			token.setHost(WebRequestUtils.getRemoteAddr(WebUtils.toHttp(WebContext.getRequest())));
			SubjectUtils.getWebSubject().login(token);
			res = true;
		} catch (AuthenticationException e) {
			e.printStackTrace();
		}
		return res;
	}

}
