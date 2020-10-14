package com.woshidaniu.authz.pac4j.oauth.profile.oschina;

import org.pac4j.oauth.config.OAuth20Configuration;
import org.pac4j.oauth.profile.creator.OAuth20ProfileCreator;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;

public class OschinaProfileCreator extends OAuth20ProfileCreator<OschinaProfile> {

	public OschinaProfileCreator(OAuth20Configuration configuration) {
		super(configuration);
	}

	@Override
	protected void signRequest(OAuth2AccessToken accessToken, OAuthRequest request) {
		super.signRequest(accessToken, request);
		// 指定返回值类型['json'|'jsonp'|'xml']
 		request.addQuerystringParameter("dataType", "json");
	}
	
}
