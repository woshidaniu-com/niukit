/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.pac4j.property;

import org.pac4j.oauth.client.FacebookClient;
import org.pac4j.oauth.profile.facebook.FacebookProfileDefinition;

public class ShiroPac4jOAuthFacebookClientProperties extends ShiroPac4jOAuthClientProperties {

	protected String fields = FacebookClient.DEFAULT_FIELDS;
	protected int limit = FacebookProfileDefinition.DEFAULT_LIMIT;
	protected boolean requiresExtendedToken = false;
	protected boolean useAppsecretProof = false;

	public String getFields() {
		return fields;
	}

	public void setFields(String fields) {
		this.fields = fields;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public boolean isRequiresExtendedToken() {
		return requiresExtendedToken;
	}

	public void setRequiresExtendedToken(boolean requiresExtendedToken) {
		this.requiresExtendedToken = requiresExtendedToken;
	}

	public boolean isUseAppsecretProof() {
		return useAppsecretProof;
	}

	public void setUseAppsecretProof(boolean useAppsecretProof) {
		this.useAppsecretProof = useAppsecretProof;
	}

}
