/**
 * 
 */
package com.woshidaniu.shiro.realm;

import java.util.List;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.shiro.authc.DefaultAccount;
import com.woshidaniu.shiro.authc.DelegateAuthenticationInfo;
import com.woshidaniu.shiro.service.AccountService;
import com.woshidaniu.shiro.token.DelegateAuthenticationToken;

/**
 * @desc 抽象realm，个业务系统自己实现接口中的方法
 * 
 * @desc 公共需要做的事：1.记录日志；2.提高更高级api；3.封装内部处理逻辑；4.事件监听；
 * 
 * @author zhangxb
 *
 */
public abstract class AccountRealm extends AuthorizingRealm {

	private static final Logger log = LoggerFactory.getLogger(AccountRealm.class);

	//realm listeners
	protected List<RealmListener> realmsListeners;
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.shiro.realm.AuthorizingRealm#doGetAuthorizationInfo(org.apache
	 * .shiro.subject.PrincipalCollection)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		if(principals == null || principals.isEmpty()){
			return null;
		}
		Set<String> permissionsInfo, rolesInfo = null;
		if(principals.asList().size() <= 1){
			permissionsInfo = getAccountService().getPermissionsInfo(principals.getPrimaryPrincipal());
			rolesInfo = getAccountService().getRolesInfo(principals.getPrimaryPrincipal());
		}else{
			permissionsInfo = getAccountService().getPermissionsInfo(principals.asSet());
			rolesInfo = getAccountService().getRolesInfo(principals.asSet());
		}
		DefaultAccount account = new DefaultAccount();
		account.setStringPermissions(permissionsInfo);
		account.setRoles(rolesInfo);
		return account;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.shiro.realm.AuthenticatingRealm#doGetAuthenticationInfo(org.
	 * apache.shiro.authc.AuthenticationToken)
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		log.info("Handle authentication token {}.", new Object[] { token });
		
		AuthenticationException ex = null;
		DefaultAccount account = null;
		try {
			
			//调用事件监听器
			if(getRealmsListeners() != null && getRealmsListeners().size() > 0){
				for (RealmListener realmListener : getRealmsListeners()) {
					realmListener.onBeforeAuthentication(token);
				}
			}
			
			// do real thing
			// new delegate authentication token and invoke doAuthc method
			DelegateAuthenticationInfo delegateAuthenticationInfo = getAccountService()
					.getAuthenticationInfo(createDelegateAuthenticationToken(token));
			if (delegateAuthenticationInfo != null) {
				account = new DefaultAccount(delegateAuthenticationInfo.getPrincipal(),
						delegateAuthenticationInfo.getCredentials(), getName());
			}
		} catch (AuthenticationException e) {
			ex = e;
		}
		
		//调用事件监听器
		if(getRealmsListeners() != null && getRealmsListeners().size() > 0){
			for (RealmListener realmListener : getRealmsListeners()) {
				if(ex != null || null == account){
					realmListener.onAuthenticationFail(token, ex);
				}else{
					realmListener.onAuthenticationSuccess(account,SecurityUtils.getSubject().getSession());
				}
			}
		}
		
		if(ex != null){
			throw ex;
		}
		
		return account;
	}

	public List<RealmListener> getRealmsListeners() {
		return realmsListeners;
	}

	public void setRealmsListeners(List<RealmListener> realmsListeners) {
		this.realmsListeners = realmsListeners;
	}
	
	public void clearAuthorizationCache(){
		clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
	}
	
	///////////////////////////////////// abstract
	///////////////////////////////////// method//////////////////////////////////////////

	public abstract AccountService getAccountService();

	protected abstract DelegateAuthenticationToken createDelegateAuthenticationToken(AuthenticationToken token);

	///////////////////////////////////// abstract
	///////////////////////////////////// method//////////////////////////////////////////
}
