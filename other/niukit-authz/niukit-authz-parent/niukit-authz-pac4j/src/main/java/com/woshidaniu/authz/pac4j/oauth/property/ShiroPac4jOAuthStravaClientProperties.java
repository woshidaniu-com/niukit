/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.pac4j.oauth.property;

public class ShiroPac4jOAuthStravaClientProperties extends ShiroPac4jOAuthClientProperties {
	   
	/**
     * approvalPrompt is by default "auto".   <br>
     * If "force", then the authorization dialog is always displayed by Strava.
     */
    private String approvalPrompt = "auto";

	public String getApprovalPrompt() {
		return approvalPrompt;
	}

	public void setApprovalPrompt(String approvalPrompt) {
		this.approvalPrompt = approvalPrompt;
	}

}
