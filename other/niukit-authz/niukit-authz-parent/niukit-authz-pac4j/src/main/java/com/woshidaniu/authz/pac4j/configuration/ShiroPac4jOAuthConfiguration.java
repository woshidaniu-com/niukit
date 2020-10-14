/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */

package com.woshidaniu.authz.pac4j.configuration;

import java.util.Map;
import java.util.function.Function;

import org.pac4j.core.client.Client;
import org.pac4j.core.context.WebContext;
import org.pac4j.oauth.client.OAuth20Client;
import org.pac4j.oauth.config.OAuth20Configuration;
import org.pac4j.oauth.profile.OAuth20Profile;
import org.pac4j.oauth.profile.definition.OAuthProfileDefinition;
import org.pac4j.oauth.profile.generic.GenericOAuth20ProfileDefinition;
import org.pac4j.scribe.builder.api.GenericApi20;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.scribejava.core.builder.api.BaseApi;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.woshidaniu.authz.pac4j.oauth.profile.zf.ZFProfileDefinition;

/**
 * 
 * @className	： ShiroPac4jOAuthConfiguration
 * @description	： pac4j的Cas配置，如需自定义其配置，在shiro-authz-pac4j-OAuth20Client.xml文件内设置其属性，利用properties引入配置
 * @author 		：康康（1571）
 * @date		： 2018年5月8日 下午2:27:33
 * @version 	V1.0
 */
@Configuration
public class ShiroPac4jOAuthConfiguration implements CreateClientSupport{
	
	private static final Logger log = LoggerFactory.getLogger(ShiroPac4jCasConfiguration.class);
	
	private OAuth20Configuration oAuth20Configuration = new OAuth20Configuration();
	
	private String authUrl;
	
	private String tokenUrl;
	
	private String profileUrl;
	
	@Bean("OAuth20Client")
	@Override
	@SuppressWarnings("rawtypes")
	public Client createClient() {
		
		if(log.isDebugEnabled()) {
			log.debug("create OAuth20Client by configuration:"+this.oAuth20Configuration);
		}
		
		GenericApi20 api = new GenericApi20(this.authUrl,this.tokenUrl);
		this.oAuth20Configuration.setApi(api);
		
		GenericOAuth20ProfileDefinition profileDefinition = new ZFProfileDefinition();
		profileDefinition.setProfileUrl(profileUrl);
		this.oAuth20Configuration.setProfileDefinition(profileDefinition);
		//创建客户端对象
		OAuth20Client client = new OAuth20Client<OAuth20Profile>();
		client.setConfiguration(this.oAuth20Configuration);
		client.setName("OAuth20Client");
		return client;
	}

	public void setCustomParams(Map<String, String> customParams) {
		oAuth20Configuration.setCustomParams(customParams);
	}

	public void setWithState(boolean withState) {
		oAuth20Configuration.setWithState(withState);
	}

	public void setStateData(String stateData) {
		oAuth20Configuration.setStateData(stateData);
	}
	
	public void setKey(String key) {
		oAuth20Configuration.setKey(key);
	}

	public void setSecret(String secret) {
		oAuth20Configuration.setSecret(secret);
	}

	public void setTokenAsHeader(boolean tokenAsHeader) {
		oAuth20Configuration.setTokenAsHeader(tokenAsHeader);
	}

	public void setConnectTimeout(int connectTimeout) {
		oAuth20Configuration.setConnectTimeout(connectTimeout);
	}

	public void setReadTimeout(int readTimeout) {
		oAuth20Configuration.setReadTimeout(readTimeout);
	}

	public void setResponseType(String responseType) {
		oAuth20Configuration.setResponseType(responseType);
	}

	public void setScope(String scope) {
		oAuth20Configuration.setScope(scope);
	}

	public void setApi(BaseApi<OAuth20Service> api) {
		oAuth20Configuration.setApi(api);
	}

	public void setHasGrantType(boolean hasGrantType) {
		oAuth20Configuration.setHasGrantType(hasGrantType);
	}

	public void setHasBeenCancelledFactory(Function<WebContext, Boolean> hasBeenCancelledFactory) {
		oAuth20Configuration.setHasBeenCancelledFactory(hasBeenCancelledFactory);
	}

	@SuppressWarnings("rawtypes")
	public void setProfileDefinition(OAuthProfileDefinition profileDefinition) {
		oAuth20Configuration.setProfileDefinition(profileDefinition);
	}

	public void setAuthUrl(String authUrl) {
		this.authUrl = authUrl;
	}

	public void setTokenUrl(String tokenUrl) {
		this.tokenUrl = tokenUrl;
	}

	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}
}
