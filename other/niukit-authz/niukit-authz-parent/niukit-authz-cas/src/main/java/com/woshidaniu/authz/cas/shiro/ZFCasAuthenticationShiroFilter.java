/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.cas.shiro;

import javax.servlet.ServletException;

import org.jasig.cas.client.Protocol;
import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.authentication.Saml11AuthenticationFilter;
import org.jasig.cas.client.configuration.ConfigurationKeys;

import com.woshidaniu.authz.cas.config.ProxyFilterConfig;
import com.woshidaniu.basicutils.StringUtils;

/**
 * 
 * @className	： ZFCasAuthenticationShiroFilter
 * @description	： cas 认证代理shiro过滤器
 * @author 		：康康（1571）
 * @date		： 2018年5月3日 上午9:30:03
 * @version 	V1.0
 */
public class ZFCasAuthenticationShiroFilter extends AbstractCasPropertiesNeedProxyShiroFilter{

	@Override
	protected void doInitDelegateAndConfigLazy(ProxyFilterConfig proxyFilterConfig) throws ServletException {
		
		log.info("doCreateDelegateAndInitConfig:{}",this.getClass().getSimpleName());
		
		if (Protocol.SAML11.equals(casProperties.getProtocol())) {
			this.delegate = new Saml11AuthenticationFilter();
		} else {
			this.delegate =  new AuthenticationFilter();
		}
		
		if(StringUtils.hasText(casProperties.getAuthenticationRedirectStrategyClass())) {	
			proxyFilterConfig.addInitParameter(ConfigurationKeys.AUTHENTICATION_REDIRECT_STRATEGY_CLASS.getName(), casProperties.getAuthenticationRedirectStrategyClass());
		}
		proxyFilterConfig.addInitParameter(ConfigurationKeys.CAS_SERVER_LOGIN_URL.getName(), casProperties.getCasServerLoginUrl());
		proxyFilterConfig.addInitParameter(ConfigurationKeys.ENCODE_SERVICE_URL.getName(), Boolean.toString(casProperties.isEncodeServiceUrl()));
		proxyFilterConfig.addInitParameter(ConfigurationKeys.GATEWAY.getName(), Boolean.toString(casProperties.isGateway()));
		if(StringUtils.hasText(casProperties.getGatewayStorageClass())) {	
			proxyFilterConfig.addInitParameter(ConfigurationKeys.GATEWAY_STORAGE_CLASS.getName(), casProperties.getGatewayStorageClass());
		}
		if(StringUtils.hasText(casProperties.getIgnorePattern())) {
			proxyFilterConfig.addInitParameter(ConfigurationKeys.IGNORE_PATTERN.getName(), casProperties.getIgnorePattern());
		}
		proxyFilterConfig.addInitParameter(ConfigurationKeys.IGNORE_URL_PATTERN_TYPE.getName(), casProperties.getIgnoreUrlPatternType().toString());
		//proxyFilterConfig.addInitParameter(ConfigurationKeys.RENEW.getName(), Boolean.toString(properties.isRenew()));
		if(StringUtils.hasText(casProperties.getServerName())) {	
			proxyFilterConfig.addInitParameter(ConfigurationKeys.SERVER_NAME.getName(), casProperties.getServerName());
		} else if(StringUtils.hasText(casProperties.getService())) {	
			proxyFilterConfig.addInitParameter(ConfigurationKeys.SERVICE.getName(), casProperties.getService());
		}
	}

}