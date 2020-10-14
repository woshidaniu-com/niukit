/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.pac4j.ext.realm;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.PrincipalCollection;
import org.pac4j.cas.profile.CasProfile;

import com.google.common.base.Optional;

import io.buji.pac4j.realm.Pac4jRealm;
import io.buji.pac4j.subject.Pac4jPrincipal;
import io.buji.pac4j.token.Pac4jToken;

@SuppressWarnings("unchecked") 
public class Pac4jBaseRealm extends Pac4jRealm {
	
	@Override
	protected Object getAuthorizationCacheKey(PrincipalCollection principals) {
	    Pac4jPrincipal pac4jPrincipal = (Pac4jPrincipal) principals.getPrimaryPrincipal();
	    return pac4jPrincipal.getProfile().getId();
	}
	
	@Override
	protected Object getAuthenticationCacheKey(AuthenticationToken token) {
	    if (token instanceof Pac4jToken) {
	        Pac4jToken pac4jToken = (Pac4jToken) token;
	        Object principal = pac4jToken.getPrincipal();
	        if (principal instanceof Optional) {
	           
	            Optional<CasProfile> casProfileOptional = (Optional<CasProfile>) principal;
	            return casProfileOptional.get().getId();
	        }
	    }
	    return super.getAuthenticationCacheKey(token);
	}

}
