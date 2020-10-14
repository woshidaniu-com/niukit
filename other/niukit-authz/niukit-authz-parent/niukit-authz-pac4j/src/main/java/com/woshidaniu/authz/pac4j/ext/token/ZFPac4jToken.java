/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.pac4j.ext.token;

import java.util.LinkedHashMap;

import org.pac4j.core.profile.CommonProfile;

import io.buji.pac4j.token.Pac4jToken;

@SuppressWarnings("serial")
public class ZFPac4jToken extends Pac4jToken {

	public ZFPac4jToken(LinkedHashMap<String, CommonProfile> profiles, boolean isRemembered) {
		super(profiles, isRemembered);
	}

}
