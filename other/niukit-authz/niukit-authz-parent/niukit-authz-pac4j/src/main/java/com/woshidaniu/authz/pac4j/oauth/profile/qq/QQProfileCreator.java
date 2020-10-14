package com.woshidaniu.authz.pac4j.oauth.profile.qq;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pac4j.core.exception.HttpCommunicationException;
import org.pac4j.oauth.config.OAuth20Configuration;
import org.pac4j.oauth.profile.creator.OAuth20ProfileCreator;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.woshidaniu.authz.pac4j.oauth.OAuth2Constants;

public class QQProfileCreator extends OAuth20ProfileCreator<QQProfile> {

	private static Pattern openIdPattern = Pattern.compile("\"openid\":\\s*\"(\\S*?)\"");
	private static final String OPENID_URL = "https://graph.qq.com/oauth2.0/me?access_token=%s";
	
    public QQProfileCreator(final OAuth20Configuration configuration) {
        super(configuration);
    }

    @Override
    protected void signRequest(final OAuth2AccessToken accessToken, final OAuthRequest request) {
    	super.signRequest(accessToken, request);
    	// Step3：使用Access Token来获取用户的OpenID
    	//  1.发送请求到如下地址（请将access_token等参数值替换为你自己的）： https://graph.qq.com/oauth2.0/me?access_token=YOUR_ACCESS_TOKEN
        String getOpenIdUrl = String.format(OPENID_URL, accessToken);
        OAuthRequest openIdRequest = createOAuthRequest(getOpenIdUrl, Verb.GET);
        Response response = openIdRequest.send();
        int code = response.getCode();
		try {
			String body = response.getBody();
			if (code != 200) {
	        	logger.error("Failed to get OpenID, code : " + code + " / body : " + body);
	            throw new HttpCommunicationException(code, body);
	        }
	        // 2. 获取到用户OpenID，返回包如下： callback( {"client_id":"YOUR_APPID","openid":"YOUR_OPENID"} ); 
	        Matcher matcher = openIdPattern.matcher(body);
	        String openid = "";
	        if(matcher.find()){
	        	openid = matcher.group(1);
	        	request.addQuerystringParameter(OAuth2Constants.OPENID, openid);
	        }
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

}
