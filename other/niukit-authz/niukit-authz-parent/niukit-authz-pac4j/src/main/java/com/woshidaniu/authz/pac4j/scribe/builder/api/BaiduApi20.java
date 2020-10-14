package com.woshidaniu.authz.pac4j.scribe.builder.api;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.extractors.OAuth2AccessTokenExtractor;
import com.github.scribejava.core.extractors.TokenExtractor;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.Verb;

/**
 * http://developer.baidu.com/wiki/index.php?title=docs/oauth/authorization
 */
public class BaiduApi20 extends DefaultApi20 {
		
	public static final String AUTHORIZE_URL = "http://openapi.baidu.com/oauth/2.0/authorize";
	public static final String ACCESS_TOKEN_URL = "https://openapi.baidu.com/oauth/2.0/token";

	protected BaiduApi20() {
	}

	private static class InstanceHolder {
		private static final BaiduApi20 INSTANCE = new BaiduApi20();
	}

	public static BaiduApi20 instance() {
		return InstanceHolder.INSTANCE;
	}

	@Override
	public Verb getAccessTokenVerb() {
		return Verb.GET;
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
  
}
