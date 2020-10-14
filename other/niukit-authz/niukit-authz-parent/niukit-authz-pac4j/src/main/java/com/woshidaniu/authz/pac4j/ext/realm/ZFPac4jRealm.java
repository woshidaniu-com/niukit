/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.pac4j.ext.realm;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.pac4j.core.profile.CommonProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.shiro.authc.DefaultAccount;
import com.woshidaniu.shiro.authc.DelegateAuthenticationInfo;
import com.woshidaniu.shiro.realm.RealmListener;
import com.woshidaniu.shiro.service.AccountService;
import com.woshidaniu.shiro.token.DelegateAuthenticationToken;
import com.woshidaniu.shiro.token.ZfSsoToken;

import io.buji.pac4j.realm.Pac4jRealm;
import io.buji.pac4j.subject.Pac4jPrincipal;
import io.buji.pac4j.token.Pac4jToken;

/**
 * 
 * @className	： ZFPac4jRealm
 * @description	：ZFPac4jRealm
 * @author 		：康康（1571）
 * @date		： 2018年4月27日 上午10:28:54
 * @version 	V1.0
 */
public class ZFPac4jRealm extends Pac4jRealm {
	
	private static final Logger log = LoggerFactory.getLogger(ZFPac4jRealm.class);

	//realm listeners
	protected List<RealmListener> realmsListeners;
	
	protected AccountService accountService;
	
	public ZFPac4jRealm() {
        setAuthenticationTokenClass(Pac4jToken.class);
    }
	
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
	
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		log.info("Handle authentication token {}.", new Object[] { token });
		
		Pac4jToken casToken = (Pac4jToken) token;
		if (casToken == null) {
			return null;
		}
		
		AuthenticationException ex = null;
		AuthenticationInfo info = null;
		ZfSsoToken newToken = null;
		try {
			
	        final LinkedHashMap<String, CommonProfile> profiles = casToken.getProfiles();  
	        final Pac4jPrincipal principal = new Pac4jPrincipal(profiles);
	        String id = principal.getProfile().getId();
	        boolean isRememberMe = casToken.isRememberMe();
	        newToken = new ZfSsoToken(id, "",isRememberMe);
			
			// do real thing
			// new delegate authentication token and invoke doAuthc method
	        DelegateAuthenticationToken delegateAuthenticationToken = createDelegateAuthenticationToken(newToken);
			DelegateAuthenticationInfo delegateAuthenticationInfo = this.accountService.getAuthenticationInfo(delegateAuthenticationToken);
			if (delegateAuthenticationInfo != null) {
				Object delegatePrincipalInfo = delegateAuthenticationInfo.getPrincipal();
				Object delegateCredentialsInfo = delegateAuthenticationInfo.getCredentials();
				info = new DefaultAccount(delegatePrincipalInfo,delegateCredentialsInfo, getName());
			}
		} catch (AuthenticationException e) {
			ex = e;
		}
		
		//调用事件监听器
		if(getRealmsListeners() != null && getRealmsListeners().size() > 0){
			for (RealmListener realmListener : getRealmsListeners()) {
				if(ex != null || null == info){
					realmListener.onAuthenticationFail(newToken, ex);
				}else{
					realmListener.onAuthenticationSuccess(info,SecurityUtils.getSubject().getSession());
				}
			}
		}
		
		if(ex != null){
			throw ex;
		}
		
		return info;
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

	public AccountService getAccountService() {
		return accountService;
	}

	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

	/* (non-Javadoc)
	 * @see com.woshidaniu.shiro.realm.AccountRealm#createDelegateAuthenticationToken(org.apache.shiro.authc.AuthenticationToken)
	 */
	protected DelegateAuthenticationToken createDelegateAuthenticationToken(AuthenticationToken token) {
		return (DelegateAuthenticationToken)token;
	}

}
