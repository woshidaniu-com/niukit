/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.cas321.shiro.realm;

import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.util.AssertionHolder;
import org.jasig.cas.client.validation.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.shiro.authc.DefaultAccount;
import com.woshidaniu.shiro.authc.DelegateAuthenticationInfo;
import com.woshidaniu.shiro.realm.RealmListener;
import com.woshidaniu.shiro.realm.SsoAccountRealm;
import com.woshidaniu.shiro.token.DelegateAuthenticationToken;

/**
 * 标准cas321认证Realm
 * @author ：康康（1571）
 */
public class ZFCas321Realm extends SsoAccountRealm {

	private static final Logger log = LoggerFactory.getLogger(ZFCas321Realm.class);

	public ZFCas321Realm() {
		setAuthenticationTokenClass(com.woshidaniu.authz.cas321.shiro.token.ZFCasToken.class);
	}

	@Override
	public boolean supports(AuthenticationToken token) {
		return token != null && token instanceof com.woshidaniu.authz.cas321.shiro.token.ZFCasToken;
	}

	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

		log.info("Handle authentication token {}.", token);

		com.woshidaniu.authz.cas321.shiro.token.ZFCasToken casToken = (com.woshidaniu.authz.cas321.shiro.token.ZFCasToken) token;

		AuthenticationException authenticationException = null;
		AuthenticationInfo authenticationInfo = null;
		try {
			Assertion assertion = AssertionHolder.getAssertion();
			AttributePrincipal principal = assertion.getPrincipal();
			
			String username = principal.getName();
			Map<String, Object> attributes = principal.getAttributes();
			
			log.info("get info by cas, username:{},attributes:[{}]",username,attributes);
			
			casToken.setUsername(username);
			casToken.setAttrs(attributes);

			DelegateAuthenticationToken delegateToken = createDelegateAuthenticationToken(casToken);
			DelegateAuthenticationInfo delegateAuthenticationInfo = getAccountService().getAuthenticationInfo(delegateToken);
			
			if (delegateAuthenticationInfo != null) {
				authenticationInfo = new DefaultAccount(delegateAuthenticationInfo.getPrincipal(),delegateAuthenticationInfo.getCredentials(), getName());
			}else{
				log.warn("can't get AuthenticationInfo by cas where username is {},using principal:{}",username,username);
				//TODO 当从本地查询数据库查询不到任何关于从cas获得的用户信息和权限信息,改怎么处理？
				//下面的这行代码会出现common包里面的User转型异常
				//authenticationInfo = new DefaultAccount(username,Collections.emptySet(),getName());
			}
		} catch (Exception e) {
			
			log.error("doGetAuthenticationInfo error,cause :"+e.getMessage(),e);
			
			authenticationException = new AuthenticationException(e.getMessage(),e);
		}

		// 调用事件监听器
		if (getRealmsListeners() != null && getRealmsListeners().size() > 0) {
			for (RealmListener realmListener : getRealmsListeners()) {
				if (authenticationException != null || null == authenticationInfo) {
					realmListener.onAuthenticationFail(casToken, authenticationException);
				} else {
					realmListener.onAuthenticationSuccess(authenticationInfo, SecurityUtils.getSubject().getSession());
				}
			}
		}

		if (authenticationException != null) {
			throw authenticationException;
		}
		return authenticationInfo;
	}
}
