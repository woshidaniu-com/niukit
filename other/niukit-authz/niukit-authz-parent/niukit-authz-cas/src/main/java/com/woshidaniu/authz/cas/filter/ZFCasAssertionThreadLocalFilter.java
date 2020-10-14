/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.cas.filter;

import javax.servlet.ServletException;

import org.jasig.cas.client.util.AssertionThreadLocalFilter;

import com.woshidaniu.authz.cas.config.ProxyFilterConfig;

/**
 * 
 * @className	： ZFCasAssertionThreadLocalFilter
 * @description	： 
 * 		创建并代理jasig提供的AssertionThreadLocalFilter拦截器。ShiroCasProperties用于初始化其被代理对象的FilterConfig
 * @author 		：康康（1571）
 * @date		： 2018年4月24日 上午10:55:02
 * @version 	V1.0
 */
public class ZFCasAssertionThreadLocalFilter extends AbstractShiroCasPropertiesNeedProxyFilter{

	@Override
	protected void doInitDelegateAndConfig(ProxyFilterConfig proxyFilterConfig) throws ServletException {
		log.info("doCreateDelegateAndInitConfig:{}",this.getClass().getSimpleName());
		this.delegate = new AssertionThreadLocalFilter();
	}

	@Override
	protected boolean isEnable() {
		return casProperties.isEnabled();
	}
}
