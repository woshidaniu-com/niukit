/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.cas.filter;


import javax.servlet.ServletException;

import org.jasig.cas.client.configuration.ConfigurationKeys;
import org.jasig.cas.client.session.SingleSignOutFilter;

import com.woshidaniu.authz.cas.config.ProxyFilterConfig;

/**
 * 
 * @className	： ZFCasSingleSignOutFilter
 * @description	： 创建并代理jasig提供的SingleSignOutFilter
 * @author 		：康康（1571）
 * @date		： 2018年4月26日 下午5:53:24
 * @version 	V1.0
 */
public class ZFCasSingleSignOutFilter extends AbstractShiroCasPropertiesNeedProxyFilter{
	
	@Override
	protected void doInitDelegateAndConfig(ProxyFilterConfig proxyFilterConfig) throws ServletException {
		
		this.delegate = new SingleSignOutFilter();
		
		proxyFilterConfig.addInitParameter(ConfigurationKeys.ARTIFACT_PARAMETER_NAME.getName(), casProperties.getArtifactParameterName());
		proxyFilterConfig.addInitParameter(ConfigurationKeys.LOGOUT_PARAMETER_NAME.getName(), casProperties.getLogoutParameterName());
		proxyFilterConfig.addInitParameter(ConfigurationKeys.RELAY_STATE_PARAMETER_NAME.getName(), casProperties.getRelayStateParameterName());
		proxyFilterConfig.addInitParameter(ConfigurationKeys.CAS_SERVER_URL_PREFIX.getName(), casProperties.getCasServerUrlPrefix());
		
	}

	@Override
	protected boolean isEnable() {
		return casProperties.isEnabled();
	}

}
