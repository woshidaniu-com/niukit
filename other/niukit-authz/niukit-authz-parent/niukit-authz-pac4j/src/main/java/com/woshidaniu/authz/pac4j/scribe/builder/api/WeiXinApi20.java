package com.woshidaniu.authz.pac4j.scribe.builder.api;

import java.util.Map;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.ParameterList;
import com.woshidaniu.authz.pac4j.oauth.OAuth2Constants;

/**
 * 用于定义获取微信返回的CODE与ACCESS_TOKEN
 */
public class WeiXinApi20 extends DefaultApi20 {

	private static final String AUTHORIZE_URL = "https://open.weixin.qq.com/connect/qrconnect";
	private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";
	
	private static class InstanceHolder {
		private static final WeiXinApi20 INSTANCE = new WeiXinApi20();
	}

	public static WeiXinApi20 instance() {
		return InstanceHolder.INSTANCE;
	}

	@Override
	protected String getAuthorizationBaseUrl() {
		return AUTHORIZE_URL;
	}
	
	@Override
	public String getAccessTokenEndpoint() {
		return ACCESS_TOKEN_URL;
	}
	
	@Override
    public String getAuthorizationUrl(final OAuthConfig config, Map<String, String> additionalParams) {
		final ParameterList parameters = new ParameterList(additionalParams);
        parameters.add(OAuthConstants.RESPONSE_TYPE, config.getResponseType());
        // 此处微信采用appid
        parameters.add(OAuth2Constants.APPID, config.getApiKey());

        final String callback = config.getCallback();
        if (callback != null) {
            parameters.add(OAuthConstants.REDIRECT_URI, callback);
        }

        final String scope = config.getScope();
        if (scope != null) {
            parameters.add(OAuthConstants.SCOPE, scope);
        }

        final String state = config.getState();
        if (state != null) {
            parameters.add(OAuthConstants.STATE, state);
        }

        return parameters.appendTo(getAuthorizationBaseUrl());
    }
	
}
