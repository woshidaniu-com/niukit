/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.cas.filter;

import javax.servlet.ServletException;

import org.jasig.cas.client.configuration.ConfigurationKeys;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;

import com.woshidaniu.authz.cas.config.ProxyFilterConfig;
import com.woshidaniu.basicutils.StringUtils;

/**
 * 
 * @className	： ZFCasRequestWrapperFilter
 * @description	： 
 * 	 	创建并代理jasig提供的HttpServletRequestWrapperFilter拦截器。。ShiroCasProperties用于初始化其被代理对象的FilterConfig
 * @author 		：康康（1571）
 * @date		： 2018年4月24日 上午10:54:47
 * @version 	V1.0
 */
public class ZFCasRequestWrapperFilter extends AbstractShiroCasPropertiesNeedProxyFilter{
	
	@Override
	protected void doInitDelegateAndConfig(ProxyFilterConfig proxyFilterConfig) throws ServletException {
		
		log.info("doCreateDelegateAndInitConfig:{}",this.getClass().getSimpleName());
		
		this.delegate = new HttpServletRequestWrapperFilter();
		
		proxyFilterConfig.addInitParameter(ConfigurationKeys.IGNORE_CASE.getName(), String.valueOf(casProperties.isIgnoreCase()));
		if(StringUtils.hasText(casProperties.getRoleAttribute())) {	
			proxyFilterConfig.addInitParameter(ConfigurationKeys.ROLE_ATTRIBUTE.getName(), casProperties.getRoleAttribute());
		}
	}

	@Override
	protected boolean isEnable() {
		return casProperties.isEnabled();
	}

}
