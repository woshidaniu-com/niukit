/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.pac4j.oauth.property;

public class ShiroPac4jOAuthOkClientProperties extends ShiroPac4jOAuthClientProperties {

	/**
	 * Public key (required as well as application key by API on ok.ru)
	 */
	private String publicKey;

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

}
