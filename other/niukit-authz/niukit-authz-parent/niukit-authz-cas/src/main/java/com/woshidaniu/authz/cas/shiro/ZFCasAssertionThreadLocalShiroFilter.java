/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.cas.shiro;

import javax.servlet.ServletException;

import org.jasig.cas.client.util.AssertionThreadLocalFilter;

import com.woshidaniu.authz.cas.config.ProxyFilterConfig;

/**
 * 
 * @className	： ZFCasAssertionThreadLocalShiroFilter
 * @description	： 
 * @author 		：康康（1571）
 * @date		： 2018年5月3日 上午9:30:23
 * @version 	V1.0
 */
public class ZFCasAssertionThreadLocalShiroFilter extends AbstractCasPropertiesNeedProxyShiroFilter{

	@Override
	protected void doInitDelegateAndConfigLazy(ProxyFilterConfig proxyFilterConfig) throws ServletException {
		log.info("doCreateDelegateAndInitConfig:{}",this.getClass().getSimpleName());
		this.delegate = new AssertionThreadLocalFilter();
	}

	@Override
	public boolean isEnabled() {
		return casProperties.isEnabled();
	}
}
