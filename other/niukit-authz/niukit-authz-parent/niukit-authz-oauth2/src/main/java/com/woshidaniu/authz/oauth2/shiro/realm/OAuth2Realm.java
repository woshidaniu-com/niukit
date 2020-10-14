/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.oauth2.shiro.realm;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.woshidaniu.authz.oauth2.shiro.exception.OAuth2AuthenticationException;
import com.woshidaniu.authz.oauth2.shiro.token.OAuth2Token;
import com.woshidaniu.shiro.realm.AccountRealm;
import com.woshidaniu.shiro.service.AccountService;
import com.woshidaniu.shiro.token.DelegateAuthenticationToken;

/**
 * 
 * @className	： OAuth2Realm
 * @description	： Oauth2认证登录的shiro的Realm
 * @author 		：康康（1571）
 * @date		： 2018年5月7日 上午9:22:13
 * @version 	V1.0
 */
public class OAuth2Realm extends AccountRealm{
	
	private static final Logger log = LoggerFactory.getLogger(OAuth2Realm.class);

	protected AccountService accountService;
	
	private String clientId;
	private String clientSecret;
	private String tokenUrl;
	private String profileUrl;
	private String redirectUrl;

	public boolean supports(AuthenticationToken token) {
		return token instanceof OAuth2Token; // 表示此Realm只支持OAuth2Token 类型
	}
	
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		OAuth2Token oAuth2Token = (OAuth2Token) token;
		String code = oAuth2Token.getAuthCode(); // 获取auth code
		String username = extractUsername(code); // 提取用户名
		oAuth2Token.setUsername(username);
		AuthenticationInfo authenticationInfo = super.doGetAuthenticationInfo(oAuth2Token);
		return authenticationInfo;
	}

	private String extractUsername(String code) {
		try {
			OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
			OAuthClientRequest accessTokenRequest = OAuthClientRequest.tokenLocation(tokenUrl)
					.setGrantType(GrantType.AUTHORIZATION_CODE).setClientId(clientId).setClientSecret(clientSecret)
					.setCode(code).setRedirectURI(redirectUrl).buildQueryMessage();
			// 获取access token
			OAuthAccessTokenResponse oAuthResponse = oAuthClient.accessToken(accessTokenRequest, OAuth.HttpMethod.POST);
			String accessToken = oAuthResponse.getAccessToken();
			Long expiresIn = oAuthResponse.getExpiresIn();
			// 获取user info
			OAuthClientRequest userInfoRequest = new OAuthBearerClientRequest(profileUrl).setAccessToken(accessToken)
					.buildQueryMessage();
			OAuthResourceResponse resourceResponse = oAuthClient.resource(userInfoRequest, OAuth.HttpMethod.GET,
					OAuthResourceResponse.class);
			String body = resourceResponse.getBody();
			JSONObject jsonObject = JSON.parseObject(body);
			String username = jsonObject.getString("id");
			return username;
		} catch (Exception e) {
			throw new OAuth2AuthenticationException(e);
		}
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getTokenUrl() {
		return tokenUrl;
	}

	public void setTokenUrl(String tokenUrl) {
		this.tokenUrl = tokenUrl;
	}

	public String getProfileUrl() {
		return profileUrl;
	}

	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	@Override
	protected DelegateAuthenticationToken createDelegateAuthenticationToken(AuthenticationToken token) {
		OAuth2Token oAuth2Token = (OAuth2Token) token;
		return new DelegateOAuth2TokenAuthenticationToken(oAuth2Token);
	}
	
	@Override
	public AccountService getAccountService() {
		return this.accountService;
	}

	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}
	
	private static final class DelegateOAuth2TokenAuthenticationToken implements DelegateAuthenticationToken{

		private static final long serialVersionUID = 1L;
		
		private OAuth2Token oAuth2Token;
		
		public DelegateOAuth2TokenAuthenticationToken(OAuth2Token oAuth2Token) {
			super();
			this.oAuth2Token = oAuth2Token;
		}

		@Override
		public String getUsername() {
			return oAuth2Token.getUsername();
		}

		@Override
		public String getUsertype() {
			return null;
		}

		@Override
		public char[] getPassword() {
			return null;
		}

		@Override
		public int getStrength() {
			return 0;
		}

		@Override
		public String getCaptcha() {
			return null;
		}

		@Override
		public String getHost() {
			return null;
		}

		@Override
		public boolean isRememberMe() {
			return false;
		}
		
	}
	
}
