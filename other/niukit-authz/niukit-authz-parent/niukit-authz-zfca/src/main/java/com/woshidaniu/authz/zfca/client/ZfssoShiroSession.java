/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.zfca.client;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;

import com.woshidaniu.shiro.token.ZfSsoToken;
import com.woshidaniu.niuca.tp.cas.client.ZfssoBean;
import com.woshidaniu.niuca.tp.cas.client.ZfssoSetsessionService;

public class ZfssoShiroSession implements ZfssoSetsessionService {

	/*
	 * 检测SESSION里面的用户是否存在,存在返回TRUE,反之为FALSE
	 * 
	 * @see
	 * com.woshidaniu.niuca.tp.cas.client.ZfssoSetsessionService#chkUserSession(com.
	 * woshidaniu.niuca.tp.cas.client.ZfssoBean)
	 */
	public Boolean chkUserSession(ZfssoBean zfssobean) {
		return SecurityUtils.getSubject().isAuthenticated();
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
			SecurityUtils.getSubject().login(token);
			res = true;
		} catch (AuthenticationException e) {
			e.printStackTrace();
		}
		return res;
	}

}
