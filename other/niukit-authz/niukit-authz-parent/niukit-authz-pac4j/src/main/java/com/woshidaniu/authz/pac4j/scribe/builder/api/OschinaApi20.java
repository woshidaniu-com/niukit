package com.woshidaniu.authz.pac4j.scribe.builder.api;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.extractors.OAuth2AccessTokenExtractor;
import com.github.scribejava.core.extractors.TokenExtractor;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.Verb;

public class OschinaApi20 extends DefaultApi20 {
	
	public static final String AUTHORIZE_URL = "http://www.oschina.net/action/oauth2/authorize";
	public static final String ACCESS_TOKEN_URL = "http://www.oschina.net/action/openapi/token";

	protected OschinaApi20() {
    }

    private static class InstanceHolder {
        private static final OschinaApi20 INSTANCE = new OschinaApi20();
    }

    public static OschinaApi20 instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.GET;
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
    public TokenExtractor<OAuth2AccessToken> getAccessTokenExtractor() {
        return OAuth2AccessTokenExtractor.instance();
    }
	
}
