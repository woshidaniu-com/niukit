package com.woshidaniu.authz.pac4j.scribe.builder.api;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.extractors.OAuth2AccessTokenExtractor;
import com.github.scribejava.core.extractors.TokenExtractor;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.woshidaniu.authz.pac4j.scribe.service.QQOAuth20ServiceImpl;

public class QQApi20 extends DefaultApi20 {

	private static final String AUTHORIZE_URL = "https://graph.qq.com/oauth2.0/authorize";
	private static final String ACCESS_TOKEN_URL = "https://graph.qq.com/oauth2.0/token";

	private static class InstanceHolder {
		private static final QQApi20 INSTANCE = new QQApi20();
	}

	public static QQApi20 instance() {
		return InstanceHolder.INSTANCE;
	}

	@Override
	public Verb getAccessTokenVerb() {
		return Verb.POST;
	}

	@Override
	public String getAccessTokenEndpoint() {
		return ACCESS_TOKEN_URL;
	}

	@Override
	protected String getAuthorizationBaseUrl() {
		return AUTHORIZE_URL;
	}

	@Override
    public TokenExtractor<OAuth2AccessToken> getAccessTokenExtractor() {
        return OAuth2AccessTokenExtractor.instance();
    }
	 
	@Override
	public OAuth20Service createService(OAuthConfig config) {
		return new QQOAuth20ServiceImpl(this, config);
	}

}
