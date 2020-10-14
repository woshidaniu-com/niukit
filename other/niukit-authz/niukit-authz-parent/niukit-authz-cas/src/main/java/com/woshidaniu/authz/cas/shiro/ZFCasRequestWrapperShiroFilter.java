/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.cas.shiro;

import javax.servlet.ServletException;

import org.jasig.cas.client.configuration.ConfigurationKeys;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;

import com.woshidaniu.authz.cas.config.ProxyFilterConfig;
import com.woshidaniu.basicutils.StringUtils;

/**
 * 
 * @className	： ZFCasRequestWrapperShiroFilter
 * @description	： cas 请求包装的shiro过滤器
 * @author 		：康康（1571）
 * @date		： 2018年5月3日 上午9:29:41
 * @version 	V1.0
 */
public class ZFCasRequestWrapperShiroFilter extends AbstractCasPropertiesNeedProxyShiroFilter{

	@Override
	protected void doInitDelegateAndConfigLazy(ProxyFilterConfig proxyFilterConfig) throws ServletException {
		
		log.info("doCreateDelegateAndInitConfig:{}",this.getClass().getSimpleName());
		
		this.delegate = new HttpServletRequestWrapperFilter();
		
		proxyFilterConfig.addInitParameter(ConfigurationKeys.IGNORE_CASE.getName(), String.valueOf(casProperties.isIgnoreCase()));
		if(StringUtils.hasText(casProperties.getRoleAttribute())) {	
			proxyFilterConfig.addInitParameter(ConfigurationKeys.ROLE_ATTRIBUTE.getName(), casProperties.getRoleAttribute());
		}
	}

}
