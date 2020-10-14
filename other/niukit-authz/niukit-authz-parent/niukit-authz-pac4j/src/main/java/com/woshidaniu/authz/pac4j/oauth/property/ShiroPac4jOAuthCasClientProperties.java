/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.pac4j.oauth.property;

public class ShiroPac4jOAuthCasClientProperties extends ShiroPac4jOAuthClientProperties {

	 /**
     * The CAS OAuth server url (without a trailing slash).
     * For example: http://localhost:8080/cas/oauth2.0
     */
    private String casOAuthUrl;

    private String casLogoutUrl;

    private boolean springSecurityCompliant = false;

    private boolean implicitFlow = false;

	public String getCasOAuthUrl() {
		return casOAuthUrl;
	}

	public void setCasOAuthUrl(String casOAuthUrl) {
		this.casOAuthUrl = casOAuthUrl;
	}

	public String getCasLogoutUrl() {
		return casLogoutUrl;
	}

	public void setCasLogoutUrl(String casLogoutUrl) {
		this.casLogoutUrl = casLogoutUrl;
	}

	public boolean isSpringSecurityCompliant() {
		return springSecurityCompliant;
	}

	public void setSpringSecurityCompliant(boolean springSecurityCompliant) {
		this.springSecurityCompliant = springSecurityCompliant;
	}

	public boolean isImplicitFlow() {
		return implicitFlow;
	}

	public void setImplicitFlow(boolean implicitFlow) {
		this.implicitFlow = implicitFlow;
	}

}
