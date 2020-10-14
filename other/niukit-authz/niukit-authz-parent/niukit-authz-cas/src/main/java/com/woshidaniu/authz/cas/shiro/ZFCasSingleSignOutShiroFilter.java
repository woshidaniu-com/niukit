/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.cas.shiro;

import javax.servlet.ServletException;

import org.jasig.cas.client.configuration.ConfigurationKeys;
import org.jasig.cas.client.session.SingleSignOutFilter;

import com.woshidaniu.authz.cas.config.ProxyFilterConfig;

/**
 * 
 * @className	： ZFCasSingleSignOutShiroFilter
 * @description	： cas sso的shiro代理过滤器
 * @author 		：康康（1571）
 * @date		： 2018年5月3日 上午9:28:32
 * @version 	V1.0
 */
public class ZFCasSingleSignOutShiroFilter extends AbstractCasPropertiesNeedProxyShiroFilter{

	@Override
	protected void doInitDelegateAndConfigLazy(ProxyFilterConfig proxyFilterConfig) throws ServletException {
		
		this.delegate = new SingleSignOutFilter();
		
		proxyFilterConfig.addInitParameter(ConfigurationKeys.ARTIFACT_PARAMETER_NAME.getName(), casProperties.getArtifactParameterName());
		proxyFilterConfig.addInitParameter(ConfigurationKeys.LOGOUT_PARAMETER_NAME.getName(), casProperties.getLogoutParameterName());
		proxyFilterConfig.addInitParameter(ConfigurationKeys.RELAY_STATE_PARAMETER_NAME.getName(), casProperties.getRelayStateParameterName());
		proxyFilterConfig.addInitParameter(ConfigurationKeys.CAS_SERVER_URL_PREFIX.getName(), casProperties.getCasServerUrlPrefix());
	}

}
